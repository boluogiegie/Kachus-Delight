package com.kachudelight.kachu.event;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.registry.BlockRegistry;
import com.kachudelight.kachu.registry.ItemRegistry;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = KachuDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 蛋包饭的堆肥配方
            ComposterBlock.COMPOSTABLES.put(ItemRegistry.OMELETTE_RICE.get(), 0.85f);
            ComposterBlock.COMPOSTABLES.put(ItemRegistry.OMELETTE_RICE1.get(), 0.85f);
            ComposterBlock.COMPOSTABLES.put(ItemRegistry.OMELETTE_RICE2.get(), 0.85f);
            ComposterBlock.COMPOSTABLES.put(ItemRegistry.KANAMI_OMELETTE_RICE.get(), 0.85f);
            ComposterBlock.COMPOSTABLES.put(ItemRegistry.KANAMI_OMELETTE_RICE1.get(), 0.85f);
            ComposterBlock.COMPOSTABLES.put(ItemRegistry.KANAMI_OMELETTE_RICE2.get(), 0.85f);

            ComposterBlock.COMPOSTABLES.put(ItemRegistry.COFFEE_BEAN.get(), 0.3f);
            ComposterBlock.COMPOSTABLES.put(BlockRegistry.WILD_COFFEE_BUSH.get().asItem(), 0.65f);
        });
    }
}