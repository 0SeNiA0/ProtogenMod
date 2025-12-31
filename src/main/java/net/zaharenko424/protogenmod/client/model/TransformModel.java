package net.zaharenko424.protogenmod.client.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.protogenmod.transform.AbstractTransform;
import net.zaharenko424.protogenmod.transform.TransformEntity;
import net.zaharenko424.protogenmod.transform.TransformUtil;
import org.jetbrains.annotations.NotNull;

public abstract class TransformModel <TF extends AbstractTransform<TF, E>, E extends TransformEntity<TF, E>> extends EntityModel<LivingEntity> {

    @Override
    public void prepareMobModel(@NotNull LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
        TF transform = (TF) TransformUtil.transformOf(entity);
        if (transform != null) prepareMobModel(transform, limbSwing, limbSwingAmount, partialTick);
    }

    public void prepareMobModel(TF transform, float limbSwing, float limbSwingAmount, float partialTick) {

    }

    @Override
    public void setupAnim(@NotNull LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        TF transform = (TF) TransformUtil.transformOf(entity);
        if (transform != null) setupAnim(transform, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public abstract void setupAnim(TF transform, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);
}
