package net.zaharenko424.protogenmod.block.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface Part {

    boolean isMainPart();

    BlockPos toMainPos(BlockPos secondaryPos, Direction direction);

    BlockPos toSecondaryPos(BlockPos mainPos, Direction direction);
}
