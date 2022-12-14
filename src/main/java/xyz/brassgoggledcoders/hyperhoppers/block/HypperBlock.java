package xyz.brassgoggledcoders.hyperhoppers.block;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.blockentity.HypperBlockEntity;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Objects;
import java.util.Random;

public class HypperBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public static final Property<Direction> FACING = BlockStateProperties.FACING;
    public static final Property<Direction> SPOUT = HyperHoppersBlockStateProperties.SPOUT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final Table<Direction, Direction, VoxelShape> VOXEL_SHAPE_TABLE = HashBasedTable.create();
    private static final EnumMap<Direction, VoxelShape> FACING_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
        map.put(Direction.DOWN, Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D));
        map.put(Direction.UP, Block.box(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D));
        map.put(Direction.NORTH, Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 6.0D));
        map.put(Direction.SOUTH, Block.box(0.0D, 0.0D, 10.0D, 16.0D, 16.0D, 16.0D));
        map.put(Direction.EAST, Block.box(10.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D));
        map.put(Direction.WEST, Block.box(0.0D, 0.0D, 0.0D, 6.0D, 16.0D, 16.0D));
    });

    private static  final EnumMap<Direction, VoxelShape> MIDDLE_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
        map.put(Direction.DOWN, Block.box(4.0D, 6.0D, 4.0D, 12.0D, 12.0D, 12.0D));
        map.put(Direction.UP, Block.box(4.0D, 4.0D, 4.0D, 12.0D, 10.0D, 12.0D));
        map.put(Direction.NORTH, Block.box(4.0D, 6.0D, 4.0D, 12.0D, 12.0D, 12.0D));
        map.put(Direction.SOUTH, Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 10.0D));
        map.put(Direction.EAST, Block.box(4.0D, 4.0D, 4.0D, 10.0D, 12.0D, 12.0D));
        map.put(Direction.WEST, Block.box(6.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D));
    });
    private static final EnumMap<Direction, VoxelShape> SPOUT_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
        map.put(Direction.DOWN, Block.box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D));
        map.put(Direction.UP, Block.box(6.0D, 12.0D, 6.0D, 10.0D, 16.0D, 10.0D));
        map.put(Direction.NORTH, Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 4.0D));
        map.put(Direction.SOUTH, Block.box(6.0D, 6.0D, 12.0D, 10.0D, 10.0D, 16.0D));
        map.put(Direction.EAST, Block.box(12.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D));
        map.put(Direction.WEST, Block.box(0.0D, 6.0D, 6.0D, 4.0D, 10.0D, 10.0D));

    });

    private final int slots;
    private final int maxAttempts;

    private final int retryWait;

    public HypperBlock(Properties properties, int upgradeSlots, int maxAttempts, int retryWait) {
        super(properties.randomTicks());
        this.slots = upgradeSlots;
        this.maxAttempts = maxAttempts;
        this.retryWait = retryWait;
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(SPOUT, Direction.DOWN)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = pState.getValue(FACING);
        Direction spout = pState.getValue(SPOUT);

        if (!VOXEL_SHAPE_TABLE.contains(facing, spout)) {
            VoxelShape middleShape = MIDDLE_SHAPE.get(facing);
            if (facing.getAxis() != spout.getAxis()) {
                middleShape = Block.box(
                        (middleShape.min(Direction.Axis.X) * 16) + (facing.getStepX() * 2),
                        (middleShape.min(Direction.Axis.Y) * 16) + (facing.getStepY() * 2),
                        (middleShape.min(Direction.Axis.Z) * 16) + (facing.getStepZ() * 2),
                        (middleShape.max(Direction.Axis.X) * 16) + (facing.getStepX() * 2),
                        (middleShape.max(Direction.Axis.Y) * 16) + (facing.getStepY() * 2),
                        (middleShape.max(Direction.Axis.Z) * 16) + (facing.getStepZ() * 2)
                );
            }
            VOXEL_SHAPE_TABLE.put(facing, spout, Shapes.or(FACING_SHAPE.get(facing), middleShape, SPOUT_SHAPE.get(spout)));
        }

        return Objects.requireNonNull(VOXEL_SHAPE_TABLE.get(facing, spout));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, SPOUT, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        Direction spout = pContext.getClickedFace().getOpposite();
        Direction facing = pContext.getNearestLookingDirection().getOpposite();

        if (spout == facing) {
            facing = facing.getOpposite();
        }

        FluidState fluidState = pContext.getLevel()
                .getFluidState(pContext.getClickedPos());

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(SPOUT, spout)
                .setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof HypperBlockEntity hypperBlockEntity) {
            hypperBlockEntity.openMenu(pPlayer);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new HypperBlockEntity(
                HyppersBlocks.HYPPER_ENTITY.get(),
                pPos,
                pState
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof HypperBlockEntity hypperBlockEntity) {
            hypperBlockEntity.tick(true);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof HypperBlockEntity hypperBlockEntity) {
            hypperBlockEntity.tick(false);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof HypperBlockEntity hypperBlockEntity) {
                for (IHypperSlot<?> hypperSlot : hypperBlockEntity.getSlots()) {
                    hypperSlot.onReplaced(hypperBlockEntity, null);
                }
            }
            pLevel.updateNeighbourForOutputSignal(pPos, this);
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(@NotNull BlockState pState) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof HypperBlockEntity hypperBlockEntity) {
            return hypperBlockEntity.getAnalogSignal();
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (!pLevel.isClientSide()) {
            return createTickerHelper(
                    pBlockEntityType,
                    HyppersBlocks.HYPPER_ENTITY.get(),
                    (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(false)
            );
        }
        return null;
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel,
                                  BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public int getSlots() {
        return slots;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getRetryWait() {
        return retryWait;
    }

    @SuppressWarnings("unchecked")
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>) p_152135_ : null;
    }
}
