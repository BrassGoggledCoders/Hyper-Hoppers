package xyz.brassgoggledcoders.hyperhoppers.upgrade.filter;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.hyperhoppers.upgrade.Upgrade;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SoftFilterUpgrade extends Upgrade implements IFilter {
    private final Set<Item> items;
    private final boolean whitelist;

    public SoftFilterUpgrade(Collection<ItemStack> inputs, boolean whitelist) {
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
