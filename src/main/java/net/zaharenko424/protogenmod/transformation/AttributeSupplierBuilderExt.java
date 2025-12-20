package net.zaharenko424.protogenmod.transformation;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public interface AttributeSupplierBuilderExt {

    static AttributeSupplierBuilderExt of(AttributeSupplier.Builder builder){
        return (AttributeSupplierBuilderExt) builder;
    }

    default AttributeSupplier.Builder self(){
        return (AttributeSupplier.Builder) this;
    }

    AttributeSupplierBuilderExt combine(AttributeSupplier supplier);

    AttributeSupplierBuilderExt add(Holder<Attribute> attribute, AttributeModifier... modifiers);

    AttributeSupplierBuilderExt add(Holder<Attribute> attribute, double baseValue, AttributeModifier... modifiers);
}
