package net.zaharenko424.protogenmod.registry;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.block.WireBlock;

public class BlockRegistry {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ProtogenMod.MODID);

    public static final DeferredBlock<WireBlock> COPPER_WIRE = BLOCKS.register("copper_wire", ()-> new WireBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final DeferredBlock<WireBlock> GOLD_WIRE = BLOCKS.register("gold_wire", ()-> new WireBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
}
