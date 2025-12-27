package net.zaharenko424.protogenmod;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
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

    public static <T> @NotNull ResourceKey<T> resourceKey(ResourceKey<? extends Registry<T>> registry, String str){
        return ResourceKey.create(registry, resourceLoc(str));
    }

    public static @NotNull ResourceLocation textureLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,"textures/" + path + ".png");
    }

    public static @NotNull ResourceLocation textureLoc(ResourceLocation loc){
        return loc.withPrefix("textures/").withSuffix(".png");
    }

    public ProtogenMod(IEventBus modEventBus, ModContainer modContainer) {

        BlockEntityRegistry.BLOCK_ENTITIES.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        ComponentRegistry.COMPONENTS.register(modEventBus);
        CreativeTabRegistry.CREATIVE_TABS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        SoundRegistry.SOUNDS.register(modEventBus);

        AttachmentRegistry.ATTACHMENT_TYPES.register(modEventBus);
        TransformRegistry.TRANSFORM_TYPES.register(modEventBus);
    }
}
