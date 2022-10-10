package xyz.brassgoggledcoders.hyperhoppers.slot;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
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
