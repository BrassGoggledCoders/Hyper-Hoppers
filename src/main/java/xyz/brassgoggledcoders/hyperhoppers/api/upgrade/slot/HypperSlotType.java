package xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;

import java.util.function.Consumer;
import java.util.function.Function;

public class HypperSlotType{
    private final Function<IHypper, ICapabilityProvider> createProvider;
    private Object renderProperties;

    public HypperSlotType(Function<IHypper, ICapabilityProvider> createProvider) {
        this.createProvider = createProvider;

        this.initClient();
    }

    public ICapabilityProvider createProvider(IHypper hypper) {
        return createProvider.apply(hypper);
    }

    public void initializeClient(Consumer<IHypperSlotRenderProperties> consumer) {
    }

    private void initClient() {
        if (FMLEnvironment.dist.isClient() && !FMLLoader.getLaunchHandler().isData()) {
            initializeClient(properties -> this.renderProperties = properties);
        }
    }

    public Object getRenderProperties() {
        return renderProperties;
    }
}
