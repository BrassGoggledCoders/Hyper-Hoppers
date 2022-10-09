package xyz.brassgoggledcoders.hyperhoppers.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;

public interface IHypper {
    IHypperSlot<?>[] getSlots();

    void setSlot(int slot, IHypperSlot<?> hypperSlot);

    Level getHypperLevel();

    BlockPos getHypperPos();

}
