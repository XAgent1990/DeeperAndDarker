package com.kyanite.deeperdarker.content;

import com.kyanite.deeperdarker.DeeperDarker;
import com.kyanite.deeperdarker.content.items.DDBoatItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SignItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DDItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DeeperDarker.MOD_ID);

    public static final RegistryObject<Item> ECHO_SIGN = ITEMS.register("echo_sign", () -> new SignItem(new Item.Properties().stacksTo(16), DDBlocks.ECHO_SIGN.get(), DDBlocks.ECHO_WALL_SIGN.get()));
    public static final RegistryObject<Item> ECHO_HANGING_SIGN = ITEMS.register("echo_hanging_sign", () -> new HangingSignItem(DDBlocks.ECHO_HANGING_SIGN.get(), DDBlocks.ECHO_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> ECHO_BOAT = ITEMS.register("echo_boat", () -> new DDBoatItem(false, new Item.Properties().stacksTo(1), DDBlocks.ECHO));
    public static final RegistryObject<Item> ECHO_CHEST_BOAT = ITEMS.register("echo_chest_boat", () -> new DDBoatItem(true, new Item.Properties().stacksTo(1), DDBlocks.ECHO));

    public static final RegistryObject<Item> GRIME_BALL = ITEMS.register("grime_ball", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GRIME_BRICK = ITEMS.register("grime_brick", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SCULK_BONE = ITEMS.register("sculk_bone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SOUL_DUST = ITEMS.register("soul_dust", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> SOUL_CRYSTAL = ITEMS.register("soul_crystal", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> HEART_OF_THE_DEEP = ITEMS.register("heart_of_the_deep", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> WARDEN_CARAPACE = ITEMS.register("warden_carapace", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> REINFORCED_ECHO_SHARD = ITEMS.register("reinforced_echo_shard", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
}