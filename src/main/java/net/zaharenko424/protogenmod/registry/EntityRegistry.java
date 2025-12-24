package net.zaharenko424.protogenmod.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.entity.ProtogenEntity;
import net.zaharenko424.protogenmod.entity.projectile.PlasmaBolt;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ProtogenMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ProtogenEntity>> PROTOGEN_ENTITY = ENTITY_TYPES
            .register("protogen", () -> EntityType.Builder.of(ProtogenEntity::new, MobCategory.CREATURE).build("null"));

    public static final DeferredHolder<EntityType<?>, EntityType<PlasmaBolt>> PLASMA_BOLT = ENTITY_TYPES
            .register("plasma_bolt", () -> EntityType.Builder.<PlasmaBolt>of(PlasmaBolt::new, MobCategory.MISC)
                    .sized(.25f, .25f).build("null"));
}
