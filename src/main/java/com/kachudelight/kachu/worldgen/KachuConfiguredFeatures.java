package com.kachudelight.kachu.worldgen;

import com.kachudelight.kachu.KachuDelight;
import net.minecraft.core.Direction;
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

public final class KachuConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_COFFEE = createKey("wild_coffee");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_TEA_BUSH = createKey("wild_tea_bush");

    private KachuConfiguredFeatures() {
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        registerPatch(context, WILD_COFFEE, "wild_coffee_bush", 32, 8, 3);
        registerPatch(context, WILD_TEA_BUSH, "wild_tea_bush", 24, 7, 2);
    }

    private static void registerPatch(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, String blockPath, int tries, int xzSpread, int ySpread) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
        Block block = blocks.getOrThrow(ResourceKey.create(Registries.BLOCK, KachuDelight.loc(blockPath))).value();
        BlockPredicate validPosition = BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.noFluid(), BlockPredicate.matchesTag(Direction.DOWN.getNormal(), BlockTags.DIRT), BlockPredicate.not(BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.FARMLAND)));
        SimpleBlockConfiguration simpleBlock = new SimpleBlockConfiguration(BlockStateProvider.simple(block));
        RandomPatchConfiguration patch = new RandomPatchConfiguration(tries, xzSpread, ySpread, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, simpleBlock, validPosition));
        FeatureUtils.register(context, key, Feature.RANDOM_PATCH, patch);
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> createKey(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, KachuDelight.loc(path));
    }
}
