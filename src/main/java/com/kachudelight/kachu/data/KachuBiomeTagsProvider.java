package com.kachudelight.kachu.data;

import com.kachudelight.kachu.KachuDelight;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class KachuBiomeTagsProvider extends BiomeTagsProvider {

    // 咖啡生成标签 - 只在丛林中生成
    public static final TagKey<Biome> COFFEE_BIOMES =
            TagKey.create(net.minecraft.core.registries.Registries.BIOME,
                    KachuDelight.loc("spawns/coffee"));

    public KachuBiomeTagsProvider(PackOutput output,
                                  CompletableFuture<HolderLookup.Provider> provider,
                                  ExistingFileHelper helper) {
        super(output, provider, KachuDelight.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 咖啡在丛林生物群系中生成
        tag(COFFEE_BIOMES)
                .addTag(BiomeTags.IS_JUNGLE)  // 所有丛林生物群系
                .add(Biomes.BAMBOO_JUNGLE);   // 竹林

        // 注意：你也可以添加模组的丛林生物群系
        // .addOptional(new ResourceLocation("modid", "mod_jungle_biome"));
    }

    // 工具方法：创建标签键
    public static TagKey<Biome> createTag(String name) {
        return TagKey.create(net.minecraft.core.registries.Registries.BIOME,
                KachuDelight.loc(name));
    }
}