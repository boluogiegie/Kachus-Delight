package com.kachudelight.kachu.item.food;

import com.kachudelight.kachu.block.food.AbstractFoodBlock;
import com.kachudelight.kachu.item.PlaceableConsumableItem;
import com.kachudelight.kachu.registry.BlockRegistry;
import com.kachudelight.kachu.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class OmeletteRiceItem extends PlaceableConsumableItem {

    private final Supplier<ItemStack> nextStageSupplier;
    private final int biteStage;
    private final boolean isLastBite;

    public OmeletteRiceItem(Properties properties) {
        super(BlockRegistry.OMELETTE_RICE_BLOCK.get(), properties);
        this.nextStageSupplier = () -> new ItemStack(ItemRegistry.OMELETTE_RICE1.get());
        this.biteStage = 0;
        this.isLastBite = false;
    }

    public OmeletteRiceItem(Properties properties, Block blockToPlace, Supplier<ItemStack> nextStageSupplier, int biteStage, boolean isLastBite) {
        super(blockToPlace, properties);
        this.nextStageSupplier = nextStageSupplier;
        this.biteStage = biteStage;
        this.isLastBite = isLastBite;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        ItemStack result = super.finishUsingItem(stack, level, consumer);
        if (!level.isClientSide && consumer instanceof Player player && !player.getAbilities().instabuild) {
            if (nextStageSupplier != null) {
                ItemStack nextStage = nextStageSupplier.get().copy();
                if (!nextStage.isEmpty()) {
                    if (result.isEmpty()) {
                        return nextStage;
                    } else {
                        if (!player.getInventory().add(nextStage)) {
                            player.drop(nextStage, false);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            Level level = context.getLevel();
            BlockPos clickedPos = context.getClickedPos();
            Direction clickedFace = context.getClickedFace();
            ItemStack stack = context.getItemInHand();

            BlockPlaceContext blockPlaceContext = new BlockPlaceContext(context);
            BlockPos placePos = blockPlaceContext.getClickedPos();
            BlockState clickedState = level.getBlockState(placePos);

            if (!clickedState.canBeReplaced(blockPlaceContext)) {
                placePos = placePos.relative(clickedFace);
            }
            if (!level.getBlockState(placePos).canBeReplaced(blockPlaceContext)) {
                return InteractionResult.FAIL;
            }

            BlockState stateForPlacement = this.blockToPlace.getStateForPlacement(blockPlaceContext);
            BlockState stateToPlace = (stateForPlacement != null) ? stateForPlacement : this.blockToPlace.defaultBlockState();
            stateToPlace = stateToPlace.setValue(AbstractFoodBlock.BITES, this.biteStage);

            if (level.setBlock(placePos, stateToPlace, 3)) {
                if (!level.isClientSide) {
                    level.playSound(null, placePos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
    }

    public int getBiteStage() {
        return biteStage;
    }

    public boolean isLastBite() {
        return isLastBite;
    }

    @Override
    public String getDescriptionId() {
        switch (this.biteStage) {
            case 1:
                return "item.kachu.omelette_rice1";
            case 2:
                return "item.kachu.omelette_rice2";
            case 0:
            default:
                return "item.kachu.omelette_rice";
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId());
    }
}