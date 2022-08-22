package xyz.brassgoggledcoders.hyperhoppers.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import xyz.brassgoggledcoders.hyperhoppers.capability.HypperSlotsHandler;
import xyz.brassgoggledcoders.hyperhoppers.capability.ModuleItemHandler;
import xyz.brassgoggledcoders.hyperhoppers.content.HyppersMenus;
import xyz.brassgoggledcoders.hyperhoppers.menu.BasicMenuProvider;
import xyz.brassgoggledcoders.hyperhoppers.menu.HypperMenu;
import xyz.brassgoggledcoders.hyperhoppers.slot.HypperSlot;

import java.util.List;

public class HypperBlockEntity extends BlockEntity {
    private final HypperSlotsHandler slots;
    private final ModuleItemHandler modules;

    public HypperBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        this.slots = new HypperSlotsHandler(5, this::setChanged);
        this.modules = new ModuleItemHandler(2, this::setChanged);
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
                                    modules,
                                    slots,
                                    ContainerLevelAccess.create(this.getLevel(), this.getBlockPos())
                            )
                    )
            );
        }
    }

    public List<HypperSlot> getSlots() {
        return slots.getHypperSlots();
    }

    public void tick(boolean random) {

    }

    public int getAnalogSignal() {
        return 0;
    }
}
