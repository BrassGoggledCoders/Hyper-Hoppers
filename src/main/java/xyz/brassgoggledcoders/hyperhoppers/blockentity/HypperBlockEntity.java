package xyz.brassgoggledcoders.hyperhoppers.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.api.block.IHypper;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.capability.UpgradeItemHandler;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersMenus;
import xyz.brassgoggledcoders.hyperhoppers.menu.BasicMenuProvider;
import xyz.brassgoggledcoders.hyperhoppers.menu.HypperMenu;
import xyz.brassgoggledcoders.hyperhoppers.slot.ItemHypperSlot;

import java.util.Objects;

public class HypperBlockEntity extends BlockEntity implements IHypper {
    private final UpgradeItemHandler upgrades;
    private final IHypperSlot<?>[] slots;

    private int countdown;

    public HypperBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        this.slots = new IHypperSlot<?>[5];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new ItemHypperSlot();
        }
        this.upgrades = new UpgradeItemHandler(this, 2, this::setChanged);
    }

    public void openMenu(Player player) {
        if (this.getLevel() != null && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(
                    serverPlayer,
                    new BasicMenuProvider(
                            this.getBlockState()
                                    .getBlock()
                                    .getName(),
                            (containerId, inventory) -> new HypperMenu(
                                    HyppersMenus.HYPPER_MENU.get(),
                                    containerId,
                                    inventory,
                                    upgrades,
                                    //TODO FIX THE SLOTS
                                    new ItemStackHandler(5),
                                    ContainerLevelAccess.create(this.getLevel(), this.getBlockPos())
                            )
                    )
            );
        }
    }

    public IHypperSlot<?>[] getSlots() {
        return this.slots;
    }

    @Override
    public void setSlot(int slot, @Nullable IHypperSlot<?> hypperSlot) {
        this.slots[slot] = Objects.requireNonNullElseGet(hypperSlot, ItemHypperSlot::new);
    }

    @Override
    @NotNull
    public Level getHypperLevel() {
        return Objects.requireNonNull(this.getLevel());
    }

    @Override
    public BlockPos getHypperPos() {
        return this.getBlockPos();
    }

    public void tick(boolean random) {
        if (random || this.countdown-- <= 0) {
            moveThings();
        }
    }

    private void moveThings() {

    }

    public int getAnalogSignal() {
        return 0;
    }
}
