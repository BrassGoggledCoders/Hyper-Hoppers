package xyz.brassgoggledcoders.hyperhoppers.content;

import com.tterrag.registrate.util.entry.ItemEntry;
import xyz.brassgoggledcoders.hyperhoppers.HyperHoppers;
import xyz.brassgoggledcoders.hyperhoppers.item.FilterModuleItem;
import xyz.brassgoggledcoders.hyperhoppers.module.filter.SoftFilterModule;

public class HyppersItems {
    public static final ItemEntry<FilterModuleItem> SOFT_WHITELIST_FILTER = HyperHoppers.getRegistrate()
            .object("soft_whitelist_filter")
            .item(properties -> new FilterModuleItem(properties, items -> new SoftFilterModule(items, true)))
            .properties(properties -> properties.stacksTo(1))
            .register();

    public static void setup() {

    }
}
