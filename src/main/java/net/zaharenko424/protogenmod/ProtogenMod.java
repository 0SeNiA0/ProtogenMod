package net.zaharenko424.protogenmod;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.protogenmod.registry.AttachmentRegistry;
import net.zaharenko424.protogenmod.registry.EntityRegistry;
import net.zaharenko424.protogenmod.registry.TransformRegistry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(ProtogenMod.MODID)
public class ProtogenMod {

    public static final String MODID = "protogenmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.protogenmod")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(EXAMPLE_BLOCK_ITEM);
            }).build());

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

        EntityRegistry.ENTITY_TYPES.register(modEventBus);

        AttachmentRegistry.ATTACHMENT_TYPES.register(modEventBus);
        TransformRegistry.TRANSFORM_TYPES.register(modEventBus);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        //modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
