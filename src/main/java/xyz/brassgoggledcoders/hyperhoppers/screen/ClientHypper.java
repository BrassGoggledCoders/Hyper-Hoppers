package xyz.brassgoggledcoders.hyperhoppers.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.slot.ItemHypperSlot;

import java.util.Objects;

public class ClientHypper implements IHypper {
    private final IHypperSlot<?>[] slots;

    public ClientHypper(int slots) {
        this.slots = new IHypperSlot[slots];
        for (int i = 0; i < slots; i++) {
            this.slots[i] = new ItemHypperSlot();
        }
    }

    @Override
    public IHypperSlot<?>[] getSlots() {
        return slots;
    }

    @Override
    public void setSlot(int slot, @Nullable IHypperSlot<?> hypperSlot) {
        this.slots[slot] = Objects.requireNonNullElseGet(hypperSlot, ItemHypperSlot::new);
    }

    @Override
    public Level getHypperLevel() {
        return LogicalSidedProvider.CLIENTWORLD.get(LogicalSide.CLIENT)
                .orElseThrow();
    }

    @Override
    public BlockPos getHypperPos() {
        return BlockPos.ZERO;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }
}
