package io.github.flintaxe.neoforge;

import io.github.flintaxe.block.entity.ModBlockEntities;
import io.github.flintaxe.worldgen.levelgen.ModDensityFunctions;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import io.github.flintaxe.IslandWorldMod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(IslandWorldMod.MOD_ID)
public final class IslandWorldModNeoForge {
    public IslandWorldModNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        //ModDensityFunctions.init();

        // Run our common setup.
        IslandWorldMod.init();

        modEventBus.addListener(this::onRegisterEvent);
    }

    private void onRegisterEvent(RegisterEvent event) {
    }
}
