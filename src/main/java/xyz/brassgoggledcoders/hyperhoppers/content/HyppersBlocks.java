package xyz.brassgoggledcoders.hyperhoppers.content;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;
import xyz.brassgoggledcoders.hyperhoppers.HyperHoppers;
import xyz.brassgoggledcoders.hyperhoppers.block.PassductBlock;
import xyz.brassgoggledcoders.hyperhoppers.blockentity.PassductBlockEntity;
import xyz.brassgoggledcoders.hyperhoppers.util.BlockModelHelper;

public class HyppersBlocks {
    public static final BlockEntry<PassductBlock> IRON_PASSDUCT = HyperHoppers.getRegistrate()
            .object("iron_passduct")
            .block(properties -> new PassductBlock(properties, 1, 4))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate(BlockModelHelper::passductBlockState)
            .blockEntity(PassductBlockEntity::new)
            .build()
            .item()
            .model((context, provider) -> provider.blockItem(context, "_full"))
            .build()
            .register();

    public static void setup() {

    }
}
