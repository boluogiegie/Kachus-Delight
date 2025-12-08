package com.kachudelight.kachu.registry;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.item.FoodList;
import com.kachudelight.kachu.item.food.KanamiOmeletteRiceItem;
import com.kachudelight.kachu.item.food.OmeletteRiceItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KachuDelight.MOD_ID);

    public static RegistryObject<Item> OMELETTE_RICE;
    public static RegistryObject<Item> OMELETTE_RICE1;
    public static RegistryObject<Item> OMELETTE_RICE2;
    public static RegistryObject<Item> KANAMI_OMELETTE_RICE;
    public static RegistryObject<Item> KANAMI_OMELETTE_RICE1;
    public static RegistryObject<Item> KANAMI_OMELETTE_RICE2;


    static {
        // 吃了两口的蛋包饭
        OMELETTE_RICE2 = ITEMS.register("omelette_rice2", () ->
                new OmeletteRiceItem(
                        new Item.Properties()
                                .stacksTo(16)
                                .craftRemainder(Items.BOWL)
                                .food(FoodList.OMELETTE_RICE2),
                        BlockRegistry.OMELETTE_RICE_BLOCK.get(),
                        () -> ItemStack.EMPTY,
                        2,
                        true
                ));
        // 吃了一口的蛋包饭
        OMELETTE_RICE1 = ITEMS.register("omelette_rice1", () ->
                new OmeletteRiceItem(
                        new Item.Properties()
                                .stacksTo(16)
                                .food(FoodList.OMELETTE_RICE1),
                        BlockRegistry.OMELETTE_RICE_BLOCK.get(),
                        () -> new ItemStack(OMELETTE_RICE2.get()),
                        1,
                        false
                ));
        // 蛋包饭
        OMELETTE_RICE = ITEMS.register("omelette_rice", () ->
                new OmeletteRiceItem(
                        new Item.Properties()
                                .stacksTo(16)
                                .food(FoodList.OMELETTE_RICE)
                ));
        // 吃了两口的香奈美蛋包饭
        KANAMI_OMELETTE_RICE2 = ITEMS.register("kanami_omelette_rice2", () ->
                new KanamiOmeletteRiceItem(
                        new Item.Properties()
                                .stacksTo(16)
                                .craftRemainder(Items.BOWL)
                                .food(FoodList.KANAMI_OMELETTE_RICE2),
                        BlockRegistry.KANAMI_OMELETTE_RICE_BLOCK.get(), // 明确传入方块
                        () -> ItemStack.EMPTY,
                        2,
                        true
                ));
        // 吃了一口的香奈美蛋包饭
        KANAMI_OMELETTE_RICE1 = ITEMS.register("kanami_omelette_rice1", () ->
                new KanamiOmeletteRiceItem(
                        new Item.Properties()
                                .stacksTo(16)
                                .food(FoodList.KANAMI_OMELETTE_RICE1),
                        BlockRegistry.KANAMI_OMELETTE_RICE_BLOCK.get(), // 明确传入方块
                        () -> new ItemStack(KANAMI_OMELETTE_RICE2.get()),
                        1,
                        false
                ));
        // 香奈美蛋包饭
        KANAMI_OMELETTE_RICE = ITEMS.register("kanami_omelette_rice", () ->
                new KanamiOmeletteRiceItem(
                        new Item.Properties()
                                .stacksTo(16)
                                .food(FoodList.KANAMI_OMELETTE_RICE)
                ));
    }
}