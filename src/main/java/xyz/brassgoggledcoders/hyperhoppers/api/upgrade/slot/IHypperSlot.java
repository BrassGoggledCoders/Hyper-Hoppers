package xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
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

    default void onChange() {

    }

    default void onReplaced(@NotNull IHypper hypper, @Nullable IHypperSlot<?> replacement) {
    }

    default boolean allowMenuClick(@NotNull ItemStack itemStack) {
        return false;
    }

    @NotNull
    default ItemStack menuClick(@NotNull ItemStack itemStack) {
        return itemStack;
    }
}
