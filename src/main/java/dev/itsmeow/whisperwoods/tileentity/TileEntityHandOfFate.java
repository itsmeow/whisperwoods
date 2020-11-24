package dev.itsmeow.whisperwoods.tileentity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;

import dev.itsmeow.whisperwoods.block.BlockGhostLight;
import dev.itsmeow.whisperwoods.block.BlockHandOfFate;
import dev.itsmeow.whisperwoods.entity.EntityWisp;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModTileEntities;
import dev.itsmeow.whisperwoods.network.HOFEffectPacket;
import dev.itsmeow.whisperwoods.network.HOFEffectPacket.HOFEffectType;
import dev.itsmeow.whisperwoods.network.WWNetwork;
import dev.itsmeow.whisperwoods.util.WWServerTaskQueue;
import dev.itsmeow.whisperwoods.util.WispColors;
import dev.itsmeow.whisperwoods.util.WispColors.WispColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityHandOfFate extends TileEntity implements ITickableTileEntity {

    public static final ImmutableBiMap<String, HOFRecipe> RECIPES = ImmutableBiMap.of(
    "hirschgeist", new HOFRecipe(TextFormatting.AQUA, true, Items.BONE, Items.DIAMOND, Items.SOUL_SAND),
    "wisp", new HOFRecipe(TextFormatting.GOLD, false, Items.BLAZE_POWDER, Items.GLOWSTONE_DUST, Items.SOUL_SAND));
    private final CurrentRecipeContainer recipeContainer = new CurrentRecipeContainer();
    private Item toDisplay = null;
    private boolean displayDirty = true;
    @OnlyIn(Dist.CLIENT)
    public float lastAnimationY = 0F;

    public TileEntityHandOfFate() {
        super(ModTileEntities.HAND_OF_FATE.get());
    }

    public Item getDisplayItem() {
        if(displayDirty) {
            this.toDisplay = this.getRecipeContainer().getDisplayItem();
            this.displayDirty = false;
        }
        return this.toDisplay;
    }

    public void notifyUpdate() {
        if(world != null && pos != null) {
            world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
        }
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

    protected void playSound(SoundEvent sound, float vol, float pitch) {
        this.world.playSound(null, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), sound, SoundCategory.BLOCKS, vol, pitch);
    }

    protected void sendToTrackers(HOFEffectPacket pkt) {
        WWNetwork.HANDLER.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), pkt);
    }

    @Override
    public void tick() {
        if(this.world != null && this.pos != null && this.world.getGameTime() % 5 == 0 && this.getBlockState() != null && !world.isRemote) {
            BlockState state = this.getBlockState();
            List<ItemEntity> items = this.world.getEntitiesWithinAABB(ItemEntity.class, state.getCollisionShape(world, pos).getBoundingBox().offset(pos).grow(0.25D));
            for(ItemEntity item : items) {
                if(item.isAlive()) {
                    ItemStack stack = item.getItem();
                    if(stack.getCount() > 0) {
                        ActionResultType r = this.reactToItem(stack, state, world, pos);
                        if(r == ActionResultType.CONSUME) {
                            item.getItem().shrink(1);
                            this.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1F, 1F);
                            this.displayDirty = true;
                            if(item.getItem() == null || item.getItem().getCount() == 0) {
                                item.remove();
                            }
                            this.notifyUpdate();
                            break;
                        } else if(stack.getItem() instanceof BlockItem && world.isAirBlock(this.pos.up())) {
                            BlockItem i = (BlockItem) stack.getItem();
                            if(i.getBlock() instanceof BlockGhostLight) {
                                item.getItem().shrink(1);
                                this.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1F, 1F);
                                world.setBlockState(pos.up(), i.getBlock().getDefaultState());
                            }
                            if(item.getItem() == null || item.getItem().getCount() == 0) {
                                item.remove();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ActionResultType r = this.reactToItem(player.getHeldItem(hand), state, worldIn, pos);
        if(r == ActionResultType.CONSUME) {
            if(!player.isCreative()) {
                player.getHeldItem(hand).shrink(1);
            }
            this.playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 1F, 1F);
            this.displayDirty = true;
        }
        return r;
    }

    public ActionResultType reactToItem(ItemStack stack, BlockState state, World worldIn, BlockPos pos) {
        boolean lit = this.isLit() || this.hasBlaze();
        if(lit || stack.getItem() == Items.BLAZE_POWDER) {
            if(!this.getRecipeContainer().hasRecipe()) {
                for(HOFRecipe recipe : RECIPES.values()) {
                    if(recipe.isFirst(stack.getItem())) {
                        this.getRecipeContainer().setRecipe(recipe);
                        if(this.getRecipeContainer().checkedAdd(stack.getItem())) {
                            return ActionResultType.CONSUME;
                        }
                    }
                }
            } else if(this.getRecipeContainer().checkedAdd(stack.getItem())) {
                if(this.getRecipeContainer().isRecipeComplete()) {
                    this.onRecipeComplete(this.getRecipeContainer().getCurrentRecipe(), state, worldIn, pos);
                }
                return ActionResultType.CONSUME;
            }
        }
        return ActionResultType.PASS;
    }

    public void onRecipeComplete(HOFRecipe recipe, BlockState state, World worldIn, BlockPos pos) {
        if(!world.isRemote && worldIn instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) worldIn;
            switch(recipe.getName()) {
            case "hirschgeist":
                HOFEffectPacket hgpk = new HOFEffectPacket(HOFEffectType.HIRSCHGEIST, new Vector3f(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F), WispColors.BLUE.getColor());
                this.sendToTrackers(hgpk);
                this.playSound(SoundEvents.ENTITY_EVOKER_CAST_SPELL, 1F, 1F);
                this.playSound(SoundEvents.BLOCK_BELL_RESONATE, 1F, 1F);
                WWServerTaskQueue.schedule(50, () -> {
                    ModEntities.HIRSCHGEIST.entityType.spawn((ServerWorld) worldIn, null, null, pos.up(), SpawnReason.EVENT, false, false);
                });
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
                double d0 = 1.0D + VoxelShapes.getAllowedOffset(Direction.Axis.Y, wisp.getBoundingBox(), world.func_234867_d_(null, new AxisAlignedBB(pos), e -> true), -1.0D);
                wisp.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY() + d0, (double) pos.getZ() + 0.5D, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                wisp.rotationYawHead = wisp.rotationYaw;
                wisp.renderYawOffset = wisp.rotationYaw;
                wisp.isHostile = wisp.getRNG().nextInt(EntityWisp.HOSTILE_CHANCE) == 0;
                wisp.getDataManager().set(EntityWisp.COLOR_VARIANT, wColor.ordinal() + 1);
                if (wisp != null && !ForgeEventFactory.doSpecialSpawn(wisp, world, pos.getX(), pos.getY(), pos.getZ(), null, SpawnReason.SPAWN_EGG)) {
                    world.func_242417_l(wisp);
                }

                int color = wisp.getWispColor().getColor();
                HOFEffectPacket packet = new HOFEffectPacket(HOFEffectType.CIRCLE, new Vector3f(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F), color);
                this.sendToTrackers(packet);
                if(wisp.isHostile) {
                    HOFEffectPacket packet2 = new HOFEffectPacket(HOFEffectType.CIRCLE, new Vector3f(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F), 0xFF0000);
                    this.sendToTrackers(packet2);

                }
                this.playSound(wisp.isHostile ? SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE : SoundEvents.ENTITY_EVOKER_CAST_SPELL, 1F, 1F);
                break;
            default:
                break;
            }
            this.notifyUpdate();
        }
        this.getRecipeContainer().setRecipe(null);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.getRecipeContainer().read(state, nbt);
        this.displayDirty = true;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT c = super.write(compound);
        this.getRecipeContainer().write(compound);
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

    public void dropItems(World worldIn, BlockPos pos) {
        if(this.getRecipeContainer().hasRecipe() && this.getRecipeContainer().data != null) {
            this.getRecipeContainer().data.getItemData().forEach((i, v) -> {
                if(v) {
                    Item toDrop = ForgeRegistries.ITEMS.getValue(new ResourceLocation(i));
                    if(toDrop != null) {
                        InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(toDrop));
                    }
                }
            });
        }
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
            } else {
                this.setRecipe(null);
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
            } else {
                data.keySet().forEach((i) -> data.put(i, false));
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

        private TextFormatting color;
        private boolean bold;
        private String name = null;
        public final ImmutableList<Item> items;

        public HOFRecipe(TextFormatting color, boolean bold, Item... items) {
            if(items.length < 1) {
                throw new IllegalArgumentException("HOFRecipe constructor: \"items\" must have at least one item!");
            }
            this.color = color;
            this.bold = bold;
            this.items = ImmutableList.<Item>copyOf(items);
        }

        public TextFormatting getColor() {
            return color;
        }

        public boolean isBold() {
            return bold;
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
