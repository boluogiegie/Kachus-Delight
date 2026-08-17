package com.kachudelight.kachu.machine.tea;

import com.kachudelight.kachu.registry.BlockEntityRegistry;
import com.kachudelight.kachu.registry.ItemRegistry;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

public class TeaBrewingMachineBlockEntity extends BlockEntity implements MenuProvider {
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int INPUT_SLOT_3 = 2;
    public static final int INPUT_SLOT_4 = 3;
    public static final int CUP_SLOT = 4;
    public static final int OUTPUT_SLOT = 5;
    public static final int WATER_IN_SLOT = 6;
    public static final int WATER_OUT_SLOT = 7;
    public static final int TOTAL_SLOTS = 8;
    public static final int MAX_WATER = 3000;
    public static final int WATER_PER_BREW = 250;

    private final ItemStackHandler itemHandler = new ItemStackHandler(TOTAL_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) { setChanged(); }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case INPUT_SLOT_1, INPUT_SLOT_2, INPUT_SLOT_3, INPUT_SLOT_4 -> true;
                case CUP_SLOT -> stack.is(ItemRegistry.GLASS_CUP.get());
                case WATER_IN_SLOT -> stack.is(Items.WATER_BUCKET) || stack.is(Items.BUCKET);
                case OUTPUT_SLOT, WATER_OUT_SLOT -> false;
                default -> false;
            };
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private int progress;
    private int maxProgress = 200;
    private int waterAmount;

    protected final ContainerData data = new ContainerData() {
        @Override public int get(int index) { return switch (index) {
            case 0 -> progress; case 1 -> maxProgress; case 2 -> waterAmount; default -> 0;
        }; }
        @Override public void set(int index, int value) { switch (index) {
            case 0 -> progress = value; case 1 -> maxProgress = value; case 2 -> waterAmount = value;
        } }
        @Override public int getCount() { return 3; }
    };

    public TeaBrewingMachineBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.TEA_BREWING_MACHINE.get(), pos, state);
    }

    @Override public Component getDisplayName() { return Component.translatable("block.kachu.tea_brewing_machine"); }
    @Nullable @Override public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new TeaBrewingMachineMenu(id, inventory, this, data);
    }
    @Override public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? lazyItemHandler.cast() : super.getCapability(cap, side);
    }
    @Override public void onLoad() { super.onLoad(); lazyItemHandler = LazyOptional.of(() -> itemHandler); }
    @Override public void invalidateCaps() { super.invalidateCaps(); lazyItemHandler.invalidate(); }

    @Override protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("progress", progress); tag.putInt("water", waterAmount);
        super.saveAdditional(tag);
    }
    @Override public void load(CompoundTag tag) {
        super.load(tag); itemHandler.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("progress"); waterAmount = tag.getInt("water");
    }
    public void drops() {
        SimpleContainer inventory = inventoryCopy();
        Containers.dropContents(level, worldPosition, inventory);
    }
    public ItemStackHandler getItemHandler() { return itemHandler; }
    public int getWaterAmount() { return waterAmount; }
    public boolean isWaterFull() { return waterAmount >= MAX_WATER; }
    public void addWater(int amount) { waterAmount = Math.min(MAX_WATER, Math.max(0, waterAmount + amount)); setChanged(); }

    public static void tick(Level level, BlockPos pos, BlockState state, TeaBrewingMachineBlockEntity machine) {
        if (level.isClientSide) return;
        machine.transferBucketWater();
        boolean hasRecipe = machine.findRecipe().filter(machine::canCraft).isPresent();
        if (hasRecipe) {
            if (++machine.progress >= machine.maxProgress) {
                machine.findRecipe().ifPresent(machine::craft);
                machine.progress = 0;
            }
        } else if (machine.progress != 0) machine.progress = 0;
        machine.setChanged();
    }

    private void transferBucketWater() {
        ItemStack input = itemHandler.getStackInSlot(WATER_IN_SLOT);
        ItemStack output = itemHandler.getStackInSlot(WATER_OUT_SLOT);
        if (input.is(Items.WATER_BUCKET) && waterAmount <= MAX_WATER - 1000 && canOutput(output, Items.BUCKET)) {
            itemHandler.extractItem(WATER_IN_SLOT, 1, false); addOutput(WATER_OUT_SLOT, Items.BUCKET); waterAmount += 1000;
        } else if (input.is(Items.BUCKET) && waterAmount >= 1000 && canOutput(output, Items.WATER_BUCKET)) {
            itemHandler.extractItem(WATER_IN_SLOT, 1, false); addOutput(WATER_OUT_SLOT, Items.WATER_BUCKET); waterAmount -= 1000;
        }
    }

    private Optional<TeaBrewingRecipe> findRecipe() {
        return level == null ? Optional.empty() : level.getRecipeManager().getRecipeFor(TeaBrewingRecipeType.INSTANCE, inventoryCopy(), level);
    }
    private boolean canCraft(TeaBrewingRecipe recipe) {
        if (waterAmount < WATER_PER_BREW || !itemHandler.getStackInSlot(CUP_SLOT).is(ItemRegistry.GLASS_CUP.get())) return false;
        ItemStack result = recipe.getResultItem(level.registryAccess());
        ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);
        return output.isEmpty() || (ItemStack.isSameItemSameTags(output, result) && output.getCount() + result.getCount() <= output.getMaxStackSize());
    }
    private void craft(TeaBrewingRecipe recipe) {
        if (!canCraft(recipe)) return;
        for (int slot = INPUT_SLOT_1; slot <= INPUT_SLOT_4; slot++) if (!itemHandler.getStackInSlot(slot).isEmpty()) itemHandler.extractItem(slot, 1, false);
        itemHandler.extractItem(CUP_SLOT, 1, false); waterAmount -= WATER_PER_BREW;
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
        ItemStack output = itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (output.isEmpty()) itemHandler.setStackInSlot(OUTPUT_SLOT, result); else output.grow(result.getCount());
    }
    private SimpleContainer inventoryCopy() {
        SimpleContainer inventory = new SimpleContainer(TOTAL_SLOTS);
        for (int i = 0; i < TOTAL_SLOTS; i++) inventory.setItem(i, itemHandler.getStackInSlot(i));
        return inventory;
    }
    private static boolean canOutput(ItemStack stack, Item item) { return stack.isEmpty() || (stack.is(item) && stack.getCount() < stack.getMaxStackSize()); }
    private void addOutput(int slot, Item item) { ItemStack stack = itemHandler.getStackInSlot(slot); if (stack.isEmpty()) itemHandler.setStackInSlot(slot, new ItemStack(item)); else stack.grow(1); }
}
