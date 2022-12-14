package xyz.brassgoggledcoders.hyperhoppers.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.hyperhoppers.HyperHoppers;
import xyz.brassgoggledcoders.hyperhoppers.api.HypperRenderProperties;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlot;
import xyz.brassgoggledcoders.hyperhoppers.api.upgrade.slot.IHypperSlotRenderProperties;
import xyz.brassgoggledcoders.hyperhoppers.menu.HypperMenu;
import xyz.brassgoggledcoders.hyperhoppers.menu.slot.HypperMenuSlot;

public class HypperScreen extends AbstractContainerScreen<HypperMenu> {
    private static final ResourceLocation SCREEN_LOCATION = HyperHoppers.rl("textures/menu/hypper_2.png");

    public HypperScreen(HypperMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.passEvents = false;
        this.imageHeight = 133;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY) {
        for (int k = 0; k < this.menu.slots.size(); ++k) {
            Slot slot = this.menu.slots.get(k);
            if (slot instanceof HypperMenuSlot hypperMenuSlot) {
                IHypperSlot<?> hypperSlot = hypperMenuSlot.getHypperSlot();
                IHypperSlotRenderProperties renderProperties = HypperRenderProperties.getProperties(hypperSlot);

                if (renderProperties != null) {
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    renderProperties.renderSlot(this, pPoseStack, slot.x, slot.y, hypperSlot);

                    if (this.isHovering(hypperMenuSlot.x, hypperMenuSlot.y, 16, 16, pMouseX, pMouseY)) {
                        this.hoveredSlot = slot;
                        int l = slot.x;
                        int i1 = slot.y;
                        renderSlotHighlight(pPoseStack, l, i1, this.getBlitOffset(), this.getSlotColor(k));
                    }
                }
            }
        }
        super.renderLabels(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SCREEN_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
