package io.github.flintaxe.block.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.flintaxe.IslandWorldMod;
import io.github.flintaxe.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(IslandWorldMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static RegistrySupplier<BlockEntityType<ItemInABottleEntity>> ITEM_IN_BOTTLE_BENTITY;

    public static void init() {

        ITEM_IN_BOTTLE_BENTITY = BLOCK_ENTITIES.register("item_in_bottle_bentity",
                () -> BlockEntityType.Builder.of(
                        (pos, state) -> new ItemInABottleEntity(ModBlockEntities.ITEM_IN_BOTTLE_BENTITY.get(), pos, state),
                        ModBlocks.ITEM_IN_BOTTLE.get()
                ).build(null));

        BLOCK_ENTITIES.register();
    }
}
