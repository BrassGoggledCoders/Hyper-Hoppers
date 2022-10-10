package xyz.brassgoggledcoders.hyperhoppers.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IItemHypperSlot;

public class ItemHypperSlotItemHandler implements IItemHandlerModifiable {
    private final IHypper hypper;

    public ItemHypperSlotItemHandler(IHypper hypper) {
        this.hypper = hypper;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        IItemHypperSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            itemHypperSlot.setContent(stack);
        }
    }

    @Override
    public int getSlots() {
        int slots = 0;
        for (IHypperSlot<?> slot : this.hypper.getSlots()) {
            if (slot instanceof IItemHypperSlot) {
                slots++;
            }
        }
        return slots;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        IItemHypperSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            return itemHypperSlot.getContent();
        }
        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        IItemHypperSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            return itemHypperSlot.insert(stack, simulate);
        }
        return stack;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        IItemHypperSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            return itemHypperSlot.extract(amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        IItemHypperSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            return itemHypperSlot.getSlotLimit();
        }
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        IItemHypperSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            return itemHypperSlot.isValid(stack);
        }
        return false;
    }

    private IItemHypperSlot getItemHyperSlot(int slotNum) {
        int slots = 0;
        for (IHypperSlot<?> slot : this.hypper.getSlots()) {
            if (slot instanceof IItemHypperSlot itemHypperSlot) {
                if (slots == slotNum) {
                    return itemHypperSlot;
                }
                slots++;
            }
        }
        return null;
    }
}
