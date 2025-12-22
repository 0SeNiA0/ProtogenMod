package net.zaharenko424.protogenmod.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.transform.TransformType;
import net.zaharenko424.protogenmod.transform.TransformProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//TODO sync baseValues or capture on client?
public record SyncTransformPacket(int entityId, @Nullable TransformType<?, ?> transformType, @Nullable TransformProgress transformProgress, @Nullable CompoundTag transformData) implements CustomPacketPayload {

    public static final Type<SyncTransformPacket> TYPE = new Type<>(ProtogenMod.resourceLoc("transform"));

    public static final StreamCodec<FriendlyByteBuf, SyncTransformPacket> CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeVarInt(packet.entityId);
                buf.writeNullable(packet.transformType, TransformType.STR_CODEC);
                buf.writeNullable(packet.transformProgress, TransformProgress.STR_CODEC);
                buf.writeNullable(packet.transformData, (b, t) -> b.writeNbt(t));
            },
            buf -> new SyncTransformPacket(buf.readVarInt(), buf.readNullable(TransformType.STR_CODEC),
                    buf.readNullable(TransformProgress.STR_CODEC), buf.readNullable(b -> b.readNbt())));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
