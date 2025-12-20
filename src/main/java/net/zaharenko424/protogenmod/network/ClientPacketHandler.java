package net.zaharenko424.protogenmod.network;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.protogenmod.network.packet.SyncTransformDataPacket;
import net.zaharenko424.protogenmod.network.packet.TransformPacket;
import net.zaharenko424.protogenmod.transformation.TransformHandler;

public class ClientPacketHandler {

    public static void handleTransform(TransformPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            Player player = context.player();

            TransformHandler handler = TransformHandler.of(player);
            if(packet.add()) handler.transform(null); else handler.untransform();
        });
    }

    public static void handleSyncTransformData(SyncTransformDataPacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            /*Entity entity = context.player().level().getEntity(packet.entityId());

            if(!(entity instanceof LivingEntity living)) return;

            TransformEntity transform = TransformUtil.transformOf(living);
            if(transform == null) return;

            transform.getEntityData().assignValues(packet.packedValues());*/
        });
    }
}
