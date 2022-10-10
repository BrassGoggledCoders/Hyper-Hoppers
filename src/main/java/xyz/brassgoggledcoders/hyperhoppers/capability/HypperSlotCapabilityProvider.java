package xyz.brassgoggledcoders.hyperhoppers.capability;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;

import java.util.function.Function;

public class HypperSlotCapabilityProvider<T> implements ICapabilityProvider {
    private final IHypper hypper;
    private final Capability<T> capability;
    private final Function<IHypper, ? extends T> creator;
    private final LazyOptional<T> lazyOptional;

    public HypperSlotCapabilityProvider(IHypper hypper, Capability<T> capability, Function<IHypper, ? extends T> creator) {
        this.hypper = hypper;
        this.capability = capability;
        this.creator = creator;
        this.lazyOptional = LazyOptional.of(this::create);
    }

    private T create() {
        return this.creator.apply(hypper);
    }

    @NotNull
    @Override
    public <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
        return capability.orEmpty(cap, lazyOptional);
    }
}
