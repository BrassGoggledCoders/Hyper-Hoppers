package xyz.brassgoggledcoders.hyperhoppers.util;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import xyz.brassgoggledcoders.hyperhoppers.block.PassductBlock;

import java.util.Arrays;

public class BlockModelHelper {

    public static <T extends Block> void passductBlockState(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider) {
        spoutBlockState(context, provider, "passduct");
    }

    public static <T extends Block> void hypperBlockState(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider) {
        spoutBlockState(context, provider, "hypper");
    }

    public static <T extends Block> void spoutBlockState(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider, String name) {
        BlockModelBuilder top = provider.models()
                .withExistingParent("%s_top".formatted(context.getName()), provider.modLoc("block/%s_top".formatted(name)))
                .texture("material", provider.modLoc("block/%s".formatted(context.getName())));
        BlockModelBuilder middleStraight = provider.models()
                .withExistingParent("%s_middle_straight".formatted(context.getName()), provider.modLoc("block/%s_middle_straight".formatted(name)))
                .texture("material", provider.modLoc("block/%s".formatted(context.getName())));
        BlockModelBuilder middleAngle = provider.models()
                .withExistingParent("%s_middle_angle".formatted(context.getName()), provider.modLoc("block/%s_middle_angle".formatted(name)))
                .texture("material", provider.modLoc("block/%s".formatted(context.getName())));
        BlockModelBuilder spout = provider.models()
                .withExistingParent("%s_spout".formatted(context.getName()), provider.modLoc("block/%s_spout".formatted(name)))
                .texture("material", provider.modLoc("block/%s".formatted(context.getName())));

        provider.models()
                .withExistingParent("%s_full".formatted(context.getName()), provider.mcLoc("block/block"))
                .customLoader(CompositeModelBuilder::begin)
                .submodel("top", top)
                .submodel("middle", middleStraight)
                .submodel("spout", spout)
                .end();

        MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(context.get());

        for (Direction direction : Direction.values()) {
            int x = switch (direction) {
                case NORTH, EAST, SOUTH, WEST -> 90;
                case DOWN -> 180;
                default -> 0;
            };
            int y = switch (direction) {
                case NORTH, UP, DOWN -> 0;
                case EAST -> 90;
                case SOUTH -> 180;
                case WEST -> 270;
            };

            builder.part()
                    .modelFile(top)
                    .rotationX(x)
                    .rotationY(y)
                    .addModel()
                    .condition(PassductBlock.FACING, direction)
                    .end();

            builder.part()
                    .modelFile(spout)
                    .rotationX(x)
                    .rotationY(y)
                    .addModel()
                    .condition(PassductBlock.SPOUT, direction.getOpposite())
                    .end();

            builder.part()
                    .modelFile(middleStraight)
                    .rotationX(x)
                    .rotationY(y)
                    .addModel()
                    .condition(PassductBlock.SPOUT, axis(direction, true))
                    .condition(PassductBlock.FACING, direction)
                    .end();

            builder.part()
                    .modelFile(middleAngle)
                    .rotationX(x)
                    .rotationY(y)
                    .addModel()
                    .condition(PassductBlock.SPOUT, axis(direction, false))
                    .condition(PassductBlock.FACING, direction)
                    .end();
        }
    }

    public static Direction[] axis(Direction direction, boolean matching) {
        return Arrays.stream(Direction.values())
                .filter(value -> matching == (value.getAxis() == direction.getAxis()))
                .toArray(Direction[]::new);
    }
}
