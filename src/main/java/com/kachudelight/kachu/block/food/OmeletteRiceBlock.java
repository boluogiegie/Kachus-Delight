package com.kachudelight.kachu.block.food;

import com.kachudelight.kachu.block.KanamiPlateBlock;
import com.kachudelight.kachu.registry.BlockRegistry;
import com.kachudelight.kachu.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class OmeletteRiceBlock extends AbstractFoodBlock {

    private static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D)
    };

    public OmeletteRiceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int bites = state.getValue(BITES);
        if (!player.getItemInHand(hand).isEmpty()) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            ItemStack foodItem = getFoodItemForBite(bites);
            if (!foodItem.isEmpty()) {
                player.eat(level, foodItem);
            }
            level.playSound(null, pos, SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            if (bites < 2) {
                level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
            } else {
                Direction facing = state.getValue(FACING);
                BlockState plateState = getContainerBlock().defaultBlockState().setValue(KanamiPlateBlock.FACING, facing);
                level.setBlock(pos, plateState, 3);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public ItemStack getFoodItemForBite(int bites) {
        switch (bites) {
            case 0:
                return new ItemStack(ItemRegistry.OMELETTE_RICE.get());
            case 1:
                return new ItemStack(ItemRegistry.OMELETTE_RICE1.get());
            case 2:
                return new ItemStack(ItemRegistry.OMELETTE_RICE2.get());
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack getContainerItem() {
        return new ItemStack(Items.BOWL); // 返回碗
    }
    public @NotNull Block getContainerBlock() {
        return BlockRegistry.KANAMI_PLATE.get();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        int bites = state.getValue(BITES);
        switch (bites) {
            case 0:
                return new ItemStack(ItemRegistry.OMELETTE_RICE.get());
            case 1:
                return new ItemStack(ItemRegistry.OMELETTE_RICE1.get());
            case 2:
                return new ItemStack(ItemRegistry.OMELETTE_RICE2.get());
            default:
                return new ItemStack(ItemRegistry.OMELETTE_RICE.get());
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