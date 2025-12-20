package net.zaharenko424.protogenmod.mixin.entity.attribute;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.*;
import net.zaharenko424.protogenmod.transformation.AttributeMapExt;
import net.zaharenko424.protogenmod.util.AttributeUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

@Mixin(AttributeMap.class)
public abstract class MixinAttributeMap implements AttributeMapExt {

    @Shadow
    @Nullable
    public abstract AttributeInstance getInstance(Holder<Attribute> attribute);

    @Shadow
    @Final
    private AttributeSupplier supplier;

    @Override
    public void mapBaseValuesAndAttributes(AttributeMap previous, AttributeSupplier transform, float delta) {
        Map<Holder<Attribute>, AttributeInstance> instances = ((AttributeSupplierAccess)supplier).getInstances();

        boolean first, second;
        double newBase;
        AttributeInstance targetInstance;
        Map<Holder<Attribute>, AttributeInstance> transformInstances = ((AttributeSupplierAccess)transform).getInstances();
        for(Holder<Attribute> attrib : instances.keySet()){
            targetInstance = getInstance(attrib);
            first = previous.hasAttribute(attrib);
            second = transform.hasAttribute(attrib);

            if(first && second){
                newBase = Mth.lerp(previous.getBaseValue(attrib), transform.getBaseValue(attrib), delta);
            } else if(first){
                newBase = Mth.lerp(previous.getBaseValue(attrib), attrib.value().getDefaultValue(), delta);
            } else if(second){
                newBase = Mth.lerp(attrib.value().getDefaultValue(), transform.getBaseValue(attrib), delta);
            } else newBase = attrib.value().getDefaultValue();

            targetInstance.setBaseValue(newBase);

            if (first) mapModifiers(previous.getInstance(attrib), targetInstance, 1 - delta);

            if (second) mapModifiers(transformInstances.get(attrib), targetInstance, delta);
        }
    }

    @Unique
    private void mapModifiers(AttributeInstance srcInstance, AttributeInstance targetInstance, float delta){
        Set<AttributeModifier> modifiers = srcInstance.getModifiers();
        Map<ResourceLocation, AttributeModifier> permanent = ((AttributeInstanceAccess)srcInstance).getPermanentModifiers();

        if(delta == 0){
            modifiers.forEach(mod -> targetInstance.removeModifier(mod.id()));
            return;
        }

        AttributeModifier mapped;
        for (AttributeModifier mod : modifiers){
            mapped = AttributeUtil.mapModifier(mod, delta);
            assert mapped != null;

            if(!permanent.containsValue(mod)){
                targetInstance.addOrUpdateTransientModifier(mapped);
            } else targetInstance.addOrReplacePermanentModifier(mapped);
        }
    }

    @Override
    public AttributeMap mergeAttributesWith(AttributeSupplier supplier) {
        AttributeSupplier.Builder builder = AttributeSupplier.builder();

        Set<Holder<Attribute>> thisAttributes = ((AttributeSupplierAccess)supplier).getInstances().keySet();
        for (Holder<Attribute> attrib : thisAttributes){
            builder.add(attrib);
        }

        for (Holder<Attribute> attrib : ((AttributeSupplierAccess)supplier).getInstances().keySet()){
            if(!thisAttributes.contains(attrib)) builder.add(attrib);
        }

        return new AttributeMap(builder.build());
    }

    @Override
    public AttributeMap mergeAttributesWith(AttributeMap other, AttributeSupplier supplier) {
        AttributeSupplier.Builder builder = AttributeSupplier.builder();

        Set<Holder<Attribute>> thisAttributes = ((AttributeSupplierAccess)supplier).getInstances().keySet();
        for (Holder<Attribute> attrib : thisAttributes){
            builder.add(attrib);
        }

        Set<Holder<Attribute>> otherAttributes = ((AttributeSupplierAccess)AttributeMapExt.of(other).supplier()).getInstances().keySet();
        for (Holder<Attribute> attrib : thisAttributes){
            if(!thisAttributes.contains(attrib)) builder.add(attrib);
        }

        for (Holder<Attribute> attrib : ((AttributeSupplierAccess)supplier).getInstances().keySet()){
            if(!thisAttributes.contains(attrib) && !otherAttributes.contains(attrib)) builder.add(attrib);
        }

        return new AttributeMap(builder.build());
    }
}
