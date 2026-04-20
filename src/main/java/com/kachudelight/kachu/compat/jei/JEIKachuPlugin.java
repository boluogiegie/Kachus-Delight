package com.kachudelight.kachu.compat.jei;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.registry.BlockRegistry;
import com.kachudelight.kachu.registry.ItemRegistry;
import com.kachudelight.kachu.recipe.CoffeeRecipe;
import com.kachudelight.kachu.recipe.CoffeeRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIKachuPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(KachuDelight.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        OmeletteRiceUpgradeCategory category = new OmeletteRiceUpgradeCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(category);
        registration.addRecipeCategories(new CoffeeMachineCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<OmeletteRiceUpgradeRecipe> recipes = new ArrayList<>();
        recipes.add(new OmeletteRiceUpgradeRecipe(ItemRegistry.OMELETTE_RICE.get(), new ItemStack(ModItems.TOMATO_SAUCE.get()), ItemRegistry.KANAMI_OMELETTE_RICE.get(), new ItemStack(Items.BOWL)));
        registration.addRecipes(OmeletteRiceUpgradeCategory.RECIPE_TYPE, recipes);
        if (Minecraft.getInstance().level != null) {
            List<CoffeeRecipe> coffeeRecipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(CoffeeRecipeType.INSTANCE);
            registration.addRecipes(CoffeeMachineCategory.COFFEE_MACHINE_TYPE, coffeeRecipes);
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.COFFEE_MACHINE.get()), CoffeeMachineCategory.COFFEE_MACHINE_TYPE);
    }
}