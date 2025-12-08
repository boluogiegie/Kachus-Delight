package com.kachudelight.kachu;

import com.kachudelight.kachu.registry.BlockRegistry;
import com.kachudelight.kachu.registry.EffectRegistry;
import com.kachudelight.kachu.registry.ItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(KachuDelight.MOD_ID)
public class KachuDelight {
    public static final String MOD_ID = "kachu";
    public static final Logger LOGGER = LogManager.getLogger(KachuDelight.class);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final RegistryObject<CreativeModeTab> KACHU_DELIGHT_GROUP = CREATIVE_MODE_TABS.register("kachu_delight", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.kachu_delight")).icon(() -> new ItemStack(ItemRegistry.OMELETTE_RICE.get())).displayItems((parameters, output) -> {ItemRegistry.ITEMS.getEntries().forEach(item -> output.accept(item.get()));}).build());

    public KachuDelight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        BlockRegistry.BLOCKS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        EffectRegistry.EFFECTS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        LOGGER.info("KachuDelight mod loaded");
    }
}