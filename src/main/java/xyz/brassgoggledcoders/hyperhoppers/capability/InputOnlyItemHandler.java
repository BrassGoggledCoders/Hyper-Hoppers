package xyz.brassgoggledcoders.hyperhoppers.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class InputOnlyItemHandler implements IItemHandler {
    private final IItemHandler internal;

    public InputOnlyItemHandler(IItemHandler internal) {
        this.internal = internal;
    }

    @Override
    public int getSlots() {
        return this.internal.getSlots();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.internal.getStackInSlot(slot);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return this.internal.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.internal.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.internal.isItemValid(slot, stack);
    }
}
