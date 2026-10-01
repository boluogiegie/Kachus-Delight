package com.kachudelight.kachu.event.food;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.registry.EffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KachuDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AlertnessSleepHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onSleep(PlayerSleepInBedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !player.hasEffect(EffectRegistry.ALERTNESS.get())
                || event.getResultStatus() != null || player.isSleeping() || !player.isAlive()) return;

        BlockPos pos = event.getPos();
        if (pos == null || !player.level().dimensionType().bedWorks()
                || !player.level().dimensionType().natural()) return;
        BlockState state = player.level().getBlockState(pos);
        if (!(state.getBlock() instanceof BedBlock)
                || state.getValue(BedBlock.OCCUPIED)) return;
        BlockPos otherPart = pos.relative(state.getValue(BedBlock.FACING).getOpposite());
        if (!isNearBed(player, pos) && !isNearBed(player, otherPart)) return;

        // 原版失败条件优先：返回后交由原版提示，并保留原版重生点设置流程。
        BlockPos aboveHead = pos.above();
        BlockPos aboveFoot = otherPart.above();
        if (player.level().getBlockState(aboveHead).isSuffocating(player.level(), aboveHead)
                || player.level().getBlockState(aboveFoot).isSuffocating(player.level(), aboveFoot)) return;
        if (!ForgeEventFactory.fireSleepingTimeCheck(player, event.getOptionalPos())) return;
        Vec3 center = Vec3.atBottomCenterOf(pos);
        if (!player.isCreative() && !player.level().getEntitiesOfClass(Monster.class,
                new AABB(center, center).inflate(8.0D, 5.0D, 8.0D),
                monster -> monster.isPreventingPlayerRest(player)).isEmpty()) return;

        // 仅当原版允许睡眠时，由提神阻止入睡；不产生额外的原版错误文案。
        event.setResult(Player.BedSleepingProblem.OTHER_PROBLEM);
        player.setRespawnPosition(player.level().dimension(), pos,
                player.getYRot(), false, false);
        player.displayClientMessage(Component.translatable("message.kachu.coffee_cannot_sleep"), true);
    }

    private static boolean isNearBed(ServerPlayer player, BlockPos pos) {
        return Math.abs(player.getX() - (pos.getX() + 0.5D)) <= 3.0D
                && Math.abs(player.getY() - pos.getY()) <= 2.0D
                && Math.abs(player.getZ() - (pos.getZ() + 0.5D)) <= 3.0D;
    }
}
