package xyz.brassgoggledcoders.hyperhoppers.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.menu.slot.FilterSlot;
import xyz.brassgoggledcoders.hyperhoppers.menu.slot.NoTouchSlot;

public class FilterMenu extends AbstractContainerMenu {
    private final ItemStack heldItem;
    private final int heldSlot;

    private final IItemHandler handler;

    public FilterMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, int heldSlot) {
        this(pMenuType, pContainerId, inventory, heldSlot, ItemStack.EMPTY, new ItemStackHandler(5));
    }

    public FilterMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, int heldSlot,
                      ItemStack heldItem, IItemHandler handler) {
        super(pMenuType, pContainerId);
        this.heldItem = heldItem;
        this.heldSlot = heldSlot;
        this.handler = handler;

        for (int handlerSlot = 0; handlerSlot < 5; ++handlerSlot) {
            this.addSlot(new FilterSlot(handler, handlerSlot, 44 + handlerSlot * 18, 20));
        }

        for (int playerInventory = 0; playerInventory < 3; ++playerInventory) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inventory, k + playerInventory * 9 + 9, 8 + k * 18, playerInventory * 18 + 51));
            }
        }

        for (int hotBar = 0; hotBar < 9; ++hotBar) {
            if (heldSlot == hotBar) {
                this.addSlot(new NoTouchSlot(inventory, hotBar, 8 + hotBar * 18, 109));
            } else {
                this.addSlot(new Slot(inventory, hotBar, 8 + hotBar * 18, 109));
            }
        }
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (pIndex != heldSlot) {

            Slot slot = this.slots.get(pIndex);
            if (slot.hasItem()) {
                ItemStack slotStack = slot.getItem().copy();
                if (pIndex < this.handler.getSlots()) {
                    slot.set(ItemStack.EMPTY);
                    slot.setChanged();
                } else if (!this.moveItemStackTo(slotStack.split(1), 0, this.handler.getSlots(), false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return pPlayer.getLevel().isClientSide() || checkHolding(pPlayer);
    }

    private boolean checkHolding(Player pPlayer) {
        return heldSlot >= 0 && pPlayer.getInventory().getItem(heldSlot) == heldItem;
    }

    public static FilterMenu create(MenuType<FilterMenu> type, int windowId, Inventory inv,
                                    @Nullable FriendlyByteBuf buffer) {
        return new FilterMenu(
                type,
                windowId,
                inv,
                buffer != null ? buffer.readInt() : -1
        );
    }
}
