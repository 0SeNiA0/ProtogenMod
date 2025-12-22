package net.zaharenko424.protogenmod.entity;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.protogenmod.registry.TransformRegistry;
import net.zaharenko424.protogenmod.transform.AbstractTransform;

public class Protogen extends AbstractTransform<Protogen, ProtogenEntity> {
    public Protogen(Either<ProtogenEntity, Player> holder) {
        super(holder, TransformRegistry.PROTOGEN.get());
    }
}
