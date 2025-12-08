package com.kachudelight.kachu.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = com.kachudelight.kachu.KachuDelight.MOD_ID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // 注册生物群系标签提供器
        generator.addProvider(event.includeServer(),
                new KachuBiomeTagsProvider(output, lookupProvider, existingFileHelper));

        // 注册数据包注册器（用于世界生成）
        generator.addProvider(event.includeServer(),
                new KachuDatapackRegistriesGen(output, lookupProvider));

        // 你可以在这里添加其他数据提供器：
        // - 物品模型提供器
        // - 方块状态提供器
        // - 战利品表提供器
        // - 配方提供器
        // - 等等...
    }
}