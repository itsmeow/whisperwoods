package dev.itsmeow.whisperwoods.item;

import javax.annotation.Nullable;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHGSkullMask;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

public class ItemBlockHirschgeistSkull extends ItemBlockModeledArmor {

    public ItemBlockHirschgeistSkull(Block blockIn) {
        super(blockIn, HGArmorMaterial.get(), EquipmentSlotType.HEAD, new Item.Properties().group(WhisperwoodsMod.TAB));
        this.addToBlockToItemMap(Item.BLOCK_TO_ITEM, this);
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    @Override
    protected <A extends BipedModel<?>> A getBaseModelInstance() {
        return (A) ModelHGSkullMask.INSTANCE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected <A extends BipedModel<?>> A displays(A armorModel, EquipmentSlotType slot) {
        armorModel.bipedHead.showModel = true;
        armorModel.bipedHeadwear.showModel = true;
        armorModel.bipedBody.showModel = false;
        armorModel.bipedRightArm.showModel = false;
        armorModel.bipedLeftArm.showModel = false;
        armorModel.bipedRightLeg.showModel = false;
        armorModel.bipedLeftLeg.showModel = false;
        return armorModel;
    }

    @Override
    @Nullable
    protected BlockState getStateForPlacement(BlockItemUseContext ctx) {
        BlockState returnedState = null;
        World world = ctx.getWorld();
        BlockPos clickPos = ctx.getPos();
        for(@SuppressWarnings("unused") Direction side : ctx.getNearestLookingDirections()) {
            BlockState newState = this.getBlock().getStateForPlacement(ctx);
            if(newState == null || !newState.isValidPosition(world, clickPos))
                continue;
            returnedState = newState;
            break;
        }
        return returnedState;
    }

    @Override
    public ActionResultType tryPlace(BlockItemUseContext ctx) {
        if(!ctx.canPlace()) {
            return ActionResultType.FAIL;
        } else {
            if(ctx.getFace() == Direction.DOWN) {
                return ActionResultType.FAIL;
            }
            BlockState placementState = this.getStateForPlacement(ctx);
            if(placementState == null) {
                return ActionResultType.FAIL;
            } else if(!this.placeBlock(ctx, placementState)) {
                return ActionResultType.FAIL;
            } else {
                BlockPos blockpos = ctx.getPos();
                World world = ctx.getWorld();
                PlayerEntity player = ctx.getPlayer();
                ItemStack stack = ctx.getItem();
                BlockState newState = world.getBlockState(blockpos);
                Block block = newState.getBlock();
                if(block == placementState.getBlock()) {
                    this.onBlockPlaced(blockpos, world, player, stack, newState);
                    block.onBlockPlacedBy(world, blockpos, newState, player, stack);
                    if(player instanceof ServerPlayerEntity) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, blockpos, stack);
                    }
                }

                SoundType soundtype = newState.getSoundType(world, blockpos, ctx.getPlayer());
                world.playSound(player, blockpos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.shrink(1);
                return ActionResultType.SUCCESS;
            }
        }
    }

    public static class HGArmorMaterial implements IArmorMaterial {

        private static HGArmorMaterial INSTANCE;

        public static final HGArmorMaterial get() {
            if(INSTANCE == null) {
                INSTANCE = new HGArmorMaterial();
            }
            return INSTANCE;
        }

        private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
        private static final int[] DAMAGE_REDUCTION_AMOUNT_ARRAY = new int[] { 2, 5, 6, 2 };

        @Override
        public int getDurability(EquipmentSlotType slotIn) {
            return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * 15;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotIn) {
            return DAMAGE_REDUCTION_AMOUNT_ARRAY[slotIn.getIndex()];
        }

        @Override
        public int getEnchantability() {
            return 9;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.fromTag(Tags.Items.BONES);
        }

        @Override
        public String getName() {
            return WhisperwoodsMod.MODID + ":hirschgeist";
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
