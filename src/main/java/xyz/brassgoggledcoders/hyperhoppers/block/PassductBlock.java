package xyz.brassgoggledcoders.hyperhoppers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.blockentity.PassductBlockEntity;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

public class PassductBlock extends Block implements EntityBlock {
    public static final Property<Direction> FACING = BlockStateProperties.FACING;
    public static final Property<PassingShape> SHAPE = EnumProperty.create(
            "shape",
            PassingShape.class
    );

    public PassductBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(SHAPE, PassingShape.STRAIGHT)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, SHAPE);
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
