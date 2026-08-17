package com.kachudelight.kachu.machine.coffee;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CoffeeRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public CoffeeRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (recipeItems.size() != 3) return false; // 3个输入
        // 咖啡豆
        boolean hasCoffeeBean = recipeItems.get(0).test(container.getItem(0));
        // 燃料
        boolean hasFuel = recipeItems.get(1).test(container.getItem(1));
        // 容器
        boolean hasContainer = recipeItems.get(2).test(container.getItem(3));
        return hasCoffeeBean && hasFuel && hasContainer;
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CoffeeRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return CoffeeRecipeType.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }
}
