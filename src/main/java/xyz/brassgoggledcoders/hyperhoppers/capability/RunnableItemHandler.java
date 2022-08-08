package xyz.brassgoggledcoders.hyperhoppers.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class RunnableItemHandler extends ItemStackHandler implements Iterable<ItemStack>  {
    private final Runnable onChange;

    public RunnableItemHandler(int slots, Runnable onChange) {
        super(slots);
        this.onChange = onChange;
    }

    @Override
    protected void onContentsChanged(int slot) {
        onChange.run();
    }

    public boolean hasItems() {
        return this.stacks.stream()
                .anyMatch(itemStack -> !itemStack.isEmpty());
    }

    @NotNull
    @Override
    public Iterator<ItemStack> iterator() {
        return this.stacks.iterator();
    }
}
