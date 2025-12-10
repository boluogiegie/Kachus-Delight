package com.kachudelight.kachu.client; // 建议创建client包来放客户端代码

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.registry.BlockRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = KachuDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRenderSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WILD_COFFEE_BUSH.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.COFFEE_CROP.get(), RenderType.cutoutMipped());
        // 添加其他镂空渲染的方块
        });
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        // 为咖啡作物注册固定颜色（不使用原版的阶段颜色变化）
        event.register((state, world, pos, tintIndex) -> {
            // 返回一个固定颜色（白色，即不应用任何颜色滤镜）
            return -1; // -1 表示使用原贴图颜色，不应用额外颜色
        }, BlockRegistry.COFFEE_CROP.get());

        // 环境颜色（随生物群系变化）
        // event.register((state, world, pos, tintIndex) -> {
        //     if (world != null && pos != null) {
        //         // 不受生物群系影响
        //         return 0x7A9D5E;
        //     }
        //     return FoliageColor.getDefaultColor(); // 默认颜色
        // }, BlockRegistry.COFFEE_CROP.get());
    }
}