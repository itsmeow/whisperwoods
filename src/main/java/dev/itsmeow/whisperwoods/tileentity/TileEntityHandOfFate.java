package dev.itsmeow.whisperwoods.tileentity;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;

import dev.itsmeow.whisperwoods.block.BlockHandOfFate;
import dev.itsmeow.whisperwoods.init.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class TileEntityHandOfFate extends TileEntity {

    private HOFRecipe currentRecipe = null;
    private final ItemData data = new ItemData();
    public static final ImmutableBiMap<String, HOFRecipe> RECIPES = ImmutableBiMap.of(
    "hirschgeist", new HOFRecipe(Items.BONE, Items.DIAMOND, Items.SOUL_SAND),
    "wisp", new HOFRecipe(Items.GLOWSTONE_DUST, Items.BLAZE_POWDER, Items.SOUL_SAND));

    public TileEntityHandOfFate() {
        super(ModTileEntities.HAND_OF_FATE.get());
    }

    public boolean isLit() {
        Block b = this.getBlockState().getBlock();
        if(b instanceof BlockHandOfFate) {
            BlockHandOfFate block = (BlockHandOfFate) b;
            return block.isLit(this.getWorld(), this.getPos());
        }
        return false;
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack held = player.getHeldItem(hand);
        boolean lit = this.isLit();
        if(lit) {
            if(currentRecipe == null) {
                for(HOFRecipe recipe : RECIPES.values()) {
                    if(recipe.isFirst(held.getItem())) {
                        this.currentRecipe = recipe;
                        
                        break;
                    }
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        data.read(state, nbt);
        if(nbt.contains("recipe", Constants.NBT.TAG_STRING)) {
            this.currentRecipe = RECIPES.getOrDefault(nbt.getString("recipe"), null);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT c = super.write(compound);
        c.put("itemdata", new CompoundNBT());
        if(currentRecipe != null) {
            compound.putString("recipe", currentRecipe.getName());
        }
        return c;
    }

    public class ItemData {

        private final Set<String> CONTAINED = new HashSet<String>();

        public void addItem(Item item) {
            CONTAINED.add(item.getRegistryName().toString());
        }

        public boolean hasItem(Item item) {
            return CONTAINED.contains(item.getRegistryName().toString());
        }

        public void read(BlockState state, CompoundNBT nbt) {
            if(nbt.contains("items", Constants.NBT.TAG_LIST)) {
                nbt.getList("items", Constants.NBT.TAG_STRING).forEach(i -> CONTAINED.add(i.getString()));
            }
        }

        public CompoundNBT write(CompoundNBT compound) {
            ListNBT list = new ListNBT();
            CONTAINED.forEach(i -> list.add(StringNBT.valueOf(i)));
            compound.put("items", list);
            return compound;
        }
    }

    public static class HOFRecipe {

        private String name = null;
        public final ImmutableList<Item> items;

        public HOFRecipe(Item... items) {
            if(items.length < 1) {
                throw new IllegalArgumentException("HOFRecipe constructor: \"items\" must have at least one item!");
            }
            this.items = ImmutableList.<Item>copyOf(items);
        }

        public String getName() {
            if(name == null) {
                this.name = TileEntityHandOfFate.RECIPES.inverse().get(this);
            }
            return name;
        }

        public Item getFirst() {
            return items.get(0);
        }

        public Item getLast() {
            return items.get(items.size() - 1);
        }

        public boolean isFirst(Item item) {
            return item == this.getFirst();
        }

        public boolean isLast(Item item) {
            return item == this.getLast();
        }

        public boolean isMiddleIngredient(Item item) {
            return items.contains(item) && !isFirst(item) && !isLast(item);
        }

        public Item getNextIngredient(Item lastIngredient) {
            int i = items.indexOf(lastIngredient) + 1;
            if(!isLast(lastIngredient) && i < items.size()) {
                return items.get(i);
            }
            return null;
        }
    }
}
