package net.zaharenko424.protogenmod.block.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Part2x3x2 implements StringRepresentable, Part {

    LEFT_TOP_FRONT   (0, 2, 0),     RIGHT_TOP_FRONT(1, 2, 0),
    LEFT_MIDDLE_FRONT(0, 1, 0),  RIGHT_MIDDLE_FRONT(1, 1, 0),
    LEFT_BOTTOM_FRONT(0, 0, 0),  RIGHT_BOTTOM_FRONT(1, 0, 0),

    LEFT_TOP_BACK   (0, 2, -1),      RIGHT_TOP_BACK(1, 2, -1),
    LEFT_MIDDLE_BACK(0, 1, -1),   RIGHT_MIDDLE_BACK(1, 1, -1),
    LEFT_BOTTOM_BACK(0, 0, -1),   RIGHT_BOTTOM_BACK(1, 0, -1);

    private final String serializedName;
    public final int offsetX, offsetY, offsetZ;

    Part2x3x2(int x, int y, int z){
        this.serializedName = toString().toLowerCase(Locale.ROOT);
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
    }

    @Override
    public boolean isMainPart() {
        return this == LEFT_BOTTOM_FRONT;
    }

    public @NotNull BlockPos toMainPos(BlockPos secondaryPos, Direction direction){
        if (isMainPart()) return secondaryPos;

        return secondaryPos
                .relative(direction.getClockWise(), offsetX)
                .below(offsetY)
                .relative(direction.getOpposite(), offsetZ);
    }

    public @NotNull BlockPos toSecondaryPos(BlockPos mainPos, Direction direction){
        return mainPos
                .relative(direction.getCounterClockWise(), offsetX)
                .above(offsetY)
                .relative(direction, offsetZ);
    }

    @Override
    public @NotNull String getSerializedName() {
        return serializedName;
    }
}
