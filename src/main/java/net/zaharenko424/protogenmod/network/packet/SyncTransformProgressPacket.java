package net.zaharenko424.protogenmod.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.transform.TransformProgress;
import org.jetbrains.annotations.NotNull;

public record SyncTransformProgressPacket(int entityId, @NotNull TransformProgress progress) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncTransformProgressPacket> TYPE = new CustomPacketPayload.Type<>(ProtogenMod.resourceLoc("transform_progress"));

    public static final StreamCodec<FriendlyByteBuf, SyncTransformProgressPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncTransformProgressPacket::entityId,
            TransformProgress.STR_CODEC, SyncTransformProgressPacket::progress,
            SyncTransformProgressPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
