package com.kachudelight.kachu.data;

import com.kachudelight.kachu.KachuDelight;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class KachuBiomeTagsProvider extends BiomeTagsProvider {

    // 只在丛林中生成
    public static final TagKey<Biome> COFFEE_BIOMES = TagKey.create(net.minecraft.core.registries.Registries.BIOME, KachuDelight.loc("spawns/coffee"));

    public KachuBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, KachuDelight.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(COFFEE_BIOMES).addTag(BiomeTags.IS_JUNGLE).add(Biomes.BAMBOO_JUNGLE);

        // 可以添加模组的丛林生物群系
        // .addOptional(new ResourceLocation("modid", "mod_jungle_biome"));
    }

    public static TagKey<Biome> createTag(String name) {
        return TagKey.create(net.minecraft.core.registries.Registries.BIOME, KachuDelight.loc(name));
    }
}