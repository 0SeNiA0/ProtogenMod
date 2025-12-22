package net.zaharenko424.protogenmod.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.protogenmod.transform.AbstractTransform;
import net.zaharenko424.protogenmod.transform.TransformUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Unique
    private Entity self(){
        return (Entity) (Object) this;
    }

    @WrapMethod(method = "getTicksRequiredToFreeze")
    private int replaceTicksToFreeze(Operation<Integer> original){
        if(!(self() instanceof LivingEntity living)) return original.call();

        AbstractTransform<?, ?> transform = TransformUtil.transformOf(living);
        return transform != null ? transform.getTicksRequiredToFreeze() : original.call();
    }

    /**
     * isSwimming -> SharedFlag -> look {@link MixinLivingEntity}
     */
    //private void replaceIsSwimming(){}

    /*@WrapMethod(method = "interact")//TODO mb replace with event handler
    private InteractionResult replaceInteract(Player player, InteractionHand hand, Operation<InteractionResult> original){
        if(!(self() instanceof LivingEntity living)) return original.call(player, hand);

        TransformEntity transform = TransformUtil.transformOf1(living);
        return transform != null ? transform.interact(player, hand) : original.call(player, hand);
    }*/
}
