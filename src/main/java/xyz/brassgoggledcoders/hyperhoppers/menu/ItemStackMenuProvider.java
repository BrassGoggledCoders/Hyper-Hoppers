package xyz.brassgoggledcoders.hyperhoppers.menu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;

public class ItemStackMenuProvider implements MenuProvider {
    private final ItemStack itemStack;
    private final BiFunction<Integer, Inventory, AbstractContainerMenu> menuMaker;

    public ItemStackMenuProvider(ItemStack itemStack, BiFunction<Integer, Inventory, AbstractContainerMenu> menuMaker) {
        this.itemStack = itemStack;
        this.menuMaker = menuMaker;
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return itemStack.getHoverName();
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return this.menuMaker.apply(
                pContainerId,
                pInventory
        );
    }
}
