package com.kachudelight.kachu.client;

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
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WILD_TEA_BUSH.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.COFFEE_CROP.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.TEA_CROP.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.STARLIGHT_COFFEE_MACHINE.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.TEA_BREWING_MACHINE.get(), RenderType.cutoutMipped());
        // 添加其他镂空渲染的方块
        });
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {event.register((state, world, pos, tintIndex) -> {return -1;}, BlockRegistry.COFFEE_CROP.get());

        // 环境颜色
        // event.register((state, world, pos, tintIndex) -> {
        //     if (world != null && pos != null) {
        //         // 不受生物群系影响
        //         return 0x7A9D5E;
        //     }
        //     return FoliageColor.getDefaultColor(); // 默认颜色
        // }, BlockRegistry.COFFEE_CROP.get());
    }
}
