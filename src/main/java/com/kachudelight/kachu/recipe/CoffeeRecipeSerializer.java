// [file name]: CoffeeRecipeSerializer.java
package com.kachudelight.kachu.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CoffeeRecipeSerializer implements RecipeSerializer<CoffeeRecipe> {

    public static final CoffeeRecipeSerializer INSTANCE = new CoffeeRecipeSerializer();
    public static final ResourceLocation ID = new ResourceLocation("kachu", "coffee_brewing");

    @Override
    public CoffeeRecipe fromJson(ResourceLocation id, JsonObject json) {
        ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

        JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
        NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

        for (int i = 0; i < ingredients.size(); i++) {
            inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
        }

        return new CoffeeRecipe(id, output, inputs);
    }

    @Override
    public @Nullable CoffeeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

        for (int i = 0; i < inputs.size(); i++) {
            inputs.set(i, Ingredient.fromNetwork(buf));
        }

        ItemStack output = buf.readItem();
        return new CoffeeRecipe(id, output, inputs);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, CoffeeRecipe recipe) {
        buf.writeInt(recipe.getIngredients().size());

        for (Ingredient ing : recipe.getIngredients()) {
            ing.toNetwork(buf);
        }

        buf.writeItem(recipe.getResultItem(null));
    }
}