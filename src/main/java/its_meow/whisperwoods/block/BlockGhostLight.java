package its_meow.whisperwoods.block;

import its_meow.whisperwoods.WhisperwoodsMod;
import its_meow.whisperwoods.tileentity.TileEntityGhostLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockGhostLight extends Block {

    private static VoxelShape SHAPE;
    static {
        double d = 0.0625D * 5;
        SHAPE = VoxelShapes.create(d, 0.0D, d, 1D - d, 1D - d, 1D - d);
    }

    private int color = 0;

    public BlockGhostLight(String name, int color) {
        super(Properties.create(Material.MISCELLANEOUS).sound(SoundType.LANTERN).lightValue(12));
        this.setRegistryName(WhisperwoodsMod.MODID, name);
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasCustomBreakingProgress(BlockState state) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState p_220081_1_, IBlockReader p_220081_2_, BlockPos p_220081_3_) {
        return false;
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
