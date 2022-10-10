package xyz.brassgoggledcoders.hyperhoppers.capability;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IFluidHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FluidHypperSlotFluidHandler implements IFluidHandler {
    private final IHypper hypper;

    public FluidHypperSlotFluidHandler(IHypper hypper) {
        this.hypper = hypper;
    }

    @Override
    public int getTanks() {
        int tanks = 0;
        for (IHypperSlot<?> slot : this.hypper.getSlots()) {
            if (slot instanceof IFluidHypperSlot) {
                tanks++;
            }
        }
        return tanks;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        IFluidHypperSlot fluidHypperSlot = this.getFluidHyperSlot(tank);
        if (fluidHypperSlot != null) {
            return fluidHypperSlot.getFluid();
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        IFluidHypperSlot fluidHypperSlot = this.getFluidHyperSlot(tank);
        if (fluidHypperSlot != null) {
            return fluidHypperSlot.getCapacity();
        }
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        IFluidHypperSlot fluidHypperSlot = this.getFluidHyperSlot(tank);
        if (fluidHypperSlot != null) {
            return fluidHypperSlot.isFluidValid(stack);
        }
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int filled = 0;
        int tank = 0;
        List<IFluidHypperSlot> fluidHypperSlots = this.getFluidHyperSlots();
        while (tank < fluidHypperSlots.size() && filled < resource.getAmount()) {
            FluidStack filling = resource;
            if (filled != 0) {
                filling = resource.copy();
                if (!filling.isEmpty()) {
                    filling.setAmount(resource.getAmount() - filled);
                }
            }
            filled += fluidHypperSlots.get(tank).fill(filling, action);
            tank++;
        }
        return filled;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack fluidStack = FluidStack.EMPTY;
        Iterator<IFluidHypperSlot> tankIterator = this.getFluidHyperSlots().iterator();
        while (tankIterator.hasNext() && fluidStack.getAmount() < resource.getAmount()) {
            IFluidTank nextTank = tankIterator.next();
            if (fluidStack.isEmpty()) {
                fluidStack = nextTank.drain(resource, action);
            } else if (resource.isFluidEqual(nextTank.getFluid())) {
                fluidStack.grow(nextTank.drain(resource.getAmount() - fluidStack.getAmount(), action).getAmount());
            }
        }
        return fluidStack;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack fluidStack = FluidStack.EMPTY;
        Iterator<IFluidHypperSlot> tankIterator = this.getFluidHyperSlots().iterator();
        while (tankIterator.hasNext() && fluidStack.getAmount() < maxDrain) {
            IFluidTank nextTank = tankIterator.next();
            if (fluidStack.isEmpty()) {
                fluidStack = nextTank.drain(maxDrain, action);
            } else if (fluidStack.isFluidEqual(nextTank.getFluid())) {
                fluidStack.grow(nextTank.drain(maxDrain - fluidStack.getAmount(), action).getAmount());
            }
        }
        return fluidStack;
    }

    private IFluidHypperSlot getFluidHyperSlot(int slotNum) {
        int tanks = 0;
        for (IHypperSlot<?> slot : this.hypper.getSlots()) {
            if (slot instanceof IFluidHypperSlot fluidHypperSlot) {
                if (tanks == slotNum) {
                    return fluidHypperSlot;
                }
                tanks++;
            }
        }
        return null;
    }

    private List<IFluidHypperSlot> getFluidHyperSlots() {
        List<IFluidHypperSlot> fluidHypperSlots = new ArrayList<>();
        for (IHypperSlot<?> slot : this.hypper.getSlots()) {
            if (slot instanceof IFluidHypperSlot fluidHypperSlot) {
                fluidHypperSlots.add(fluidHypperSlot);
            }
        }
        return fluidHypperSlots;
    }
}
