package xyz.brassgoggledcoders.hyperhoppers.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.capability.RunnableItemHandler;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersMenus;
import xyz.brassgoggledcoders.hyperhoppers.menu.BasicMenuProvider;
import xyz.brassgoggledcoders.hyperhoppers.menu.FilterMenu;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.IUpgradeProvider;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.Upgrade;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class FilterUpgradeItem extends Item implements IUpgradeProvider {
    private final Function<Collection<ItemStack>, Upgrade> supplier;

    public FilterUpgradeItem(Properties pProperties, Function<Collection<ItemStack>, Upgrade> supplier) {
        super(pProperties);
        this.supplier = supplier;
    }

    @Override
    public Collection<Upgrade> apply(ItemStack itemStack) {
        RunnableItemHandler itemHandler = getFilterInventory(itemStack);
        return Collections.singleton(supplier.apply(itemHandler.getStacks()));
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        int selected = pPlayer.getInventory().selected;
        if (Inventory.isHotbarSlot(selected) && pPlayer instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(
                    serverPlayer,
                    new BasicMenuProvider(
                            itemstack.getHoverName(),
                            (containerId, inventory) -> new FilterMenu(
                                    HyppersMenus.FILTER_MENU.get(),
                                    containerId,
                                    inventory,
                                    selected,
                                    itemstack,
                                    getFilterInventory(itemstack)
                            )
                    ),
                    friendlyByteBuf -> friendlyByteBuf.writeInt(selected)
            );
        }
        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }

    public static RunnableItemHandler getFilterInventory(ItemStack itemStack) {
        RunnableItemHandler itemStacks = new RunnableItemHandler(5);
        if (itemStack.getOrCreateTag().contains("Inventory")) {
            CompoundTag inventory = itemStack.getOrCreateTagElement("Inventory");
            inventory.remove("Size");
            itemStacks.deserializeNBT(inventory);
        }

        itemStacks.setOnChange(() -> itemStack.getOrCreateTag().put("Inventory", itemStacks.serializeNBT()));
        return itemStacks;
    }
}
