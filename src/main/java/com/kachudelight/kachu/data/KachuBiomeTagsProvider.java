package com.kachudelight.kachu.data;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.worldgen.KachuBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class KachuBiomeTagsProvider extends BiomeTagsProvider {

    public KachuBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, KachuDelight.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(KachuBiomeTags.COFFEE_BIOMES).addTag(net.minecraft.tags.BiomeTags.IS_JUNGLE);
        tag(KachuBiomeTags.TEA_BIOMES).add(Biomes.MEADOW, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST);

        // 可以添加模组的丛林生物群系
        // .addOptional(new ResourceLocation("modid", "mod_jungle_biome"));
    }

}
