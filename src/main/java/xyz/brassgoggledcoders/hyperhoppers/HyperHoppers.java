package xyz.brassgoggledcoders.hyperhoppers;

import com.google.common.base.Suppliers;
import com.tterrag.registrate.Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.HypperSlotType;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.HypperSlotTypes;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersBlocks;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersItems;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersMenus;
import xyz.brassgoggledcoders.hyperhoppers.slot.FluidicHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.slot.ItemHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.upgrade.slot.FluidHypperSlotType;
import xyz.brassgoggledcoders.shadyskies.containersyncing.ContainerSyncing;

import java.util.function.Supplier;

@Mod(HyperHoppers.ID)
public class HyperHoppers {
    public static final String ID = "hyper_hoppers";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    private static final Supplier<Registrate> REGISTRATE_SUPPLIER = Suppliers.memoize(() -> Registrate.create(ID)
            .creativeModeTab(() -> new CreativeModeTab(ID) {
                @Override
                @NotNull
                public ItemStack makeIcon() {
                    return HyppersBlocks.IRON_PASSDUCT.asStack();
                }
            }, "Hyper Hoppers")
    );

    public HyperHoppers() {
        ContainerSyncing.setup(ID, LOGGER);

        HyppersBlocks.setup();
        HyppersItems.setup();
        HyppersMenus.setup();

        HypperSlotTypes.ITEM = new HypperSlotType(ItemHypperSlot::createForItem);
        HypperSlotTypes.FLUID = new FluidHypperSlotType();
    }

    public static Registrate getRegistrate() {
        return REGISTRATE_SUPPLIER.get();
    }

    public static ResourceLocation rl(String s) {
        return new ResourceLocation(ID, s);
    }
}
