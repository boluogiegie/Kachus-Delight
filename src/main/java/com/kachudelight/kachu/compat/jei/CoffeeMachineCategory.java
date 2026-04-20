package com.kachudelight.kachu.compat.jei;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.recipe.CoffeeRecipe;
import com.kachudelight.kachu.registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CoffeeMachineCategory implements IRecipeCategory<CoffeeRecipe> {
    public static final RecipeType<CoffeeRecipe> COFFEE_MACHINE_TYPE =
            RecipeType.create(KachuDelight.MOD_ID, "coffee_brewing", CoffeeRecipe.class);

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(KachuDelight.MOD_ID, "textures/gui/coffee_machine_gui.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawable waterTank;

    public CoffeeMachineCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.COFFEE_MACHINE.get()));
        this.arrow = helper.drawableBuilder(TEXTURE, 178, 2, 31, 10)
                .buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.waterTank = helper.createDrawable(TEXTURE, 176, 14, 18, 97);
    }

    @Override
    public RecipeType<CoffeeRecipe> getRecipeType() {
        return COFFEE_MACHINE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.kachudelight.coffee_brewing");
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return 176; // 对应新宽度
    }

    @Override
    public int getHeight() {
        return 80; // 对应新高度
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CoffeeRecipe recipe, IFocusGroup focuses) {
        // 坐标完全不用动！

        // 咖啡豆槽
        builder.addSlot(RecipeIngredientRole.INPUT, 36, 21)
                .addIngredients(recipe.getIngredients().get(0));

        // 辅料槽
        builder.addSlot(RecipeIngredientRole.INPUT, 36, 39)
                .addIngredients(recipe.getIngredients().get(1));

        // 容器槽
        builder.addSlot(RecipeIngredientRole.INPUT, 70, 48)
                .addIngredients(recipe.getIngredients().get(2));

        // 成品槽
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 30)
                .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void draw(CoffeeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // 画出完整的背景
        background.draw(guiGraphics, 0, 0);

        // 画上蓝蓝的水位条！原X: 151, Y: 11。相对坐标: X=131(151-20), Y=1(11-10)
        waterTank.draw(guiGraphics, 151,  11);

        // 画动画箭头
        arrow.draw(guiGraphics, 66, 33);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CoffeeRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        // 1. 判断鼠标是否悬停在【箭头】区域
        // 按照你屏幕坐标计算：x起点约66，宽度25；y起点33，高度10
        if (mouseX >= 66 && mouseX <= 91 && mouseY >= 33 && mouseY <= 43) {
            tooltip.add(Component.literal("需要10s喵~"));
        }

        // 2. 判断鼠标是否悬停在【水槽】区域
        // 按照你屏幕坐标计算：x起点151，宽度18；y起点11，高度67
        if (mouseX >= 151 && mouseX <= 169 && mouseY >= 11 && mouseY <= 78) {
            tooltip.add(Component.literal("消耗200 mB水喵~"));
        }
    }
}