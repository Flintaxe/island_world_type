package io.github.flintaxe.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flintaxe.block.custom.ItemInABottle;
import io.github.flintaxe.block.entity.ItemInABottleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class ItemInABottleBlockEntityRenderer implements BlockEntityRenderer<ItemInABottleEntity> {
    public ItemInABottleBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(ItemInABottleEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = blockEntity.item;

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.1f, 0.5f);
        if(has2DItemModel(stack)) {
            poseStack.scale(0.25f, 0.25f, 0.25f);
        }
        else
        {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }
        poseStack.mulPose(Axis.XN.rotationDegrees(90f));
        switch (blockEntity.getBlockState().getValue(ItemInABottle.FACING)) {
            case NORTH -> poseStack.mulPose(Axis.ZN.rotationDegrees(0f));
            case EAST -> poseStack.mulPose(Axis.ZN.rotationDegrees(90f));
            case SOUTH -> poseStack.mulPose(Axis.ZN.rotationDegrees(180f));
            case WEST -> poseStack.mulPose(Axis.ZN.rotationDegrees(270f));
        }


        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(blockEntity.getLevel(),
                blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos blockPos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, blockPos);
        int sLight = level.getBrightness(LightLayer.SKY, blockPos);

        return LightTexture.pack(bLight, sLight);
    }

    private static boolean has2DItemModel(ItemStack stack) {
        if (stack.isEmpty()) return false;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        BakedModel model = itemRenderer.getModel(stack, null, null, 0);

        // Check if it's a 2D item model (not a 3D block model)
        return !model.isGui3d();
    }
}
