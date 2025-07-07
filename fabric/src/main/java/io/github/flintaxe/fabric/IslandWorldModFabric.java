package io.github.flintaxe.fabric;

import io.github.flintaxe.worldgen.levelgen.ModDensityFunctions;
import net.fabricmc.api.ModInitializer;

import io.github.flintaxe.IslandWorldMod;

public final class IslandWorldModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.



        // Run our common setup.
        IslandWorldMod.init();
    }
}
