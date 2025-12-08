package com.kachudelight.kachu.item.food;

import com.kachudelight.kachu.block.food.AbstractFoodBlock;
import com.kachudelight.kachu.item.PlaceableConsumableItem;
import com.kachudelight.kachu.registry.BlockRegistry;
import com.kachudelight.kachu.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
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

public class KanamiOmeletteRiceItem extends PlaceableConsumableItem {
    private static final ChatFormatting PINK_STYLE = ChatFormatting.LIGHT_PURPLE;
    private final Supplier<ItemStack> nextStageSupplier;
    private final int biteStage;
    private final boolean isLastBite;

    // 完整蛋包饭的构造函数（三阶段简化版）
    public KanamiOmeletteRiceItem(Properties properties) {
        super(BlockRegistry.KANAMI_OMELETTE_RICE_BLOCK.get(), properties);
        this.nextStageSupplier = () -> new ItemStack(ItemRegistry.KANAMI_OMELETTE_RICE1.get());
        this.biteStage = 0;
        this.isLastBite = false;
    }

    // 完整构造函数（用于创建不同阶段的蛋包饭）
    public KanamiOmeletteRiceItem(Properties properties, Block blockToPlace,
                                   Supplier<ItemStack> nextStageSupplier,
                                   int biteStage, boolean isLastBite) {
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

            BlockState stateToPlace = this.blockToPlace.defaultBlockState()
                    .setValue(AbstractFoodBlock.BITES, this.biteStage); // 关键设置

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
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flagIn) {
        // 先调用父类方法，这会添加农夫乐事的食物效果提示
        super.appendHoverText(stack, level, tooltip, flagIn);

        // 然后添加你自己的自定义描述
        String description = getDescriptionForStage(biteStage);
        Component customDescription = Component.literal(description)
                .withStyle(ChatFormatting.LIGHT_PURPLE)
                .withStyle(ChatFormatting.ITALIC);
        tooltip.add(customDescription);
    }

    private String getDescriptionForStage(int stage) {
        switch (stage) {
            case 1:
                return "已经吃过一口了哦~ 还剩下两口喵！";
            case 2:
                return "最后一口啦，要好好品尝喵！";
            default: // 0
                return "香奈美的独家秘方！香香甜甜的蛋包饭，快变美味吧~";
        }
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
                return "item.kachu.kanami_omelette_rice1";
            case 2:
                return "item.kachu.kanami_omelette_rice2";
            case 0:
            default:
                return "item.kachu.kanami_omelette_rice";
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        Component baseName = Component.translatable(this.getDescriptionId());
        return baseName.copy()
                .withStyle(PINK_STYLE)
                .withStyle(ChatFormatting.ITALIC);
    }
}