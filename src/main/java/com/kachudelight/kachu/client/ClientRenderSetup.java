package com.kachudelight.kachu.client; // 建议创建client包来放客户端代码

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.registry.BlockRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = KachuDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRenderSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // 等待所有东西都加载完毕后再执行
        event.enqueueWork(() -> {
            // 将“野生咖啡灌木”方块的渲染类型设置为 CUTOUT（镂空）
            ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WILD_COFFEE_BUSH.get(), RenderType.cutout());

            // 如果你以后添加了其他需要镂空渲染的方块（比如未来的咖啡作物），可以在这里继续添加：
            // ItemBlockRenderTypes.setRenderLayer(BlockRegistry.COFFEE_PLANT.get(), RenderType.cutout());
        });
    }
}