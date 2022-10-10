package xyz.brassgoggledcoders.hyperhoppers.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;

public interface IHypper extends ICapabilityProvider {
    IHypperSlot<?>[] getSlots();

    void setSlot(int slot, @Nullable IHypperSlot<?> hypperSlot);

    Level getHypperLevel();

    BlockPos getHypperPos();
}
