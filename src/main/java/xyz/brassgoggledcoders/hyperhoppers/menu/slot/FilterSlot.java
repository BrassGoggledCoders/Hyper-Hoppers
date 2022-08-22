package xyz.brassgoggledcoders.hyperhoppers.menu.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class FilterSlot extends SlotItemHandler {
    public FilterSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @NotNull
    @Override
    public ItemStack remove(int amount) {
        super.remove(amount);
        return ItemStack.EMPTY;
    }

    @Override
    public void set(@Nonnull ItemStack stack) {
        ItemStack copiedStack = stack.copy();
        copiedStack.setCount(1);
        super.set(copiedStack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return 1;
    }
}