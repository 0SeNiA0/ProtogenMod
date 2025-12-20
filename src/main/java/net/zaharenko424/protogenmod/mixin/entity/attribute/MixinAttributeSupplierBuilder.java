package net.zaharenko424.protogenmod.mixin.entity.attribute;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.zaharenko424.protogenmod.transformation.AttributeSupplierBuilderExt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(AttributeSupplier.Builder.class)
public abstract class MixinAttributeSupplierBuilder implements AttributeSupplierBuilderExt {

    @Shadow
    @Final
    private Map<Holder<Attribute>, AttributeInstance> builder;

    @Shadow
    public abstract boolean hasAttribute(Holder<Attribute> attribute);

    @Shadow
    protected abstract AttributeInstance create(Holder<Attribute> attribute);

    @Unique
    private AttributeInstance getOrCreateWithMods(Holder<Attribute> attribute, AttributeModifier[] modifiers){
        AttributeInstance instance = hasAttribute(attribute) ? builder.get(attribute) : create(attribute);

        for(AttributeModifier mod : modifiers){
            instance.addOrReplacePermanentModifier(mod);//Use permanent as attribs are going to be saved instead of regenerating every reload
        }

        return instance;
    }

    @Override
    public AttributeSupplierBuilderExt combine(AttributeSupplier supplier) {
        builder.putAll(((AttributeSupplierAccess)supplier).getInstances());
        return this;
    }

    @Override
    public AttributeSupplierBuilderExt add(Holder<Attribute> attribute, AttributeModifier... modifiers) {
        getOrCreateWithMods(attribute, modifiers);
        return this;
    }

    @Override
    public AttributeSupplierBuilderExt add(Holder<Attribute> attribute, double baseValue, AttributeModifier... modifiers) {
        AttributeInstance instance = getOrCreateWithMods(attribute, modifiers);
        instance.setBaseValue(baseValue);
        return this;
    }
}
