package net.zaharenko424.protogenmod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.protogenmod.item.AbstractWeapon;
import net.zaharenko424.protogenmod.network.packet.ServerboundWeaponFirePacket;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onComputeFOVModifier(ComputeFovModifierEvent event){
        Player player = event.getPlayer();
        ItemStack stack = player.getUseItem();
        if(!player.isUsingItem() || !(stack.getItem() instanceof AbstractWeapon)) return;

        float f1 = (float) player.getTicksUsingItem() / AbstractWeapon.ANIM_DURATION;
        if (f1 > 1.0F) {
            f1 = 1.0F;
        } else {
            f1 *= f1;
        }

        event.setNewFovModifier(event.getNewFovModifier() * (1.0F - f1 * 0.25F));
    }

    private static boolean attackingBlock;

    @SubscribeEvent
    public static void onInput(InputEvent.MouseButton.Post event){
        if (!Minecraft.getInstance().options.keyAttack.isDown()) attackingBlock = false;
    }

    @SubscribeEvent
    public static void onInput(InputEvent.InteractionKeyMappingTriggered event){
        Minecraft minecraft = Minecraft.getInstance();
        if (event.getKeyMapping() != Minecraft.getInstance().options.keyAttack) return;

        Player player = minecraft.player;
        HitResult result = minecraft.hitResult;
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        if ((result != null && result.getType() == HitResult.Type.ENTITY) || !(stack.getItem() instanceof AbstractWeapon)) return;

        if (!attackingBlock) {
            tryShoot(player, stack, hand);
            attackingBlock = true;
        }
        event.setSwingHand(false);
        event.setCanceled(true);
    }

    public static void tryShoot(Player player, ItemStack stack, InteractionHand hand){
        if(stack.isEmpty() || !(stack.getItem() instanceof AbstractWeapon weapon)) return;

        if (player.getCooldowns().isOnCooldown(weapon)) return;

        PacketDistributor.sendToServer(new ServerboundWeaponFirePacket(hand));
    }
}
