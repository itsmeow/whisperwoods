package dev.itsmeow.whisperwoods.block;

import dev.itsmeow.whisperwoods.blockentity.GhostLightBlockEntity;
import dev.itsmeow.whisperwoods.util.IHaveColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GhostLightBlock extends Block implements EntityBlock, IHaveColor {

    private static final VoxelShape SHAPE;
    static {
        double d = 0.0625D * 5;
        SHAPE = Shapes.box(d, 0.0D, d, 1D - d, 1D - d, 1D - d);
    }

    private int color;

    public GhostLightBlock(int color) {
        super(Properties.of().sound(SoundType.LANTERN).lightLevel(state -> 12));
        this.color = color;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GhostLightBlockEntity(pos, state);
    }
    public int getColor() {
        return this.color;
    }

}
