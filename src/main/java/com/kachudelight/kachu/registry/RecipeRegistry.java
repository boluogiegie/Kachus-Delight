package com.kachudelight.kachu.registry;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.recipe.CoffeeRecipe;
import com.kachudelight.kachu.recipe.CoffeeRecipeSerializer;
import com.kachudelight.kachu.recipe.CoffeeRecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, KachuDelight.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, KachuDelight.MOD_ID);

    // 咖啡机配方类型
    public static final RegistryObject<RecipeType<CoffeeRecipe>> COFFEE_RECIPE_TYPE =
            RECIPE_TYPES.register("coffee_brewing",
                    () -> CoffeeRecipeType.INSTANCE);

    // 咖啡机配方序列化器
    public static final RegistryObject<RecipeSerializer<CoffeeRecipe>> COFFEE_RECIPE_SERIALIZER =
            RECIPE_SERIALIZERS.register("coffee_brewing",
                    CoffeeRecipeSerializer::new);
}