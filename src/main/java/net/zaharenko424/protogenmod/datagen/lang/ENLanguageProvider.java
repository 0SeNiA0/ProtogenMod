package net.zaharenko424.protogenmod.datagen.lang;

import net.minecraft.data.PackOutput;
import net.zaharenko424.protogenmod.DamageSources;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.registry.CreativeTabRegistry;
import net.zaharenko424.protogenmod.registry.SoundRegistry;
import net.zaharenko424.protogenmod.registry.TransformRegistry;

import static net.zaharenko424.protogenmod.registry.EntityRegistry.PLASMA_BOLT;
import static net.zaharenko424.protogenmod.registry.ItemRegistry.*;

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
        addDeathMessage(DamageSources.plasma, "%1$s was melted with plasma", "%1$s was melted with plasma by %2$s using %3$s", "%1$s was melted with plasma by %2$s");
        addDeathMessage(DamageSources.plasmaExplosion, "%1$s burned to death in a fiery ball of plasma", "%1$s was exploded with plasma by %2$s using %3$s", "%1$s walked into a plasma explosion while fighting %2$s");


        //DNA Types


        //Effects


        //Entities
        addEntityType(PLASMA_BOLT, "Plasma Bolt");


        //Game rules


        //Keybindings


        add("key." + modid + ".keyCategory", "Protogen Mod");

        //Items
        addItemFromId(THE_RIFLE);
        addItem(RAM_64GB, "DDR8 RAM 64GB");
        addItem(RAM_128GB, "DDR8 RAM 128GB");
        addItem(RAM_256GB, "DDR8 RAM 256GB");
        addItemFromId(COPPER_WIRE_ITEM);
        addItemFromId(GOLD_WIRE_ITEM);

        add(CreativeTabRegistry.MAIN_TAB.getId().toLanguageKey("itemGroup"), "Protogen Mod");

        //Message


        //Misc


        //Screen


        //Sounds
        addSound(SoundRegistry.PLASMA_RIFLE, "Plasma Rifle Shot");

        //Tooltips
        addTooltip("ram", "Mmm, RAM Cookies");
        addTooltip("ram_alt", "Electronic Component");


        //Transfurs
        TransformRegistry.TRANSFORM_TYPES.getEntries().forEach(this::addTransform);
    }
}