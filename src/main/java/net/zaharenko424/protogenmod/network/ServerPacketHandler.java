package net.zaharenko424.protogenmod.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.protogenmod.item.AbstractWeapon;
import net.zaharenko424.protogenmod.network.packet.ServerboundWeaponFirePacket;

public class ServerPacketHandler {

    public static void handleWeaponFirePacket(ServerboundWeaponFirePacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            ServerPlayer sender = (ServerPlayer) context.player();
            ItemStack stack = sender.getItemInHand(packet.hand());
            if (!(stack.getItem() instanceof AbstractWeapon weapon) || sender.getCooldowns().isOnCooldown(weapon)) return;

            weapon.fire(sender, stack, packet.hand());//TODO make custom cooldowns for per ItemStack cooldown?
            sender.getCooldowns().addCooldown(weapon, weapon.fireCooldownTicks(sender, stack));
        });
    }
}
