package net.zaharenko424.protogenmod.datagen.loot_table;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static net.zaharenko424.protogenmod.registry.EntityRegistry.ENTITY_TYPES;

public class EntityLootTableProvider extends EntityLootSubProvider {

    public EntityLootTableProvider(HolderLookup.Provider lookup) {
        super(FeatureFlags.REGISTRY.allFlags(), lookup);
    }

    @Override
    public void generate() {

    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return ENTITY_TYPES.getEntries().stream().map(DeferredHolder::get);
    }
}