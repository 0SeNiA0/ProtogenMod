package net.zaharenko424.protogenmod.transform.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.zaharenko424.protogenmod.transform.TransformType;
import org.jetbrains.annotations.Nullable;

public class TransformedEvent extends LivingEvent {

    private final TransformType<?, ?> oldType;
    private final TransformType<?, ?> newType;

    public TransformedEvent(LivingEntity entity, TransformType<?, ?> oldType, TransformType<?, ?> newType) {
        super(entity);
        this.oldType = oldType;
        this.newType = newType;
    }

    public @Nullable TransformType<?, ?> oldType() {
        return oldType;
    }

    public TransformType<?, ?> newType() {
        return newType;
    }
}
