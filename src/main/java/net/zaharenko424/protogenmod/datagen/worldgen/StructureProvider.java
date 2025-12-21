package net.zaharenko424.protogenmod.datagen.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class StructureProvider {

    public static void bootstrap(BootstrapContext<Structure> context){
        HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);


    }

    public static void structureSet(BootstrapContext<StructureSet> context){
        HolderGetter<Structure> structureGetter = context.lookup(Registries.STRUCTURE);


    }
}