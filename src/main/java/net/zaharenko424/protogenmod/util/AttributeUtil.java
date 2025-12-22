package net.zaharenko424.protogenmod.util;

import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.zaharenko424.protogenmod.mixin.entity.attribute.AttributeMapAccess;
import net.zaharenko424.protogenmod.mixin.entity.attribute.AttributeSupplierAccess;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
public class AttributeUtil {

    public static void mapBaseValues(AttributeMap current, AttributeMap base, AttributeSupplier transform, float delta){
        Map<Holder<Attribute>, AttributeInstance> instances = ((AttributeSupplierAccess)((AttributeMapAccess)current).getSupplier()).getInstances();

        boolean first, second;
        double newBase;
        AttributeInstance targetInstance;
        for(Holder<Attribute> attrib : instances.keySet()){
            targetInstance = current.getInstance(attrib);
            first = base.hasAttribute(attrib);
            second = transform.hasAttribute(attrib);

            if(first && second){
                newBase = Mth.lerp(base.getBaseValue(attrib), transform.getBaseValue(attrib), delta);
            } else if(first){
                newBase = Mth.lerp(base.getBaseValue(attrib), attrib.value().getDefaultValue(), delta);
            } else if(second){
                newBase = Mth.lerp(attrib.value().getDefaultValue(), transform.getBaseValue(attrib), delta);
            } else newBase = attrib.value().getDefaultValue();

            targetInstance.setBaseValue(newBase);
        }
    }

    public static void mapBaseValues(AttributeMap current, AttributeSupplier prevTransform, AttributeSupplier transform, float delta){
        Map<Holder<Attribute>, AttributeInstance> instances = ((AttributeSupplierAccess)((AttributeMapAccess)current).getSupplier()).getInstances();

        boolean first, second;
        double newBase;
        AttributeInstance targetInstance;
        for(Holder<Attribute> attrib : instances.keySet()){
            targetInstance = current.getInstance(attrib);
            first = prevTransform.hasAttribute(attrib);
            second = transform.hasAttribute(attrib);

            if(first && second){
                newBase = Mth.lerp(prevTransform.getBaseValue(attrib), transform.getBaseValue(attrib), delta);
            } else if(first){
                newBase = Mth.lerp(prevTransform.getBaseValue(attrib), attrib.value().getDefaultValue(), delta);
            } else if(second){
                newBase = Mth.lerp(attrib.value().getDefaultValue(), transform.getBaseValue(attrib), delta);
            } else newBase = attrib.value().getDefaultValue();

            targetInstance.setBaseValue(newBase);
        }
    }
}
