package net.zaharenko424.protogenmod.transformation;

import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransformEntity<TF extends AbstractTransform<TF, SELF>, SELF extends TransformEntity<TF, SELF>> extends PathfinderMob {

    protected static final String TRANSFORM_DATA = "transform";

    protected final TransformType<TF, SELF> transformType;
    protected final TF transform;

    protected TransformEntity(EntityType<? extends TransformEntity<TF, SELF>> entityType, Level level, TransformType<TF, SELF> transformType) {
        super(entityType, level);
        this.transformType = transformType;
        this.transform = transformType.createTransform(Either.left((SELF)this));//Hopefully this won't blow up
    }

    public TransformType<TF, SELF> transformType(){
        return transformType;
    }

    public TF transform(){
        return transform;
    }

    @Override
    public void tick() {
        super.tick();
        transform.tickTransform();
        transform.moveCloak();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        transform.updateBob();
    }

    @Override
    protected void triggerItemUseEffects(@NotNull ItemStack stack, int amount) {
        if (transform.triggerItemUseEffects(stack, amount)) return;
        super.triggerItemUseEffects(stack, amount);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return transform.getHurtSound(damageSource);
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return transform.getDeathSound();
    }

    @Override
    protected boolean doesEmitEquipEvent(@NotNull EquipmentSlot slot) {
        return transform.doesEmitEquipEvent(slot);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlotForItem(@NotNull ItemStack stack) {
        return transform.getEquipmentSlotForItem(stack);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        CompoundTag tag = transform.save();
        if (tag != null) compound.put(TRANSFORM_DATA, compound);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(TRANSFORM_DATA)) transform.load(compound.getCompound(TRANSFORM_DATA));
    }

}
