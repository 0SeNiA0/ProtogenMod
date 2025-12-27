package net.zaharenko424.protogenmod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.protogenmod.block.entity.machine.AbstractProxyWire;
import net.zaharenko424.protogenmod.registry.BlockEntityRegistry;

public class WireEntity extends AbstractProxyWire {

    public WireEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.WIRE_ENTITY.get(), pos, state);
    }
}