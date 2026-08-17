package com.kachudelight.kachu.machine.tea;

import com.kachudelight.kachu.registry.ItemRegistry;
import com.kachudelight.kachu.registry.MenuRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class TeaBrewingMachineMenu extends AbstractContainerMenu {
    public final TeaBrewingMachineBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public TeaBrewingMachineMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public TeaBrewingMachineMenu(int id, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(MenuRegistry.TEA_BREWING_MACHINE_MENU.get(), id);
        blockEntity = (TeaBrewingMachineBlockEntity) entity;
        level = inventory.player.level();
        this.data = data;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, TeaBrewingMachineBlockEntity.INPUT_SLOT_1, 26, 20));
            addSlot(new SlotItemHandler(handler, TeaBrewingMachineBlockEntity.INPUT_SLOT_2, 44, 20));
            addSlot(new SlotItemHandler(handler, TeaBrewingMachineBlockEntity.INPUT_SLOT_3, 26, 38));
            addSlot(new SlotItemHandler(handler, TeaBrewingMachineBlockEntity.INPUT_SLOT_4, 44, 38));
            addSlot(new SlotItemHandler(handler, TeaBrewingMachineBlockEntity.CUP_SLOT, 72, 48));
            addSlot(new SlotItemHandler(handler, TeaBrewingMachineBlockEntity.OUTPUT_SLOT, 103, 28));
            addSlot(new SlotItemHandler(handler, TeaBrewingMachineBlockEntity.WATER_IN_SLOT, 132, 7));
            addSlot(new SlotItemHandler(handler, TeaBrewingMachineBlockEntity.WATER_OUT_SLOT, 132, 60));
        });
        addDataSlots(data);
    }

    public int getScaledProgress() {
        int progress = data.get(0);
        int maximum = data.get(1);
        return progress == 0 || maximum == 0 ? 0 : progress * 31 / maximum;
    }

    public int getWaterAmount() {
        return data.get(2);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack original = sourceStack.copy();

        if (index >= 36 && index < 44) {
            if (!moveItemStackTo(sourceStack, 0, 36, false)) return ItemStack.EMPTY;
            if (index == 41) sourceSlot.onQuickCraft(sourceStack, original);
        } else if (index >= 0 && index < 36) {
            if (sourceStack.is(Items.WATER_BUCKET) || sourceStack.is(Items.BUCKET)) {
                if (!moveItemStackTo(sourceStack, 42, 43, false)) return ItemStack.EMPTY;
            } else if (sourceStack.is(ItemRegistry.GLASS_CUP.get())) {
                if (!moveItemStackTo(sourceStack, 40, 41, false)) return ItemStack.EMPTY;
            } else if (!moveItemStackTo(sourceStack, 36, 40, false)) {
                if (index < 27) {
                    if (!moveItemStackTo(sourceStack, 27, 36, false)) return ItemStack.EMPTY;
                } else if (!moveItemStackTo(sourceStack, 0, 27, false)) return ItemStack.EMPTY;
            }
        }

        if (sourceStack.isEmpty()) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();
        if (sourceStack.getCount() == original.getCount()) return ItemStack.EMPTY;
        sourceSlot.onTake(player, sourceStack);
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player,
                blockEntity.getBlockState().getBlock());
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }
    }
}
