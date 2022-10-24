package xyz.brassgoggledcoders.hyperhoppers.capability;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.IUpgradeProvider;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.Upgrade;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UpgradeItemHandler implements IItemHandlerModifiable {
    private final IHypper hypper;
    private final int slots;
    private final NonNullList<Pair<ItemStack, Set<Upgrade>>> upgrades;
    private final Runnable onChange;

    public UpgradeItemHandler(IHypper hypper, int slots) {
        this(hypper, slots, () -> {
        });
    }

    public UpgradeItemHandler(IHypper hypper, int slots, Runnable onChange) {
        this.hypper = hypper;
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
                Set<Upgrade> insertedUpgrades = new HashSet<>(provider.apply(newStack));
                this.upgrades.set(slot, Pair.of(newStack, insertedUpgrades));
                insertedUpgrades.forEach(upgrade -> upgrade.onAdded(this.hypper, slot));
                this.onChange.run();
            }
        }
        return stack;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        Pair<ItemStack, Set<Upgrade>> upgradePair = this.upgrades.get(slot);
        if (!simulate && !upgradePair.getFirst().isEmpty()) {
            upgradePair.getSecond().forEach(upgrade -> upgrade.onRemoved(this.hypper, slot));
            this.upgrades.set(slot, Pair.of(ItemStack.EMPTY, Collections.emptySet()));
            this.onChange.run();
        }
        return upgradePair.getFirst();
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
    public void setStackInSlot(int slot, @NotNull ItemStack newStack) {
        if (newStack.getItem() instanceof IUpgradeProvider provider) {
            this.upgrades.get(slot).getSecond().forEach(upgrade -> upgrade.onRemoved(hypper, slot));
            Set<Upgrade> insertedUpgrades = new HashSet<>(provider.apply(newStack));
            this.upgrades.set(slot, Pair.of(newStack, insertedUpgrades));
            insertedUpgrades.forEach(upgrade -> upgrade.onAdded(this.hypper, slot));
        } else if (newStack.isEmpty()) {
            this.upgrades.get(slot).getSecond().forEach(upgrade -> upgrade.onRemoved(hypper, slot));
            this.upgrades.set(slot, Pair.of(newStack, Collections.emptySet()));
        }
        this.onChange.run();
    }

    public CompoundTag serializeNBT() {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < this.upgrades.size(); i++) {
            if (!upgrades.get(i).getFirst().isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                upgrades.get(i).getFirst().save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("ItemStacks", nbtTagList);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < upgrades.size()) {
                ItemStack itemStack = ItemStack.of(itemTags);
                this.setStackInSlot(slot, itemStack);
            }
        }
    }
}
