package xyz.brassgoggledcoders.hyperhoppers.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.hyperhoppers.block.PassductBlock;
import xyz.brassgoggledcoders.hyperhoppers.capability.InputOnlyItemHandler;
import xyz.brassgoggledcoders.hyperhoppers.capability.RunnableItemHandler;

import java.util.Iterator;

public class PassductBlockEntity extends BlockEntity {
    private int attempts = 0;
    private int nextSlot = 0;
    private final RunnableItemHandler itemHandler;

    private final LazyOptional<IItemHandler> thisHandlerLazyOptional;

    public PassductBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        if (pBlockState.getBlock() instanceof PassductBlock passductBlock) {
            this.itemHandler = new RunnableItemHandler(passductBlock.getSlots(), this::onChange);
        } else {
            this.itemHandler = new RunnableItemHandler(1, this::onChange);
        }
        this.thisHandlerLazyOptional = LazyOptional.of(this::getInputOnly);
    }

    @NotNull
    private IItemHandler getInputOnly() {
        return new InputOnlyItemHandler(this.itemHandler);
    }

    /**
     * @return if the block should be considered jammed
     */
    public boolean routinePush() {
        if (itemHandler.hasItems() && this.level != null) {
            if (nextSlot + 1 == this.itemHandler.getSlots()) {
                attempts++;
            } else if (nextSlot >= this.itemHandler.getSlots()) {
                this.nextSlot = 0;
            }
            LazyOptional<IItemHandler> otherHandler = this.getHandlerAtSpout();

            otherHandler.ifPresent(handler -> {
                ItemStack toTry = this.itemHandler.extractItem(nextSlot, 64, true);
                if (!toTry.isEmpty()) {
                    ItemStack leftOver = ItemHandlerHelper.insertItemStacked(handler, toTry, true);
                    if (leftOver.isEmpty()) {
                        ItemHandlerHelper.insertItemStacked(
                                handler,
                                this.itemHandler.extractItem(nextSlot, 64, false),
                                false
                        );
                        this.attempts = 0;
                    } else {
                        this.nextSlot++;
                    }
                } else {
                    this.nextSlot++;
                }
            });
        } else {
            attempts++;
        }
        if (this.getLevel() != null) {
            if (attempts < this.getMaxAttempts()) {
                this.getLevel().scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 20);
            } else {
                this.getLevel().setBlock(this.getBlockPos(), this.getBlockState().setValue(PassductBlock.JAMMED, true), Block.UPDATE_ALL);
            }
        }
        return false;
    }

    public boolean pushOnce() {
        if (this.itemHandler.hasItems()) {
            LazyOptional<IItemHandler> handlerAtSpout = this.getHandlerAtSpout();
            return handlerAtSpout.map(handler -> {
                        boolean movedItems = false;
                        Iterator<ItemStack> heldItemStacks = this.itemHandler.iterator();
                        while (!movedItems && heldItemStacks.hasNext()) {
                            ItemStack nextAttempt = heldItemStacks.next();

                        }
                        return true;
                    })
                    .orElse(true);
        } else {
            return false;
        }
    }

    private LazyOptional<IItemHandler> getHandlerAtSpout() {
        LazyOptional<IItemHandler> otherHandler = LazyOptional.empty();
        BlockPos handlerPos = this.getBlockPos().relative(this.getBlockState().getValue(PassductBlock.SPOUT));

        if (this.level != null && this.level.isLoaded(handlerPos)) {
            BlockEntity handlerEntity = this.level.getBlockEntity(handlerPos);
            if (handlerEntity != null) {
                otherHandler = handlerEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
            }
        }
        return otherHandler;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != this.getBlockState().getValue(PassductBlock.SPOUT)) {
            return this.thisHandlerLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.thisHandlerLazyOptional.invalidate();
    }

    public RunnableItemHandler getItemHandler() {
        return this.itemHandler;
    }

    public int getMaxAttempts() {
        return this.getBlockState().getBlock() instanceof PassductBlock passductBlock ? passductBlock.getMaxAttempts() : 1;
    }

    public void onChange() {
        this.setChanged();
        if (this.getItemHandler().hasItems() && this.level != null) {
            if (!this.level.getBlockTicks().hasScheduledTick(this.getBlockPos(), this.getBlockState().getBlock())) {
                this.level.scheduleTick(this.getBlockPos(), this.getBlockState().getBlock(), 20);
            }
        }
    }
}
