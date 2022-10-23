package xyz.brassgoggledcoders.hyperhoppers.upgrade.slot;

import net.minecraftforge.fluids.FluidAttributes;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.Upgrade;
import xyz.brassgoggledcoders.hyperhoppers.slot.FluidicHypperSlot;

public class FluidicUpgrade extends Upgrade {
    @Override
    public void onAdded(IHypper hypper, int upgradeSlot) {
        hypper.setSlot(upgradeSlot, new FluidicHypperSlot(8 * FluidAttributes.BUCKET_VOLUME));
    }

    @Override
    public void onRemoved(IHypper hypper, int slot) {
        if (hypper.getSlots()[slot] instanceof FluidicHypperSlot) {
            hypper.setSlot(slot, null);
        }
    }
}
