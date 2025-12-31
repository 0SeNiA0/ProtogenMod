package net.zaharenko424.protogenmod.transform.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.zaharenko424.protogenmod.transform.TransformType;

public class TransformStartedEvent extends LivingEvent {

    private final TransformType<?, ?> transformInto;
    private final int duration;

    public TransformStartedEvent(LivingEntity entity, TransformType<?, ?> transformInto, int duration) {
        super(entity);
        this.transformInto = transformInto;
        this.duration = duration;
    }

    public TransformType<?, ?> transformInto() {
        return transformInto;
    }

    public int duration() {
        return duration;
    }
}
