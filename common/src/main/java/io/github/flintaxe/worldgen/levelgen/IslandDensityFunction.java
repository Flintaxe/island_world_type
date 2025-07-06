package io.github.flintaxe.worldgen.levelgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public record IslandDensityFunction(
        double centerX,
        double centerZ,
        double baseRadius,
        int variations,
        double minValue,
        double maxValue
) implements DensityFunction {

    public static final MapCodec<IslandDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.DOUBLE.fieldOf("center_x").forGetter(IslandDensityFunction::centerX),
                    Codec.DOUBLE.fieldOf("center_z").forGetter(IslandDensityFunction::centerZ),
                    Codec.DOUBLE.fieldOf("base_radius").forGetter(IslandDensityFunction::baseRadius),
                    Codec.INT.fieldOf("variations").forGetter(IslandDensityFunction::variations),
                    Codec.DOUBLE.optionalFieldOf("min_value", -1.0).forGetter(IslandDensityFunction::minValue),
                    Codec.DOUBLE.optionalFieldOf("max_value", 1.0).forGetter(IslandDensityFunction::maxValue)
            ).apply(instance, IslandDensityFunction::new)
    );

    @Override
    public double compute(FunctionContext context) {
        double x = context.blockX();
        double z = context.blockZ();

        long worldSeed = context.blockX() ^ context.blockZ();

        double dx = x - centerX;
        double dz = z - centerZ;
        double distance = Math.sqrt(dx * dx + dz * dz);
        double angle = Math.atan2(dz, dx);

        double islandRadius = getIslandRadius(angle, worldSeed);

        double falloff = distance / islandRadius;
        double alpha = Math.exp(-falloff * falloff * 1.5);

        return minValue + alpha * (maxValue - minValue);
    }

    private double getIslandRadius(double angle, long worldSeed) {
        // This method remains unchanged
        long combinedSeed = worldSeed ^ (long)(centerX * 1000) ^ (long)(centerZ * 1000);
        XoroshiroRandomSource random = new XoroshiroRandomSource(combinedSeed);
        double radius = baseRadius;

        for (int i = 0; i < variations; i++) {
            double pointAngle = (2.0 * Math.PI * i) / variations;
            double influence = Math.cos(angle - pointAngle);
            if (influence > 0) {
                random.setSeed(combinedSeed + i);
                double variation = (random.nextFloat() * 2.0 - 1.0) * 0.3;
                radius += influence * influence * variation * baseRadius;
            }
        }

        for (int i = 0; i < variations * 2; i++) {
            double pointAngle = (2.0 * Math.PI * i) / (variations * 2);
            double influence = Math.cos(2.0 * (angle - pointAngle));
            if (influence > 0) {
                random.setSeed(combinedSeed + variations + i);
                double variation = (random.nextFloat() * 2.0 - 1.0) * 0.15;
                radius += influence * variation * baseRadius;
            }
        }

        return Math.max(radius, baseRadius * 0.3);
    }

    @Override
    public void fillArray(double[] array, ContextProvider contextProvider) {
        contextProvider.fillAllDirectly(array, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new IslandDensityFunction(
                centerX, centerZ, baseRadius, variations, minValue, maxValue
        ));
    }

    @Override
    public double minValue() {
        return minValue;
    }

    @Override
    public double maxValue() {
        return maxValue;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return KeyDispatchDataCodec.of(CODEC);
    }
}