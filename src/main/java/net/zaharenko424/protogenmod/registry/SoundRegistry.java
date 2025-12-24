package net.zaharenko424.protogenmod.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.protogenmod.ProtogenMod;
import org.jetbrains.annotations.NotNull;

public class SoundRegistry {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ProtogenMod.MODID);

    /**
     * Sound Effect by <a href="https://pixabay.com/users/freesound_community-46691455/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=100378">freesound_community</a> from <a href="https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=100378">Pixabay</a>
     */
    public static final DeferredHolder<SoundEvent, SoundEvent> PLASMA_RIFLE = registerVariableRange("plasma_rifle");

    private static @NotNull DeferredHolder<SoundEvent, SoundEvent> registerVariableRange(String id){
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(ProtogenMod.resourceLoc(id)));
    }
}
