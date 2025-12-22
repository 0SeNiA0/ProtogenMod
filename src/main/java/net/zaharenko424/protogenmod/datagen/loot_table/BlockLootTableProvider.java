package net.zaharenko424.protogenmod.datagen.loot_table;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

import static net.zaharenko424.protogenmod.registry.BlockRegistry.BLOCKS;
import static net.zaharenko424.protogenmod.registry.BlockRegistry.EXAMPLE_BLOCK;

public class BlockLootTableProvider extends BlockLootSubProvider {

    private static final Set<Item> EXPLOSION_RESISTANT = Set.of();

    public BlockLootTableProvider(HolderLookup.Provider lookup) {
        super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), lookup);
    }

    @Override
    protected void generate() {
        dropSelf(EXAMPLE_BLOCK.get());
    }

    private void dropSlab(DeferredBlock<? extends SlabBlock> slab){
        add(slab.get(), createSlabItemTable(slab.get()));
    }

    /*private void derelictMachineDrops(AbstractDerelictMachine derelictMachine, float dropMultiplier){
        add(derelictMachine, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(
                        applyExplosionCondition(derelictMachine, LootItem.lootTableItem(ItemRegistry.IRON_PLATE)
                                .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, .5f * dropMultiplier))))
                )
        ).withPool(LootPool.lootPool()
                .add(
                        applyExplosionCondition(derelictMachine, LootItem.lootTableItem(ItemRegistry.GOLDEN_PLATE)
                                .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, .25f * dropMultiplier))))
                )
        ).withPool(LootPool.lootPool()
                .add(
                        applyExplosionCondition(derelictMachine, LootItem.lootTableItem(Items.DIAMOND)
                                .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, .1f * dropMultiplier))))
                )
        ));
    }

    private void doublePartBlockDrops(Block block){
        add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StateProperties.PART2,0)))
                .add(
                        applyExplosionCondition(block, LootItem.lootTableItem(block))
                )));
    }

    private void xPartMultiBlockDrops(AbstractMultiBlock block, IntegerProperty parts){
        add(block, LootTable.lootTable().withPool(LootPool.lootPool()
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(parts,0)))
                .add(
                        applyExplosionCondition(block, LootItem.lootTableItem(block))
                )));
    }

    private void twelvePartMultiBlockDrops(AbstractMultiBlock block){
        xPartMultiBlockDrops(block, StateProperties.PART12);
    }

    private void ninePartMultiBlockDrops(AbstractMultiBlock block){
        xPartMultiBlockDrops(block, StateProperties.PART9);
    }

    private void sixPartMultiBlockDrops(AbstractMultiBlock block){
        xPartMultiBlockDrops(block, StateProperties.PART6);
    }

    private void fourPartMultiBlockDrops(AbstractMultiBlock block){
        xPartMultiBlockDrops(block, StateProperties.PART4);
    }*/


    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet());
    }
}