package xyz.brassgoggledcoders.hyperhoppers.api.upgrade;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.function.Function;

public interface IUpgradeProvider extends Function<ItemStack, Collection<Upgrade>> {
}
