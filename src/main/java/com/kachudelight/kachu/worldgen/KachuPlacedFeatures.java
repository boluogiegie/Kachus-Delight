package com.kachudelight.kachu.worldgen;

import com.kachudelight.kachu.KachuDelight;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public final class KachuPlacedFeatures {
    public static final ResourceKey<PlacedFeature> WILD_COFFEE = createKey("wild_coffee");
    public static final ResourceKey<PlacedFeature> WILD_TEA_BUSH = createKey("wild_tea_bush");

    private KachuPlacedFeatures() {
    }

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        register(context, WILD_COFFEE, KachuConfiguredFeatures.WILD_COFFEE, 2);
        register(context, WILD_TEA_BUSH, KachuConfiguredFeatures.WILD_TEA_BUSH, 8);
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> placedKey, ResourceKey<ConfiguredFeature<?, ?>> configuredKey, int averageChunksPerPatch) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> configuredFeature = configuredFeatures.getOrThrow(configuredKey);
        PlacementUtils.register(context, placedKey, configuredFeature, RarityFilter.onAverageOnceEvery(averageChunksPerPatch), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES), BiomeFilter.biome());
    }

    private static ResourceKey<PlacedFeature> createKey(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, KachuDelight.loc(path));
    }
}
