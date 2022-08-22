package xyz.brassgoggledcoders.hyperhoppers.module.filter;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.hyperhoppers.module.Module;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SoftFilterModule extends Module implements IFilter {
    private final Set<Item> items;
    private final boolean whitelist;

    public SoftFilterModule(Collection<ItemStack> inputs, boolean whitelist) {
        this.whitelist = whitelist;
        this.items = new HashSet<>();

        for (ItemStack itemStack : inputs) {
            if (!itemStack.isEmpty()) {
                this.items.add(itemStack.getItem());
            }
        }
    }

    @Override
    public boolean test(ItemStack itemStack) {
        if (whitelist) {
            return this.items.contains(itemStack.getItem());
        } else {
            for (Item item : items) {
                if (item == itemStack.getItem()) {
                    return false;
                }
            }
            return true;
        }
    }
}
