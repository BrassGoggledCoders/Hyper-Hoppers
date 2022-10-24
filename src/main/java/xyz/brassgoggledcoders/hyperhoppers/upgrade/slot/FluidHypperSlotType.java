package xyz.brassgoggledcoders.hyperhoppers.upgrade.slot;

import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.HypperSlotType;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IFluidHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlotRenderProperties;
import xyz.brassgoggledcoders.hyperhoppers.slot.FluidicHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.util.FluidRenderHelper;

import java.util.function.Consumer;

public class FluidHypperSlotType extends HypperSlotType {
    public FluidHypperSlotType() {
        super(FluidicHypperSlot::createForFluid);
    }

    @Override
    public void initializeClient(Consumer<IHypperSlotRenderProperties> consumer) {
        consumer.accept((screen, poseStack, slotX, slotY, hypperSlot) -> {
            if (hypperSlot instanceof IFluidHypperSlot fluidHypperSlot) {
                FluidRenderHelper.renderFluidSlot(poseStack, fluidHypperSlot.getFluid(), fluidHypperSlot.getCapacity(), slotX, slotY);
            }
        });
    }
}
