package net.zaharenko424.protogenmod.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.zaharenko424.protogenmod.item.AbstractWeapon;
import net.zaharenko424.protogenmod.network.packet.ServerboundWeaponFirePacket;

import java.util.WeakHashMap;

public class ServerPacketHandler {

    private static final WeakHashMap<ServerPlayer, Long> lastShots = new WeakHashMap<>();

    public static void handleWeaponFirePacket(ServerboundWeaponFirePacket packet, IPayloadContext context){
        context.enqueueWork(() -> {
            ServerPlayer sender = (ServerPlayer) context.player();
            ItemStack stack = sender.getItemInHand(packet.hand());
            if (!(stack.getItem() instanceof AbstractWeapon weapon)) return;

            long lastShot = lastShots.getOrDefault(sender, 0L);
            long overworldTime = sender.getServer().overworld().getGameTime();
            if (lastShot + weapon.fireCooldownTicks(sender, stack) > overworldTime) return;

            lastShots.put(sender, overworldTime);
            weapon.fire(sender, stack, packet.hand());
        });
    }
}
