package xyz.brassgoggledcoders.hyperhoppers.capability;

import net.minecraftforge.items.ItemStackHandler;

public class RunnableItemHandler extends ItemStackHandler {
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
}
