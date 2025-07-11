package io.github.flintaxe.block.entity;

import io.github.flintaxe.IslandWorldMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.ticks.ContainerSingleItem;
import org.jetbrains.annotations.Nullable;

public class ItemInABottleEntity extends BlockEntity implements Container {
    public ItemStack item;

    public ItemInABottleEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ITEM_IN_BOTTLE_BENTITY.get(), blockPos, blockState);
        item = ItemStack.EMPTY;
        //IslandWorldMod.LOGGER.info("Created ItemInABottleEntity at {}", blockPos);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        //IslandWorldMod.LOGGER.info("loadAdditional called with tag: {}", tag);
        super.loadAdditional(tag, provider);

        // Load the item stack
        if (tag.contains("Item", 10)) { // 10 = CompoundTag type
            CompoundTag itemTag = tag.getCompound("Item");
            //IslandWorldMod.LOGGER.info("Found Item tag: {}", itemTag);
            this.item = ItemStack.parseOptional(provider, itemTag);
        } else {
            //IslandWorldMod.LOGGER.info("No Item tag found in NBT");
            this.item = ItemStack.EMPTY;
        }
        //IslandWorldMod.LOGGER.info("Loaded item: {}", this.item);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        //IslandWorldMod.LOGGER.info("saveAdditional called, item: {}", this.item);
        super.saveAdditional(tag, provider);

        if (!this.item.isEmpty()) {
            // Use saveOptional and put the Tag directly
            tag.put("Item", this.item.saveOptional(provider));
            //IslandWorldMod.LOGGER.info("Saved item to NBT: {}", this.item.saveOptional(provider));
        } else {
            //IslandWorldMod.LOGGER.info("Item is empty, not saving");
        }
        //IslandWorldMod.LOGGER.info("Final NBT tag: {}", tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = saveWithoutMetadata(provider);
        //IslandWorldMod.LOGGER.info("getUpdateTag called, returning: {}", tag);
        return tag;
    }

    // Add this method to help with client sync
    public void onDataPacket(CompoundTag tag, HolderLookup.Provider provider) {
        //IslandWorldMod.LOGGER.info("onDataPacket called with: {}", tag);
        loadAdditional(tag, provider);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return item.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return i == 0 ? item : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int i, int amount) {
        if (i == 0) {
            ItemStack result = item.split(amount);
            if (!result.isEmpty()) {
                setChanged();
                //IslandWorldMod.LOGGER.info("Removed {} items, remaining: {}", amount, this.item);
            }
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if (i == 0) {
            ItemStack itemStack = item;
            item = ItemStack.EMPTY;
            //IslandWorldMod.LOGGER.info("Removed item without update: {}", itemStack);
            return itemStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i == 0) {
            ItemStack oldItem = this.item;
            item = itemStack;
            setChanged();
            //IslandWorldMod.LOGGER.info("Set item from {} to {}", oldItem, itemStack);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        item = ItemStack.EMPTY;
        setChanged();
        //IslandWorldMod.LOGGER.info("Cleared content");
    }

    public void drops() {
        if (this.level != null && !this.level.isClientSide && !item.isEmpty()) {
            net.minecraft.world.Containers.dropItemStack(
                    this.level,
                    this.worldPosition.getX(),
                    this.worldPosition.getY(),
                    this.worldPosition.getZ(),
                    item
            );
            item = ItemStack.EMPTY;
            //IslandWorldMod.LOGGER.info("Dropped item at {}", this.worldPosition);
        }
    }
}
