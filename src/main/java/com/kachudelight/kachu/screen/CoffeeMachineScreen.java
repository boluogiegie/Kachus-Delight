package com.kachudelight.kachu.screen;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.menu.CoffeeMachineMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CoffeeMachineScreen extends AbstractContainerScreen<CoffeeMachineMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(KachuDelight.MOD_ID, "textures/gui/coffee_machine_gui.png");

    public CoffeeMachineScreen(CoffeeMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 1000;
        this.titleLabelY = 1000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 74, y + 35, 176, 14, menu.getScaledProgress() + 1, 16);
        }

        // 绘制水槽
        int maxWaterHeight = 67;
        int currentWaterAmount = menu.getWaterAmount();
        int scaledWaterHeight = 0;
        switch (currentWaterAmount) {
            case 1 -> scaledWaterHeight = 23;
            case 2 -> scaledWaterHeight = 44;
            case 3 -> scaledWaterHeight = maxWaterHeight;
        }

        if (scaledWaterHeight > 0) {
            // 核心参数：
            // x + 132, y + 12: 屏幕上的起点（要根据你的贴图位置对齐）
            // 176, 40: 在贴图文件里，“满水条”那个图案的左上角 UV 坐标
            // 18: 水条宽度
            // scaledWaterHeight: 我们要画的高度

            // 关键：我们要从下往上画，所以 y 坐标要加上 (总高度 - 当前高度)
            guiGraphics.blit(TEXTURE,
                    x + 151, y + 11 + (maxWaterHeight - scaledWaterHeight),
                    176, 14 + (maxWaterHeight - scaledWaterHeight),
                    18, scaledWaterHeight);
        }

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}