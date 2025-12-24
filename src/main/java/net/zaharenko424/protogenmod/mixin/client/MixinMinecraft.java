package net.zaharenko424.protogenmod.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.zaharenko424.protogenmod.event.ClientEvent;
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

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;itemUsed(Lnet/minecraft/world/InteractionHand;)V"),
            method = "startUseItem")
    private boolean cancelItemUse(ItemInHandRenderer instance, InteractionHand hand){
        return !player.getUseItem().is(ItemRegistry.THE_RIFLE);
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0),
            method = "handleKeybinds")
    private boolean shootWhileAiming(boolean original){
        if(!original) return false;

        if (options.keyAttack.consumeClick()) ClientEvent.tryShoot(player, player.getUseItem(), player.getUsedItemHand());
        return true;
    }
}
