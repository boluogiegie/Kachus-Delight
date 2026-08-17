package com.kachudelight.kachu.worldgen;

import com.kachudelight.kachu.KachuDelight;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public final class KachuBiomeModifiers {
    private static final ResourceKey<BiomeModifier> COFFEE = createKey("coffee");
    private static final ResourceKey<BiomeModifier> TEA = createKey("tea");

    private KachuBiomeModifiers() {
    }

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        register(context, COFFEE, KachuBiomeTags.COFFEE_BIOMES, KachuPlacedFeatures.WILD_COFFEE, biomes, placedFeatures);
        register(context, TEA, KachuBiomeTags.TEA_BIOMES, KachuPlacedFeatures.WILD_TEA_BUSH, biomes, placedFeatures);
    }

    private static void register(
            BootstapContext<BiomeModifier> context,
            ResourceKey<BiomeModifier> modifierKey,
            TagKey<Biome> biomeTag,
            ResourceKey<PlacedFeature> featureKey,
            HolderGetter<Biome> biomes,
            HolderGetter<PlacedFeature> placedFeatures
    ) {
        HolderSet<Biome> targetBiomes = biomes.getOrThrow(biomeTag);
        Holder<PlacedFeature> feature = placedFeatures.getOrThrow(featureKey);
        BiomeModifier modifier = new ForgeBiomeModifiers.AddFeaturesBiomeModifier(targetBiomes, HolderSet.direct(feature), GenerationStep.Decoration.VEGETAL_DECORATION);
        context.register(modifierKey, modifier);
    }

    private static ResourceKey<BiomeModifier> createKey(String path) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, KachuDelight.loc(path));
    }
}
