package com.kachudelight.kachu.item.food;

import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class TeaLeafItem extends ItemNameBlockItem {
    private static final int EAT_DURATION_TICKS = 10;

    public TeaLeafItem(Block cropBlock, Properties properties) {
        super(cropBlock, properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return EAT_DURATION_TICKS;
    }
}
