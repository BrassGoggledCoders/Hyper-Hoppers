package xyz.brassgoggledcoders.hyperhoppers.menu.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.module.IModuleProvider;

import javax.annotation.Nonnull;

public class ModuleSlot extends SlotItemHandler {
    public ModuleSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return 1;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return super.mayPlace(stack) && stack.getItem() instanceof IModuleProvider;
    }
}
