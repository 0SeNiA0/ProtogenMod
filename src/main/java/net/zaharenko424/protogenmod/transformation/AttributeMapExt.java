package net.zaharenko424.protogenmod.transformation;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public interface AttributeMapExt {

    static AttributeMapExt of(AttributeMap map){
        return (AttributeMapExt) map;
    }

    AttributeSupplier supplier();

    void mapBaseValuesAndAttributes(AttributeMap previous, AttributeSupplier transform, float delta);

    AttributeMap mergeAttributesWith(AttributeSupplier supplier);

    AttributeMap mergeAttributesWith(AttributeMap other, AttributeSupplier supplier);

}
