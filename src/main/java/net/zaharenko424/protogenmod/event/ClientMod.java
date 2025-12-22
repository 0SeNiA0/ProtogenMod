package net.zaharenko424.protogenmod.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.zaharenko424.protogenmod.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber
public class ClientMod {

    @SubscribeEvent
    public static void onRegisterItemExtensions(RegisterClientExtensionsEvent event){
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(@NotNull LivingEntity entityLiving, @NotNull InteractionHand hand, @NotNull ItemStack rifle) {
                return HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        }, ItemRegistry.THE_RIFLE);
    }
}
