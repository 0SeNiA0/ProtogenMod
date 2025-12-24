package net.zaharenko424.protogenmod.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.zaharenko424.protogenmod.client.renderer.PlasmaBoltRenderer;
import net.zaharenko424.protogenmod.registry.EntityRegistry;
import net.zaharenko424.protogenmod.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@EventBusSubscriber(Dist.CLIENT)
public class ClientMod {

    //public static final ItemTransform RIFLE_FP_R = new ItemTransform(new Vector3f(), new Vector3f(2 / 16f, 5f / 16, 14 / 16f), new Vector3f(1));
    public static final ItemTransform RIFLE_FP_R = new ItemTransform(new Vector3f(), new Vector3f(0, 5.5f / 16, 17 / 16f), new Vector3f(1));
    //public static final ItemTransform RIFLE_FP_L = new ItemTransform(new Vector3f(), new Vector3f(-14 / 16f, 5f / 16, 13.25f / 16f), new Vector3f(1));
    public static final ItemTransform RIFLE_FP_L = new ItemTransform(new Vector3f(), new Vector3f(-16 / 16f, 5.5f / 16, 16.25f / 16f), new Vector3f(1));

    @SubscribeEvent
    public static void onRegisterItemExtensions(RegisterClientExtensionsEvent event){
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(@NotNull LivingEntity entityLiving, @NotNull InteractionHand hand, @NotNull ItemStack rifle) {
                //return CUSTOM_POSE_PROXY.getValue();
                if (entityLiving.getUseItem() == rifle) return HumanoidModel.ArmPose.BOW_AND_ARROW;
                return entityLiving instanceof Player pl && pl.getCooldowns().isOnCooldown(rifle.getItem())
                        ? HumanoidModel.ArmPose.BOW_AND_ARROW : HumanoidModel.ArmPose.ITEM;
            }
        }, ItemRegistry.THE_RIFLE);
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityRegistry.PLASMA_BOLT.get(), PlasmaBoltRenderer::new);
    }
}
