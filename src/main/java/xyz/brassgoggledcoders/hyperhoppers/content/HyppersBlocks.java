package xyz.brassgoggledcoders.hyperhoppers.content;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import xyz.brassgoggledcoders.hyperhoppers.HyperHoppers;
import xyz.brassgoggledcoders.hyperhoppers.block.PassductBlock;
import xyz.brassgoggledcoders.hyperhoppers.block.PassingShape;
import xyz.brassgoggledcoders.hyperhoppers.blockentity.PassductBlockEntity;

public class HyppersBlocks {
    public static final BlockEntry<PassductBlock> IRON_PASSDUCT = HyperHoppers.getRegistrate()
            .object("iron_passduct")
            .block(PassductBlock::new)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate((context, provider) -> {
                ModelFile angled = provider.models()
                        .withExistingParent("iron_passduct_angled", provider.modLoc("block/passduct_angled"))
                        .texture("material", provider.mcLoc("block/iron_block"));
                ModelFile straight = provider.models()
                        .withExistingParent("iron_passduct_straight", provider.modLoc("block/passduct_straight"))
                        .texture("material", provider.mcLoc("block/iron_block"));

                provider.getVariantBuilder(context.get())
                        .forAllStates(blockState -> {
                            Direction facing = blockState.getValue(PassductBlock.FACING);
                            PassingShape shape = blockState.getValue(PassductBlock.SHAPE);
                            boolean positive = facing.getAxisDirection() == AxisDirection.POSITIVE;
                            int y = switch (facing.getAxis()) {
                                case X -> 0;
                                case Y -> (shape.getRotation() + (positive ? 0 : 180)) % 360;
                                case Z -> 0;
                            };
                            int x = switch (facing.getAxis()) {
                                case X -> 0;
                                case Y -> facing.getAxisDirection() == AxisDirection.POSITIVE ? 0 : 180;
                                case Z -> facing.getAxisDirection() == AxisDirection.POSITIVE ? 90 : 270;
                            };
                            return ConfiguredModel.builder()
                                    .modelFile(switch (shape) {
                                        case STRAIGHT -> straight;
                                        case ZERO, NINETY, ONE_EIGHTY, TWO_SEVENTY -> angled;
                                    })
                                    .rotationX(x)
                                    .rotationY(y)
                                    .build();
                        });
            })
            .blockEntity(PassductBlockEntity::new)
            .build()
            .item()
            .model((context, provider) -> provider.blockItem(context, "_straight"))
            .build()
            .register();

    public static void setup() {

    }
}
