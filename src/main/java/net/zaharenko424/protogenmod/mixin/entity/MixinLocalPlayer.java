package net.zaharenko424.protogenmod.mixin.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer {

    public MixinLocalPlayer(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Unique
    private LocalPlayer self_(){
        return (LocalPlayer) (Object) this;
    }

    /*@WrapMethod(method = "isMovingSlowly")
    private boolean replaceMovingSlowly(Operation<Boolean> original){//TODO to remove?
        TransformEntity transform = TransformUtil.transformOf1(self_());

        return transform != null ? transform.isMovingSlowly() : original.call();
    }*/
}
