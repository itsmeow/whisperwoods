package dev.itsmeow.whisperwoods.block;

import dev.itsmeow.whisperwoods.blockentity.HandOfFateBlockEntity;
import dev.itsmeow.whisperwoods.blockentity.HandOfFateBlockEntity.HOFRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class HandOfFateBlock extends Block implements EntityBlock {

    public static final EnumProperty<Orientation> ROTATION = EnumProperty.create("rotation", Orientation.class);
    private static final VoxelShape SHAPE;
    static {
        double d = 0.0625D * 3;
        SHAPE = Shapes.box(d, 0.0D, d, 1D - d, 1.55D, 1D - d);
    }

    public HandOfFateBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.getStateDefinition().any().setValue(ROTATION, Orientation.NORTH).setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public boolean isLit(Level world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this && world.getBlockState(pos.above()).getBlock() instanceof GhostLightBlock;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!state.is(newState.getBlock())) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if(te instanceof HandOfFateBlockEntity) {
                ((HandOfFateBlockEntity)te).dropItems(worldIn, pos);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        boolean lit = isLit(worldIn, pos);
        if(held.getItem() instanceof BlockItem && ((BlockItem)held.getItem()).getBlock() instanceof GhostLightBlock && !lit) {
            BlockItem i = ((BlockItem) held.getItem());
            if(worldIn.isEmptyBlock(pos.above())) {
                if(!player.isCreative()) {
                    held.shrink(1);
                }
                worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1F, 1F);
                worldIn.setBlockAndUpdate(pos.above(), i.getBlock().defaultBlockState());
                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.FAIL;
            }
        } else if(worldIn.getBlockEntity(pos) != null) {
            BlockEntity te = worldIn.getBlockEntity(pos);
            if(te instanceof HandOfFateBlockEntity) {
                HandOfFateBlockEntity tehof = (HandOfFateBlockEntity) te;
                return tehof.onBlockActivated(state, worldIn, pos, player, hand, hit);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION, BlockStateProperties.WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return Block.canSupportCenter(worldIn, pos.below(), Direction.UP) && (!worldIn.getBlockState(pos.above()).canOcclude() || worldIn.getBlockState(pos.above()).getBlock() instanceof GhostLightBlock);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(ROTATION, Orientation.fromAngle((360F + (context.getRotation() % 360)) % 360F)).setValue(BlockStateProperties.WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new HandOfFateBlockEntity();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        Style gIS = Style.EMPTY.applyFormat(ChatFormatting.GRAY).withItalic(true);
        String tooltipPrefix = "block.whisperwoods.hand_of_fate.tooltip.";
        String recipePrefix = tooltipPrefix + "recipe.";
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslatableComponent(tooltipPrefix + "recipehint").setStyle(gIS));
            for (String recipeKey : HandOfFateBlockEntity.RECIPES.keySet()) {
                HOFRecipe recipe = HandOfFateBlockEntity.RECIPES.get(recipeKey);
                tooltip.add(
                    new TranslatableComponent("block.whisperwoods.hand_of_fate.tooltip.recipe_format" + (I18n.exists(recipePrefix + recipeKey + ".hint") ? "_hint" : ""),
                        new TranslatableComponent(recipePrefix + recipeKey).setStyle(Style.EMPTY.applyFormat(recipe.getColor()).withBold(recipe.isBold())),
                        new TranslatableComponent(recipe.getFirst().getDescriptionId()).withStyle(ChatFormatting.WHITE),
                        new TranslatableComponent(recipePrefix + recipeKey + ".hint").withStyle(ChatFormatting.GRAY)
                    ).withStyle(ChatFormatting.GRAY)
                );
            }
        } else {
            tooltip.add(new TranslatableComponent(tooltipPrefix + "shiftdown").setStyle(gIS));
        }
    }

    public enum Orientation implements StringRepresentable {
        SOUTH(Direction.SOUTH),
        SOUTHWEST(Direction.SOUTH, Direction.WEST),
        WEST(Direction.WEST),
        NORTHWEST(Direction.NORTH, Direction.WEST),
        NORTH(Direction.NORTH),
        NORTHEAST(Direction.NORTH, Direction.EAST),
        EAST(Direction.EAST),
        SOUTHEAST(Direction.SOUTH, Direction.EAST);

        Direction[] directions;

        Orientation(Direction... directions) {
            this.directions = directions;
        }

        public static Orientation nearestYaw(float yaw) {
            return null;
        }

        public static Orientation fromAngle(double angle) {
            return byIndex(Mth.floor(angle / 45.0D + 0.5D));
        }

        public static Orientation byIndex(int index) {
            return Orientation.values()[Mth.abs(index % 8)];
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }

        public float getHorizontalAngle() {
            return this.ordinal() * 45F;
        }
    }

}
