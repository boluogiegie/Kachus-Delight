package com.kachudelight.kachu.crop;

import com.kachudelight.kachu.KachuDelight;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class CoffeeCrop {
    private final String name;
    private Block wildPlant;
    private final Block plant;
    private final int rarity; // 生成稀有度
    private final int density; // 生成密度

    public static final CoffeeCrop COFFEE = new CoffeeCrop("coffee", null, null, 5, 16);

    private CoffeeCrop(String name, Block wildPlant, Block plant, int rarity, int density) {
        this.name = name;
        this.wildPlant = wildPlant;
        this.plant = plant;
        this.rarity = rarity;
        this.density = density;
    }

    public ResourceKey<ConfiguredFeature<?, ?>> getConfigKey() {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, KachuDelight.loc("wild_" + name));
    }

    public ResourceKey<PlacedFeature> getPlacementKey() {
        return ResourceKey.create(Registries.PLACED_FEATURE, KachuDelight.loc("wild_" + name));
    }

    public String getName() {
        return name;
    }

    public Block getWildPlant() {
        return wildPlant;
    }

    public void setWildPlant(Block block) {
        this.wildPlant = block;
    }

    // 注册配置特征
    public void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
        HolderGetter<Block> blockLookup = ctx.lookup(Registries.BLOCK);
        Holder<Block> wildCoffeeHolder = blockLookup.getOrThrow(ResourceKey.create(Registries.BLOCK, KachuDelight.loc("wild_coffee_bush")));
        FeatureUtils.register(ctx, getConfigKey(), Feature.RANDOM_PATCH, new RandomPatchConfiguration(density, 8, 3, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(wildCoffeeHolder.value())), BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.noFluid(), BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.ROOTED_DIRT), BlockPredicate.allOf(BlockPredicate.not(BlockPredicate.matchesTag(BlockPos.ZERO.above(), BlockTags.LEAVES)), BlockPredicate.not(BlockPredicate.matchesTag(BlockPos.ZERO.north(), BlockTags.LEAVES)), BlockPredicate.not(BlockPredicate.matchesTag(BlockPos.ZERO.south(), BlockTags.LEAVES)), BlockPredicate.not(BlockPredicate.matchesTag(BlockPos.ZERO.west(), BlockTags.LEAVES)), BlockPredicate.not(BlockPredicate.matchesTag(BlockPos.ZERO.east(), BlockTags.LEAVES)))))));
    }

    // 注册放置特征
    public void registerPlacements(BootstapContext<PlacedFeature> ctx) {
        PlacementUtils.register(ctx, getPlacementKey(), ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(getConfigKey()), RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
    }
}