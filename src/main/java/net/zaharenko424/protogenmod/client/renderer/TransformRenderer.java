package net.zaharenko424.protogenmod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.protogenmod.client.model.TransformModel;
import net.zaharenko424.protogenmod.transform.AbstractTransform;
import net.zaharenko424.protogenmod.transform.TransformEntity;

public abstract class TransformRenderer <TF extends AbstractTransform<TF, E>, E extends TransformEntity<TF, E>, M extends TransformModel<TF, E>> extends LivingEntityRenderer<LivingEntity, M> {

    public TransformRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    public void render(TF transform, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        render(transform.unwrappedHolder(), entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
