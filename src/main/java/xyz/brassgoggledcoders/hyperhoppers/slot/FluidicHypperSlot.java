package xyz.brassgoggledcoders.hyperhoppers.slot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IFluidHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.capability.FluidHypperSlotFluidHandler;
import xyz.brassgoggledcoders.hyperhoppers.capability.HypperSlotCapabilityProvider;

public class FluidicHypperSlot extends FluidTank implements IFluidHypperSlot {


    public FluidicHypperSlot(int capacity) {
        super(capacity);
    }

    @Override
    public void setContent(FluidStack content) {
        this.setFluid(content);
    }

    @Override
    public boolean allowMenuClick(@NotNull ItemStack itemStack) {
        return itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                .isPresent();
    }

    @Override
    @NotNull
    public ItemStack menuClick(@NotNull ItemStack itemStack) {
        ItemStack containerCopy = ItemHandlerHelper.copyStackWithSize(itemStack, 1);
        LazyOptional<IFluidHandlerItem> lazyOptional = FluidUtil.getFluidHandler(containerCopy);
        boolean tryEmpty = lazyOptional.map(fluidHandlerItem -> {
            for (int i = 0; i < fluidHandlerItem.getTanks(); i++) {
                if (!fluidHandlerItem.getFluidInTank(i).isEmpty()) {
                    return true;
                }
            }
            return false;
        }).orElse(false);

        return lazyOptional.filter(handler -> {
                    if (tryEmpty) {
                        return !FluidUtil.tryFluidTransfer(this, handler, Integer.MAX_VALUE, true).isEmpty();
                    } else {
                        return !FluidUtil.tryFluidTransfer(handler, this, Integer.MAX_VALUE, true).isEmpty();
                    }
                })
                .map(IFluidHandlerItem::getContainer)
                .orElse(itemStack);
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("fluid", this.fluid.writeToNBT(new CompoundTag()));
        return compoundTag;
    }

    @Override
    public void fromNBT(CompoundTag compoundTag) {
        this.fluid = FluidStack.loadFluidStackFromNBT(compoundTag.getCompound("fluid"));
    }

    public static ICapabilityProvider createForFluid(IHypper hypper) {
        return new HypperSlotCapabilityProvider<>(
                hypper,
                CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                FluidHypperSlotFluidHandler::new
        );
    }
}
