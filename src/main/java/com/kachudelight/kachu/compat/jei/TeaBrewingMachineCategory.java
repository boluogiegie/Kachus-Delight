package com.kachudelight.kachu.compat.jei;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.machine.tea.TeaBrewingRecipe;
import com.kachudelight.kachu.registry.BlockRegistry;
import com.kachudelight.kachu.registry.ItemRegistry;
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

public class TeaBrewingMachineCategory implements IRecipeCategory<TeaBrewingRecipe> {
    public static final RecipeType<TeaBrewingRecipe> RECIPE_TYPE =
            RecipeType.create(KachuDelight.MOD_ID, "tea_brewing", TeaBrewingRecipe.class);
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(KachuDelight.MOD_ID, "textures/gui/tea_brewing_machine_gui.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawable waterTank;

    public TeaBrewingMachineCategory(IGuiHelper helper) {
        background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(BlockRegistry.TEA_BREWING_MACHINE.get()));
        arrow = helper.drawableBuilder(TEXTURE, 178, 2, 31, 10)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        waterTank = helper.createDrawable(TEXTURE, 176, 14, 18, 66);
    }

    @Override public RecipeType<TeaBrewingRecipe> getRecipeType() { return RECIPE_TYPE; }
    @Override public Component getTitle() { return Component.translatable("jei.kachudelight.tea_brewing"); }
    @Override public int getWidth() { return background.getWidth(); }
    @Override public int getHeight() { return background.getHeight(); }
    @Override public IDrawable getIcon() { return icon; }

    @Override
    public void draw(TeaBrewingRecipe recipe, IRecipeSlotsView recipeSlotsView,
                     GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics, 0, 0);
        waterTank.draw(guiGraphics, 153, 10);
        arrow.draw(guiGraphics, 66, 31);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TeaBrewingRecipe recipe, IFocusGroup focuses) {
        int[] inputX = {26, 44, 26, 44};
        int[] inputY = {20, 20, 38, 38};
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, inputX[i], inputY[i])
                    .addIngredients(recipe.getIngredients().get(i));
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 72, 48)
                .addItemStack(new ItemStack(ItemRegistry.GLASS_CUP.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 28)
                .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, TeaBrewingRecipe recipe, IRecipeSlotsView recipeSlotsView,
                           double mouseX, double mouseY) {
        if (mouseX >= 66 && mouseX <= 97 && mouseY >= 31 && mouseY <= 41) {
            tooltip.add(Component.literal("需要10s喵~"));
        }
        if (mouseX >= 153 && mouseX <= 171 && mouseY >= 10 && mouseY <= 76) {
            tooltip.add(Component.literal("消耗250 mB水喵~"));
        }
    }
}
