package net.zaharenko424.protogenmod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.zaharenko424.protogenmod.ProtogenMod;
import org.jetbrains.annotations.NotNull;

public class ItemModelProvider extends net.neoforged.neoforge.client.model.generators.ItemModelProvider {

    public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ProtogenMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }

    private @NotNull ResourceLocation itemLoc(@NotNull DeferredItem<?> item){
        return item.getId().withPrefix(ITEM_FOLDER + "/");
    }

    private @NotNull ResourceLocation blockLoc(@NotNull DeferredBlock<?> block){
        return block.getId().withPrefix(BLOCK_FOLDER + "/");
    }

    private @NotNull ItemModelBuilder basicItemBlockTexture(@NotNull DeferredItem<?> item, @NotNull DeferredBlock<?> block){
        return getBuilder(item.getId().getPath()).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", blockLoc(block));
    }

    protected void spawnEgg(@NotNull DeferredItem<SpawnEggItem> egg){
        withExistingParent(egg.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }
}