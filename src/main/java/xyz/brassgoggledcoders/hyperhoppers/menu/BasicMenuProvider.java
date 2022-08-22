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

public class BasicMenuProvider implements MenuProvider {
    private final Component name;
    private final BiFunction<Integer, Inventory, AbstractContainerMenu> menuMaker;

    public BasicMenuProvider(Component name, BiFunction<Integer, Inventory, AbstractContainerMenu> menuMaker) {
        this.name = name;
        this.menuMaker = menuMaker;
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return this.name;
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
