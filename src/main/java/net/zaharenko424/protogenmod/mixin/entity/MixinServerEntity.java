package net.zaharenko424.protogenmod.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.bundle.PacketAndPayloadAcceptor;
import net.zaharenko424.protogenmod.entity.projectile.PlasmaBolt;
import net.zaharenko424.protogenmod.network.packet.EntityMovementPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

/**
 * Projectile sync fix from Draconic-Evolution
 */
@Mixin(ServerEntity.class)
public class MixinServerEntity {

    @Shadow
    @Final
    private Entity entity;

    @WrapOperation(at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 2),
            method = "sendChanges")
    private <T> void onVelocity1(Consumer<Packet<?>> instance, T t, Operation<Void> original){
        onVelocity(instance, t, original);
    }
//TODO make more specific when superclass of PlasmaBolt is decided
    @WrapOperation(at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 3),
            method = "sendChanges")
    private <T> void onVelocity2(Consumer<Packet<?>> instance, T t, Operation<Void> original){
        onVelocity(instance, t, original);
    }

    @Unique
    private <T> void onVelocity(Consumer<Packet<?>> instance, T t, Operation<Void> original){
        if(!(entity instanceof PlasmaBolt)) {
            original.call(instance, t);
            return;
        }

        instance.accept(new EntityMovementPacket(entity, false).toVanillaClientbound());
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/network/bundle/PacketAndPayloadAcceptor;accept(Lnet/minecraft/network/protocol/Packet;)Lnet/neoforged/neoforge/network/bundle/PacketAndPayloadAcceptor;", ordinal = 3),
            method = "sendPairingData")
    private <L extends ClientCommonPacketListener> PacketAndPayloadAcceptor<L> onPairingData(PacketAndPayloadAcceptor<L> instance, Packet<? super L> packet, Operation<PacketAndPayloadAcceptor<L>> original){
        if(!(entity instanceof PlasmaBolt)) {
            original.call(instance, packet);
            return instance;
        }

        instance.accept(new EntityMovementPacket(entity, false));
        return instance;
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 4),
            method = "sendChanges")
    private <T> void onMovePacket(Consumer<Packet<?>> instance, T t, Operation<Void> original){
        if (!(t instanceof ClientboundMoveEntityPacket.PosRot) || !(entity instanceof PlasmaBolt)) {
            original.call(instance, t);
            return;
        }

        instance.accept(new EntityMovementPacket(entity, true).toVanillaClientbound());
    }
}
