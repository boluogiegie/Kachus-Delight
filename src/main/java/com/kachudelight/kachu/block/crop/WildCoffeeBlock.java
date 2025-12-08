package com.kachudelight.kachu.block.crop;

import com.kachudelight.kachu.crop.CoffeeCrop;
import com.kachudelight.kachu.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collections;
import java.util.List;

public class WildCoffeeBlock extends BushBlock {
    private final CoffeeCrop crop;

    public WildCoffeeBlock(Properties properties, CoffeeCrop crop) {
        super(properties);
        this.crop = crop;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(this);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack tool = builder.getParameter(LootContextParams.TOOL);
        if (tool != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
            return Collections.singletonList(new ItemStack(this));
        } else {
            int count = 1 + builder.getLevel().random.nextInt(2); // 1-2个
            return Collections.singletonList(new ItemStack(ItemRegistry.COFFEE_BEAN.get(), count));
        }
    }


    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(net.minecraft.tags.BlockTags.DIRT) ||
                state.is(net.minecraft.world.level.block.Blocks.FARMLAND);
    }
}