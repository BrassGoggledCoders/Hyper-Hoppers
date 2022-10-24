package xyz.brassgoggledcoders.hyperhoppers.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;
import xyz.brassgoggledcoders.hyperhoppers.block.HypperBlock;
import xyz.brassgoggledcoders.hyperhoppers.capability.UpgradeItemHandler;
import xyz.brassgoggledcoders.hyperhoppers.menu.slot.HypperMenuSlot;
import xyz.brassgoggledcoders.hyperhoppers.menu.slot.UpgradeSlot;
import xyz.brassgoggledcoders.hyperhoppers.screen.ClientHypper;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.IPropertyManaged;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyManager;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyTypes;

public class HypperMenu extends AbstractContainerMenu implements IPropertyManaged {
    private final ContainerLevelAccess containerLevelAccess;
    private final PropertyManager propertyManager;
    private final Inventory inventory;

    public HypperMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory) {
        this(pMenuType, pContainerId, inventory, new ClientHypper(5));
    }

    public HypperMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, IHypper hypper) {
        this(pMenuType, pContainerId, inventory, new UpgradeItemHandler(hypper, 2), hypper, ContainerLevelAccess.NULL);
    }

    public HypperMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory inventory, UpgradeItemHandler upgradeItemHandler,
                      IHypper hypper, ContainerLevelAccess access) {
        super(pMenuType, pContainerId);
        this.containerLevelAccess = access;
        this.inventory = inventory;
        this.propertyManager = new PropertyManager((short) pContainerId);

        for (int moduleSlot = 0; moduleSlot < 2; ++moduleSlot) {
            this.addSlot(new UpgradeSlot(upgradeItemHandler, moduleSlot, 8 + moduleSlot * 18, 20));
        }

        for (int contentSlot = 0; contentSlot < 5; ++contentSlot) {
            int finalContentSlot = contentSlot;
            this.propertyManager.addTrackedProperty(PropertyTypes.COMPOUND_TAG.create(
                    () -> hypper.getSlots()[finalContentSlot].toNBT(),
                    nbt -> hypper.getSlots()[finalContentSlot].fromNBT(nbt)
            ));
            this.addSlot(new HypperMenuSlot(hypper, contentSlot, 62 + contentSlot * 18, 20));
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

    @Override
    public void broadcastFullState() {
        super.broadcastFullState();
        this.propertyManager.sendChanges(inventory, true);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        this.propertyManager.sendChanges(inventory, false);
    }

    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }
}
