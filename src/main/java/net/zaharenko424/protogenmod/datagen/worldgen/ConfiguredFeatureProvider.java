package net.zaharenko424.protogenmod.datagen.worldgen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.NotNull;

public class ConfiguredFeatureProvider {

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?,?>> context){

    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(@NotNull BootstrapContext<ConfiguredFeature<?,?>> context, ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}