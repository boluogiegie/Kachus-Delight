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

    // 定义槽位索引
    public static final int INPUT_SLOT = 0;      // 咖啡豆输入
    public static final int FUEL_SLOT = 1;       // 燃料（糖/奶等）
    public static final int OUTPUT_SLOT = 2;     // 咖啡输出
    public static final int CONTAINER_SLOT = 3;  // 容器（杯子）
    public static final int TOTAL_SLOTS = 4;

    private final ItemStackHandler itemHandler = new ItemStackHandler(TOTAL_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200; // 10秒（20tick/秒）

    public CoffeeMachineBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.COFFEE_MACHINE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    // 修复：getDisplayName 必须是 public
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
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("coffee_machine.progress");
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

    // 添加一个简单的更新方法
    public static void tick(Level level, BlockPos pos, BlockState state, CoffeeMachineBlockEntity entity) {
        if (level.isClientSide()) return;

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

            // 添加输出
            ItemStack result = recipe.get().getResultItem(level.registryAccess()).copy();
            int currentCount = entity.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();
            entity.itemHandler.setStackInSlot(OUTPUT_SLOT,
                    new ItemStack(result.getItem(), currentCount + result.getCount()));

            entity.progress = 0;
        }
    }

    private static boolean hasRecipe(CoffeeMachineBlockEntity entity) {
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