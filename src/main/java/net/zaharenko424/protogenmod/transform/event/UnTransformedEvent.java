package net.zaharenko424.protogenmod.transform.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.zaharenko424.protogenmod.transform.TransformType;

public class UnTransformedEvent extends LivingEvent {

    private final TransformType<?, ?> transformType;

    public UnTransformedEvent(LivingEntity entity, TransformType<?, ?> transformType) {
        super(entity);
        this.transformType = transformType;
    }

    public TransformType<?, ?> transformType() {
        return transformType;
    }
}
