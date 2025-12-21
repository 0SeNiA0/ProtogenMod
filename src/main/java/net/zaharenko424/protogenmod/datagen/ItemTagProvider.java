package net.zaharenko424.protogenmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zaharenko424.protogenmod.ProtogenMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends ItemTagsProvider {

    public static final TagKey<Item> PLATES_COPPER = ItemTags.create(ResourceLocation.parse("c:plates/copper"));
    public static final TagKey<Item> PLATES_GOLD = ItemTags.create(ResourceLocation.parse("c:plates/gold"));
    public static final TagKey<Item> PLATES_IRON = ItemTags.create(ResourceLocation.parse("c:plates/iron"));
    public static final TagKey<Item> WIRES_COPPER = ItemTags.create(ResourceLocation.parse("c:wires/copper"));

    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> holderLookup, CompletableFuture<TagLookup<Block>> tagLookup, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, holderLookup, tagLookup, ProtogenMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider p_256380_) {

    }
}