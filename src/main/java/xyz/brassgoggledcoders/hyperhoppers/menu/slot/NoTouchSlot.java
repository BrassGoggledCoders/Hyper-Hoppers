package xyz.brassgoggledcoders.hyperhoppers.menu.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NoTouchSlot extends Slot {
    public NoTouchSlot(Container pContainer, int pIndex, int pX, int pY) {
        super(pContainer, pIndex, pX, pY);
    }

    @Override
    public boolean mayPickup(@NotNull Player pPlayer) {
        return false;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        return false;
    }
}
