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
    private final Block plant; // 预留种植版本
    private final int rarity; // 生成稀有度
    private final int density; // 生成密度

    public static final CoffeeCrop COFFEE = new CoffeeCrop(
            "coffee",
            null, // 野生方块将在后面注册时设置
            null, // 种植方块暂不处理
            5,    // 稀有度：平均每8个区块生成一次
            16    // 密度：每片区域最多12个
    );

    private CoffeeCrop(String name, Block wildPlant, Block plant, int rarity, int density) {
        this.name = name;
        this.wildPlant = wildPlant;
        this.plant = plant;
        this.rarity = rarity;
        this.density = density;
    }

    // 配置特征键
    public ResourceKey<ConfiguredFeature<?, ?>> getConfigKey() {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                KachuDelight.loc("wild_" + name));
    }

    // 放置特征键
    public ResourceKey<PlacedFeature> getPlacementKey() {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                KachuDelight.loc("wild_" + name));
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
        // 关键修改：在运行时从注册表获取方块
        HolderGetter<Block> blockLookup = ctx.lookup(Registries.BLOCK);
        // 使用你的方块注册ID
        Holder<Block> wildCoffeeHolder = blockLookup.getOrThrow(
                ResourceKey.create(Registries.BLOCK, KachuDelight.loc("wild_coffee_bush"))
        );

        FeatureUtils.register(ctx, getConfigKey(), Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(density, 8, 3,
                        PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
                                        // 使用获取到的方块Holder
                                        BlockStateProvider.simple(wildCoffeeHolder.value())),
                                BlockPredicate.allOf(
                                        BlockPredicate.replaceable(),
                                        BlockPredicate.noFluid(),
                                        BlockPredicate.matchesBlocks(
                                                Direction.DOWN.getNormal(),
                                                Blocks.GRASS_BLOCK,
                                                Blocks.DIRT,
                                                Blocks.COARSE_DIRT,
                                                Blocks.PODZOL,
                                                Blocks.ROOTED_DIRT
                                        ),
                                        BlockPredicate.allOf(
                                                // 上方没有树叶
                                                BlockPredicate.not(BlockPredicate.matchesTag(BlockPos.ZERO.above(), BlockTags.LEAVES)),
                                                // 北面没有树叶 (南-北方向，Z轴负向)
                                                BlockPredicate.not(BlockPredicate.matchesTag(BlockPos.ZERO.north(), BlockTags.LEAVES)),
                                                // 南面没有树叶 (Z轴正向)
                                                BlockPredicate.not(BlockPredicate.matchesTag(BlockPos.ZERO.south(), BlockTags.LEAVES)),
                                                // 西面没有树叶 (东-西方向，X轴负向)
                                                BlockPredicate.not(BlockPredicate.matchesTag(BlockPos.ZERO.west(), BlockTags.LEAVES)),
                                                // 东面没有树叶 (X轴正向)
                                                BlockPredicate.not(BlockPredicate.matchesTag(BlockPos.ZERO.east(), BlockTags.LEAVES))
                                        )
                                )
                        )));
    }

    // 注册放置特征
    public void registerPlacements(BootstapContext<PlacedFeature> ctx) {
        PlacementUtils.register(ctx, getPlacementKey(),
                ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(getConfigKey()),
                RarityFilter.onAverageOnceEvery(rarity),  // 稀有度
                InSquarePlacement.spread(),               // 方形分布
                PlacementUtils.HEIGHTMAP,                 // 高度图适应
                BiomeFilter.biome()                       // 生物群系过滤
        );
    }
}