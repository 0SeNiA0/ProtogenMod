package net.zaharenko424.protogenmod;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.zaharenko424.protogenmod.registry.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(ProtogenMod.MODID)
public class ProtogenMod {

    public static final String MODID = "protogenmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation resourceLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static @NotNull ResourceLocation textureLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,"textures/" + path + ".png");
    }

    public static @NotNull ResourceLocation textureLoc(ResourceLocation loc){
        return loc.withPrefix("textures/").withSuffix(".png");
    }

    public ProtogenMod(IEventBus modEventBus, ModContainer modContainer) {

        BlockRegistry.BLOCKS.register(modEventBus);
        CreativeTabRegistry.CREATIVE_TABS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);

        AttachmentRegistry.ATTACHMENT_TYPES.register(modEventBus);
        TransformRegistry.TRANSFORM_TYPES.register(modEventBus);
    }
}
