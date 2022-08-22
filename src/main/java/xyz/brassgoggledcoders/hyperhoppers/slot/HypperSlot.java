package xyz.brassgoggledcoders.hyperhoppers.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class HypperSlot {
    private Runnable onChange;

    public void onRemove(Level pLevel, int x, int y, int z) {
    }

    public abstract ItemStack getItemStack();

    public abstract ItemStack extract(int count, boolean simulate);

    public abstract ItemStack insert(ItemStack itemStack, boolean simulate);

    public boolean isValid(ItemStack stack) {
        return true;
    }

    public void setOnChange(Runnable onChange) {
        this.onChange = onChange;
    }

    public void onChange() {
        if (this.onChange != null) {
            this.onChange.run();
        }
    }
}
