package net.zaharenko424.protogenmod.transformation;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.protogenmod.registry.TransformRegistry;

import java.util.function.Function;

public class TransformType<TF extends AbstractTransform<TF, E>, E extends TransformEntity<TF, E>> {

    public static final Codec<TransformType<?, ?>> CODEC = ResourceLocation.CODEC.xmap(
            TransformRegistry.TRANSFORM_REGISTRY::get, a -> a.loc
    );

    public final ResourceLocation loc;

    private final DeferredHolder<EntityType<?>, EntityType<E>> entityHolder;
    private final Function<Either<E, Player>, TF> transformFunc;

    private final AttributeSupplier attributeSupplier;

    public TransformType(ResourceLocation loc, Properties<TF, E> properties){
        this.loc = loc;
        this.entityHolder = properties.entityHolder;
        this.transformFunc = properties.transformFunc;
        this.attributeSupplier = properties.attributeSupplier;
    }

    public E createEntity(Level level){
        return entityHolder.get().create(level);
    }

    TF createTransform(Either<E, Player> holder){
        return transformFunc.apply(holder);
    }

    public boolean is(TransformType<?, ?> other){
        return this == other;
    }

    public boolean is(DeferredHolder<TransformType<?, ?>, TransformType<?, ?>> other){
        return this == other.get();
    }

    public boolean is(TagKey<TransformType<?, ?>> tag){
        return TransformRegistry.TRANSFORM_REGISTRY.getHolder(loc).map(holder -> holder.is(tag)).orElse(false);
    }

    public AttributeSupplier getAttributes(){
        return attributeSupplier;
    }

    public static class Properties<TF extends AbstractTransform<TF, E>, E extends TransformEntity<TF, E>> {

        private final DeferredHolder<EntityType<?>, EntityType<E>> entityHolder;
        private final Function<Either<E, Player>, TF> transformFunc;

        private AttributeSupplier attributeSupplier = null;

        public Properties(DeferredHolder<EntityType<?>, EntityType<E>> entityHolder, Function<Either<E, Player>, TF> transformFunc){
            this.entityHolder = entityHolder;
            this.transformFunc = transformFunc;
        }

        public static <TF extends AbstractTransform<TF, E>, E extends TransformEntity<TF, E>> Properties<TF, E> of(DeferredHolder<EntityType<?>, EntityType<E>> entityHolder, Function<Either<E, Player>, TF> transformFunc){
            return new Properties<>(entityHolder, transformFunc);
        }

        public Properties<TF, E> withAttributes(AttributeSupplier attributeSupplier){
            this.attributeSupplier = attributeSupplier;
            return this;
        }
    }
}
