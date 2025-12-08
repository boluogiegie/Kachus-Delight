package com.kachudelight.kachu.block.food;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractFoodBlock extends Block {
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 2);

    protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),  // 完整状态
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),  // 吃了一口
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),  // 吃了两口
    };

    public AbstractFoodBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }

    @Override
    public abstract InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit);

    // 抽象方法：获取对应食物的物品
    public abstract ItemStack getFoodItemForBite(int bites);

    // 抽象方法：吃完后返回的物品（如碗、盘子等）
    public abstract ItemStack getContainerItem();

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY; // 防止活塞推动
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return 3 - state.getValue(BITES); // 比较器输出剩余口数
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int bites = state.getValue(BITES);
        return SHAPE_BY_BITE[bites];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }
}