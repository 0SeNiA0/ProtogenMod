package net.zaharenko424.protogenmod.transformation;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public abstract class AbstractTransform<SELF extends AbstractTransform<SELF, E>, E extends TransformEntity<SELF, E>> {

    protected double xCloak, yCloak, zCloak;
    protected double xCloakO, yCloakO, zCloakO;
    protected float bob, bobO;

    protected final Either<E, Player> holder;
    protected final LivingEntity living;
    protected final TransformType<SELF, E> transformType;//Is this needed?

    public AbstractTransform(Either<E, Player> holder, TransformType<SELF, E> transformType){
        this.living = Either.unwrap(holder);
        this.holder = holder;
        if(TransformUtil.typeOf(living) != transformType) throw new IllegalStateException("Holder transform does not match argument!");
        this.transformType = transformType;
    }

    public Either<E, Player> holder(){
        return holder;
    }

    public LivingEntity unwrappedHolder(){
        return living;
    }

    public TransformType<SELF, E> transformType(){
        return transformType;
    }

    //================================================================================================================//

    public double xCloak(float partialTick){
        return holder.map(e -> Mth.lerp(partialTick, xCloakO, xCloak), pl -> Mth.lerp(partialTick, pl.xCloak, pl.xCloak));
    }

    public double yCloak(float partialTick){
        return holder.map(e -> Mth.lerp(partialTick, yCloakO, yCloak), pl -> Mth.lerp(partialTick, pl.yCloak, pl.yCloak));
    }

    public double zCloak(float partialTick){
        return holder.map(e -> Mth.lerp(partialTick, zCloakO, zCloak), pl -> Mth.lerp(partialTick, pl.zCloak, pl.zCloak));
    }

    public float bob(float partialTick){
        return holder.map(e -> Mth.lerp(partialTick, bobO, bob), pl -> Mth.lerp(partialTick, pl.oBob, pl.bob));
    }

    public boolean isLeftHanded() {
        return getMainArm() == HumanoidArm.LEFT;
    }

    public HumanoidArm getMainArm() {
        return holder.map(tf -> tf.isLeftHanded() ? HumanoidArm.LEFT : HumanoidArm.RIGHT, Player::getMainArm);
    }

    @ApiStatus.OverrideOnly
    protected void moveCloak(){
        xCloakO = xCloak;
        yCloakO = yCloak;
        zCloakO = zCloak;
        double d0 = living.getX() - xCloak;
        double d1 = living.getY() - yCloak;
        double d2 = living.getZ() - zCloak;
        if (d0 > 10.0) {
            xCloak = living.getX();
            xCloakO = xCloak;
        }

        if (d2 > 10.0) {
            zCloak = living.getZ();
            zCloakO = zCloak;
        }

        if (d1 > 10.0) {
            yCloak = living.getY();
            yCloakO = yCloak;
        }

        if (d0 < -10.0) {
            xCloak = living.getX();
            xCloakO = xCloak;
        }

        if (d2 < -10.0) {
            zCloak = living.getZ();
            zCloakO = zCloak;
        }

        if (d1 < -10.0) {
            yCloak = living.getY();
            yCloakO = yCloak;
        }

        xCloak += d0 * 0.25;
        zCloak += d2 * 0.25;
        yCloak += d1 * 0.25;
    }

    @ApiStatus.OverrideOnly
    protected void updateBob(){
        bobO = bob;
        if(living.getVehicle() != null){
            bob = 0;
            return;
        }

        float f;
        if (living.onGround() && !living.isDeadOrDying() && !living.isSwimming()) {
            f = Math.min(0.1F, (float) living.getDeltaMovement().horizontalDistance());
        } else {
            f = 0.0F;
        }
        bob = bob + (f - bob) * 0.4F;
    }

    //================================================================================================================//

    public boolean isBaby(){
        return false;
    }

    public int getTicksRequiredToFreeze(){
        return 140;
    }

    @ApiStatus.OverrideOnly
    public void tickTransform(){}

    /**
     * Override to modify vanilla breathing.
     */
    public void breathe(LivingBreatheEvent event){}

    /**
     * @return Whether vanilla code should be canceled.
     */
    public boolean triggerItemUseEffects(ItemStack stack, int amount){
        return false;
    }

    //TODO mb hook up using interact event?
    public InteractionResult interact(Player player, InteractionHand hand){
        return InteractionResult.PASS;
    }

    /**
     * @return Non DEFAULT to replace player tryToStartFallFlying logic.
     */
    public TriState tryToStartFallFlying(){
        return TriState.DEFAULT;
    }

    /*/**
     * @return Whether vanilla code should be canceled.
     *//*
    public boolean updatePlayerFallFlying(){
        return false;
    }*/

    /**
     * Mimic vanilla {@link LivingEntity#onClimbable()}
     */
    public boolean onClimbable() {
        if (living.isSpectator()) return false;

        BlockPos blockpos = living.blockPosition();
        BlockState blockstate = living.getInBlockState();
        Optional<BlockPos> ladderPos = CommonHooks.isLivingOnLadder(blockstate, living.level(), blockpos, living);
        if (ladderPos.isPresent()) living.lastClimbablePos = ladderPos;
        return ladderPos.isPresent();
    }

    /**
     * Mimic vanilla {@link LivingEntity#isInvulnerableTo}
     */
    public boolean isInvulnerableTo(DamageSource source) {
        boolean isVanillaInvulnerable = living.isRemoved()//Entity
                || living.isInvulnerable() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.isCreativePlayer()
                || source.is(DamageTypeTags.IS_FIRE) && living.fireImmune()
                || source.is(DamageTypeTags.IS_FALL) && living.getType().is(EntityTypeTags.FALL_DAMAGE_IMMUNE);
        if(CommonHooks.isEntityInvulnerableTo(living, source, isVanillaInvulnerable)) return true;

        //LivingEntity
        return living.level() instanceof ServerLevel serverlevel && EnchantmentHelper.isImmuneToDamage(serverlevel, living, source);
    }

    public SoundEvent getHurtSound(DamageSource damageSource){
        return SoundEvents.GENERIC_HURT;
    }

    public SoundEvent getDeathSound(){
        return SoundEvents.GENERIC_DEATH;
    }

    /**
     * Mimic vanilla {@link Player#doesEmitEquipEvent}
     */
    public boolean doesEmitEquipEvent(EquipmentSlot slot){
        return slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR;
    }

    /**
     * Mimic vanilla {@link Player#canUseSlot}
     */
    public boolean canUseSlot(EquipmentSlot slot){
        return slot != EquipmentSlot.BODY;
    }

    /**
     * Mimic vanilla {@link LivingEntity#getEquipmentSlotForItem}
     */
    public EquipmentSlot getEquipmentSlotForItem(ItemStack stack){
        final EquipmentSlot slot = stack.getEquipmentSlot();
        if (slot != null) return slot; // FORGE: Allow modders to set a non-default equipment slot for a stack; e.g. a non-armor chestplate-slot item
        Equipable equipable = Equipable.get(stack);
        if (equipable != null) {
            EquipmentSlot equipmentslot = equipable.getEquipmentSlot();
            if (living.canUseSlot(equipmentslot)) {
                return equipmentslot;
            }
        }

        return EquipmentSlot.MAINHAND;
    }

    public float foodExhaustionModifier(){
        return 1;
    }

    public @Nullable CompoundTag save(){
        return null;
    }

    public void load(CompoundTag tag){}
}
