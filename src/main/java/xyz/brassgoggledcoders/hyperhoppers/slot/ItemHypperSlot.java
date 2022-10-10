package xyz.brassgoggledcoders.hyperhoppers.slot;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.HypperSlotType;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.HypperSlotTypes;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IItemHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.capability.HypperSlotCapabilityProvider;
import xyz.brassgoggledcoders.hyperhoppers.capability.ItemHypperSlotItemHandler;

public class ItemHypperSlot implements IItemHypperSlot {

    private ItemStack itemStack = ItemStack.EMPTY;

    @Override
    public HypperSlotType getType() {
        return HypperSlotTypes.ITEM;
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
        if (replacement instanceof ItemHypperSlot itemHypperSlot) {
            this.itemStack = itemHypperSlot.insert(this.itemStack, false);
        }
        if (!this.itemStack.isEmpty()) {
            this.itemStack = hypper.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    .map(cap -> ItemHandlerHelper.insertItemStacked(cap, this.itemStack, false))
                    .orElse(this.itemStack);
        }
        if (!this.itemStack.isEmpty()) {
            BlockPos blockPos = hypper.getHypperPos();
            Containers.dropItemStack(hypper.getHypperLevel(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack);
        }
    }

    @Override
    public ItemStack insert(ItemStack itemStack, boolean simulate) {
        return itemStack;
    }

    @Override
    public boolean isValid(ItemStack itemStack) {
        return true;
    }

    @Override
    public int getSlotLimit() {
        return 64;
    }

    @Override
    public ItemStack extract(int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    public static ICapabilityProvider createForItem(IHypper hypper) {
        return new HypperSlotCapabilityProvider<>(
                hypper,
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                ItemHypperSlotItemHandler::new
        );
    }
}
