package io.github.flintaxe.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.flintaxe.IslandWorldMod;
import io.github.flintaxe.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(IslandWorldMod.MOD_ID, Registries.ITEM);

    public static RegistrySupplier<Item> ITEM_IN_BOTTLE;

    public static void init() {
        ITEM_IN_BOTTLE = ITEMS.register(ResourceLocation.fromNamespaceAndPath(IslandWorldMod.MOD_ID, "item_in_bottle"), () -> new BlockItem(ModBlocks.ITEM_IN_BOTTLE.get(), new Item.Properties()
                .arch$tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
                .stacksTo(1)
        ));


        ITEMS.register();
    }
}
