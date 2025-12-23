package net.zaharenko424.protogenmod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.protogenmod.item.AbstractWeapon;
import net.zaharenko424.protogenmod.network.packet.ServerboundWeaponFirePacket;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvent {

    private static long lastShot;
    private static boolean attackingBlock;

    @SubscribeEvent
    public static void onInput(InputEvent.MouseButton.Post event){
        if(!Minecraft.getInstance().options.keyAttack.isDown()) attackingBlock = false;
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event){
        tryShoot(event.getEntity(), event.getItemStack(), event.getHand());
    }

    @SubscribeEvent
    public static void onInput(InputEvent.InteractionKeyMappingTriggered event){
        Minecraft minecraft = Minecraft.getInstance();
        if (event.getKeyMapping() != Minecraft.getInstance().options.keyAttack) return;

        Player player = minecraft.player;
        HitResult result = minecraft.hitResult;
        if ((result != null && result.getType() == HitResult.Type.ENTITY) || !(player.getItemInHand(event.getHand()).getItem() instanceof AbstractWeapon)) return;
        event.setSwingHand(false);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;

        if(attackingBlock) {
            event.setCanceled(true);
            return;
        }

        event.setCanceled(tryShoot(event.getEntity(), event.getItemStack(), event.getHand()) || event.isCanceled());
        attackingBlock = true;
    }

    public static boolean tryShoot(Player player, ItemStack stack, InteractionHand hand){
        if(stack.isEmpty() || !(stack.getItem() instanceof AbstractWeapon)) return false;

        long gameTime = player.level().getGameTime();
        if(lastShot == gameTime) return false;//Max 1 packet per tick but whether packet will be accepted depends on the server -> no incentive to use autoclickers & lower chance of sync issues

        lastShot = gameTime;
        PacketDistributor.sendToServer(new ServerboundWeaponFirePacket(hand));
        return true;
    }
}
