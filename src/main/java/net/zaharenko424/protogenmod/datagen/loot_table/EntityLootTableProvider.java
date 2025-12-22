package net.zaharenko424.protogenmod.datagen.loot_table;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.protogenmod.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static net.zaharenko424.protogenmod.registry.EntityRegistry.ENTITY_TYPES;

public class EntityLootTableProvider extends EntityLootSubProvider {

    public EntityLootTableProvider(HolderLookup.Provider lookup) {
        super(FeatureFlags.REGISTRY.allFlags(), lookup);
    }

    @Override
    public void generate() {
        add(EntityRegistry.PROTOGEN_ENTITY.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .add(
                                        LootItem.lootTableItem(Items.IRON_INGOT)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))
                                )
                )
        );
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return ENTITY_TYPES.getEntries().stream().map(DeferredHolder::get);
    }
}