package xyz.brassgoggledcoders.hyperhoppers.content;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.Tags;
import xyz.brassgoggledcoders.hyperhoppers.HyperHoppers;
import xyz.brassgoggledcoders.hyperhoppers.block.HypperBlock;
import xyz.brassgoggledcoders.hyperhoppers.block.PassductBlock;
import xyz.brassgoggledcoders.hyperhoppers.blockentity.HypperBlockEntity;
import xyz.brassgoggledcoders.hyperhoppers.blockentity.PassductBlockEntity;
import xyz.brassgoggledcoders.hyperhoppers.util.BlockModelHelper;

public class HyppersBlocks {
    public static final BlockEntry<PassductBlock> IRON_PASSDUCT = HyperHoppers.getRegistrate()
            .object("iron_passduct")
            .block(properties -> new PassductBlock(properties, 1, 4, true))
            .initialProperties(Material.METAL, MaterialColor.METAL)
            .properties(properties -> properties.strength(3.0F, 4.8F)
                    .sound(SoundType.METAL)
            )
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate(BlockModelHelper::passductBlockState)
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get(), 4)
                    .pattern("ici")
                    .pattern(" i ")
                    .define('c', Tags.Items.CHESTS)
                    .define('i', Tags.Items.INGOTS_IRON)
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(Tags.Items.INGOTS_IRON))
                    .save(provider)
            )
            .model((context, provider) -> provider.blockItem(context, "_full"))
            .build()
            .register();

    public static final BlockEntry<HypperBlock> IRON_HYPPER = HyperHoppers.getRegistrate()
            .object("iron_hypper")
            .block(properties -> new HypperBlock(properties, 1, 4, 20))
            .initialProperties(Material.METAL, MaterialColor.METAL)
            .properties(properties -> properties.strength(3.0F, 4.8F)
                    .sound(SoundType.METAL)
            )
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate(BlockModelHelper::passductBlockState)
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get(), 4)
                    .pattern("ici")
                    .pattern(" i ")
                    .define('c', Tags.Items.CHESTS)
                    .define('i', Tags.Items.INGOTS_IRON)
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(Tags.Items.INGOTS_IRON))
                    .save(provider)
            )
            .model((context, provider) -> provider.blockItem(context, "_full"))
            .build()
            .register();

    public static final BlockEntry<PassductBlock> COPPER_PASSDUCT = HyperHoppers.getRegistrate()
            .object("copper_passduct")
            .block(properties -> new PassductBlock(properties, 3, 1, true))
            .initialProperties(Material.METAL, MaterialColor.COLOR_ORANGE)
            .properties(properties -> properties.strength(3.0F, 4.8F)
                    .sound(SoundType.METAL)
            )
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate(BlockModelHelper::passductBlockState)
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get(), 4)
                    .pattern("ici")
                    .pattern(" i ")
                    .define('c', Tags.Items.CHESTS)
                    .define('i', Tags.Items.INGOTS_COPPER)
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(Tags.Items.INGOTS_COPPER))
                    .save(provider)
            )
            .model((context, provider) -> provider.blockItem(context, "_full"))
            .build()
            .register();

    public static final BlockEntry<PassductBlock> GOLD_PASSDUCT = HyperHoppers.getRegistrate()
            .object("gold_passduct")
            .block(properties -> new PassductBlock(properties, 5, 1, true))
            .initialProperties(Material.METAL, MaterialColor.GOLD)
            .properties(properties -> properties.strength(3.0F, 4.8F)
                    .sound(SoundType.METAL)
            )
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate(BlockModelHelper::passductBlockState)
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get(), 4)
                    .pattern("ici")
                    .pattern(" i ")
                    .define('c', Tags.Items.CHESTS)
                    .define('i', Tags.Items.INGOTS_GOLD)
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(Tags.Items.INGOTS_GOLD))
                    .save(provider)
            )
            .model((context, provider) -> provider.blockItem(context, "_full"))
            .build()
            .register();

    public static final BlockEntry<PassductBlock> WOOD_PASSDUCT = HyperHoppers.getRegistrate()
            .object("wood_passduct")
            .block(properties -> new PassductBlock(properties, 1, 1, false))
            .initialProperties(Material.WOOD, MaterialColor.WOOD)
            .properties(properties -> properties.strength(3.0F, 4.8F)
                    .sound(SoundType.WOOD)
            )
            .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(BlockBehaviour.Properties::randomTicks)
            .blockstate(BlockModelHelper::passductBlockState)
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .item()
            .recipe((context, provider) -> ShapedRecipeBuilder.shaped(context.get(), 4)
                    .pattern("wcw")
                    .pattern(" w ")
                    .define('c', Tags.Items.CHESTS)
                    .define('w', ItemTags.PLANKS)
                    .unlockedBy("has_item", RegistrateRecipeProvider.has(ItemTags.PLANKS))
                    .save(provider)
            )
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

    public static BlockEntityEntry<HypperBlockEntity> HYPPER_ENTITY = HyperHoppers.getRegistrate()
            .blockEntity(HypperBlockEntity::new)
            .validBlock(IRON_HYPPER)
            .register();

    public static void setup() {

    }
}
