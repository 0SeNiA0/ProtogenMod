package net.zaharenko424.protogenmod.registry;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.item.PlasmaRifle;
import net.zaharenko424.protogenmod.item.Ram;

public class ItemRegistry {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ProtogenMod.MODID);

    public static final DeferredItem<PlasmaRifle> THE_RIFLE = ITEMS.register("plasma_rifle", () -> new PlasmaRifle(new Item.Properties()));

    //Electronic components
    public static final DeferredItem<Ram> RAM_64GB = ITEMS.register("ram_64", () -> new Ram(new Item.Properties().rarity(Rarity.UNCOMMON), new FoodProperties.Builder().nutrition(5).saturationModifier(.5f).build()));
    public static final DeferredItem<Ram> RAM_128GB = ITEMS.register("ram_128", () -> new Ram(new Item.Properties().rarity(Rarity.UNCOMMON), new FoodProperties.Builder().nutrition(10).saturationModifier(.75f).build()));
    public static final DeferredItem<Ram> RAM_256GB = ITEMS.register("ram_256", () -> new Ram(new Item.Properties().rarity(Rarity.RARE), new FoodProperties.Builder().nutrition(20).saturationModifier(1).build()));
}
