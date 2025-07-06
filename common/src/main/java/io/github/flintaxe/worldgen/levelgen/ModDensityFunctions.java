package io.github.flintaxe.worldgen.levelgen;

import com.mojang.serialization.MapCodec;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.flintaxe.IslandWorldMod;
import io.github.flintaxe.worldgen.levelgen.IslandDensityFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;

public class ModDensityFunctions {
    public static final DeferredRegister<MapCodec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = DeferredRegister.create(IslandWorldMod.MOD_ID, Registries.DENSITY_FUNCTION_TYPE);
    public static RegistrySupplier<MapCodec<IslandDensityFunction>> ISLAND_DENSITY;

    public static void init() {
        ISLAND_DENSITY = DENSITY_FUNCTION_TYPES.register(ResourceLocation.fromNamespaceAndPath(IslandWorldMod.MOD_ID, "islandgen"), ()-> IslandDensityFunction.CODEC);
        DENSITY_FUNCTION_TYPES.register();
    }

    private static <T extends DensityFunction> MapCodec<T> register(String name, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE,
                ResourceLocation.fromNamespaceAndPath(IslandWorldMod.MOD_ID, name), codec);
    }
}
