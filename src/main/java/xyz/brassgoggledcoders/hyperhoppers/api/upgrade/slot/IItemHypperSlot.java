package xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot;

import net.minecraft.world.item.ItemStack;

public interface IItemHypperSlot extends IHypperSlot<ItemStack> {
    ItemStack extract(int amount, boolean simulate);

    ItemStack insert(ItemStack itemStack, boolean simulate);

    boolean isValid(ItemStack itemStack);

    int getSlotLimit();

    @Override
    default HypperSlotType getType() {
        return HypperSlotTypes.ITEM;
    }
}
