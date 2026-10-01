package com.kachudelight.kachu.registry;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.effect.AlertnessEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, KachuDelight.MOD_ID);

    public static final RegistryObject<MobEffect> ALERTNESS = EFFECTS.register("alertness", AlertnessEffect::new);
}
