package xyz.brassgoggledcoders.hyperhoppers.block;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.blockentity.PassductBlockEntity;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.Objects;
import java.util.Random;

public class PassductBlock extends Block implements EntityBlock {
    public static final Property<Direction> FACING = BlockStateProperties.FACING;
    public static final Property<Direction> SPOUT = EnumProperty.create("spout", Direction.class);

    private static final Table<Direction, Direction, VoxelShape> VOXEL_SHAPE_TABLE = HashBasedTable.create();
    private static final EnumMap<Direction, VoxelShape> FACING_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
        map.put(Direction.DOWN, Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D));
        map.put(Direction.UP, Block.box(3.0D, 8.0D, 3.0D, 13.0D, 16.0D, 13.0D));
        map.put(Direction.NORTH, Block.box(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 8.0D));
        map.put(Direction.SOUTH, Block.box(3.0D, 3.0D, 8.0D, 13.0D, 13.0D, 16.0D));
        map.put(Direction.EAST, Block.box(8.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D));
        map.put(Direction.WEST, Block.box(0.0D, 3.0D, 3.0D, 8.0D, 13.0D, 13.0D));
    });
    private static final EnumMap<Direction, VoxelShape> SPOUT_SHAPE = Util.make(new EnumMap<>(Direction.class), map -> {
        map.put(Direction.DOWN, Block.box(5.0D, 0.0D, 5.0D, 11.0D, 8.0D, 11.0D));
        map.put(Direction.UP, Block.box(5.0D, 8.0D, 5.0D, 11.0D, 16.0D, 11.0D));
        map.put(Direction.NORTH, Block.box(5.0D, 5.0D, 0.0D, 11.0D, 11.0D, 8.0D));
        map.put(Direction.SOUTH, Block.box(5.0D, 5.0D, 8.0D, 11.0D, 11.0D, 16.0D));
        map.put(Direction.EAST, Block.box(8.0D, 5.0D, 5.0D, 16.0D, 11.0D, 11.0D));
        map.put(Direction.WEST, Block.box(0.0D, 5.0D, 5.0D, 8.0D, 11.0D, 11.0D));

    });

    private final int slots;
    private final int maxAttempts;

    private final boolean scheduleTicks;

    public PassductBlock(Properties properties, int slots, int maxAttempts, boolean scheduleTicks) {
        super(properties.randomTicks());
        this.slots = slots;
        this.maxAttempts = maxAttempts;
        this.scheduleTicks = scheduleTicks;
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(SPOUT, Direction.DOWN)
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
            VOXEL_SHAPE_TABLE.put(facing, spout, Shapes.or(FACING_SHAPE.get(facing), SPOUT_SHAPE.get(spout)));
        }

        return Objects.requireNonNull(VOXEL_SHAPE_TABLE.get(facing, spout));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, SPOUT);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        Direction spout = pContext.getClickedFace().getOpposite();
        Direction facing = pContext.getNearestLookingDirection().getOpposite();

        if (spout == facing) {
            facing = facing.getOpposite();
        }

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(SPOUT, spout);
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        pLevel.scheduleTick(pPos, pState.getBlock(), 20);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PassductBlockEntity(
                HyppersBlocks.PASSDUCT_ENTITY.get(),
                pPos,
                pState
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof PassductBlockEntity passductBlock) {
            passductBlock.push(true);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof PassductBlockEntity passductBlock) {
            passductBlock.push(false);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @ParametersAreNonnullByDefault
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof PassductBlockEntity passductBlockEntity) {
                for (ItemStack itemStack : passductBlockEntity.getItemHandler()) {
                    Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), itemStack);
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
        if (pLevel.getBlockEntity(pPos) instanceof PassductBlockEntity passductBlock) {
            return ItemHandlerHelper.calcRedstoneFromInventory(passductBlock.getItemHandler());
        } else {
            return 0;
        }
    }

    public int getSlots() {
        return slots;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public boolean isScheduleTicks() {
        return scheduleTicks;
    }
}
