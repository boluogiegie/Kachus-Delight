package com.kachudelight.kachu.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class PlaceableConsumableItem extends ConsumableItem {
    protected final Block blockToPlace;

    public PlaceableConsumableItem(Block blockToPlace, Properties properties) {
        super(properties, true); // 总是显示食物效果提示
        this.blockToPlace = blockToPlace;
    }

    public PlaceableConsumableItem(Block blockToPlace, Properties properties, boolean hasFoodEffectTooltip) {
        super(properties, hasFoodEffectTooltip);
        this.blockToPlace = blockToPlace;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();

        if (player != null && player.isShiftKeyDown()) {

            BlockPlaceContext blockPlaceContext = new BlockPlaceContext(context);

            if (!blockToPlace.canSurvive(blockToPlace.defaultBlockState(), level, clickedPos)) {
                return InteractionResult.FAIL;
            }

            BlockPos placePos = blockPlaceContext.getClickedPos();
            BlockState clickedState = level.getBlockState(placePos);

            if (!clickedState.canBeReplaced(blockPlaceContext)) {
                placePos = placePos.relative(clickedFace);
            }

            if (!level.getBlockState(placePos).canBeReplaced(blockPlaceContext)) {
                return InteractionResult.FAIL;
            }

            if (level.setBlock(placePos, blockToPlace.defaultBlockState(), 3)) {
                if (!level.isClientSide) {
                    level.playSound(null,
                            placePos,
                            SoundEvents.WOOL_PLACE,
                            SoundSource.BLOCKS,
                            1.0F,
                            1.0F);
                }

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }
}