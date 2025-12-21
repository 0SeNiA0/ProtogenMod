package net.zaharenko424.protogenmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.datagen.worldgen.BiomeModifierProvider;
import net.zaharenko424.protogenmod.datagen.worldgen.ConfiguredFeatureProvider;
import net.zaharenko424.protogenmod.datagen.worldgen.PlacedFeatureProvider;
import net.zaharenko424.protogenmod.datagen.worldgen.StructureProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class DatapackEntriesProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, DatapackEntriesProvider::biome)
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureProvider::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatureProvider::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifierProvider::bootstrap)
            .add(Registries.DAMAGE_TYPE, DatapackEntriesProvider::damageType)
            .add(Registries.PROCESSOR_LIST, DatapackEntriesProvider::processorList)
            .add(Registries.TEMPLATE_POOL, DatapackEntriesProvider::templatePools)
            .add(Registries.STRUCTURE, StructureProvider::bootstrap)
            .add(Registries.STRUCTURE_SET, StructureProvider::structureSet);

    public DatapackEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ProtogenMod.MODID));
    }

    private static void damageType(BootstrapContext<DamageType> context){

    }

    private static void biome(BootstrapContext<Biome> context){

    }

    private static void processorList(BootstrapContext<StructureProcessorList> context){

    }

    private static void templatePools(BootstrapContext<StructureTemplatePool> context){

    }
}