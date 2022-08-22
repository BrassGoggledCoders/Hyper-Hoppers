package xyz.brassgoggledcoders.hyperhoppers.blockentity;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import xyz.brassgoggledcoders.hyperhoppers.slot.HypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.slot.ItemSlot;

import java.util.List;

public class HypperBlockEntity extends BlockEntity {
    private final HypperSlot[] slots = new HypperSlot[]{
            new ItemSlot(),
            new ItemSlot(),
            new ItemSlot(),
            new ItemSlot(),
            new ItemSlot()
    };

    public HypperBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public List<HypperSlot> getSlots() {
        return ImmutableList.copyOf(slots);
    }
}
