package net.zaharenko424.protogenmod.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.protogenmod.ProtogenMod;
import org.jetbrains.annotations.NotNull;

public record EntityMovementPacket(int entityId, Vec3 deltaMovement, boolean hasRot, float xRot, float yRot) implements CustomPacketPayload {

    public static final Type<EntityMovementPacket> TYPE = new Type<>(ProtogenMod.resourceLoc("entity_movement"));

    public static final StreamCodec<FriendlyByteBuf, EntityMovementPacket> CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeVarInt(packet.entityId);
                Vec3 deltaMovement = packet.deltaMovement;
                buf.writeFloat((float) deltaMovement.x).writeFloat((float) deltaMovement.y).writeFloat((float) deltaMovement.z);

                buf.writeBoolean(packet.hasRot);
                if (!packet.hasRot()) return;

                buf.writeFloat(packet.xRot).writeFloat(packet.yRot);
            }, buf -> {
                int entityId = buf.readVarInt();
                Vec3 deltaMovement = new Vec3(buf.readFloat(), buf.readFloat(), buf.readFloat());
                if (!buf.readBoolean()) return new EntityMovementPacket(entityId, deltaMovement, false, 0, 0);
                return new EntityMovementPacket(entityId, deltaMovement, true, buf.readFloat(), buf.readFloat());
            });

    public EntityMovementPacket(Entity entity, boolean sendRot){
        this(entity.getId(), entity.getDeltaMovement(), sendRot, sendRot ? entity.getXRot() : 0, sendRot ? entity.getYRot() : 0);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
