package net.zaharenko424.protogenmod.client.renderer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.zaharenko424.protogenmod.transformation.AbstractTransform;
import net.zaharenko424.protogenmod.transformation.TransformEntity;

public abstract class TransformRenderer <TF extends AbstractTransform<TF, E>, E extends TransformEntity<TF, E>, M extends EntityModel<E>> extends MobRenderer<E, M> {

    public TransformRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

}
