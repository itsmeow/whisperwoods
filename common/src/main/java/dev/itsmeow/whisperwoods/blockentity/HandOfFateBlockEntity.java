package dev.itsmeow.whisperwoods.blockentity;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import dev.itsmeow.whisperwoods.block.GhostLightBlock;
import dev.itsmeow.whisperwoods.block.HandOfFateBlock;
import dev.itsmeow.whisperwoods.entity.EntityWisp;
import dev.itsmeow.whisperwoods.init.ModBlockEntities;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.network.HOFEffectPacket;
import dev.itsmeow.whisperwoods.network.HOFEffectPacket.HOFEffectType;
import dev.itsmeow.whisperwoods.network.WWNetwork;
import dev.itsmeow.whisperwoods.util.TaskQueue;
import dev.itsmeow.whisperwoods.util.WispColors;
import dev.itsmeow.whisperwoods.util.WispColors.WispColor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import org.joml.Vector3f;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HandOfFateBlockEntity extends BlockEntity {

    public static final ImmutableBiMap<String, HOFRecipe> RECIPES = ImmutableBiMap.of(
            "hirschgeist", new HOFRecipe(ChatFormatting.AQUA, true, Items.BONE, Items.DIAMOND, Items.SOUL_SAND),
            "wisp", new HOFRecipe(ChatFormatting.GOLD, false, Items.BLAZE_POWDER, Items.GLOWSTONE_DUST, Items.SOUL_SAND));
    private final CurrentRecipeContainer recipeContainer = new CurrentRecipeContainer();
    private Item toDisplay = null;
    boolean displayDirty = true;
    public float lastAnimationY = 0F;

    public HandOfFateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HAND_OF_FATE.get(), pos, state);
    }

    public Item getDisplayItem() {
        if (displayDirty) {
            this.toDisplay = this.getRecipeContainer().getDisplayItem();
            this.displayDirty = false;
        }
        return this.toDisplay;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.sync();
    }

    public void sync() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public boolean isLit() {
        Block b = this.getBlockState().getBlock();
        if (b instanceof HandOfFateBlock block && this.hasLevel()) {
            return block.isLit(this.getLevel(), this.getBlockPos());
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
        if (this.hasLevel()) {
            this.getLevel().playSound(null, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), sound, SoundSource.BLOCKS, vol, pitch);
        }
    }

    protected void sendToTrackers(HOFEffectPacket pkt) {
        if (this.hasLevel()) {
            WWNetwork.HANDLER.sendToPlayers(((ServerChunkCache) this.getLevel().getChunkSource()).chunkMap.getPlayers(new ChunkPos(worldPosition), false), pkt);
        }
    }

    public static <T extends HandOfFateBlockEntity> void serverTick(Level level, BlockPos pos, BlockState state, T blockEntity) {
        if (level.getGameTime() % 5 == 0) {
            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, state.getBlockSupportShape(level, pos).bounds().move(pos).inflate(0.25D));
            for (ItemEntity item : items) {
                if (item.isAlive()) {
                    ItemStack stack = item.getItem();
                    if (stack.getCount() > 0) {
                        InteractionResult r = blockEntity.reactToItem(stack, state, level, pos);
                        if (r == InteractionResult.CONSUME) {
                            item.getItem().shrink(1);
                            blockEntity.playSound(SoundEvents.END_PORTAL_FRAME_FILL, 1F, 1F);
                            blockEntity.displayDirty = true;
                            if (item.getItem().getCount() == 0) {
                                item.discard();
                            }
                            blockEntity.setChanged();
                            break;
                        } else if (stack.getItem() instanceof BlockItem blockItem && level.isEmptyBlock(pos.above())) {
                            if (blockItem.getBlock() instanceof GhostLightBlock) {
                                item.getItem().shrink(1);
                                blockEntity.playSound(SoundEvents.END_PORTAL_FRAME_FILL, 1F, 1F);
                                level.setBlockAndUpdate(pos.above(), blockItem.getBlock().defaultBlockState());
                            }
                            if (item.getItem().getCount() == 0) {
                                item.discard();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public InteractionResult onBlockActivated(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult r = this.reactToItem(player.getItemInHand(hand), state, worldIn, pos);
        if (r == InteractionResult.CONSUME) {
            if (!player.isCreative()) {
                player.getItemInHand(hand).shrink(1);
            }
            this.playSound(SoundEvents.END_PORTAL_FRAME_FILL, 1F, 1F);
            this.displayDirty = true;
            this.setChanged();
        }
        return r;
    }

    public InteractionResult reactToItem(ItemStack stack, BlockState state, Level worldIn, BlockPos pos) {
        boolean lit = this.isLit() || this.hasBlaze();
        if (lit || stack.getItem() == Items.BLAZE_POWDER) {
            if (!this.getRecipeContainer().hasRecipe()) {
                for (HOFRecipe recipe : RECIPES.values()) {
                    if (recipe.isFirst(stack.getItem())) {
                        this.getRecipeContainer().setRecipe(recipe);
                        if (this.getRecipeContainer().checkedAdd(stack.getItem())) {
                            return InteractionResult.CONSUME;
                        }
                    }
                }
            } else if (this.getRecipeContainer().checkedAdd(stack.getItem())) {
                if (this.getRecipeContainer().isRecipeComplete()) {
                    this.onRecipeComplete(this.getRecipeContainer().getCurrentRecipe(), state, worldIn, pos);
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    public void onRecipeComplete(HOFRecipe recipe, BlockState state, Level worldIn, BlockPos pos) {
        if (worldIn instanceof ServerLevel world && !worldIn.isClientSide) {
            switch (recipe.getName()) {
                case "hirschgeist":
                    HOFEffectPacket hgpk = new HOFEffectPacket(HOFEffectType.HIRSCHGEIST, new Vector3f(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F), WispColors.BLUE.getColor());
                    this.sendToTrackers(hgpk);
                    this.playSound(SoundEvents.EVOKER_CAST_SPELL, 1F, 1F);
                    this.playSound(SoundEvents.BELL_RESONATE, 1F, 1F);
                    TaskQueue.QUEUE_SERVER.schedule(50, () -> ModEntities.HIRSCHGEIST.getEntityType().spawn((ServerLevel) worldIn, (CompoundTag) null, null, pos.above(), MobSpawnType.EVENT, false, false));
                    break;
                case "wisp":
                    EntityWisp wisp = ModEntities.WISP.getEntityType().create(world);
                    WispColor wColor;
                    Block above = world.getBlockState(pos.above()).getBlock();
                    if (above instanceof GhostLightBlock) {
                        wColor = WispColors.byColor(((GhostLightBlock) above).getColor());
                    } else {
                        wColor = WispColors.values()[wisp.getRandom().nextInt(WispColors.values().length)];
                    }
                    wisp.setPos((double) pos.getX() + 0.5D, (double) pos.getY() + 1D, (double) pos.getZ() + 0.5D);
                    double d0 = 1.0D + Shapes.collide(Direction.Axis.Y, wisp.getBoundingBox(), world.getCollisions(null, new AABB(pos)), -1.0D);
                    wisp.moveTo((double) pos.getX() + 0.5D, (double) pos.getY() + d0, (double) pos.getZ() + 0.5D, Mth.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
                    wisp.yHeadRot = wisp.getYRot();
                    wisp.yBodyRot = wisp.getYRot();
                    wisp.isHostile = wisp.getNewHostileChance();
                    wisp.getEntityData().set(EntityWisp.COLOR_VARIANT, wColor.ordinal() + 1);
                    worldIn.addFreshEntity(wisp);

                    int color = wisp.getWispColor().getColor();
                    HOFEffectPacket packet = new HOFEffectPacket(HOFEffectType.CIRCLE, new Vector3f(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F), color);
                    this.sendToTrackers(packet);
                    if (wisp.isHostile) {
                        HOFEffectPacket packet2 = new HOFEffectPacket(HOFEffectType.CIRCLE, new Vector3f(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F), 0xFF0000);
                        this.sendToTrackers(packet2);

                    }
                    this.playSound(wisp.isHostile ? SoundEvents.ELDER_GUARDIAN_CURSE : SoundEvents.EVOKER_CAST_SPELL, 1F, 1F);
                    break;
                default:
                    break;
            }
            this.setChanged();
        }
        this.getRecipeContainer().setRecipe(null);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.getRecipeContainer().read(compoundTag);
        this.displayDirty = true;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        this.getRecipeContainer().write(compound);
    }

    public void dropItems(Level worldIn, BlockPos pos) {
        if (worldIn != null && this.getRecipeContainer().hasRecipe() && this.getRecipeContainer().data != null) {
            this.getRecipeContainer().data.getItemData().forEach((i, v) -> {
                if (v) {
                    Item toDrop = BuiltInRegistries.ITEM.get(new ResourceLocation(i));
                    if (toDrop != null) {
                        Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(toDrop));
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
            if (recipe != null) {
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
            if (this.hasRecipe() && data != null) {
                boolean anyFalse = false;
                for (Item item : this.getCurrentRecipe().items) {
                    if (!this.data.hasItem(item)) {
                        anyFalse = true;
                    }
                }
                return !anyFalse;
            }
            return false;
        }

        public HOFRecipe getCurrentRecipe() {
            return currentRecipe;
        }

        public boolean canRecipeAccept(Item item) {
            if (this.hasRecipe() && data != null) {
                String next = data.getNextNonContainedItem();
                if (next != null) {
                    return next.equals(BuiltInRegistries.ITEM.getKey(item).toString());
                }
            }
            return false;
        }

        public boolean checkedAdd(Item item) {
            return canRecipeAccept(item) && data.addItem(item);
        }

        public Item getDisplayItem() {
            if (this.hasRecipe() && data != null) {
                String itemName = data.getNextNonContainedItem();
                if (itemName != null) {
                    return BuiltInRegistries.ITEM.get(new ResourceLocation(itemName));
                }
            }
            return null;
        }

        public void read(CompoundTag nbt) {
            if (nbt.contains("recipe") && !"empty".equals(nbt.getString("recipe"))) {
                this.setRecipe(RECIPES.getOrDefault(nbt.getString("recipe"), null));
                if (this.data != null) {
                    this.data.read(nbt);
                }
            } else {
                this.setRecipe(null);
            }
        }

        public CompoundTag write(CompoundTag compound) {
            if (this.hasRecipe()) {
                compound.putString("recipe", this.getCurrentRecipe().getName());
                if (this.data != null) {
                    this.data.write(compound);
                }
            } else {
                compound.putString("recipe", "empty");
            }
            return compound;
        }
    }

    public static class RecipeItemData {

        private final Map<String, Boolean> data = new LinkedHashMap<>();

        public RecipeItemData(HOFRecipe recipe) {
            recipe.items.forEach(item -> data.put(BuiltInRegistries.ITEM.getKey(item).toString(), false));
        }

        public RecipeItemData(HOFRecipe recipe, Set<String> items) {
            recipe.items.forEach(item -> {
                String key = BuiltInRegistries.ITEM.getKey(item).toString();
                data.put(key, items.contains(key));
            });
        }

        public String getNextNonContainedItem() {
            for (String key : data.keySet()) {
                if (!data.get(key)) {
                    return key;
                }
            }
            return null;
        }

        public boolean addItem(Item item) {
            String key = BuiltInRegistries.ITEM.getKey(item).toString();
            if (data.containsKey(key)) {
                data.put(key, true);
                return true;
            }
            return false;
        }

        public boolean hasItem(Item item) {
            return data.getOrDefault(BuiltInRegistries.ITEM.getKey(item).toString(), false);
        }

        public void read(CompoundTag nbt) {
            if (nbt.contains("items")) {
                nbt.getList("items", Tag.TAG_STRING).forEach(i -> data.put(i.getAsString(), true));
            } else {
                data.keySet().forEach((i) -> data.put(i, false));
            }
        }

        public CompoundTag write(CompoundTag compound) {
            ListTag list = new ListTag();
            data.forEach((i, v) -> {
                if (v)
                    list.add(StringTag.valueOf(i));
            });
            compound.put("items", list);
            return compound;
        }

        public Map<String, Boolean> getItemData() {
            return data;
        }
    }

    public static class HOFRecipe {

        private final ChatFormatting color;
        private final boolean bold;
        private String name = null;
        public final ImmutableList<Item> items;

        public HOFRecipe(ChatFormatting color, boolean bold, Item... items) {
            if (items.length < 1) {
                throw new IllegalArgumentException("HOFRecipe constructor: \"items\" must have at least one item!");
            }
            this.color = color;
            this.bold = bold;
            this.items = ImmutableList.copyOf(items);
        }

        public ChatFormatting getColor() {
            return color;
        }

        public boolean isBold() {
            return bold;
        }

        public String getName() {
            if (name == null) {
                this.name = HandOfFateBlockEntity.RECIPES.inverse().get(this);
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
            if (!isLast(lastIngredient) && i < items.size()) {
                return items.get(i);
            }
            return null;
        }
    }

}
