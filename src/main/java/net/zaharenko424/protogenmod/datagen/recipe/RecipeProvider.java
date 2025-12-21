package net.zaharenko424.protogenmod.datagen.recipe;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import net.zaharenko424.protogenmod.datagen.ItemTagProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput out) {

        //Crafting recipes
        Criterion<?> hasCopper = has(Tags.Items.INGOTS_COPPER);
        Criterion<?> hasIron = has(Tags.Items.INGOTS_IRON);
        Criterion<?> hasCopperPlates = has(ItemTagProvider.PLATES_COPPER);
        Criterion<?> hasIronPlates = has(ItemTagProvider.PLATES_IRON);

    }

    private void labBlock(DeferredItem<?> result, Item material, RecipeOutput out){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 24)
                .pattern("CIC")
                .pattern("ICI")
                .pattern("CIC")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', material)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .save(out);
    }

    private void stonecuttingAllToAll(DeferredItem<?> @NotNull [] items, RecipeCategory category, RecipeOutput out){
        for(DeferredItem<?> material : items){
            for(DeferredItem<?> result : items){
                if(material == result) continue;
                SingleItemRecipeBuilder.stonecutting((Ingredient.of(material)), category, result)
                        .unlockedBy(getHasName(material), has(material))
                        .save(out, result.getId().withSuffix("_stonecutting_from_" + material.getId().getPath()));
            }
        }
    }
}