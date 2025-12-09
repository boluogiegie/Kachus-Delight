package com.kachudelight.kachu.registry;

import com.kachudelight.kachu.KachuDelight;
import com.kachudelight.kachu.block.KanamiPlateBlock;
import com.kachudelight.kachu.block.crop.CoffeeBlock;
import com.kachudelight.kachu.block.crop.WildCoffeeBlock;
import com.kachudelight.kachu.block.food.KanamiOmeletteRiceBlock;
import com.kachudelight.kachu.block.food.OmeletteRiceBlock;
import com.kachudelight.kachu.crop.CoffeeCrop;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, KachuDelight.MOD_ID);
    // 香奈美蛋包饭方块
    public static final RegistryObject<Block> KANAMI_OMELETTE_RICE_BLOCK = BLOCKS.register("kanami_omelette_rice_block", () -> new KanamiOmeletteRiceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(0.5F).sound(SoundType.WOOL).noOcclusion().instabreak()));
    // 蛋包饭方块
    public static final RegistryObject<Block> OMELETTE_RICE_BLOCK = BLOCKS.register("omelette_rice_block", () -> new OmeletteRiceBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(0.5F).sound(SoundType.WOOL).noOcclusion().instabreak()));
    // 香奈美的盘子
    public static final RegistryObject<Block> KANAMI_PLATE = BLOCKS.register("kanami_plate", () -> new KanamiPlateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(0.3F).sound(SoundType.WOOD).noOcclusion()));
    // 野生咖啡灌木
    public static final RegistryObject<Block> WILD_COFFEE_BUSH = BLOCKS.register("wild_coffee_bush", () -> new WildCoffeeBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS), CoffeeCrop.COFFEE));
    // 咖啡作物
    public static final RegistryObject<Block> COFFEE_CROP = BLOCKS.register("coffee_crop", () -> new CoffeeBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

}