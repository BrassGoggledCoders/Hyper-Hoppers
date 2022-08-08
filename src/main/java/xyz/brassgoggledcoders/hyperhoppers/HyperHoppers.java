package xyz.brassgoggledcoders.hyperhoppers;

import com.google.common.base.Suppliers;
import com.tterrag.registrate.Registrate;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersBlocks;

import java.util.function.Supplier;

@Mod(HyperHoppers.ID)
public class HyperHoppers {
    public static final String ID = "hyper_hoppers";

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
        HyppersBlocks.setup();
    }

    public static Registrate getRegistrate() {
        return REGISTRATE_SUPPLIER.get();
    }
}
