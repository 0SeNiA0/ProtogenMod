package net.zaharenko424.protogenmod.block.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractMultiBlock <P extends Enum<P> & Part & StringRepresentable> extends Block {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected final EnumProperty<P> partProperty;

    public AbstractMultiBlock(Properties properties, EnumProperty<P> part) {
        super(properties);
        this.partProperty = part;
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return canSurvive(state,level,pos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        if (canBePlaced(context.getClickedPos(), direction, context.getLevel()))
            return defaultBlockState().setValue(FACING, direction);
        return null;
    }

    protected boolean canBePlaced(BlockPos mainPos, Direction direction, Level level){
        BlockPos pos;
        for (P part : partProperty.getPossibleValues()) {
            pos = part.toSecondaryPos(mainPos, direction);
            if (!level.isInWorldBounds(pos) || !level.getBlockState(pos).canBeReplaced()) return false;
        }

        return true;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.isClientSide) return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);

        BlockPos mainPos = getMainPos(state, pos);
        BlockState mainState = level.getBlockState(mainPos);
        if (!super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)) return false;

        if (!mainState.isAir() && mainPos != pos) {
            if (willHarvest) Block.dropResources(mainState, level, mainPos,null, player, player.getMainHandItem());
        }
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.is(newState.getBlock())) return;

        super.onRemove(state, level, pos, newState, movedByPiston);
        BlockPos mainPos = getMainPos(state, pos);
        if (!state.getValue(partProperty).isMainPart()){
            if (level.getBlockState(mainPos).is(this)) level.setBlockAndUpdate(mainPos, Blocks.AIR.defaultBlockState());
            return;
        }

        Direction direction = state.getValue(FACING);
        BlockPos pos1;
        for (P part : partProperty.getPossibleValues()) {
            if (part.isMainPart()) continue;

            pos1 = part.toSecondaryPos(pos, direction);
            if (level.getBlockState(pos1).is(this)) level.setBlockAndUpdate(pos1, Blocks.AIR.defaultBlockState());
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction direction = state.getValue(FACING);
        for (P part : partProperty.getPossibleValues()) {
            if (part.isMainPart()) continue;
            level.setBlockAndUpdate(part.toSecondaryPos(pos, direction), state.setValue(partProperty, part));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, partProperty);
    }

    protected BlockPos getMainPos(BlockState state, BlockPos pos){
        return state.getValue(partProperty).toMainPos(pos, state.getValue(FACING));
    }

    protected boolean isPowered(BlockPos mainPos, BlockState mainState, Level level){
        Direction direction = mainState.getValue(FACING);
        for (P part : partProperty.getPossibleValues()) {
            if (level.hasNeighborSignal(part.toSecondaryPos(mainPos, direction))) return true;
        }

        return false;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    /**
     * By default, mirroring is prohibited. Not possible to reliably detect multiblock
     */
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return mirror == Mirror.NONE ? state : Blocks.AIR.defaultBlockState();
    }
}