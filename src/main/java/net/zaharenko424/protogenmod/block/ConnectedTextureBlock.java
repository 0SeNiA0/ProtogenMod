package net.zaharenko424.protogenmod.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public abstract class ConnectedTextureBlock extends Block {

    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final ImmutableMap<Direction, BooleanProperty> propByDirection = ImmutableMap.of(
            Direction.UP, UP, Direction.DOWN, DOWN, Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST);

    public ConnectedTextureBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(UP,false)
                .setValue(DOWN,false)
                .setValue(NORTH,false)
                .setValue(EAST,false)
                .setValue(SOUTH,false)
                .setValue(WEST,false));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        //Connect to neighbor if it is connected to this
        if ((isSame(neighborState) && isConnected(direction, neighborState))
                || shouldConnectTo(neighborState, neighborPos, level, direction))
            return state.setValue(propByDirection.get(direction), true);

        return state;
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        BlockPos clickedPos = context.getClickedPos();
        BlockState state = defaultBlockState();
        Level level = context.getLevel();

        Player player = context.getPlayer();
        if (player != null && player.isCrouching()){//Here connect to clicked only(or none if cant)
            Direction dir = context.getClickedFace().getOpposite();
            BlockState clicked = level.getBlockState(blockPos.setWithOffset(clickedPos, dir.getNormal()));
            if (isSame(clicked) || shouldConnectTo(clicked, blockPos, level, dir)) {
                state = state.setValue(propByDirection.get(dir), true);
            }

            BlockState other;
            for(Direction direction : Direction.values()){//Connect to all same states that are connected to this
                if (direction == dir) continue;

                other = level.getBlockState(blockPos.setWithOffset(clickedPos, direction));
                if(isSame(other) && isConnected(direction, other))
                    state = state.setValue(propByDirection.get(direction),true);
            }

            return state;
        }

        BlockState other;
        for(Direction direction : Direction.values()){//Connect to all connectable regardless of whether they are connected to this
            blockPos.setWithOffset(clickedPos, direction);
            other = level.getBlockState(blockPos);
            if(isSame(other) || shouldConnectTo(other, blockPos, level, direction))
                state = state.setValue(propByDirection.get(direction),true);
        }
        return state;
    }

    protected boolean isSame(BlockState other){
        return other.is(this);
    }

    protected boolean isConnected(Direction direction, BlockState other){
        return other.getValue(propByDirection.get(direction.getOpposite()));
    }

    protected boolean shouldConnectTo(@NotNull BlockState state, BlockPos pos, LevelAccessor level, Direction direction){
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    protected @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        if(rotation == Rotation.NONE) return state;
        BlockState newState = state;
        for(Direction direction : Direction.Plane.HORIZONTAL){
            newState = newState.setValue(propByDirection.get(rotation.rotate(direction)), state.getValue(propByDirection.get(direction)));
        }
        return newState;
    }
}