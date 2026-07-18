package com.kachudelight.kachu.registry;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.item.FoodList;
import com.kachudelight.kachu.item.food.KanamiOmeletteRiceItem;
import com.kachudelight.kachu.item.food.OmeletteRiceItem;
import com.kachudelight.kachu.item.food.QuickEdibleItem;
import com.kachudelight.kachu.item.food.TeaLeafItem;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KachuDelight.MOD_ID);
    // 咖啡豆
    public static final RegistryObject<Item> COFFEE_BEAN = ITEMS.register("coffee_bean", () -> new ItemNameBlockItem(BlockRegistry.COFFEE_CROP.get(), new Item.Properties().food(FoodList.COFFEE_BEAN)));
    // 野生咖啡灌木
    public static final RegistryObject<Item> WILD_COFFEE_BUSH = ITEMS.register("wild_coffee_bush", () -> new BlockItem(BlockRegistry.WILD_COFFEE_BUSH.get(), new Item.Properties()));
    // 野生茶灌木
    public static final RegistryObject<Item> WILD_TEA_BUSH = ITEMS.register("wild_tea_bush", () -> new BlockItem(BlockRegistry.WILD_TEA_BUSH.get(), new Item.Properties()));
    // 星芒咖啡机
    public static final RegistryObject<Item> STARLIGHT_COFFEE_MACHINE = ITEMS.register("starlight_coffee_machine", () -> new BlockItem(BlockRegistry.STARLIGHT_COFFEE_MACHINE.get(), new Item.Properties()));
    // 咖啡杯
    public static final RegistryObject<Item> COFFEE_CUP = ITEMS.register("coffee_cup", () -> new Item(new Item.Properties().stacksTo(16)));
    // 茶叶
    public static final RegistryObject<Item> TEA_LEAF = ITEMS.register("tea_leaf", () -> new TeaLeafItem(BlockRegistry.TEA_CROP.get(), new Item.Properties().food(FoodList.TEA_LEAF)));
    // 干茶叶
    public static final RegistryObject<Item> DRIED_TEA_LEAF = ITEMS.register("dried_tea_leaf", () -> new QuickEdibleItem(new Item.Properties().food(FoodList.DRIED_TEA_LEAF)));
    // 盘子
    public static final RegistryObject<Item> PLATE = ITEMS.register("plate", () -> new Item(new Item.Properties().stacksTo(64)));

    public static RegistryObject<Item> OMELETTE_RICE;
    public static RegistryObject<Item> OMELETTE_RICE1;
    public static RegistryObject<Item> OMELETTE_RICE2;
    public static RegistryObject<Item> KANAMI_OMELETTE_RICE;
    public static RegistryObject<Item> KANAMI_OMELETTE_RICE1;
    public static RegistryObject<Item> KANAMI_OMELETTE_RICE2;

    static {
        // 吃了两口的蛋包饭
        OMELETTE_RICE2 = ITEMS.register("omelette_rice2", () -> new OmeletteRiceItem(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(FoodList.OMELETTE_RICE2), BlockRegistry.OMELETTE_RICE_BLOCK.get(), () -> ItemStack.EMPTY, 2, true));
        // 吃了一口的蛋包饭
        OMELETTE_RICE1 = ITEMS.register("omelette_rice1", () -> new OmeletteRiceItem(new Item.Properties().stacksTo(16).food(FoodList.OMELETTE_RICE1), BlockRegistry.OMELETTE_RICE_BLOCK.get(), () -> new ItemStack(OMELETTE_RICE2.get()), 1, false));
        // 蛋包饭
        OMELETTE_RICE = ITEMS.register("omelette_rice", () -> new OmeletteRiceItem(new Item.Properties().stacksTo(16).food(FoodList.OMELETTE_RICE)));
        // 吃了两口的香奈美蛋包饭
        KANAMI_OMELETTE_RICE2 = ITEMS.register("kanami_omelette_rice2", () -> new KanamiOmeletteRiceItem(new Item.Properties().stacksTo(16).craftRemainder(Items.BOWL).food(FoodList.KANAMI_OMELETTE_RICE2), BlockRegistry.KANAMI_OMELETTE_RICE_BLOCK.get(), () -> ItemStack.EMPTY, 2, true));
        // 吃了一口的香奈美蛋包饭
        KANAMI_OMELETTE_RICE1 = ITEMS.register("kanami_omelette_rice1", () -> new KanamiOmeletteRiceItem(new Item.Properties().stacksTo(16).food(FoodList.KANAMI_OMELETTE_RICE1), BlockRegistry.KANAMI_OMELETTE_RICE_BLOCK.get(), () -> new ItemStack(KANAMI_OMELETTE_RICE2.get()), 1, false));
        // 香奈美蛋包饭
        KANAMI_OMELETTE_RICE = ITEMS.register("kanami_omelette_rice", () -> new KanamiOmeletteRiceItem(new Item.Properties().stacksTo(16).food(FoodList.KANAMI_OMELETTE_RICE)));
    }
}
