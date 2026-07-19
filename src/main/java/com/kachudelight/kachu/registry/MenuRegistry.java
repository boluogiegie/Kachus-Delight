package com.kachudelight.kachu.registry;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.machine.coffee.StarlightCoffeeMachineMenu;
import com.kachudelight.kachu.machine.tea.TeaBrewingMachineMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, KachuDelight.MOD_ID);
    public static final RegistryObject<MenuType<StarlightCoffeeMachineMenu>> STARLIGHT_COFFEE_MACHINE_MENU = MENUS.register("starlight_coffee_machine", () -> IForgeMenuType.create(StarlightCoffeeMachineMenu::new));
    public static final RegistryObject<MenuType<TeaBrewingMachineMenu>> TEA_BREWING_MACHINE_MENU = MENUS.register("tea_brewing_machine", () -> IForgeMenuType.create(TeaBrewingMachineMenu::new));
}
