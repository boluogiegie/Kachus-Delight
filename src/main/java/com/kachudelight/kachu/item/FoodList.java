package com.kachudelight.kachu.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class FoodList {
    // 蛋包饭
    public static final FoodProperties OMELETTE_RICE = new FoodProperties.Builder().nutrition(4).saturationMod(0.8F).effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 30 * 20, 0), 1.0F).build();
    // 吃过一口的蛋包饭
    public static final FoodProperties OMELETTE_RICE1 = new FoodProperties.Builder().nutrition(4).saturationMod(0.8F).effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 30 * 20, 0), 1.0F).build();
    // 吃过两口的蛋包饭
    public static final FoodProperties OMELETTE_RICE2 = new FoodProperties.Builder().nutrition(4).saturationMod(0.8F).effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 30 * 20, 0), 1.0F).build();
    // 香奈美的蛋包饭
    public static final FoodProperties KANAMI_OMELETTE_RICE= new FoodProperties.Builder().nutrition(20).saturationMod(1.0F).effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 2 * 60 * 20, 0), 1.0F).effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 2 * 60 * 20, 0), 1.0F).effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 2 * 60 * 20, 4), 1.0F).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 60 * 20, 1), 1.0F).build();
    // 吃过一口的香奈美的蛋包饭
    public static final FoodProperties KANAMI_OMELETTE_RICE1= new FoodProperties.Builder().nutrition(20).saturationMod(1.0F).effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 2 * 60 * 20, 0), 1.0F).effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 2 * 60 * 20, 0), 1.0F).effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 2 * 60 * 20, 4), 1.0F).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 60 * 20, 1), 1.0F).build();
    // 吃过两口的香奈美的蛋包饭
    public static final FoodProperties KANAMI_OMELETTE_RICE2= new FoodProperties.Builder().nutrition(20).saturationMod(1.0F).effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 2 * 60 * 20, 0), 1.0F).effect(() -> new MobEffectInstance(ModEffects.NOURISHMENT.get(), 2 * 60 * 20, 0), 1.0F).effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 2 * 60 * 20, 4), 1.0F).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 60 * 20, 1), 1.0F).build();
    // 咖啡豆
    public static final FoodProperties COFFEE_BEAN = new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).fast().build();
}
