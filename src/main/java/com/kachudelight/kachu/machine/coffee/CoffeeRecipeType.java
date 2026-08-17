package com.kachudelight.kachu.machine.coffee;

import net.minecraft.world.item.crafting.RecipeType;

public class CoffeeRecipeType implements RecipeType<CoffeeRecipe> {
    public static final CoffeeRecipeType INSTANCE = new CoffeeRecipeType();
    public static final String ID = "coffee_brewing";

    private CoffeeRecipeType() {}
}
