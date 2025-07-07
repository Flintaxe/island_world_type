package io.github.flintaxe.fabric.client;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import io.github.flintaxe.block.entity.ModBlockEntities;
import io.github.flintaxe.block.entity.renderer.ItemInABottleBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;

public final class IslandWorldModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BlockEntityRendererRegistry.register(ModBlockEntities.ITEM_IN_BOTTLE_BENTITY.get(),
                ItemInABottleBlockEntityRenderer::new);
    }
}
