package com.kachudelight.kachu.item.food;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;
import java.util.function.Supplier;

public class DrinkItem extends Item {
    private final Supplier<Item> container;

    public DrinkItem(Properties properties, Supplier<Item> container) {
        super(properties);
        this.container = container;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        TextUtils.addFoodEffectTooltip(stack, tooltip, 1.0F);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        if (consumer instanceof ServerPlayer player) {
            CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            FoodProperties food = getFoodProperties(stack, consumer);
            if (food != null) {
                if (consumer instanceof Player player) {
                    player.getFoodData().eat(food.getNutrition(), food.getSaturationModifier());
                }
                for (var effect : food.getEffects()) {
                    if (effect.getFirst() != null && level.random.nextFloat() < effect.getSecond()) {
                        consumer.addEffect(new MobEffectInstance(effect.getFirst()));
                    }
                }
            }
            consumer.gameEvent(GameEvent.DRINK);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.INSTANT_EFFECT,
                        consumer.getX(), consumer.getEyeY() - 0.15D, consumer.getZ(),
                        6, 0.18D, 0.08D, 0.18D, 0.0025D);
            }
        }

        if (consumer instanceof Player player && player.getAbilities().instabuild) return stack;
        stack.shrink(1);
        ItemStack emptyContainer = new ItemStack(container.get());
        if (stack.isEmpty()) return emptyContainer;
        if (!level.isClientSide && consumer instanceof Player player) {
            if (!player.getInventory().add(emptyContainer)) player.drop(emptyContainer, false);
        }
        return stack;
    }
}
