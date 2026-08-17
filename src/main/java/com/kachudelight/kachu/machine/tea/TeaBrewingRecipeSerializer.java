package com.kachudelight.kachu.machine.tea;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.Nullable;

public class TeaBrewingRecipeSerializer implements RecipeSerializer<TeaBrewingRecipe> {

    public static final TeaBrewingRecipeSerializer INSTANCE = new TeaBrewingRecipeSerializer();
    public static final ResourceLocation ID = new ResourceLocation("kachu", "tea_brewing");

    @Override
    public TeaBrewingRecipe fromJson(ResourceLocation id, JsonObject json) {
        ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

        JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
        if (ingredients.isEmpty() || ingredients.size() > 4) {
            throw new com.google.gson.JsonSyntaxException("Tea brewing recipes require 1 to 4 ingredients");
        }
        NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

        for (int i = 0; i < ingredients.size(); i++) {
            inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
        }

        return new TeaBrewingRecipe(id, output, inputs);
    }

    @Override
    public @Nullable TeaBrewingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        int ingredientCount = buf.readInt();
        if (ingredientCount < 1 || ingredientCount > 4) {
            throw new IllegalArgumentException("Tea brewing recipes require 1 to 4 ingredients");
        }
        NonNullList<Ingredient> inputs = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

        for (int i = 0; i < inputs.size(); i++) {
            inputs.set(i, Ingredient.fromNetwork(buf));
        }

        ItemStack output = buf.readItem();
        return new TeaBrewingRecipe(id, output, inputs);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, TeaBrewingRecipe recipe) {
        buf.writeInt(recipe.getIngredients().size());

        for (Ingredient ing : recipe.getIngredients()) {
            ing.toNetwork(buf);
        }

        buf.writeItem(recipe.getResultItem(null));
    }
}
