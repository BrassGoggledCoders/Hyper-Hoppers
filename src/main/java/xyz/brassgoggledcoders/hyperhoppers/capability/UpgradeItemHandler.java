package xyz.brassgoggledcoders.hyperhoppers.capability;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.IUpgradeProvider;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.Upgrade;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UpgradeItemHandler implements IItemHandlerModifiable {
    private final int slots;
    private final NonNullList<Pair<ItemStack, Set<Upgrade>>> upgrades;
    private final Runnable onChange;

    public UpgradeItemHandler(int slots, Runnable onChange) {
        this.slots = slots;
        this.upgrades = NonNullList.withSize(slots, Pair.of(ItemStack.EMPTY, Collections.emptySet()));
        this.onChange = onChange;
    }

    @Override
    public int getSlots() {
        return this.slots;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return upgrades.get(slot).getFirst();
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.getItem() instanceof IUpgradeProvider provider && upgrades.get(slot).getFirst().isEmpty()) {
            stack = stack.copy();
            ItemStack newStack = stack.split(1);
            if (!newStack.isEmpty() && !simulate) {
                this.upgrades.set(slot, Pair.of(newStack, new HashSet<>(provider.apply(newStack))));
                this.onChange.run();
            }
        }
        return stack;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        Pair<ItemStack, Set<Upgrade>> modulePair = this.upgrades.get(slot);
        if (!simulate && !modulePair.getFirst().isEmpty()) {
            this.upgrades.set(slot, Pair.of(ItemStack.EMPTY, Collections.emptySet()));
            this.onChange.run();
        }
        return modulePair.getFirst();
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.getItem() instanceof IUpgradeProvider;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        if (stack.getItem() instanceof IUpgradeProvider provider) {
            this.upgrades.set(slot, Pair.of(stack, new HashSet<>(provider.apply(stack))));
        } else if (stack.isEmpty()) {
            this.upgrades.set(slot, Pair.of(stack, Collections.emptySet()));
        }
    }
}
