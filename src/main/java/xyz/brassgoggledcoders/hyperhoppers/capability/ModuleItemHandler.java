package xyz.brassgoggledcoders.hyperhoppers.capability;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.module.IModuleProvider;
import xyz.brassgoggledcoders.hyperhoppers.module.Module;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ModuleItemHandler implements IItemHandler {
    private final int slots;
    private final NonNullList<Pair<ItemStack, Set<Module>>> modules;

    public ModuleItemHandler(int slots) {
        this.slots = slots;
        this.modules = NonNullList.withSize(slots, Pair.of(ItemStack.EMPTY, Collections.emptySet()));
    }

    @Override
    public int getSlots() {
        return this.slots;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return modules.get(slot).getFirst();
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.getItem() instanceof IModuleProvider provider && modules.get(slot).getFirst().isEmpty()) {
            ItemStack newStack = stack.split(1);
            if (!newStack.isEmpty() && !simulate) {
                modules.set(slot, Pair.of(newStack, new HashSet<>(provider.apply(newStack))));
            }
            return stack;
        }
        return stack;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        Pair<ItemStack, Set<Module>> modulePair = this.modules.get(slot);
        if (!simulate && !modulePair.getFirst().isEmpty()) {
            this.modules.set(slot, Pair.of(ItemStack.EMPTY, Collections.emptySet()));
        }
        return modulePair.getFirst();
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.getItem() instanceof IModuleProvider;
    }
}
