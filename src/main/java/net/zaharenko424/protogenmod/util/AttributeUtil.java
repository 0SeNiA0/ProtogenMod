package net.zaharenko424.protogenmod.util;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.zaharenko424.protogenmod.transformation.AttributeMapExt;
import org.jetbrains.annotations.Nullable;

public class AttributeUtil {

    public static @Nullable AttributeModifier mapModifier(AttributeModifier original, float delta){
        double mappedAmount = original.amount() * delta;
        return mappedAmount != 0 ? new AttributeModifier(original.id(), mappedAmount, original.operation()) : null;
    }

    public static void mapBaseValuesAndAttributes(AttributeMap current, AttributeMap previous, AttributeSupplier transform, float delta){
        AttributeMapExt.of(current).mapBaseValuesAndAttributes(previous, transform, delta);
    }

    public static AttributeMap mergeAttributes(AttributeMap base, AttributeSupplier transform){
        return AttributeMapExt.of(base).mergeAttributesWith(transform);
    }

    public static AttributeMap mergeAttributes(AttributeMap base, AttributeMap previous, AttributeSupplier transform){
        return AttributeMapExt.of(base).mergeAttributesWith(previous, transform);
    }

    public static AttributeMap mergeAndMapAttributes(AttributeMap base, AttributeSupplier transform, float delta){
        AttributeMap map = mergeAttributes(base, transform);
        mapBaseValuesAndAttributes(map, base, transform, delta);
        return map;
    }

    public static AttributeMap mergeAndMapAttributes(AttributeMap base, AttributeMap previous, AttributeSupplier transform, float delta){
        AttributeMap map = mergeAttributes(base, previous, transform);
        mapBaseValuesAndAttributes(map, previous, transform, delta);
        return map;
    }
}
