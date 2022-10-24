package xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

public interface IHypperSlotRenderProperties {
    void renderSlot(Screen screen, PoseStack poseStack, int slotX, int slotY, IHypperSlot<?> hypperSlot);
}
