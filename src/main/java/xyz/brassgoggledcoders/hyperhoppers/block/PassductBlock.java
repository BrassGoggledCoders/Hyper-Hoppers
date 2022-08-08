package xyz.brassgoggledcoders.hyperhoppers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.blockentity.PassductBlockEntity;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class PassductBlock extends Block implements EntityBlock {
    public static final Property<Direction> FACING = BlockStateProperties.FACING;
    public static final Property<Direction> SPOUT = EnumProperty.create("spout", Direction.class);

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
