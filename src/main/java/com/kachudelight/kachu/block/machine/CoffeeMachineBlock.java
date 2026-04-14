package com.kachudelight.kachu.block.machine;

import com.kachudelight.kachu.block.entity.CoffeeMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class CoffeeMachineBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    // 基础碰撞箱（朝北方向）- 逆时针旋转90度后的正确形状
    private static final VoxelShape BASE_SHAPE_NORTH = Shapes.or(
            Block.box(2.0D, 0.0D, 12.0D, 4.0D, 0.5D, 14.0D),
            Block.box(2.0D, 0.0D, 2.0D, 4.0D, 0.5D, 4.0D),
            Block.box(10.0D, 0.0D, 2.0D, 12.0D, 0.5D, 4.0D),
            Block.box(10.0D, 0.0D, 12.0D, 12.0D, 0.5D, 14.0D),
            Block.box(2.0D, 0.5D, 2.0D, 12.0D, 2.0D, 14.0D),
            Block.box(3.0D, 2.0D, 3.0D, 12.0D, 2.5D, 13.0D),
            Block.box(7.0D, 2.5D, 3.0D, 12.0D, 8.0D, 13.0D),
            Block.box(3.0D, 8.0D, 3.0D, 12.0D, 11.0D, 13.0D),
            Block.box(5.0D, 11.0D, 4.25D, 7.5D, 13.0D, 6.75D)
    );

    public CoffeeMachineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);

        // 根据朝向返回相应的碰撞箱（逆时针旋转）
        return getRotatedShape(BASE_SHAPE_NORTH, facing);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // 玩家面对的方向，咖啡机会朝向玩家
        Direction facing = context.getHorizontalDirection();
        return this.defaultBlockState().setValue(FACING, facing);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

//    @Override
//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
//        if (!level.isClientSide()) {
//            BlockEntity blockEntity = level.getBlockEntity(pos);
//            if (blockEntity instanceof CoffeeMachineBlockEntity) {
//                NetworkHooks.openScreen((ServerPlayer) player,
//                        (CoffeeMachineBlockEntity) blockEntity,
//                        buf -> buf.writeBlockPos(pos));
//                return InteractionResult.CONSUME;
//            }
//        }
//        return InteractionResult.SUCCESS;
//    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CoffeeMachineBlockEntity machine) {
                ItemStack heldItem = player.getItemInHand(hand);

                // 逻辑：右键接水（拿着空桶）
                if (heldItem.is(net.minecraft.world.item.Items.BUCKET) && machine.getWaterAmount() > 0) {
                    machine.addWater(-1); // 减水
                    level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);

                    ItemStack waterBucket = new ItemStack(net.minecraft.world.item.Items.WATER_BUCKET);
                    if (!player.isCreative()) {
                        heldItem.shrink(1);
                        if (heldItem.isEmpty()) player.setItemInHand(hand, waterBucket);
                        else if (!player.getInventory().add(waterBucket)) player.drop(waterBucket, false);
                    }
                    return InteractionResult.SUCCESS;
                }
                // 逻辑：右键灌水（拿着水桶）
                else if (heldItem.is(net.minecraft.world.item.Items.WATER_BUCKET) && !machine.isWaterFull()) {
                    machine.addWater(1);
                    level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.isCreative()) {
                        heldItem.shrink(1);
                        if (heldItem.isEmpty()) {
                            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                        }
                        else if (!player.getInventory().add(new ItemStack(Items.BUCKET))) {
                            player.drop(new ItemStack(Items.BUCKET), false);
                        }
                    }
                    return InteractionResult.SUCCESS;
                } else if (heldItem.is(Items.WATER_BUCKET) && machine.isWaterFull()) {
                    player.displayClientMessage(net.minecraft.network.chat.Component.literal("水已经满了喵！"), true);
                    return InteractionResult.SUCCESS;
                }
                NetworkHooks.openScreen((ServerPlayer) player, machine, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CoffeeMachineBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level,
                                                                  BlockState state,
                                                                  BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType,
                com.kachudelight.kachu.registry.BlockEntityRegistry.COFFEE_MACHINE.get(),
                CoffeeMachineBlockEntity::tick);
    }

    /**
     * 获取旋转后的碰撞箱（逆时针旋转）
     */
    private VoxelShape getRotatedShape(VoxelShape baseShape, Direction direction) {
        // 根据朝向计算旋转次数（逆时针）
        int rotations = 0;
        switch (direction) {
            case EAST -> rotations = 0;
            case SOUTH -> rotations = 3;
            case WEST -> rotations = 2;
            case NORTH -> rotations = 1;
        }

        // 应用逆时针旋转
        VoxelShape rotatedShape = baseShape;
        for (int i = 0; i < rotations; i++) {
            rotatedShape = rotateCounterClockwise(rotatedShape);
        }

        return rotatedShape;
    }

    /**
     * 将碰撞箱逆时针旋转90度
     */
    private VoxelShape rotateCounterClockwise(VoxelShape shape) {
        AtomicReference<VoxelShape> result = new AtomicReference<>(Shapes.empty());

        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            // 逆时针旋转90度：x' = z, z' = 1 - x
            double newMinX = minZ;
            double newMaxX = maxZ;
            double newMinZ = 1 - maxX;
            double newMaxZ = 1 - minX;

            result.set(Shapes.or(result.get(),
                    Shapes.box(newMinX, minY, newMinZ, newMaxX, maxY, newMaxZ)));
        });

        return result.get();
    }
}