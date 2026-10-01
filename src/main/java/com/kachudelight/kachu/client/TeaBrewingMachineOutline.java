package com.kachudelight.kachu.client;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 泡茶机精修用的亮粉色选中轮廓。 */
@Mod.EventBusSubscriber(modid = KachuDelight.MOD_ID, value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TeaBrewingMachineOutline {
    /* 暂停自定义高亮，精修时可取消注释重新启用。
    @SubscribeEvent
    public static void renderOutline(RenderHighlightEvent.Block event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) return;

        BlockPos pos = event.getTarget().getBlockPos();
        BlockState state = minecraft.level.getBlockState(pos);
        if (!state.is(BlockRegistry.TEA_BREWING_MACHINE.get())) return;

        VoxelShape shape = state.getShape(minecraft.level, pos,
                CollisionContext.of(minecraft.player));
        Vec3 camera = event.getCamera().getPosition();
        PoseStack poses = event.getPoseStack();
        VertexConsumer lines = event.getMultiBufferSource().getBuffer(RenderType.lines());

        poses.pushPose();
        poses.translate(pos.getX() - camera.x, pos.getY() - camera.y, pos.getZ() - camera.z);
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                LevelRenderer.renderLineBox(poses, lines,
                        minX, minY, minZ, maxX, maxY, maxZ,
                        1.0F, 0.2F, 0.8F, 1.0F));
        poses.popPose();

        event.setCanceled(true);
    }
    */
}
