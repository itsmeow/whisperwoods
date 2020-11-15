package dev.itsmeow.whisperwoods.block;

import javax.annotation.Nullable;

import dev.itsmeow.whisperwoods.tileentity.TileEntityGhostLight;
import dev.itsmeow.whisperwoods.util.IHaveColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.PushReaction;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class BlockWispLantern extends Block implements IWaterLoggable, IHaveColor {

    private static VoxelShape[] SHAPES = new VoxelShape[Direction.values().length];
    static {
        for(Direction facing : Direction.values()) {
            final double d = 0.0625D * 4;
            if(facing == Direction.UP) {
                SHAPES[facing.ordinal()] = VoxelShapes.create(d, 0.0D, d, 1D - d, 1D, 1D - d);
            } else if(facing == Direction.DOWN) {
                SHAPES[facing.ordinal()] = VoxelShapes.create(d, 0.0D, d, 1D - d, 1D - (0.0625D * 3), 1D - d);
            } else {
                int x = facing == Direction.WEST ? 1 : (facing == Direction.EAST ? -1 : 0);
                int z = facing == Direction.NORTH ? 1 : (facing == Direction.SOUTH ? -1 : 0);
                SHAPES[facing.ordinal()] = VoxelShapes.create(d - (d * x), 0.0D, d - (d * z), 1D - d - (d * x), 1D - (0.0625D * 3), 1D - d - (d * z));
            }
        }
    }
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final DirectionProperty HORIZONTAL_FACING = DirectionProperty.create("horizontal", Direction.Plane.HORIZONTAL);
    private int color = 0;

    public BlockWispLantern(int color, Properties properties) {
        super(properties);
        this.color = color;
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.DOWN).with(HORIZONTAL_FACING, Direction.NORTH).with(WATERLOGGED, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
        return new TileEntityGhostLight();
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext ctx) {
        return SHAPES[state.get(FACING).ordinal()];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());

        for(Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate = this.getDefaultState().with(FACING, direction);
            if(blockstate.isValidPosition(context.getWorld(), context.getPos())) {
                if(direction.getAxis() == Axis.Y) {
                    blockstate = blockstate.with(HORIZONTAL_FACING, context.getPlayer().getAdjustedHorizontalFacing().getOpposite());
                }
                return blockstate.with(WATERLOGGED, Boolean.valueOf(fluidstate.getFluid() == Fluids.WATER));
            }
        }

        return null;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, HORIZONTAL_FACING, WATERLOGGED);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = state.get(FACING);
        return Block.hasEnoughSolidSide(worldIn, pos.offset(direction), direction.getOpposite());
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if(stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return stateIn.get(FACING).getOpposite() == facing && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

}
