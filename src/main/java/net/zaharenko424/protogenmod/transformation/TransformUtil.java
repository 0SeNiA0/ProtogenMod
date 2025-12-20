package net.zaharenko424.protogenmod.transformation;

import net.minecraft.world.entity.LivingEntity;

public class TransformUtil {

    public static TransformType<?, ?> typeOf(LivingEntity entity){
        if(entity instanceof TransformEntity<?,?> transform) return transform.transformType();

        TransformHandler handler = TransformHandler.of(entity);
        return handler != null ? handler.transformType() : null;
    }

    public static AbstractTransform<?, ?> transformOf(LivingEntity entity){
        if(entity instanceof TransformEntity<?,?> transform) return transform.transform();

        TransformHandler handler = TransformHandler.of(entity);
        return handler != null ? handler.transform() : null;
    }
}
