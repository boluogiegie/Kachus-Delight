package com.kachudelight.kachu.compat.jei;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * 这是一个简单的数据类，用于在JEI中表示蛋包饭升级配方。
 * 因为用户的配方是自定义的右键交互逻辑，而不是标准的Minecraft Recipe，
 * 所以我们创建一个POJO来存储JEI显示所需的数据。
 */
public record OmeletteRiceUpgradeRecipe(
        Item inputItem,
        ItemStack inputSauce,
        Item outputItem,
        ItemStack outputBowl
) {
}