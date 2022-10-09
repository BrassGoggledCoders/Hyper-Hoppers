package xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;

import java.util.function.Function;

public record HypperSlotType(
        Function<IHypper, ICapabilityProvider> createProvider
) {

}
