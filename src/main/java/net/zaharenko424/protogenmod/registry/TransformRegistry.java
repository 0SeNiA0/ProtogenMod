package net.zaharenko424.protogenmod.registry;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.entity.Protogen;
import net.zaharenko424.protogenmod.entity.ProtogenEntity;
import net.zaharenko424.protogenmod.transformation.AbstractTransform;
import net.zaharenko424.protogenmod.transformation.TransformEntity;
import net.zaharenko424.protogenmod.transformation.TransformType;

import java.util.function.Supplier;

import static net.zaharenko424.protogenmod.ProtogenMod.resourceLoc;

public class TransformRegistry {

    public static final DeferredRegister<TransformType<?, ?>> TRANSFORM_TYPES = DeferredRegister.create(resourceLoc("transform_type"), ProtogenMod.MODID);
    public static final Registry<TransformType<?, ?>> TRANSFORM_REGISTRY = TRANSFORM_TYPES.makeRegistry(a -> {});

    public static final DeferredHolder<TransformType<?, ?>, TransformType<Protogen, ProtogenEntity>> PROTOGEN = register("protogen", () ->
            TransformType.Properties.of(EntityRegistry.PROTOGEN_ENTITY, Protogen::new));

    private static <TF extends AbstractTransform<TF, E>, E extends TransformEntity<TF, E>> DeferredHolder<TransformType<?, ?>, TransformType<TF, E>> register(String name, Supplier<TransformType.Properties<TF, E>> func){
        return TRANSFORM_TYPES.register(name, ()-> new TransformType<>(resourceLoc(name), func.get()));
    }
}
