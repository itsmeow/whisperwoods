package dev.itsmeow.whisperwoods.item;

import dev.architectury.platform.Platform;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHGSkullMask;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class ItemBlockHirschgeistSkull extends ItemBlockModeledArmor {

    public ItemBlockHirschgeistSkull(Block blockIn) {
        super(blockIn, HGArmorMaterial.get(), Type.HELMET, new Item.Properties());
        this.addToBlockToItemMap(Item.BY_BLOCK, this);
    }

    @SuppressWarnings("unchecked")
    @Environment(EnvType.CLIENT)
    @Override
    protected <A extends HumanoidModel<?>> A getBaseModelInstance() {
        if(ModelHGSkullMask.INSTANCE == null) {
            ModelHGSkullMask.INSTANCE = new ModelHGSkullMask<>(new EntityRendererProvider.Context(Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer(), Minecraft.getInstance().getBlockRenderer(), Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().font).bakeLayer(new ModelLayerLocation(new ResourceLocation(WhisperwoodsMod.MODID, "hirschgeist_skull_mask"), "main")));
        }
        return (A) ModelHGSkullMask.INSTANCE;
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected <A extends HumanoidModel<?>> A displays(A armorModel, EquipmentSlot slot) {
        armorModel.head.visible = true;
        armorModel.hat.visible = true;
        armorModel.body.visible = false;
        armorModel.rightArm.visible = false;
        armorModel.leftArm.visible = false;
        armorModel.rightLeg.visible = false;
        armorModel.leftLeg.visible = false;
        return armorModel;
    }

    @Override
    protected BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState returnedState = null;
        Level world = ctx.getLevel();
        BlockPos clickPos = ctx.getClickedPos();
        for(@SuppressWarnings("unused") Direction side : ctx.getNearestLookingDirections()) {
            BlockState newState = this.getBlock().getStateForPlacement(ctx);
            if(newState == null || !newState.canSurvive(world, clickPos))
                continue;
            returnedState = newState;
            break;
        }
        return returnedState;
    }

    @Override
    public InteractionResult tryPlace(BlockPlaceContext ctx) {
        if(!ctx.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            if(ctx.getClickedFace() == Direction.DOWN) {
                return InteractionResult.FAIL;
            }
            BlockState placementState = this.getStateForPlacement(ctx);
            if(placementState == null) {
                return InteractionResult.FAIL;
            } else if(!this.placeBlock(ctx, placementState)) {
                return InteractionResult.FAIL;
            } else {
                BlockPos blockpos = ctx.getClickedPos();
                Level world = ctx.getLevel();
                Player player = ctx.getPlayer();
                ItemStack stack = ctx.getItemInHand();
                BlockState newState = world.getBlockState(blockpos);
                Block block = newState.getBlock();
                if(block == placementState.getBlock()) {
                    this.onBlockPlaced(blockpos, world, player, stack, newState);
                    block.setPlacedBy(world, blockpos, newState, player, stack);
                    if(player instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos, stack);
                    }
                }

                SoundType soundtype = newState.getSoundType();
                world.playSound(player, blockpos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
    }

    public static class HGArmorMaterial implements ArmorMaterial {

        private static HGArmorMaterial INSTANCE;

        public static HGArmorMaterial get() {
            if(INSTANCE == null) {
                INSTANCE = new HGArmorMaterial();
            }
            return INSTANCE;
        }

        private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
        private static final int[] DAMAGE_REDUCTION_AMOUNT_ARRAY = new int[] { 2, 5, 6, 2 };

        @Override
        public int getDurabilityForType(Type type) {
            return MAX_DAMAGE_ARRAY[type.getSlot().getIndex()] * 15;
        }

        @Override
        public int getDefenseForType(Type type) {
            return DAMAGE_REDUCTION_AMOUNT_ARRAY[type.getSlot().getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 9;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_GENERIC;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(Items.BONE);
        }

        @Override
        public String getName() {
            return (Platform.isForge() ? WhisperwoodsMod.MODID + ":" : "") + "hirschgeist";
        }

        @Override
        public float getToughness() {
            return 0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0F;
        }

    }
}
