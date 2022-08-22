package xyz.brassgoggledcoders.hyperhoppers.capability;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.slot.HypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.slot.ItemSlot;

import java.util.List;

public class HypperSlotsHandler implements IItemHandler {
    private final HypperSlot[] slots;
    public HypperSlotsHandler(int size, Runnable onChange) {
        this.slots = new HypperSlot[size];
        for (int i = 0; i < size; i++) {
            this.slots[i] = new ItemSlot();
            this.slots[i].setOnChange(onChange);
        }
    }

    @Override
    public int getSlots() {
        return slots.length;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slots[slot].getItemStack();
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!slots[slot].isValid(stack)) {
            return stack;
        }
        return slots[slot].insert(stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        }
        return slots[slot].extract(amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slots[slot].isValid(stack);
    }

    public List<HypperSlot> getHypperSlots() {
        return ImmutableList.copyOf(slots);
    }
}
