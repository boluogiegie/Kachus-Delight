package com.kachudelight.kachu.block.crop;

import com.kachudelight.kachu.crop.CoffeeCrop;
import com.kachudelight.kachu.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // 如果你需要更复杂的随机性，可以在这里添加自定义属性
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // 使用随机数决定使用哪个变体
        RandomSource random = context.getLevel().getRandom();
        return this.defaultBlockState(); // 随机性由方块状态variant系统自动处理
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