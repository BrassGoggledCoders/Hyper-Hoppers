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
import xyz.brassgoggledcoders.hyperhoppers.capability.RunnableItemHandler;

public class ItemHypperSlot implements IItemHypperSlot {

    private final RunnableItemHandler itemHandler;

    public ItemHypperSlot() {
        this.itemHandler = new RunnableItemHandler(1, this::onChange);
    }

    @Override
    public HypperSlotType getType() {
        return HypperSlotTypes.ITEM;
    }

    @Override
    public ItemStack getContent() {
        return this.itemHandler.getStackInSlot(0);
    }

    @Override
    public void setContent(ItemStack content) {
        this.itemHandler.setStackInSlot(0, content);
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("itemStack", this.itemHandler.getStackInSlot(0).save(new CompoundTag()));
        return compoundTag;
    }

    @Override
    public void fromNBT(CompoundTag compoundTag) {
        this.setContent(ItemStack.of(compoundTag.getCompound("itemStack")));
    }

    @Override
    public void onReplaced(@NotNull IHypper hypper, @Nullable IHypperSlot<?> replacement) {
        ItemStack current = this.itemHandler.getStackInSlot(0);
        if (replacement instanceof ItemHypperSlot itemHypperSlot) {
            current = itemHypperSlot.insert(current, false);
        }
        if (!hypper.getHypperLevel().isClientSide()) {
            if (!current.isEmpty()) {
                ItemStack finalCurrent = current;
                current= hypper.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                        .map(cap -> ItemHandlerHelper.insertItemStacked(cap, finalCurrent, false))
                        .orElse(current);
            }
            if (!current.isEmpty()) {
                BlockPos blockPos = hypper.getHypperPos();
                Containers.dropItemStack(hypper.getHypperLevel(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), current);
            }
        }
    }

    @Override
    public ItemStack insert(ItemStack inputStack, boolean simulate) {
        return this.itemHandler.insertItem(0, inputStack, simulate);
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
        return this.itemHandler.extractItem(0, amount, simulate);
    }

    public static ICapabilityProvider createForItem(IHypper hypper) {
        return new HypperSlotCapabilityProvider<>(
                hypper,
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                ItemHypperSlotItemHandler::new
        );
    }
}
