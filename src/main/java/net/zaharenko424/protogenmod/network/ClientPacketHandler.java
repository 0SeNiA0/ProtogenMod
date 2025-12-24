package net.zaharenko424.protogenmod.network;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.protogenmod.network.packet.EntityMovementPacket;
import net.zaharenko424.protogenmod.network.packet.transform.SyncTransformPacket;
import net.zaharenko424.protogenmod.network.packet.transform.SyncTransformProgressPacket;
import net.zaharenko424.protogenmod.transform.TransformHandler;

public class ClientPacketHandler {

    public static void handleTransformProgress(SyncTransformProgressPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            Entity entity = context.player().level().getEntity(packet.entityId());
            if (!(entity instanceof LivingEntity living)) return;

            TransformHandler handler = TransformHandler.of(living);
            if (handler != null) handler.loadFromPacket(packet);
        });
    }

    public static void handleTransform(SyncTransformPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            Entity entity = context.player().level().getEntity(packet.entityId());
            if (!(entity instanceof LivingEntity living)) return;

            TransformHandler handler = TransformHandler.of(living);
            if (handler != null) handler.loadFromPacket(packet);
        });
    }

    public static void handleEntityMovement(EntityMovementPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            Entity entity = context.player().level().getEntity(packet.entityId());
            if (entity == null) return;

            entity.setDeltaMovement(packet.deltaMovement());
            if (!packet.hasRot()) return;

            entity.setXRot(packet.xRot());
            entity.setYRot(packet.yRot());
        });
    }
}
