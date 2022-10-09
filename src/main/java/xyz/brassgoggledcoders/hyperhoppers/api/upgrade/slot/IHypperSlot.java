package xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;

public interface IHypperSlot<U> {

    HypperSlotType getType();

    U getContent();

    void setContent(U content);

    CompoundTag toNBT();

    void fromNBT(CompoundTag compoundTag);

    default void setOnChange(Runnable runnable) {

    }

    default void onReplaced(@NotNull IHypper hypper, @Nullable IHypperSlot<?> replacement) {
    }
}
