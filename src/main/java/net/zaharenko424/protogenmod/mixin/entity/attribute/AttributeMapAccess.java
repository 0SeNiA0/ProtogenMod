package net.zaharenko424.protogenmod.mixin.entity.attribute;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AttributeMap.class)
public interface AttributeMapAccess {

    @Accessor("supplier")
    AttributeSupplier getSupplier();
}
