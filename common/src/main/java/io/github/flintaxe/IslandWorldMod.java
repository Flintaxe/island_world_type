package io.github.flintaxe;


import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.github.flintaxe.block.ModBlocks;
import io.github.flintaxe.block.entity.ModBlockEntities;
import io.github.flintaxe.item.ModItems;
import io.github.flintaxe.worldgen.levelgen.ModDensityFunctions;
import net.minecraft.client.renderer.RenderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IslandWorldMod {
    public static final String MOD_ID = "island_world";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
        ModDensityFunctions.init();

        EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
            ClientLifecycleEvent.CLIENT_SETUP.register(minecraft -> {
                // This runs after registries are populated
                RenderTypeRegistry.register(RenderType.cutout(), ModBlocks.ITEM_IN_BOTTLE.get());
            });
        });

    }

}
