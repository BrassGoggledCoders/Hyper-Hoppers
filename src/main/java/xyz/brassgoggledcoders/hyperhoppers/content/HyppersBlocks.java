package xyz.brassgoggledcoders.hyperhoppers.content;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import xyz.brassgoggledcoders.hyperhoppers.HyperHoppers;
import xyz.brassgoggledcoders.hyperhoppers.block.PassductBlock;
import xyz.brassgoggledcoders.hyperhoppers.blockentity.PassductBlockEntity;
import xyz.brassgoggledcoders.hyperhoppers.util.BlockModelHelper;

public class HyppersBlocks {
    public static final BlockEntry<PassductBlock> IRON_PASSDUCT = HyperHoppers.getRegistrate()
            .object("iron_passduct")
            .block(properties -> new PassductBlock(properties, 1, 4, true))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate(BlockModelHelper::passductBlockState)
            .item()
            .model((context, provider) -> provider.blockItem(context, "_full"))
            .build()
            .register();

    public static final BlockEntry<PassductBlock> COPPER_PASSDUCT = HyperHoppers.getRegistrate()
            .object("copper_passduct")
            .block(properties -> new PassductBlock(properties, 3, 1, true))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate(BlockModelHelper::passductBlockState)
            .item()
            .model((context, provider) -> provider.blockItem(context, "_full"))
            .build()
            .register();

    public static final BlockEntry<PassductBlock> GOLD_PASSDUCT = HyperHoppers.getRegistrate()
            .object("gold_passduct")
            .block(properties -> new PassductBlock(properties, 5, 1, true))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate(BlockModelHelper::passductBlockState)
            .item()
            .model((context, provider) -> provider.blockItem(context, "_full"))
            .build()
            .register();

    public static final BlockEntry<PassductBlock> WOOD_PASSDUCT = HyperHoppers.getRegistrate()
            .object("wood_passduct")
            .block(properties -> new PassductBlock(properties, 1, 1, false))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate(BlockModelHelper::passductBlockState)
            .item()
            .model((context, provider) -> provider.blockItem(context, "_full"))
            .build()
            .register();

    public static BlockEntityEntry<PassductBlockEntity> PASSDUCT_ENTITY = HyperHoppers.getRegistrate()
            .blockEntity(PassductBlockEntity::new)
            .validBlock(IRON_PASSDUCT)
            .validBlock(GOLD_PASSDUCT)
            .validBlock(COPPER_PASSDUCT)
            .validBlock(WOOD_PASSDUCT)
            .register();

    public static void setup() {

    }
}
