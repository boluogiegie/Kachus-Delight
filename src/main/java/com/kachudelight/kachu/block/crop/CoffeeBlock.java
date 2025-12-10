package com.kachudelight.kachu.block.crop;

import com.kachudelight.kachu.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collections;
import java.util.List;

public class CoffeeBlock extends CropBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);

    public CoffeeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ItemRegistry.COFFEE_BEAN.get();
    }

    @Override
    public int getMaxAge() {
        return 4;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        int age = state.getValue(AGE);
        ItemStack tool = builder.getParameter(LootContextParams.TOOL);
        boolean hasSilkTouch = false;
        if (tool != null) {
            hasSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;
        }
        if (age < getMaxAge()) {
            return Collections.singletonList(new ItemStack(getBaseSeedId(), 1));
        } else {
            int count = 2 + builder.getLevel().getRandom().nextInt(3);
            return Collections.singletonList(new ItemStack(getBaseSeedId(), count));
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return !isMaxAge(state);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int currentAge = state.getValue(AGE);
        int maxAge = getMaxAge();

        if (currentAge < maxAge) {
            if (random.nextFloat() < 0.8f) {
                int newAge = currentAge + 1;
                if (newAge > maxAge) {
                    newAge = maxAge;
                }
                level.setBlock(pos, state.setValue(AGE, newAge), 2);
            }
        }
    }
}