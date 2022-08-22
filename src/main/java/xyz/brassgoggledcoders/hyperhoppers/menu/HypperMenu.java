package xyz.brassgoggledcoders.hyperhoppers.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.block.HypperBlock;
import xyz.brassgoggledcoders.hyperhoppers.menu.slot.ModuleSlot;

public class HypperMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess containerLevelAccess;
    private final IItemHandler moduleItemHandler;
    private final IItemHandler contentItemHandler;

    public HypperMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory) {
        this(pMenuType, pContainerId, inventory, new ItemStackHandler(2), new ItemStackHandler(5), ContainerLevelAccess.NULL);
    }

    public HypperMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, IItemHandler moduleItemHandler,
                      IItemHandler contentItemHandler, ContainerLevelAccess access) {
        super(pMenuType, pContainerId);
        this.moduleItemHandler = moduleItemHandler;
        this.contentItemHandler = contentItemHandler;
        this.containerLevelAccess = access;

        for (int moduleSlot = 0; moduleSlot < 2; ++moduleSlot) {
            this.addSlot(new ModuleSlot(moduleItemHandler, moduleSlot, 8 + moduleSlot * 18, 20));
        }

        for (int contentSlot = 0; contentSlot < 5; ++contentSlot) {
            this.addSlot(new SlotItemHandler(contentItemHandler, contentSlot, 62 + contentSlot * 18, 20));
        }

        for (int inventoryRow = 0; inventoryRow < 3; ++inventoryRow) {
            for (int inventoryColumn = 0; inventoryColumn < 9; ++inventoryColumn) {
                this.addSlot(new Slot(inventory, inventoryColumn + inventoryRow * 9 + 9, 8 + inventoryColumn * 18,
                        inventoryRow * 18 + 51));
            }
        }

        for (int hotBarSlot = 0; hotBarSlot < 9; ++hotBarSlot) {
            this.addSlot(new Slot(inventory, hotBarSlot, 8 + hotBarSlot * 18, 109));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return containerLevelAccess.evaluate(
                (level, blockPos) -> level.getBlockState(blockPos).getBlock() instanceof HypperBlock &&
                        pPlayer.distanceToSqr((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D,
                                (double) blockPos.getZ() + 0.5D) <= 64.0D, true);
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }
}
