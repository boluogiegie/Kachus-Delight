package com.kachudelight.kachu.registry;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.machine.coffee.StarlightCoffeeMachineBlockEntity;
import com.kachudelight.kachu.machine.tea.TeaBrewingMachineBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, KachuDelight.MOD_ID);
    public static final RegistryObject<BlockEntityType<StarlightCoffeeMachineBlockEntity>> STARLIGHT_COFFEE_MACHINE = BLOCK_ENTITIES.register("starlight_coffee_machine", () -> BlockEntityType.Builder.of(StarlightCoffeeMachineBlockEntity::new, BlockRegistry.STARLIGHT_COFFEE_MACHINE.get()).build(null));
    public static final RegistryObject<BlockEntityType<TeaBrewingMachineBlockEntity>> TEA_BREWING_MACHINE = BLOCK_ENTITIES.register("tea_brewing_machine", () -> BlockEntityType.Builder.of(TeaBrewingMachineBlockEntity::new, BlockRegistry.TEA_BREWING_MACHINE.get()).build(null));
}
