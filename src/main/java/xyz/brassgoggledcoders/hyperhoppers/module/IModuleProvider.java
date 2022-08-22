package xyz.brassgoggledcoders.hyperhoppers.module;

import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.function.Function;

public interface IModuleProvider extends Function<ItemStack, Collection<Module>> {
}
