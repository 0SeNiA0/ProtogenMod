package net.zaharenko424.protogenmod.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.SynchedEntityData;
import net.zaharenko424.protogenmod.ProtogenMod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record SyncTransformDataPacket(int entityId, List<SynchedEntityData.DataValue<?>> packedValues) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncTransformDataPacket> TYPE = new CustomPacketPayload.Type<>(ProtogenMod.resourceLoc("ltc_data_sync"));

    public SyncTransformDataPacket(int entityId, SynchedEntityData data){
        this(entityId, data.packDirty());
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTransformDataPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncTransformDataPacket::entityId,
            StreamCodec.of(SyncTransformDataPacket::pack, SyncTransformDataPacket::unpack),
            SyncTransformDataPacket::packedValues,
            SyncTransformDataPacket::new
    );

    private static void pack(RegistryFriendlyByteBuf buffer, List<SynchedEntityData.DataValue<?>> dataValues) {
        for (SynchedEntityData.DataValue<?> datavalue : dataValues) {
            datavalue.write(buffer);
        }

        buffer.writeByte(255);
    }

    private static List<SynchedEntityData.DataValue<?>> unpack(RegistryFriendlyByteBuf buffer) {
        List<SynchedEntityData.DataValue<?>> list = new ArrayList<>();

        int i;
        while ((i = buffer.readUnsignedByte()) != 255) {
            list.add(SynchedEntityData.DataValue.read(buffer, i));
        }

        return list;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
