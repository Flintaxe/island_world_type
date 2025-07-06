package io.github.flintaxe.block.custom;

import com.mojang.serialization.MapCodec;
import io.github.flintaxe.block.entity.ItemInABottleEntity;
import io.github.flintaxe.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ItemInABottle extends BaseEntityBlock {
    public static final MapCodec<ItemInABottle> CODEC = simpleCodec(ItemInABottle::new);

    public static DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape SHAPE_NORTH = Block.box(6.0D, 0.0D, 5.0D, 10.0D, 4.0D, 13.0D);
    public static final VoxelShape SHAPE_EAST = Block.box(3.0D, 0.0D, 6.0D, 11.0D, 4.0D, 10.0D);
    public static final VoxelShape SHAPE_SOUTH = Block.box(6.0D, 0.0D, 3.0D, 10.0D, 4.0D, 11.0D);
    public static final VoxelShape SHAPE_WEST = Block.box(5.0D, 0.0D, 6.0D, 13.0D, 4.0D, 10.0D);

    public ItemInABottle(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        FACING = HorizontalDirectionalBlock.FACING;
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(FACING)) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH; // NORTH is default, no rotation needed
        };
    }


    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /* entity shit */

    @Override
    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ItemInABottleEntity( ModBlockEntities.ITEM_IN_BOTTLE_BENTITY.get(), blockPos, blockState );
    }

    @Override
    protected void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getBlock() != blockState2.getBlock()) {
            if(level.getBlockEntity(blockPos) instanceof ItemInABottleEntity itemInABottleEntity) {
                itemInABottleEntity.drops();
                level.updateNeighbourForOutputSignal(blockPos, this);
            }
        }

        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(level.getBlockEntity(blockPos) instanceof ItemInABottleEntity itemInABottleEntity) {
            if (itemInABottleEntity.isEmpty() && !itemStack.isEmpty()){
                itemInABottleEntity.item = itemStack.copyWithCount(1);
                if (!player.isCreative()) itemStack.shrink(1);
                level.playSound(player, blockPos, SoundEvents.ITEM_FRAME_PLACE, SoundSource.BLOCKS);
                return ItemInteractionResult.CONSUME;
            }
        }
        return ItemInteractionResult.FAIL;
    }
}
