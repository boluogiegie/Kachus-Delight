package com.kachudelight.kachu.data;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.crop.CoffeeCrop;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class KachuDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

    public static final TagKey<Biome> COFFEE_BIOMES = TagKey.create(Registries.BIOME, KachuDelight.loc("spawns/coffee"));
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.CONFIGURED_FEATURE, ctx -> {CoffeeCrop.COFFEE.registerConfigs(ctx);}).add(Registries.PLACED_FEATURE, ctx -> {CoffeeCrop.COFFEE.registerPlacements(ctx);}).add(ForgeRegistries.Keys.BIOME_MODIFIERS, ctx -> registerBiomeModifiers(ctx));

    public KachuDatapackRegistriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", KachuDelight.MOD_ID));
    }

    private static void registerBiomeModifiers(BootstapContext<BiomeModifier> ctx) {
        HolderGetter<Biome> biomes = ctx.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = ctx.lookup(Registries.PLACED_FEATURE);
        registerCropBiomeModifier(ctx, CoffeeCrop.COFFEE, biomes.getOrThrow(COFFEE_BIOMES), placedFeatures.getOrThrow(CoffeeCrop.COFFEE.getPlacementKey()));
    }

    private static void registerCropBiomeModifier(BootstapContext<BiomeModifier> ctx, CoffeeCrop crop, HolderSet<Biome> biomes, Holder<PlacedFeature> feature) {
        BiomeModifier modifier = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.VEGETAL_DECORATION);
        ResourceKey<BiomeModifier> key = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, KachuDelight.loc(crop.getName()));ctx.register(key, modifier);
    }

    @Override
    public String getName() {
        return "Kachu Delight Data";
    }
}