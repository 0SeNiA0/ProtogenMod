package net.zaharenko424.protogenmod.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.protogenmod.client.model.TransformModel;
import net.zaharenko424.protogenmod.transform.AbstractTransform;
import net.zaharenko424.protogenmod.transform.TransformEntity;
import net.zaharenko424.protogenmod.transform.TransformUtil;
import org.jetbrains.annotations.NotNull;

public abstract class TransformLayer <TF extends AbstractTransform<TF, E>, E extends TransformEntity<TF, E>, M extends TransformModel<TF, E>> extends RenderLayer<LivingEntity, M> {

    public TransformLayer(RenderLayerParent<LivingEntity, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        TF transform = (TF) TransformUtil.transformOf(entity);
        if (transform != null) render(poseStack, bufferSource, packedLight, transform, limbSwing, limbSwingAmount, partialTick, ageInTicks, netHeadYaw, headPitch);
    }

    public abstract void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, TF transform, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch);
}
