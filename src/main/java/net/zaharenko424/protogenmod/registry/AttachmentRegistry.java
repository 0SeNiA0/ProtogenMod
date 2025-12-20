package net.zaharenko424.protogenmod.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.transformation.TransformHandler;

public class AttachmentRegistry {

    public static DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ProtogenMod.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<TransformHandler>> TRANSFORM_HANDLER = ATTACHMENT_TYPES
            .register("transform_handler", () -> AttachmentType.builder(TransformHandler::new)
                    .serialize(TransformHandler.Serializer.INSTANCE).build());
}
