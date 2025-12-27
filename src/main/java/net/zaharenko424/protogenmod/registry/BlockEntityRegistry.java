package net.zaharenko424.protogenmod.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.protogenmod.ProtogenMod;
import net.zaharenko424.protogenmod.block.entity.WireEntity;

import static net.zaharenko424.protogenmod.registry.BlockRegistry.COPPER_WIRE;
import static net.zaharenko424.protogenmod.registry.BlockRegistry.GOLD_WIRE;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ProtogenMod.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WireEntity>> WIRE_ENTITY = BLOCK_ENTITIES
            .register("copper_wire", ()-> BlockEntityType.Builder.of(WireEntity::new,
                    COPPER_WIRE.get(), GOLD_WIRE.get()).build(null));
}
