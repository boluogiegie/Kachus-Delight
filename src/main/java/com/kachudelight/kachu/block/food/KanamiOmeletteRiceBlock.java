package com.kachudelight.kachu.block.food;

import com.kachudelight.kachu.registry.BlockRegistry;
import com.kachudelight.kachu.registry.ItemRegistry;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class KanamiOmeletteRiceBlock extends AbstractFoodBlock {

    // 可以定义自己的形状数组
    private static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D)
    };

    public KanamiOmeletteRiceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
    }

    // KanamiOmeletteRiceBlock.java 修改后的 use 方法
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int bites = state.getValue(BITES);

        // 只检查玩家是否空手，移除饥饿值检查
        if (!player.getItemInHand(hand).isEmpty()) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            // 给予食物效果
            ItemStack foodItem = getFoodItemForBite(bites);
            if (!foodItem.isEmpty()) {
                player.eat(level, foodItem);
            }

            // 播放食用音效
            level.playSound(null, pos, SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);

            // 更新方块状态
            if (bites < 2) {
                level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            } else {
                level.setBlock(pos, getContainerBlock().defaultBlockState(), 3);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public ItemStack getFoodItemForBite(int bites) {
        switch (bites) {
            case 0:
                return new ItemStack(ItemRegistry.KANAMI_OMELETTE_RICE.get());
            case 1:
                return new ItemStack(ItemRegistry.KANAMI_OMELETTE_RICE1.get());
            case 2:
                return new ItemStack(ItemRegistry.KANAMI_OMELETTE_RICE2.get());
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack getContainerItem() {
        return new ItemStack(Items.BOWL); // 返回碗
    }

    // 获取吃完后的盘子方块（需要注册这个方块）
    public @NotNull Block getContainerBlock() {
        return BlockRegistry.KANAMI_PLATE.get();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        int bites = state.getValue(BITES);
        switch (bites) {
            case 0:
                return new ItemStack(ItemRegistry.KANAMI_OMELETTE_RICE.get());
            case 1:
                return new ItemStack(ItemRegistry.KANAMI_OMELETTE_RICE1.get());
            case 2:
                return new ItemStack(ItemRegistry.KANAMI_OMELETTE_RICE2.get());
            default:
                return new ItemStack(ItemRegistry.KANAMI_OMELETTE_RICE.get());
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);

        if (!level.isClientSide && !player.isCreative()) {
            ItemStack droppedItem = getFoodItemForBite(state.getValue(BITES));
            if (!droppedItem.isEmpty()) {
                popResource(level, pos, droppedItem);
            }
        }
    }

}