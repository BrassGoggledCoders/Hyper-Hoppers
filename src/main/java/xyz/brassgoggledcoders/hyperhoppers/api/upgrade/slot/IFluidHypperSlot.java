package xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface IFluidHypperSlot extends IHypperSlot<FluidStack>, IFluidTank {
    @Override
    default HypperSlotType getType() {
        return HypperSlotTypes.FLUID;
    }

    @Override
    default FluidStack getContent() {
        return this.getFluid();
    }
}
