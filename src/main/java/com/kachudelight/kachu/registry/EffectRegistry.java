package com.kachudelight.kachu.registry;

import com.kachudelight.kachu.KachuDelight;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, KachuDelight.MOD_ID);

    // 注册自己的效果
    // public static final RegistryObject<MobEffect> CUSTOM_EFFECT = EFFECTS.register("custom_effect",
    //     CustomEffect::new);
}