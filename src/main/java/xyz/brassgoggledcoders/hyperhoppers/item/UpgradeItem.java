package xyz.brassgoggledcoders.hyperhoppers.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.IUpgradeProvider;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.Upgrade;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public class UpgradeItem extends Item implements IUpgradeProvider {
    private final Supplier<Upgrade> upgradeSupplier;

    public UpgradeItem(Properties pProperties, Supplier<Upgrade> upgradeSupplier) {
        super(pProperties);
        this.upgradeSupplier = upgradeSupplier;
    }

    @Override
    public Collection<Upgrade> apply(ItemStack itemStack) {
        return Collections.singletonList(upgradeSupplier.get());
    }
}
