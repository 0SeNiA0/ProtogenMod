package net.zaharenko424.protogenmod;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DamageSources {

    ResourceKey<DamageType> plasma = create("plasma");
    ResourceKey<DamageType> plasmaExplosion = create("plasma_explosion");

    static DamageSource plasma(@NotNull Entity projectile, @Nullable Entity shooter){
        return new DamageSource(holder(projectile.level(), plasma), projectile, shooter, projectile.position());
    }

    static DamageSource plasmaExplosion(@NotNull Entity projectile, @Nullable Entity shooter){
        return new DamageSource(holder(projectile.level(), plasmaExplosion), projectile, shooter, projectile.position());
    }

    private static @NotNull Holder<DamageType> holder(@NotNull Level level, ResourceKey<DamageType> key){
        return level.holderOrThrow(key);
    }

    private static @NotNull ResourceKey<DamageType> create(String str){
        return ResourceKey.create(Registries.DAMAGE_TYPE, ProtogenMod.resourceLoc(str));
    }
}
