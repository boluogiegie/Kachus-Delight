package com.kachudelight.kachu;

import com.kachudelight.kachu.registry.*;
import com.kachudelight.kachu.screen.CoffeeMachineScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(KachuDelight.MOD_ID)
public class KachuDelight {
    public static final String MOD_ID = "kachu";
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final RegistryObject<CreativeModeTab> KACHU_DELIGHT_GROUP = CREATIVE_MODE_TABS.register("kachu_delight", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.kachu_delight")).icon(() -> new ItemStack(ItemRegistry.OMELETTE_RICE.get())).displayItems((parameters, output) -> {ItemRegistry.ITEMS.getEntries().forEach(item -> output.accept(item.get()));}).build());

    public KachuDelight() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        BlockRegistry.BLOCKS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        EffectRegistry.EFFECTS.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);
        MenuRegistry.MENUS.register(modEventBus);
        RecipeRegistry.RECIPE_TYPES.register(modEventBus);
        RecipeRegistry.RECIPE_SERIALIZERS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modEventBus.addListener(this::clientSetup);

    }

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 注册咖啡机屏幕
            MenuScreens.register(MenuRegistry.COFFEE_MACHINE_MENU.get(), CoffeeMachineScreen::new);
        });
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}