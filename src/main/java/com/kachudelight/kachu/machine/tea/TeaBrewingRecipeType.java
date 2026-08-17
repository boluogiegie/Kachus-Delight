package com.kachudelight.kachu.machine.tea;

import net.minecraft.world.item.crafting.RecipeType;

public class TeaBrewingRecipeType implements RecipeType<TeaBrewingRecipe> {
    public static final TeaBrewingRecipeType INSTANCE = new TeaBrewingRecipeType();
    public static final String ID = "tea_brewing";

    private TeaBrewingRecipeType() {}
}
