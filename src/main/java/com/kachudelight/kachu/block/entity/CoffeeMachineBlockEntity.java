// [file name]: CoffeeMachineBlockEntity.java
package com.kachudelight.kachu.block.entity;

import com.kachudelight.kachu.menu.CoffeeMachineMenu;
import com.kachudelight.kachu.recipe.CoffeeRecipe;
import com.kachudelight.kachu.recipe.CoffeeRecipeType;
import com.kachudelight.kachu.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CoffeeMachineBlockEntity extends BlockEntity implements MenuProvider {

    private int waterAmount = 0;
    public static final int MAX_WATER = 3000;
    public static final int INPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;
    public static final int CONTAINER_SLOT = 3;
    public static final int WATER_IN_SLOT = 4;
    public static final int WATER_OUT_SLOT = 5;
    public static final int TOTAL_SLOTS = 6;

    private final ItemStackHandler itemHandler = new ItemStackHandler(TOTAL_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case INPUT_SLOT -> stack.is(com.kachudelight.kachu.registry.ItemRegistry.COFFEE_BEAN.get());
                case FUEL_SLOT -> true; //辅料包还没做，暂时允许放入任何物品
                case CONTAINER_SLOT -> true; //杯子还没做，暂时允许放入任何物品
                case WATER_IN_SLOT -> stack.is(net.minecraft.world.item.Items.WATER_BUCKET) || stack.is(net.minecraft.world.item.Items.BUCKET);
                case OUTPUT_SLOT, WATER_OUT_SLOT -> false;
                default -> super.isItemValid(slot, stack);
            };
        }

    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    public CoffeeMachineBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.COFFEE_MACHINE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    case 2 -> waterAmount;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                    case 2 -> waterAmount = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.kachu.coffee_machine");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new CoffeeMachineMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("coffee_machine.progress", progress);
        tag.putInt("coffee_machine.water", waterAmount);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        if (itemHandler.getSlots() < TOTAL_SLOTS) {
            itemHandler.setSize(TOTAL_SLOTS);
        }
        progress = tag.getInt("coffee_machine.progress");
        waterAmount = tag.getInt("coffee_machine.water");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public ContainerData getContainerData() {
        return data;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CoffeeMachineBlockEntity entity) {
        if (level.isClientSide()) return;

        ItemStack waterInStack = entity.itemHandler.getStackInSlot(WATER_IN_SLOT);
        ItemStack waterOutStack = entity.itemHandler.getStackInSlot(WATER_OUT_SLOT);

        // --- 逻辑 A：放入水桶 -> 机器加水 1000ml -> 产出空桶 ---
        if (waterInStack.is(net.minecraft.world.item.Items.WATER_BUCKET) && entity.waterAmount <= MAX_WATER - 1000) {
            if (canOutputItem(waterOutStack, net.minecraft.world.item.Items.BUCKET)) {
                entity.itemHandler.extractItem(WATER_IN_SLOT, 1, false);
                fillOutputSlot(entity, net.minecraft.world.item.Items.BUCKET);
                entity.waterAmount += 1000;
                entity.setChanged();
            }
        }
        // --- 逻辑 B：放入空桶 -> 机器减水 1000ml -> 产出水桶 ---
        else if (waterInStack.is(net.minecraft.world.item.Items.BUCKET) && entity.waterAmount >= 1000) {
            if (canOutputItem(waterOutStack, net.minecraft.world.item.Items.WATER_BUCKET)) {
                entity.itemHandler.extractItem(WATER_IN_SLOT, 1, false);
                fillOutputSlot(entity, net.minecraft.world.item.Items.WATER_BUCKET);
                entity.waterAmount -= 1000;
                entity.setChanged();
            }
        }

        if (hasRecipe(entity)) {
            entity.progress++;
            entity.setChanged();

            if (entity.progress >= entity.maxProgress) {
                craftItem(entity);
                entity.progress = 0;
            }
        } else {
            entity.progress = 0;
            entity.setChanged();
        }
    }

    private static boolean canOutputItem(ItemStack stackInSlot, net.minecraft.world.item.Item itemToOutput) {
        return stackInSlot.isEmpty() || (stackInSlot.is(itemToOutput) && stackInSlot.getCount() < stackInSlot.getMaxStackSize());
    }

    private static void fillOutputSlot(CoffeeMachineBlockEntity entity, net.minecraft.world.item.Item item) {
        ItemStack stack = entity.itemHandler.getStackInSlot(WATER_OUT_SLOT);
        if (stack.isEmpty()) {
            entity.itemHandler.setStackInSlot(WATER_OUT_SLOT, new ItemStack(item));
        } else {
            stack.grow(1);
        }
    }

    public boolean isWaterFull() {
        return this.waterAmount >= MAX_WATER;
    }

    public int getWaterAmount() {
        return this.waterAmount;
    }

    public void addWater(int amount) {
        this.waterAmount = Math.min(Math.max(this.waterAmount + amount, 0), MAX_WATER);
        setChanged();
    }

    private static void craftItem(CoffeeMachineBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<CoffeeRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(CoffeeRecipeType.INSTANCE, inventory, level);

        if (recipe.isPresent() && hasRecipe(entity)) {
            // 消耗输入物品
            entity.itemHandler.extractItem(INPUT_SLOT, 1, false);
            entity.itemHandler.extractItem(FUEL_SLOT, 1, false);
            entity.itemHandler.extractItem(CONTAINER_SLOT, 1, false);

            entity.waterAmount -= 200; // 消耗 200ml

            // 添加输出
            ItemStack result = recipe.get().getResultItem(level.registryAccess()).copy();
            int currentCount = entity.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();
            entity.itemHandler.setStackInSlot(OUTPUT_SLOT,
                    new ItemStack(result.getItem(), currentCount + result.getCount()));

            entity.progress = 0;
        }
    }

    private static boolean hasRecipe(CoffeeMachineBlockEntity entity) {
        if (entity.waterAmount < 200) return false;
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<CoffeeRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(CoffeeRecipeType.INSTANCE, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory, recipe.get().getResultItem(level.registryAccess()))
                && canInsertItemIntoOutputSlot(inventory, recipe.get().getResultItem(level.registryAccess()));
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        ItemStack outputStack = inventory.getItem(OUTPUT_SLOT);
        return outputStack.isEmpty() || outputStack.is(stack.getItem());
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        ItemStack outputStack = inventory.getItem(OUTPUT_SLOT);
        return outputStack.getMaxStackSize() >= outputStack.getCount() + stack.getCount();
    }
}