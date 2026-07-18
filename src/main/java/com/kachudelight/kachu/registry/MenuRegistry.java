package com.kachudelight.kachu.registry;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.menu.StarlightCoffeeMachineMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, KachuDelight.MOD_ID);

    // 星芒咖啡机菜单
    public static final RegistryObject<MenuType<StarlightCoffeeMachineMenu>> STARLIGHT_COFFEE_MACHINE_MENU =
            MENUS.register("starlight_coffee_machine",
                    () -> IForgeMenuType.create(StarlightCoffeeMachineMenu::new));
}