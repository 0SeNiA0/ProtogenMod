package net.zaharenko424.protogenmod.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.protogenmod.transform.AbstractTransform;
import net.zaharenko424.protogenmod.transform.TransformUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    @Shadow
    public abstract AttributeMap getAttributes();

    public MixinLivingEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private LivingEntity self_(){
        return (LivingEntity) (Object) this;
    }

    @WrapMethod(method = "isBaby")
    private boolean replaceIsBaby(Operation<Boolean> original){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());

        return transform != null ? transform.isBaby() : original.call();
    }

    /*@WrapMethod(method = "updateFallFlying")
    private void updateFallFlying(Operation<Void> original){
        TransformEntity transform = TransformUtil.transformOf1(self_());

        if(transform != null){
            if(transform.updatePlayerFallFlying()) return;
        }

        original.call();
    }*/

    @WrapMethod(method = "onClimbable")
    private boolean replaceOnClimbable(Operation<Boolean> original){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());

        return transform != null ? transform.onClimbable() : original.call();
    }

    @WrapMethod(method = "triggerItemUseEffects")
    private void replaceTriggerItemUseEffects(ItemStack stack, int amount, Operation<Void> original){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());

        if(transform != null && transform.triggerItemUseEffects(stack, amount)) return;

        original.call(stack, amount);
    }

    @WrapMethod(method = "isInvulnerableTo")//TMP mb replace with event?
    private boolean replaceIsInvulnerableTo(DamageSource source, Operation<Boolean> original){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());

        return transform != null ? transform.isInvulnerableTo(source) : original.call(source);
    }

    /*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"), method = "travel")
    private float modifyFriction(BlockState instance, LevelReader levelReader, BlockPos blockPos, Entity entity, Operation<Float> original){
        return original.call(instance, levelReader, blockPos, entity);
    }*///TODO
}
