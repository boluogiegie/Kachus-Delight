package com.kachudelight.kachu.data;

import com.kachudelight.kachu.worldgen.KachuBiomeModifiers;
import com.kachudelight.kachu.worldgen.KachuConfiguredFeatures;
import com.kachudelight.kachu.worldgen.KachuPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class KachuDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, context -> {
                KachuConfiguredFeatures.bootstrap(context);
            })
            .add(Registries.PLACED_FEATURE, context -> {
                KachuPlacedFeatures.bootstrap(context);
            })
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, KachuBiomeModifiers::bootstrap);

    public KachuDatapackRegistriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", com.kachudelight.kachu.KachuDelight.MOD_ID));
    }

    @Override
    public String getName() {
        return "Kachu Delight Data";
    }
}
