package com.kyanite.deeperdarker.registry.blocks.custom;

import com.kyanite.deeperdarker.DeeperAndDarker;
import com.kyanite.deeperdarker.registry.blocks.custom.entity.AncientChestEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ai.behavior.ShowTradesToPlayer;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class AncientChestBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public AncientChestEntity entity;

    private VoxelShape openShape = Stream.of(Block.box(1, 0, 1, 15, 10, 15)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    private VoxelShape closedShape = Stream.of(
            Block.box(1, 9, 1, 15, 15, 15), Block.box(1, 0, 1, 15, 10, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public AncientChestBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH))).setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue(FACING, rotation.rotate((Direction)blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation((Direction)blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, WATERLOGGED});
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        if ((Boolean)blockState.getValue(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
        }

        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        this.entity = new AncientChestEntity(blockPos, blockState);
        return this.entity;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return (level0, pos, state0, blockEntity) -> ((AncientChestEntity) blockEntity).tick();
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(this.entity == null) return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);

        if(this.entity.cooldownTicks == 0) {
            if(this.entity.closeTicks > 3) {
                MenuProvider menuProvider = this.getMenuProvider(blockState, level, blockPos);
                if (menuProvider != null) {
                    player.openMenu(menuProvider);
                }
                DeeperAndDarker.LOGGER.info("HI");
                return InteractionResult.SUCCESS;
            }

            this.entity.lidAttempts = this.entity.lidAttempts + 3;
            DeeperAndDarker.LOGGER.info(String.valueOf(this.entity.lidAttempts));
            if(this.entity.lidAttempts > 9) {
                if(level.isClientSide()) {
                    level.playSound(player, blockPos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 1, 1);
                }
                this.entity.closeTicks = 720;
            }else{
                this.entity.wiggleTicks = 15;
                if(level.isClientSide()) {
                    level.playSound(player, blockPos, SoundEvents.DEEPSLATE_TILES_STEP, SoundSource.BLOCKS, 1, 1);
                }
            }
            this.entity.cooldownTicks = 65;
            return InteractionResult.CONSUME;
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if(this.entity == null) return closedShape;
        return(this.entity.lidAttempts == 10 ? openShape : closedShape);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}