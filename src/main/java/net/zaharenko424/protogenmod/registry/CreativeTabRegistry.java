package net.zaharenko424.protogenmod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.protogenmod.ProtogenMod;

import static net.zaharenko424.protogenmod.registry.ItemRegistry.*;

public class CreativeTabRegistry {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ProtogenMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.protogenmod.main"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(RAM_64GB::toStack)
            .displayItems((parameters, output) -> {
                output.accept(THE_RIFLE);
                output.accept(RAM_64GB);
                output.accept(RAM_128GB);
                output.accept(RAM_256GB);
            }).build());
}
