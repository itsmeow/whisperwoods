package dev.itsmeow.whisperwoods.block;

import dev.itsmeow.whisperwoods.blockentity.GhostLightBlockEntity;
import dev.itsmeow.whisperwoods.util.IHaveColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WispLanternBlock extends Block implements EntityBlock, SimpleWaterloggedBlock, IHaveColor {

    private static final VoxelShape[] SHAPES = new VoxelShape[Direction.values().length];
    static {
        for(Direction facing : Direction.values()) {
            final double d = 0.0625D * 4;
            if(facing == Direction.UP) {
                SHAPES[facing.ordinal()] = Shapes.box(d, 0.0D, d, 1D - d, 1D, 1D - d);
            } else if(facing == Direction.DOWN) {
                SHAPES[facing.ordinal()] = Shapes.box(d, 0.0D, d, 1D - d, 1D - (0.0625D * 3), 1D - d);
            } else {
                int x = facing == Direction.WEST ? 1 : (facing == Direction.EAST ? -1 : 0);
                int z = facing == Direction.NORTH ? 1 : (facing == Direction.SOUTH ? -1 : 0);
                SHAPES[facing.ordinal()] = Shapes.box(d - (d * x), 0.0D, d - (d * z), 1D - d - (d * x), 1D - (0.0625D * 3), 1D - d - (d * z));
            }
        }
    }
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final DirectionProperty HORIZONTAL_FACING = DirectionProperty.create("horizontal", Direction.Plane.HORIZONTAL);
    private int color;

    public WispLanternBlock(int color, Properties properties) {
        super(properties);
        this.color = color;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN).setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GhostLightBlockEntity(pos, state);
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext ctx) {
        return SHAPES[state.getValue(FACING).ordinal()];
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

        for(Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate = this.defaultBlockState().setValue(FACING, direction);
            if(blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                if(direction.getAxis() == Axis.Y) {
                    blockstate = blockstate.setValue(HORIZONTAL_FACING, context.getPlayer().getMotionDirection().getOpposite());
                }
                return blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            }
        }

        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HORIZONTAL_FACING, WATERLOGGED);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        return Block.canSupportCenter(worldIn, pos.relative(direction), direction.getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if(stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }

        return stateIn.getValue(FACING) == facing && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

}
