package dev.itsmeow.whisperwoods.tileentity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;

import dev.itsmeow.whisperwoods.block.BlockGhostLight;
import dev.itsmeow.whisperwoods.block.BlockHandOfFate;
import dev.itsmeow.whisperwoods.entity.EntityWisp;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModTileEntities;
import dev.itsmeow.whisperwoods.network.HOFEffectPacket;
import dev.itsmeow.whisperwoods.network.WWNetwork;
import dev.itsmeow.whisperwoods.util.WispColors;
import dev.itsmeow.whisperwoods.util.WispColors.WispColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityHandOfFate extends TileEntity {

    public static final ImmutableBiMap<String, HOFRecipe> RECIPES = ImmutableBiMap.of(
    "hirschgeist", new HOFRecipe(Items.BONE, Items.DIAMOND, Items.SOUL_SAND),
    "wisp", new HOFRecipe(Items.BLAZE_POWDER, Items.GLOWSTONE_DUST, Items.SOUL_SAND));
    private final CurrentRecipeContainer recipeContainer = new CurrentRecipeContainer();

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

    public boolean hasBlaze() {
        return this.recipeContainer.hasItemInRecipe(Items.BLAZE_POWDER);
    }

    public CurrentRecipeContainer getRecipeContainer() {
        return recipeContainer;
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack held = player.getHeldItem(hand);
        boolean lit = this.isLit() || this.hasBlaze();
        if(lit || held.getItem() == Items.BLAZE_POWDER) {
            if(!this.getRecipeContainer().hasRecipe()) {
                for(HOFRecipe recipe : RECIPES.values()) {
                    if(recipe.isFirst(held.getItem())) {
                        this.getRecipeContainer().setRecipe(recipe);
                        if(this.getRecipeContainer().checkedAdd(held.getItem())) {
                            if(!player.isCreative()) {
                                held.shrink(1);
                            }
                            player.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1F, 1F);
                            return ActionResultType.CONSUME;
                        }
                    }
                }
            } else if(this.getRecipeContainer().checkedAdd(held.getItem())) {
                if(!player.isCreative()) {
                    held.shrink(1);
                }
                if(this.getRecipeContainer().isRecipeComplete()) {
                    this.onRecipeComplete(this.getRecipeContainer().getCurrentRecipe(), state, worldIn, pos, player, hand, hit);
                }
                player.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1F, 1F);
                return ActionResultType.CONSUME;
            }
        }
        return ActionResultType.PASS;
    }

    public void onRecipeComplete(HOFRecipe recipe, BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(!world.isRemote && worldIn instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) worldIn;
            switch(recipe.getName()) {
            case "hirschgeist":

                break;
            case "wisp":
                EntityWisp wisp = ModEntities.WISP.entityType.create(world);
                WispColor wColor;
                Block above = world.getBlockState(pos.up()).getBlock();
                if(above instanceof BlockGhostLight) {
                    wColor = WispColors.byColor(((BlockGhostLight) above).getColor());
                } else {
                    wColor = WispColors.values()[wisp.getRNG().nextInt(WispColors.values().length)];
                }
                wisp.setPosition((double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D);
                double d0 = bbOffset(world, pos, false, wisp.getBoundingBox());
                wisp.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY() + d0, (double) pos.getZ() + 0.5D, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                wisp.rotationYawHead = wisp.rotationYaw;
                wisp.renderYawOffset = wisp.rotationYaw;
                wisp.isHostile = wisp.getRNG().nextInt(EntityWisp.HOSTILE_CHANCE) == 0;
                wisp.getDataManager().set(EntityWisp.COLOR_VARIANT, wColor.ordinal() + 1);
                if (wisp != null && !ForgeEventFactory.doSpecialSpawn(wisp, world, pos.getX(), pos.getY(), pos.getZ(), null, SpawnReason.SPAWN_EGG)) {
                    world.func_242417_l(wisp);
                }

                int color = wisp.getWispColor().getColor();
                HOFEffectPacket packet = new HOFEffectPacket(new Vector3f(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F), color);
                Consumer<HOFEffectPacket> sender = p -> WWNetwork.HANDLER.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), p);
                sender.accept(packet);
                if(wisp.isHostile) {
                    HOFEffectPacket packet2 = new HOFEffectPacket(new Vector3f(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F), 0xFF0000);
                    sender.accept(packet2);

                }
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), wisp.isHostile ? SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE : SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.BLOCKS, 1F, 1F);
                break;
            default:
                break;
            }
        }
        this.getRecipeContainer().setRecipe(null);
    }

    private static double bbOffset(IWorldReader world, BlockPos pos, boolean down, AxisAlignedBB bb) {
        AxisAlignedBB axisalignedbb = new AxisAlignedBB(pos);
        if(down) {
            axisalignedbb = axisalignedbb.expand(0.0D, -1.0D, 0.0D);
        }
        Stream<VoxelShape> stream = world.func_234867_d_(null, axisalignedbb, e -> true);
        return 1.0D + VoxelShapes.getAllowedOffset(Direction.Axis.Y, bb, stream, down ? -2.0D : -1.0D);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT c = super.write(compound);

        return c;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        this.write(tag);
        return new SUpdateTileEntityPacket(this.pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        this.read(null, packet.getNbtCompound());
        this.world.getPendingBlockTicks().scheduleTick(this.pos, this.getBlockState().getBlock(), 100);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = new CompoundNBT();
        this.write(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    public static class CurrentRecipeContainer {
        private HOFRecipe currentRecipe = null;
        private RecipeItemData data = null;

        public void setRecipe(HOFRecipe recipe) {
            this.currentRecipe = recipe;
            if(recipe != null) {
                this.data = new RecipeItemData(recipe);
            } else {
                this.data = null;
            }
        }

        public boolean hasRecipe() {
            return this.currentRecipe != null;
        }

        public boolean hasItemInRecipe(Item item) {
            return this.hasRecipe() && data != null && data.hasItem(item);
        }

        public boolean isRecipeComplete() {
            if(this.hasRecipe() && data != null) {
                boolean anyFalse = false;
                for(Item item : this.getCurrentRecipe().items) {
                    if(!this.data.hasItem(item)) {
                        anyFalse = true;
                    }
                }
                return !anyFalse;
            }
            return false;
        }

        @Nullable
        public HOFRecipe getCurrentRecipe() {
            return currentRecipe;
        }

        public boolean canRecipeAccept(Item item) {
            if(this.hasRecipe() && data != null) {
                String next = data.getNextNonContainedItem();
                if(next != null) {
                    return next.equals(item.getRegistryName().toString());
                }
            }
            return false;
        }

        public boolean checkedAdd(Item item) {
            return canRecipeAccept(item) && data.addItem(item);
        }

        @Nullable
        public Item getDisplayItem() {
            if(this.hasRecipe() && data != null) {
                String itemName = data.getNextNonContainedItem();
                if(itemName != null) {
                    return ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
                }
            }
            return null;
        }

        public void read(BlockState state, CompoundNBT nbt) {
            if(nbt.contains("recipe", Constants.NBT.TAG_STRING)) {
                this.setRecipe(RECIPES.getOrDefault(nbt.getString("recipe"), null));
                if(this.data != null) {
                    this.data.read(state, nbt);
                }
            }
        }

        public CompoundNBT write(CompoundNBT compound) {
            if(this.hasRecipe()) {
                compound.putString("recipe", this.getCurrentRecipe().getName());
                if(this.data != null) {
                    this.data.write(compound);
                }
            }
            return compound;
        }
    }

    public static class RecipeItemData {

        private final Map<String, Boolean> data = new LinkedHashMap<String, Boolean>();

        public RecipeItemData(HOFRecipe recipe) {
            recipe.items.forEach(item -> {
                data.put(item.getRegistryName().toString(), false);
            });
        }

        public RecipeItemData(HOFRecipe recipe, Set<String> items) {
            recipe.items.forEach(item -> {
                String key = item.getRegistryName().toString();
                data.put(key, items.contains(key));
            });
        }

        @Nullable
        public String getNextNonContainedItem() {
            for(String key : data.keySet()) {
                if(!data.get(key)) {
                    return key;
                }
            }
            return null;
        }

        public boolean addItem(Item item) {
            String key = item.getRegistryName().toString();
            if(data.containsKey(key)) {
                data.put(key, true);
                return true;
            }
            return false;
        }

        public boolean hasItem(Item item) {
            return data.getOrDefault(item.getRegistryName().toString(), false);
        }

        public void read(BlockState state, CompoundNBT nbt) {
            if(nbt.contains("items", Constants.NBT.TAG_LIST)) {
                nbt.getList("items", Constants.NBT.TAG_STRING).forEach(i -> data.put(i.getString(), true));
            }
        }

        public CompoundNBT write(CompoundNBT compound) {
            ListNBT list = new ListNBT();
            data.forEach((i, v) -> {
                if(v)
                    list.add(StringNBT.valueOf(i));
            });
            compound.put("items", list);
            return compound;
        }

        public Map<String, Boolean> getItemData() {
            return data;
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
