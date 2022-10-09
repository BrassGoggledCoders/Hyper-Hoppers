package xyz.brassgoggledcoders.hyperhoppers.slot;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.HypperSlotType;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;

public class ItemSlot implements IHypperSlot<ItemStack> {
    public static HypperSlotType ITEM = new HypperSlotType(hypper -> null);

    private ItemStack itemStack = ItemStack.EMPTY;

    @Override
    public HypperSlotType getType() {
        return ITEM;
    }

    @Override
    public ItemStack getContent() {
        return itemStack;
    }

    @Override
    public void setContent(ItemStack content) {
        this.itemStack = content;
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("itemStack", this.itemStack.save(new CompoundTag()));
        return compoundTag;
    }

    @Override
    public void fromNBT(CompoundTag compoundTag) {
        this.itemStack = ItemStack.of(compoundTag.getCompound("itemStack"));
    }

    @Override
    public void onReplaced(@NotNull IHypper hypper, @Nullable IHypperSlot<?> replacement) {
        if (replacement instanceof ItemSlot itemSlot) {
            this.itemStack = itemSlot.insert(this.itemStack, false);
        }
        if (!itemStack.isEmpty()) {
            BlockPos blockPos = hypper.getHypperPos();
            Containers.dropItemStack(hypper.getHypperLevel(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack);
        }
    }

    public ItemStack insert(ItemStack itemStack, boolean simulate) {
        return itemStack;
    }

    public ItemStack extract(ItemStack itemStack, boolean simulate) {
        return ItemStack.EMPTY;
    }
}
