package dev.itsmeow.whisperwoods.block;

import dev.itsmeow.whisperwoods.tileentity.TileEntityGhostLight;
import dev.itsmeow.whisperwoods.util.IHaveColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockGhostLight extends Block implements IHaveColor {

    private static VoxelShape SHAPE;
    static {
        double d = 0.0625D * 5;
        SHAPE = VoxelShapes.create(d, 0.0D, d, 1D - d, 1D - d, 1D - d);
    }

    private int color = 0;

    public BlockGhostLight(int color) {
        super(Properties.create(Material.MISCELLANEOUS).sound(SoundType.LANTERN).lightValue(12));
        this.color = color;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext ctx) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
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

}
