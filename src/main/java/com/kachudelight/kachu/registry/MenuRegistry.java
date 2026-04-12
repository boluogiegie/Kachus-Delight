package com.kachudelight.kachu.registry;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.menu.CoffeeMachineMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, KachuDelight.MOD_ID);

    // 咖啡机菜单
    public static final RegistryObject<MenuType<CoffeeMachineMenu>> COFFEE_MACHINE_MENU =
            MENUS.register("coffee_machine",
                    () -> IForgeMenuType.create(CoffeeMachineMenu::new));
}