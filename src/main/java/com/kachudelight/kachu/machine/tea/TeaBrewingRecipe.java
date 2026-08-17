package com.kachudelight.kachu.machine.tea;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TeaBrewingRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public TeaBrewingRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (recipeItems.isEmpty() || recipeItems.size() > 4) return false;

        List<ItemStack> inputs = new ArrayList<>(4);
        for (int slot = 0; slot < 4; slot++) {
            ItemStack stack = container.getItem(slot);
            if (!stack.isEmpty()) inputs.add(stack);
        }
        if (inputs.size() != recipeItems.size()) return false;

        return matchesIngredients(inputs, new boolean[inputs.size()], 0);
    }

    private boolean matchesIngredients(List<ItemStack> inputs, boolean[] used, int ingredientIndex) {
        if (ingredientIndex == recipeItems.size()) return true;
        Ingredient ingredient = recipeItems.get(ingredientIndex);
        for (int inputIndex = 0; inputIndex < inputs.size(); inputIndex++) {
            if (!used[inputIndex] && ingredient.test(inputs.get(inputIndex))) {
                used[inputIndex] = true;
                if (matchesIngredients(inputs, used, ingredientIndex + 1)) return true;
                used[inputIndex] = false;
            }
        }
        return false;
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
        return TeaBrewingRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return TeaBrewingRecipeType.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }
}
