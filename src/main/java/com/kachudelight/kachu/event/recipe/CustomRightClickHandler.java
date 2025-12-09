package com.kachudelight.kachu.event.recipe;

import com.kachudelight.kachu.block.food.AbstractFoodBlock;
import com.kachudelight.kachu.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.registry.ModItems;

@Mod.EventBusSubscriber(modid = "kachu", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomRightClickHandler {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        ItemStack heldItem = event.getItemStack();

        if (state.getBlock() == BlockRegistry.OMELETTE_RICE_BLOCK.get() && heldItem.getItem() == ModItems.TOMATO_SAUCE.get()) {
            event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide));
            event.setCanceled(true);
            if (!world.isClientSide) {
                int currentBites = 0;
                if (state.hasProperty(AbstractFoodBlock.BITES)) {
                    currentBites = state.getValue(AbstractFoodBlock.BITES);
                }
                Direction facing = state.hasProperty(AbstractFoodBlock.FACING) ? state.getValue(AbstractFoodBlock.FACING) : Direction.NORTH;
                BlockState kanamiState = BlockRegistry.KANAMI_OMELETTE_RICE_BLOCK.get().defaultBlockState().setValue(AbstractFoodBlock.BITES, currentBites).setValue(AbstractFoodBlock.FACING, facing);world.setBlock(pos, kanamiState, 3);
                if (!event.getEntity().getAbilities().instabuild) {
                    heldItem.shrink(1);
                    ItemStack bottleStack = new ItemStack(Items.BOWL);
                    if (!event.getEntity().getInventory().add(bottleStack)) {
                        event.getEntity().drop(bottleStack, false);
                    }
                }
                world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY,
                        SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            else {
                spawnLoveParticles(world, pos);
            }
        }
    }

    private static void spawnLoveParticles(Level world, BlockPos pos) {
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.3;  // 方块中心高度
        double centerZ = pos.getZ() + 0.5;

        int totalParticles = 20;
        double radius = 0.6;  // 包裹半径

        for (int i = 0; i < totalParticles; i++) {
            double phi = Math.PI * world.random.nextDouble();
            double theta = 2 * Math.PI * world.random.nextDouble();
            double offsetX = radius * Math.sin(phi) * Math.cos(theta);
            double offsetY = radius * Math.cos(phi) * 0.6;
            double offsetZ = radius * Math.sin(phi) * Math.sin(theta);
            double pulseSpeed = 0.002;
            double pulseOffset = Math.sin(world.getGameTime() * 0.1 + i * 0.3) * 0.05;
            world.addParticle(ParticleTypes.HEART, centerX + offsetX + pulseOffset * offsetX, centerY + offsetY + pulseOffset * offsetY, centerZ + offsetZ + pulseOffset * offsetZ, 0.0, -1, 0.0);
        }
    }
}