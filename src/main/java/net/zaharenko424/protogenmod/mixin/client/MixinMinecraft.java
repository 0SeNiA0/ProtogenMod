package net.zaharenko424.protogenmod.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.HitResult;
import net.zaharenko424.protogenmod.event.ClientEvent;
import net.zaharenko424.protogenmod.item.AbstractWeapon;
import net.zaharenko424.protogenmod.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Final
    public Options options;

    @Shadow
    @Nullable
    public HitResult hitResult;

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;itemUsed(Lnet/minecraft/world/InteractionHand;)V"),
            method = "startUseItem")
    private boolean cancelItemUse(ItemInHandRenderer instance, InteractionHand hand){
        return !player.getUseItem().is(ItemRegistry.THE_RIFLE);
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0),
            method = "handleKeybinds")
    private boolean shoot(boolean original){
        if(!original) return false;

        if (options.keyAttack.consumeClick()) ClientEvent.tryShoot(player, player.getUseItem(), player.getUsedItemHand());
        return true;
    }

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetAttackStrengthTicker()V"),
            method = "startAttack")
    private boolean cancelAttackStrengthReset(LocalPlayer instance){
        return hitResult.getType() == HitResult.Type.ENTITY || !(player.getMainHandItem().getItem() instanceof AbstractWeapon);
    }
}
