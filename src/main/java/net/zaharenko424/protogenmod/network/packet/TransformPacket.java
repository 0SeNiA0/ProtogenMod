package net.zaharenko424.protogenmod.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.protogenmod.ProtogenMod;
import org.jetbrains.annotations.NotNull;

public record TransformPacket(boolean add) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TransformPacket> TYPE = new CustomPacketPayload.Type<>(ProtogenMod.resourceLoc("transform"));

    public static final StreamCodec<FriendlyByteBuf, TransformPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            TransformPacket::add,
            TransformPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
