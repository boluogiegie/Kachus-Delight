package com.kachudelight.kachu.item.food;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ProcessedTeaLeafItem extends Item {
    private static final int EAT_DURATION_TICKS = 10;

    public ProcessedTeaLeafItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return EAT_DURATION_TICKS;
    }
}
