package net.zaharenko424.protogenmod.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.protogenmod.entity.projectile.PlasmaBolt;
import net.zaharenko424.protogenmod.registry.SoundRegistry;

public class PlasmaRifle extends AbstractWeapon {

    public PlasmaRifle(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public void fire(LivingEntity shooter, ItemStack stack, InteractionHand hand) {
        Level level = shooter.level();
        level.playSound(null, shooter, SoundRegistry.PLASMA_RIFLE.get(), shooter.getSoundSource(), 1, 1);

        float inaccuracy = shooter.getUseItem() == stack ? 0.8f : 2f;

        PlasmaBolt plasmaBolt = new PlasmaBolt(shooter, stack);
        plasmaBolt.shootFromRotation(shooter, shooter.getXRot(), shooter.getYHeadRot(), 0, 5f, inaccuracy);

        //Offset projectile to make it look like its actually being fired from the rifle
        Vec3 offset = shooter.getLookAngle().cross(new Vec3(0, 1, 0)).scale(0.3 * (arm(shooter, hand) == HumanoidArm.RIGHT ? 1 : -1));
        plasmaBolt.setPos(plasmaBolt.position().add(0, 1.35, 0).add(offset).add(plasmaBolt.getDeltaMovement().normalize().scale(1)));

        level.addFreshEntity(plasmaBolt);
    }
}
