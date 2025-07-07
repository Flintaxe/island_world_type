package io.github.flintaxe.block;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import io.github.flintaxe.IslandWorldMod;
import io.github.flintaxe.block.custom.ItemInABottle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(IslandWorldMod.MOD_ID, Registries.BLOCK);

    public static RegistrySupplier<ItemInABottle> ITEM_IN_BOTTLE;

    public static void init() {
        ITEM_IN_BOTTLE = BLOCKS.register(ResourceLocation.fromNamespaceAndPath(IslandWorldMod.MOD_ID, "item_in_bottle"), () -> new ItemInABottle(
                BlockBehaviour.Properties.of()
                        .strength(0.3F)
                        .sound(SoundType.GLASS)
                        .noOcclusion()
                        .isValidSpawn(ModBlocks::never)
                        .isRedstoneConductor(ModBlocks::never)
                        .isSuffocating(ModBlocks::never)
                        .isViewBlocking(ModBlocks::never)
                        .pushReaction(PushReaction.DESTROY)
        ));



        BLOCKS.register();
    }

    private static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }
}
