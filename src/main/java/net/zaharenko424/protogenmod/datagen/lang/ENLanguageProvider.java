package net.zaharenko424.protogenmod.datagen.lang;

import net.minecraft.data.PackOutput;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.registry.TransformRegistry;

public class ENLanguageProvider extends LanguageProvider {

    public ENLanguageProvider(PackOutput output) {
        super(output, ProtogenMod.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        //Advancements


        //Attributes


        //Blocks


        //Command


        //Container


        //Death Messages


        //DNA Types


        //Effects


        //Entities


        //Game rules


        //Keybindings


        add("key." + modid + ".keyCategory", "Protogen Mod");

        //Items


        add("itemGroup." + modid + ".main", "Protogen Mod");

        //Message


        //Misc


        //Screen


        //Sounds


        //Tooltips


        //Transfurs
        TransformRegistry.TRANSFORM_TYPES.getEntries().forEach(this::addTransform);
    }
}