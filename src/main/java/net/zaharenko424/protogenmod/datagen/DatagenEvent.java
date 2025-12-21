package net.zaharenko424.protogenmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.datagen.advancement.AdvancementSubProvider;
import net.zaharenko424.protogenmod.datagen.lang.ENLanguageProvider;
import net.zaharenko424.protogenmod.datagen.loot_table.BlockLootTableProvider;
import net.zaharenko424.protogenmod.datagen.loot_table.EntityLootTableProvider;
import net.zaharenko424.protogenmod.datagen.recipe.RecipeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ProtogenMod.MODID)
public final class DatagenEvent {

    @SubscribeEvent
    public static void onGatherData(@NotNull GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        PackOutput out = generator.getPackOutput();

        generator.addProvider(event.includeClient(), new BlockStateProvider(out, helper));
        generator.addProvider(event.includeClient(), new ItemModelProvider(out, helper));
        generator.addProvider(event.includeClient(), new SoundDefinitionProvider(out, helper));

        BlockTagProvider tagProvider = generator.addProvider(event.includeServer(),new BlockTagProvider(out, lookup, helper));
        generator.addProvider(event.includeServer(), new ItemTagProvider(out, lookup, tagProvider.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new EntityTypeTagProvider(out, lookup, helper));

        CompletableFuture<HolderLookup.Provider> lookup0 =
                generator.addProvider(event.includeServer(), new DatapackEntriesProvider(out, lookup)).getRegistryProvider();
        generator.addProvider(event.includeServer(), new BiomeTagProvider(out, lookup0, helper));
        generator.addProvider(event.includeServer(), new DamageTypeTagProvider(out, lookup0, helper));

        generator.addProvider(event.includeServer(), new RecipeProvider(out, lookup0));
        generator.addProvider(event.includeServer(), new LootTableProvider(out, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(BlockLootTableProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(EntityLootTableProvider::new, LootContextParamSets.ENTITY)
        ), lookup0));

        generator.addProvider(event.includeServer(), new AdvancementProvider(out, lookup, helper, List.of(
                new AdvancementSubProvider()
        )));

        generator.addProvider(event.includeClient(), new ENLanguageProvider(out));
    }
}