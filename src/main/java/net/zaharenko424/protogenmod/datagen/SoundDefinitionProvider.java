package net.zaharenko424.protogenmod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.protogenmod.ProtogenMod;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.protogenmod.registry.SoundRegistry.PLASMA_RIFLE;

@ParametersAreNonnullByDefault
public class SoundDefinitionProvider extends SoundDefinitionsProvider {
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param helper The existing file helper provided by the event you are initializing this provider in.
     */
    public SoundDefinitionProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, ProtogenMod.MODID, helper);
    }

    @Override
    public void registerSounds() {
        addWithPath(PLASMA_RIFLE, "futuristic-smg-sfx");
    }

    private void addWithPath(DeferredHolder<SoundEvent, SoundEvent> sound, String filePath){
        ResourceLocation id = sound.getId();
        add(sound, definition()
                .subtitle(subtitle(id.getPath()))
                .with(sound(ProtogenMod.resourceLoc(filePath))));
    }

    private void addSimpleSound(DeferredHolder<SoundEvent, SoundEvent> sound){
        ResourceLocation id = sound.getId();
        add(sound, definition()
                .subtitle(subtitle(id.getPath()))
                .with(sound(id)));
    }

    @Contract(pure = true)
    private @NotNull String subtitle(String str){
        return "sounds." + ProtogenMod.MODID + "." + str;
    }
}