package net.zaharenko424.protogenmod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractWeapon extends Item {

    public AbstractWeapon(Properties properties) {
        super(properties);
    }

    public int fireCooldownTicks(LivingEntity shooter, ItemStack stack){
        return 5;
    }

    public abstract void fire(LivingEntity shooter, ItemStack stack, InteractionHand hand);
}
