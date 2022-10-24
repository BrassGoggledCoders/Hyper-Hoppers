package xyz.brassgoggledcoders.hyperhoppers.api;

import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlotRenderProperties;

public class HypperRenderProperties {
    public static IHypperSlotRenderProperties getProperties(IHypperSlot<?> hypperSlot) {
        if (hypperSlot.getType().getRenderProperties() instanceof IHypperSlotRenderProperties renderProperties) {
            return renderProperties;
        } else {
            return null;
        }
    }
}
