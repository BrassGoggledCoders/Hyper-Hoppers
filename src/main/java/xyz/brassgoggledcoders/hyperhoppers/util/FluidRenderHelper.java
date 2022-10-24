package xyz.brassgoggledcoders.hyperhoppers.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import xyz.brassgoggledcoders.hyperhoppers.HyperHoppers;

import java.awt.*;

public class FluidRenderHelper {
    private static final ResourceLocation FLUID_SLOT_OVERLAY = HyperHoppers.rl("textures/slot/fluid_overlay.png");

    public static void renderFluidSlot(PoseStack poseStack, FluidStack fluidStack, int capacity, int slotX, int slotY) {
        int height = 16;
        if (!fluidStack.isEmpty()) {
            int stored = fluidStack.getAmount();
            if (stored > capacity) {
                stored = capacity;
            }
            int offset = stored * height / capacity;

            FluidAttributes fluidAttributes = fluidStack.getFluid()
                    .getAttributes();
            ResourceLocation flowing = fluidAttributes.getStillTexture(fluidStack);
            if (flowing != null) {
                TextureAtlasSprite flowingSprite = Minecraft.getInstance()
                        .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                        .apply(flowing);
                RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

                Color color = new Color(fluidStack.getFluid()
                        .getAttributes()
                        .getColor()
                );

                RenderSystem.setShaderColor(
                        (float) color.getRed() / 255.0F,
                        (float) color.getGreen() / 255.0F,
                        (float) color.getBlue() / 255.0F,
                        (float) color.getAlpha() / 255.0F
                );
                RenderSystem.enableBlend();
                int startY = slotY + (fluidAttributes.isGaseous() ? 0 : height - offset);
                GuiComponent.blit(poseStack, slotX, startY, 0, 16, offset, flowingSprite);
                RenderSystem.disableBlend();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        RenderSystem.setShaderTexture(0, FLUID_SLOT_OVERLAY);
        GuiComponent.blit(poseStack, slotX, slotY, 0, 0, 16, 16, 16, 16);
    }
}
