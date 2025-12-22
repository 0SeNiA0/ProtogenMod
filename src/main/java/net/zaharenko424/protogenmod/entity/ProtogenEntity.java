package net.zaharenko424.protogenmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.zaharenko424.protogenmod.registry.TransformRegistry;
import net.zaharenko424.protogenmod.transform.TransformEntity;

public class ProtogenEntity extends TransformEntity<Protogen, ProtogenEntity> {

    public ProtogenEntity(EntityType<? extends TransformEntity<Protogen, net.zaharenko424.protogenmod.entity.ProtogenEntity>> entityType, Level level) {
        super(entityType, level, TransformRegistry.PROTOGEN.get());
    }
}
