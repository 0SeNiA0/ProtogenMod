package net.zaharenko424.protogenmod;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class ProtogenModTags {

    public static class Entity {

        public static final TagKey<EntityType<?>> TRANSFORMABLE = key("transformable");

        private static TagKey<EntityType<?>> key(String name){
            return TagKey.create(Registries.ENTITY_TYPE, ProtogenMod.resourceLoc(name));
        }
    }
}
