package xyz.brassgoggledcoders.hyperhoppers.slot;

import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemSlot extends HypperSlot {
    private ItemStack itemStack = ItemStack.EMPTY;

    @Override
    public void onRemove(Level pLevel, int x, int y, int z) {
        if (!itemStack.isEmpty()) {
            Containers.dropItemStack(pLevel, x, y, z, itemStack);
        }
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}
