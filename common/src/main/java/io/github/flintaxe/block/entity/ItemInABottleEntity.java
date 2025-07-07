package io.github.flintaxe.block.entity;

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

public class ItemInABottleEntity extends BlockEntity implements Container, RandomizableContainer {
    @Nullable
    protected ResourceKey<LootTable> lootTable;
    protected long lootTableSeed = 0L;
    public ItemStack item;


    public ItemInABottleEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ITEM_IN_BOTTLE_BENTITY.get(), blockPos, blockState);
        item = ItemStack.EMPTY;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) { //TODO: THIS ISN'T WORKING AT ALL HELP
        super.loadAdditional(tag, provider);
        if (tag.contains("LootTable", 8)) {
            this.lootTable = ResourceKey.create(Registries.LOOT_TABLE,
                    ResourceLocation.tryParse(tag.getString("LootTable")));
            this.lootTableSeed = tag.getLong("LootTableSeed");
        } else {
            NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(tag, items, provider);
            this.item = items.get(0);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) { //TODO: THIS ISN'T WORKING AT ALL HELP
        super.saveAdditional(tag, provider);
        if (this.lootTable != null) {
            tag.putString("LootTable", this.lootTable.toString());
            if (this.lootTableSeed != 0L) {
                tag.putLong("LootTableSeed", this.lootTableSeed);
            }
        } else {
            NonNullList<ItemStack> items = NonNullList.withSize(1, this.item);
            ContainerHelper.saveAllItems(tag, items, provider);
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() { //TODO: THIS ISN'T WORKING AT ALL I THINK
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) { //TODO: THIS ISN'T WORKING AT ALL I THINK
        return saveWithoutMetadata(provider);
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
            return item.split(amount);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if (i == 0) {
            ItemStack itemStack = item;
            item = ItemStack.EMPTY;
            return itemStack;
        }
        return ItemStack.EMPTY;

    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i == 0) {
            item = itemStack;
        }

    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);

    }

    @Override
    public void clearContent() {
        item = ItemStack.EMPTY;
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
        }
    }

    @Override
    public @Nullable ResourceKey<LootTable> getLootTable() {
        return lootTable;// TODO
    }

    @Override
    public void setLootTable(@Nullable ResourceKey<LootTable> resourceKey) {
        lootTable = resourceKey;//TODO
    }

    @Override
    public long getLootTableSeed() {
        return lootTableSeed; //TODO
    }

    @Override
    public void setLootTableSeed(long l) {
        lootTableSeed = l;//TODO
    }
}
