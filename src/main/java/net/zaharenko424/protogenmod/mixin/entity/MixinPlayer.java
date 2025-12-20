package net.zaharenko424.protogenmod.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.TriState;
import net.zaharenko424.protogenmod.transformation.AbstractTransform;
import net.zaharenko424.protogenmod.transformation.TransformUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {

    protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private Player self_(){
        return (Player) (Object) this;
    }

    @WrapMethod(method = "tryToStartFallFlying")
    private boolean replaceTryToStartFallFlying(Operation<Boolean> original){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());

        if(transform != null){
            TriState state = transform.tryToStartFallFlying();
            if(!state.isDefault()) return state.isTrue();
        }

        return original.call();
    }

    @WrapMethod(method = "getHurtSound")
    private SoundEvent replaceHurtSound(DamageSource damageSource, Operation<SoundEvent> original){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());

        return transform != null ? transform.getHurtSound(damageSource) : original.call(damageSource);
    }

    @WrapMethod(method = "getDeathSound")
    private SoundEvent replaceDeathSound(Operation<SoundEvent> original){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());

        return transform != null ? transform.getDeathSound() : original.call();
    }

    @WrapMethod(method = "canUseSlot")
    private boolean replaceCanUseSlot(EquipmentSlot slot, Operation<Boolean> original){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());

        return transform != null ? transform.canUseSlot(slot) : original.call(slot);
    }

    /*@WrapMethod(method = "getItemBySlot")
    private ItemStack replaceGetItemBySlot(EquipmentSlot slot, Operation<ItemStack> original){//TODO to remove?
        TransformEntity transform = TransformUtil.transformOf1(self_());
        if(transform == null) return original.call(slot);

        ItemStack stack = transform.getPlayerItemBySlot(slot);
        return stack == null ? original.call(slot) : stack;
    }*/

    @WrapMethod(method = "setItemSlot")
    private void denyInvalidArmor(EquipmentSlot slot, ItemStack stack, Operation<Void> original){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());
        original.call(transform != null ? transform.getEquipmentSlotForItem(stack) : slot, stack);

        /*TransformEntity transform = TransformUtil.transformOf(self_());//TODO forward setItemInSlot to transform?
        if(transform == null){
            original.call(slot, stack);
            return;
        }

        if(transform.setPlayerItemSlot(slot, stack)) return;

        original.call(transform.getEquipmentSlotForItem(stack), stack);*/
    }

    @WrapMethod(method = "doesEmitEquipEvent")
    private boolean replaceDoesEmitEquipEvent(EquipmentSlot slot, Operation<Boolean> original){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());

        return transform != null ? transform.doesEmitEquipEvent(slot) : original.call(slot);
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V"), method = "causeFoodExhaustion")
    private float modifyFoodExhaustion(float exhaustion){
        AbstractTransform<?, ?> transform = TransformUtil.transformOf(self_());

        return transform != null ? transform.foodExhaustionModifier()  * exhaustion : exhaustion;
    }

    /*@WrapMethod(method = "getDefaultDimensions")//FIXME pull dimensions from entityType in TFType and return them in AbstractTransform?
    private EntityDimensions replaceDimensions(Pose pose, Operation<EntityDimensions> original){
        TransformEntity transform = TransformUtil.transformOf1(self_());

        return transform != null ? transform.getDimensions(pose) : original.call(pose);
    }*/
}
