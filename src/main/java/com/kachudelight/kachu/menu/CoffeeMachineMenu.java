package com.kachudelight.kachu.menu;

import com.kachudelight.kachu.block.entity.CoffeeMachineBlockEntity;
import com.kachudelight.kachu.registry.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class CoffeeMachineMenu extends AbstractContainerMenu {
    public final CoffeeMachineBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public CoffeeMachineMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public CoffeeMachineMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegistry.COFFEE_MACHINE_MENU.get(), id);
        checkContainerSize(inv, CoffeeMachineBlockEntity.TOTAL_SLOTS);
        blockEntity = (CoffeeMachineBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, CoffeeMachineBlockEntity.INPUT_SLOT, 36, 21));
            this.addSlot(new SlotItemHandler(handler, CoffeeMachineBlockEntity.FUEL_SLOT, 36, 39));
            this.addSlot(new SlotItemHandler(handler, CoffeeMachineBlockEntity.CONTAINER_SLOT, 70, 48));
            this.addSlot(new SlotItemHandler(handler, CoffeeMachineBlockEntity.OUTPUT_SLOT, 104, 30));
            this.addSlot(new SlotItemHandler(handler, CoffeeMachineBlockEntity.WATER_IN_SLOT, 132, 12));
            this.addSlot(new SlotItemHandler(handler, CoffeeMachineBlockEntity.WATER_OUT_SLOT, 132, 60));
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowWidth = 25;
        if (maxProgress == 0 || progress == 0) return 0;
        return (progress * arrowWidth) / maxProgress;
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = CoffeeMachineBlockEntity.TOTAL_SLOTS;

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        final int PLAYER_INV_START = 0;
        final int PLAYER_INV_END = 35;
        final int TE_SLOT_START = 36;
        final int TE_SLOT_END = 41;
        if (index >= TE_SLOT_START && index <= TE_SLOT_END) {
            if (!moveItemStackTo(sourceStack, PLAYER_INV_START, PLAYER_INV_END + 1, false)) {
                return ItemStack.EMPTY;
            }
            if (index == 39 || index == 41) {
                sourceSlot.onQuickCraft(sourceStack, copyOfSourceStack);
            }
        }
        else if (index >= PLAYER_INV_START && index <= PLAYER_INV_END) {
            if (sourceStack.is(net.minecraft.world.item.Items.WATER_BUCKET) || sourceStack.is(net.minecraft.world.item.Items.BUCKET)) {
                if (!moveItemStackTo(sourceStack, 40, 41, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (sourceStack.is(com.kachudelight.kachu.registry.ItemRegistry.COFFEE_BEAN.get())) {
                if (!moveItemStackTo(sourceStack, 36, 37, false)) {
                    return ItemStack.EMPTY;
                }
            }
            //未来可以在这里添加判断，如果是"辅料"就 move 到 37，如果是"杯子"就 move 到 38
            else {
                if (index < 27) {
                    if (!moveItemStackTo(sourceStack, 27, 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 36) {
                    if (!moveItemStackTo(sourceStack, 0, 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        if (sourceStack.getCount() == copyOfSourceStack.getCount()) {
            return ItemStack.EMPTY;
        }

        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, blockEntity.getBlockState().getBlock());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public int getWaterAmount() {
        return this.data.get(2);
    }
}