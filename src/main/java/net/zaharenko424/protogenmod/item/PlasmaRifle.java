package net.zaharenko424.protogenmod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class PlasmaRifle extends Item {

    public PlasmaRifle(Properties properties) {//TODO aim on use? -> lmb to shoot? then no attack..
        super(properties.stacksTo(1).rarity(Rarity.RARE));
    }
}
