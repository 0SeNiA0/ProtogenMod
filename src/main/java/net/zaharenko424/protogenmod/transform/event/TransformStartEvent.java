package net.zaharenko424.protogenmod.transform.event;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.zaharenko424.protogenmod.transform.TransformType;

public class TransformStartEvent extends LivingEvent {

    private final TransformType<?, ?> transformInto;
    private final int duration;
    private int newDuration;

    public TransformStartEvent(LivingEntity entity, TransformType<?, ?> transformInto, int duration) {
        super(entity);
        this.transformInto = transformInto;
        this.duration = duration;
        newDuration = duration;
    }

    public TransformType<?, ?> transformInto() {
        return transformInto;
    }

    public int duration() {
        return duration;
    }

    public int newDuration() {
        return newDuration;
    }

    public void newDuration(int newDuration) {
        this.newDuration = newDuration;
    }
}
