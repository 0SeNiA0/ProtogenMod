package net.zaharenko424.protogenmod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zaharenko424.protogenmod.network.ClientPacketHandler;
import net.zaharenko424.protogenmod.network.packet.SyncTransformDataPacket;
import net.zaharenko424.protogenmod.network.packet.TransformPacket;

@EventBusSubscriber
public class CommonMod {

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event){
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(TransformPacket.TYPE, TransformPacket.CODEC, ClientPacketHandler::handleTransform);

        registrar.playToClient(SyncTransformDataPacket.TYPE, SyncTransformDataPacket.CODEC, ClientPacketHandler::handleSyncTransformData);
    }

    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event){
        //Fired after reg so all DeferredHolders are filled -> can use attributes directly from TransformType
    }
}
