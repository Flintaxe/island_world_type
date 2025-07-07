package io.github.flintaxe.neoforge;

import io.github.flintaxe.block.entity.ModBlockEntities;
import io.github.flintaxe.block.entity.renderer.ItemInABottleBlockEntityRenderer;
import io.github.flintaxe.worldgen.levelgen.ModDensityFunctions;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

import io.github.flintaxe.IslandWorldMod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(IslandWorldMod.MOD_ID)
public final class IslandWorldModNeoForge {
    public IslandWorldModNeoForge(IEventBus modEventBus, ModContainer modContainer) {



        // Run our common setup.
        IslandWorldMod.init();

        modEventBus.addListener(this::onRegisterEvent);
    }


    @EventBusSubscriber(modid = IslandWorldMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        /**
        * registerBER means register block entity renderers lol!!
        */

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.ITEM_IN_BOTTLE_BENTITY.get(), ItemInABottleBlockEntityRenderer::new);
        }
    }


    private void onRegisterEvent(RegisterEvent event) {
    }
}
