package com.kachudelight.kachu.worldgen;

import com.kachudelight.kachu.KachuDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class KachuBiomeTags {
    public static final TagKey<Biome> COFFEE_BIOMES = create("spawns/coffee");
    public static final TagKey<Biome> TEA_BIOMES = create("spawns/tea");

    private KachuBiomeTags() {
    }

    private static TagKey<Biome> create(String path) {
        return TagKey.create(Registries.BIOME, KachuDelight.loc(path));
    }
}
