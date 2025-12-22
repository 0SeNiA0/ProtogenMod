package net.zaharenko424.protogenmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.ProtogenModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagProvider extends EntityTypeTagsProvider {

    public EntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> holderLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, holderLookup, ProtogenMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider lookup) {
        tag(ProtogenModTags.Entity.TRANSFORMABLE).add(EntityType.PLAYER);//TODO add more entities
    }
}