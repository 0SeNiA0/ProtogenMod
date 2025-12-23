package net.zaharenko424.protogenmod.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.zaharenko424.protogenmod.ProtogenMod;
import org.jetbrains.annotations.NotNull;
//Need to keep track of last packet sent & received to avoid spam clicking
public record ServerboundWeaponFirePacket(InteractionHand hand) implements CustomPacketPayload {

    public static final Type<ServerboundWeaponFirePacket> TYPE = new Type<>(ProtogenMod.resourceLoc("weapon_fire"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundWeaponFirePacket> CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(InteractionHand.class), ServerboundWeaponFirePacket::hand,
            ServerboundWeaponFirePacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
