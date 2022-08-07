package xyz.brassgoggledcoders.hyperhoppers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
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

public class PassductBlock extends Block implements EntityBlock {
    public static final Property<Direction> FACING = BlockStateProperties.FACING;
    public static final Property<Direction> SPOUT = EnumProperty.create("spout", Direction.class);

    public PassductBlock(Properties properties) {
        super(properties);
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
        Direction spout = pContext.getClickedFace();
        Direction facing = pContext.getNearestLookingDirection().getOpposite();

        if (spout == facing) {
            facing = facing.getOpposite();
        }

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(SPOUT, spout);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PassductBlockEntity(
                HyppersBlocks.IRON_PASSDUCT.getSibling(ForgeRegistries.BLOCK_ENTITIES)
                        .get(),
                pPos,
                pState
        );
    }
}
