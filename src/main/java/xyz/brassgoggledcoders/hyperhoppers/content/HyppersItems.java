package xyz.brassgoggledcoders.hyperhoppers.content;

import com.tterrag.registrate.util.entry.ItemEntry;
import xyz.brassgoggledcoders.hyperhoppers.HyperHoppers;
import xyz.brassgoggledcoders.hyperhoppers.item.FilterUpgradeItem;
import xyz.brassgoggledcoders.hyperhoppers.item.UpgradeItem;
import xyz.brassgoggledcoders.hyperhoppers.upgrade.filter.SoftFilterUpgrade;
import xyz.brassgoggledcoders.hyperhoppers.upgrade.slot.FluidicUpgrade;

@SuppressWarnings("unused")
public class HyppersItems {

    public static final ItemEntry<UpgradeItem> FLUIDIC_UPGRADE = HyperHoppers.getRegistrate()
            .object("fluidic_upgrade")
            .item(properties -> new UpgradeItem(properties, FluidicUpgrade::new))
            .register();

    public static final ItemEntry<FilterUpgradeItem> SOFT_WHITELIST_FILTER = HyperHoppers.getRegistrate()
            .object("soft_whitelist_filter")
            .item(properties -> new FilterUpgradeItem(properties, items -> new SoftFilterUpgrade(items, true)))
            .properties(properties -> properties.stacksTo(1))
            .register();

    public static void setup() {

    }
}
