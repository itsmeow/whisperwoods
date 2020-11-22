package dev.itsmeow.whisperwoods.block;

import java.util.List;

import javax.annotation.Nullable;

import dev.itsmeow.whisperwoods.tileentity.TileEntityHandOfFate;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHandOfFate.HOFRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockHandOfFate extends Block {

    public static final EnumProperty<Orientation> ROTATION = EnumProperty.create("rotation", Orientation.class);
    private static VoxelShape SHAPE;
    static {
        double d = 0.0625D * 3;
        SHAPE = VoxelShapes.create(d, 0.0D, d, 1D - d, 1.55D, 1D - d);
    }

    public BlockHandOfFate(Properties builder) {
        super(builder);
        this.setDefaultState(this.getStateContainer().getBaseState().with(ROTATION, Orientation.NORTH).with(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public boolean isLit(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this && world.getBlockState(pos.up()).getBlock() instanceof BlockGhostLight;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack held = player.getHeldItem(hand);
        boolean lit = isLit(worldIn, pos);
        if(held.getItem() instanceof BlockItem && ((BlockItem)held.getItem()).getBlock() instanceof BlockGhostLight && !lit) {
            BlockItem i = ((BlockItem) held.getItem());
            if(worldIn.isAirBlock(pos.up())) {
                if(!player.isCreative()) {
                    held.shrink(1);
                }
                player.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1F, 1F);
                worldIn.setBlockState(pos.up(), i.getBlock().getDefaultState());
                return ActionResultType.CONSUME;
            } else {
                return ActionResultType.FAIL;
            }
        } else if(worldIn.getTileEntity(pos) != null) {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TileEntityHandOfFate) {
                TileEntityHandOfFate tehof = (TileEntityHandOfFate) te;
                return tehof.onBlockActivated(state, worldIn, pos, player, hand, hit);
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ROTATION, BlockStateProperties.WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return Block.hasEnoughSolidSide(worldIn, pos.down(), Direction.UP) && (!worldIn.getBlockState(pos.up()).isSolid() || worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGhostLight);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext ctx) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(ROTATION, Orientation.fromAngle((360F + (context.getPlacementYaw() % 360)) % 360F)).with(BlockStateProperties.WATERLOGGED, context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
        return new TileEntityHandOfFate();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        Style gIS = Style.EMPTY.applyFormatting(TextFormatting.GRAY).setItalic(true);
        String tooltipPrefix = "block.whisperwoods.hand_of_fate.tooltip.";
        String recipePrefix = tooltipPrefix + "recipe.";
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent(tooltipPrefix + "recipehint").setStyle(gIS));
            for(String recipeKey : TileEntityHandOfFate.RECIPES.keySet()) {
                HOFRecipe recipe = TileEntityHandOfFate.RECIPES.get(recipeKey);
                IFormattableTextComponent t = new TranslationTextComponent(recipePrefix + recipeKey)
                .setStyle(Style.EMPTY.applyFormatting(recipe.getColor()).setBold(recipe.isBold()))
                .appendString(": ")
                .append(new TranslationTextComponent(recipe.getFirst().getTranslationKey()).setStyle(Style.EMPTY.applyFormatting(TextFormatting.WHITE).setBold(false)));
                if(I18n.hasKey(recipePrefix + recipeKey + ".hint")) {
                    t.appendString(" ").append(new TranslationTextComponent(recipePrefix + recipeKey + ".hint").setStyle(Style.EMPTY.applyFormatting(TextFormatting.GRAY).setBold(false)));
                }
                tooltip.add(t);
            }
        } else {
            tooltip.add(new TranslationTextComponent(tooltipPrefix + "shiftdown").setStyle(gIS));
        }
    }

    public enum Orientation implements IStringSerializable {
        SOUTH(Direction.SOUTH),
        SOUTHWEST(Direction.SOUTH, Direction.WEST),
        WEST(Direction.WEST),
        NORTHWEST(Direction.NORTH, Direction.WEST),
        NORTH(Direction.NORTH),
        NORTHEAST(Direction.NORTH, Direction.EAST),
        EAST(Direction.EAST),
        SOUTHEAST(Direction.SOUTH, Direction.EAST);

        Direction[] directions;

        private Orientation(Direction... directions) {
            this.directions = directions;
        }

        public static Orientation nearestYaw(float yaw) {
            return null;
        }

        public static Orientation fromAngle(double angle) {
            return byIndex(MathHelper.floor(angle / 45.0D + 0.5D));
        }

        public static Orientation byIndex(int index) {
            return Orientation.values()[MathHelper.abs(index % 8)];
        }

        @Override
        public String getString() {
            return this.name().toLowerCase();
        }

        public float getHorizontalAngle() {
            return this.ordinal() * 45F;
        }
    }

}
