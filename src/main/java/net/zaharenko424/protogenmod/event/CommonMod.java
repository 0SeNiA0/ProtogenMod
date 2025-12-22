package net.zaharenko424.protogenmod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zaharenko424.protogenmod.network.ClientPacketHandler;
import net.zaharenko424.protogenmod.network.packet.SyncTransformPacket;
import net.zaharenko424.protogenmod.network.packet.SyncTransformProgressPacket;
import net.zaharenko424.protogenmod.registry.TransformRegistry;
import net.zaharenko424.protogenmod.transform.TransformType;

@EventBusSubscriber
public class CommonMod {

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event){
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(SyncTransformProgressPacket.TYPE, SyncTransformProgressPacket.CODEC, ClientPacketHandler::handleTransformProgress);

        registrar.playToClient(SyncTransformPacket.TYPE, SyncTransformPacket.CODEC, ClientPacketHandler::handleTransform);
    }

    @SubscribeEvent
    public static void createEntityAttributes(EntityAttributeCreationEvent event){
        //Fired after reg so all DeferredHolders are filled -> can use attributes directly from TransformType
        for (TransformType<?, ?> transformType : TransformRegistry.TRANSFORM_REGISTRY){
            event.put(transformType.entityType(), transformType.getAttributes());
        }
    }
}
