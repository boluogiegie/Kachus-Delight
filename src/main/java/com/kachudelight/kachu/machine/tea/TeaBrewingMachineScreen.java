package com.kachudelight.kachu.machine.tea;

import com.kachudelight.kachu.KachuDelight;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TeaBrewingMachineScreen extends AbstractContainerScreen<TeaBrewingMachineMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(KachuDelight.MOD_ID, "textures/gui/tea_brewing_machine_gui.png");

    public TeaBrewingMachineScreen(TeaBrewingMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 72;
        this.titleLabelY = 8;
        this.titleLabelX = 10;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x000000, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        int scaledProgress = menu.getScaledProgress();
        if (scaledProgress > 0) {
            guiGraphics.blit(TEXTURE, x + 66, y + 31, 178, 2, scaledProgress, 10);
        }

        int maxWaterHeight = 66;
        int currentWaterAmount = menu.getWaterAmount();

        int scaledWaterHeight = (currentWaterAmount * maxWaterHeight) / 3000;

        if (scaledWaterHeight > 0) {
            guiGraphics.blit(TEXTURE, x + 153, y + 10 + (maxWaterHeight - scaledWaterHeight), 176, 14 + (maxWaterHeight - scaledWaterHeight), 18, scaledWaterHeight);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        if (mouseX >= x + 151 && mouseX <= x + 151 + 18 &&
                mouseY >= y + 11 && mouseY <= y + 11 + 67) {

            int currentWater = menu.getWaterAmount();
            Component tooltip = Component.literal("当前水量: " + currentWater + " / 3000 mB");
            guiGraphics.renderTooltip(this.font, tooltip, mouseX, mouseY);
        }
    }
}
