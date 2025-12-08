package com.kachudelight.kachu.event;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.registry.ItemRegistry;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent; // 注意：导入的是 ClientSetupEvent

@Mod.EventBusSubscriber(modid = KachuDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 在此处添加堆肥配方（或其他依赖于纹理加载的后期初始化）
            // 例如：ComposterBlock.COMPOSTABLES.put(ItemRegistry.OMELETTE_RICE.get(), 0.85f);
        });
    }

}