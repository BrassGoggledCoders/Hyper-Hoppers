package xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IItemHypperSlot extends IHypperSlot<ItemStack> {
    ItemStack extract(int amount, boolean simulate);

    ItemStack insert(ItemStack itemStack, boolean simulate);

    boolean isValid(ItemStack itemStack);

    int getSlotLimit();

    @Override
    default boolean allowMenuClick(@NotNull ItemStack itemStack) {
        return this.isValid(itemStack);
    }

    @Override
    default HypperSlotType getType() {
        return HypperSlotTypes.ITEM;
    }
}
