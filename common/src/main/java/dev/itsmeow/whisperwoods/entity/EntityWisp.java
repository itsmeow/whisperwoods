package dev.itsmeow.whisperwoods.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import dev.itsmeow.imdlib.entity.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.interfaces.IContainerEntity;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModItems;
import dev.itsmeow.whisperwoods.network.WWNetwork;
import dev.itsmeow.whisperwoods.network.WispAttackPacket;
import dev.itsmeow.whisperwoods.util.WispColors;
import dev.itsmeow.whisperwoods.util.WispColors.WispColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;

public class EntityWisp extends Animal implements IContainerEntity<EntityWisp> {

    public final DamageSource WISP = new EntityDamageSource("wisp", this).bypassMagic().bypassArmor();
    public boolean isHostile = false;
    public long lastSpawn = 0;
    private BlockPos targetPosition;
    public static final EntityDataAccessor<Integer> ATTACK_STATE = SynchedEntityData.defineId(EntityWisp.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<String> TARGET_ID = SynchedEntityData.defineId(EntityWisp.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> TARGET_NAME = SynchedEntityData.defineId(EntityWisp.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Float> PASSIVE_SCALE = SynchedEntityData.defineId(EntityWisp.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> COLOR_VARIANT = SynchedEntityData.defineId(EntityWisp.class, EntityDataSerializers.INT);
    private static final TargetingConditions PASSIVE_SCALE_PREDICATE = new TargetingConditions().allowInvulnerable().allowSameTeam().allowNonAttackable();
    private static final TargetingConditions HOSTILE_TARGET_PREDICATE = TargetingConditions.DEFAULT.selector(e -> EntitySelector.ATTACK_ALLOWED.test(e) && e.getItemBySlot(EquipmentSlot.HEAD).getItem() != ModItems.HIRSCHGEIST_SKULL.get());
    protected ResourceLocation targetTexture;
    private boolean shouldBeHostile = false;
    private int attackCooldown = 0;
    private boolean isHirschgeistSummon = false;

    public EntityWisp(EntityType<? extends EntityWisp> entityType, Level world) {
        super(entityType, world);
    }

    public WispColor getWispColor() {
        int c = this.entityData.get(COLOR_VARIANT);
        if (c <= WispColors.values().length && c > 0) {
            return WispColors.values()[c - 1];
        }
        return WispColors.BLUE;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_STATE, 0);
        this.entityData.define(TARGET_ID, "");
        this.entityData.define(TARGET_NAME, "");
        this.entityData.define(PASSIVE_SCALE, 1F);
        this.entityData.define(COLOR_VARIANT, 0);
    }

    public void tick() {
        super.tick();
        if (this.isHostile && level.getDifficulty() == Difficulty.PEACEFUL) {
            this.isHostile = false;
            this.shouldBeHostile = true;
        } else if (this.shouldBeHostile && level.getDifficulty() != Difficulty.PEACEFUL) {
            this.isHostile = true;
            this.shouldBeHostile = false;
        }
        int state = this.entityData.get(ATTACK_STATE);
        if (!this.hasSoul()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.5D, 0.6D, 0.5D));
            this.noPhysics = false;
        } else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1D, 0.6D, 1D));
            this.noPhysics = true;
            this.entityData.set(ATTACK_STATE, state + 1);
        }
        if(this.getTarget() != null && !HOSTILE_TARGET_PREDICATE.test(this, this.getTarget())) {
            this.setTarget(null);
        }
        if (!this.level.isClientSide && this.isHirschgeistSummon() && this.getTarget() != null) {
            double distance = this.distanceTo(this.getTarget());
            if (this.attackCooldown <= 0) {
                if (distance < 10D) {
                    WWNetwork.HANDLER.sendToPlayers(((ServerChunkCache)this.level.getChunkSource()).chunkMap.entityMap.get(this.getId()).seenBy, new WispAttackPacket(this.position().add(0F, this.getBbHeight(), 0F), this.getWispColor().getColor()));
                    this.getTarget().hurt(DamageSource.MAGIC, 1F);
                    this.attackCooldown = 40 + this.getRandom().nextInt(6);
                }
            } else {
                this.attackCooldown--;
            }
        }
        if (state == 400 && !level.isClientSide && level.getServer() != null) {
            Player soul = null;
            if (this.getTarget() instanceof Player) {
                soul = (Player) this.getTarget();
            }
            if (soul == null) {
                soul = level.getServer().getPlayerList().getPlayer(UUID.fromString(this.entityData.get(TARGET_ID)));
            }
            if (soul == null) {
                soul = level.getServer().getPlayerList().getPlayerByName(this.entityData.get(TARGET_NAME));
            }
            resetAttackState();
            if (soul != null && HOSTILE_TARGET_PREDICATE.test(this, soul)) {
                soul.hurt(WISP, 3000F);
            }
            this.targetPosition = null;
            this.setTarget(null);
        }
        if (this.isPassive() && !level.isClientSide) {
            if (level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(10)).size() > 0) {
                Player nearest = level.getNearestEntity(Player.class, PASSIVE_SCALE_PREDICATE, null, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(10));
                if (nearest != null) {
                    this.entityData.set(PASSIVE_SCALE, nearest.distanceTo(this) / 12F);
                } else {
                    this.entityData.set(PASSIVE_SCALE, 0.3F);
                }
            } else {
                this.entityData.set(PASSIVE_SCALE, 1.0F);
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source.getEntity() == this || source == DamageSource.MAGIC || source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE || source.getEntity() instanceof EntityHirschgeist;
    }

    public boolean isPassive() {
        return !this.isHostile && !this.isHirschgeistSummon();
    }

    public boolean isHirschgeistSummon() {
        return this.isHirschgeistSummon;
    }

    public void setHirschgeistSummon(boolean value) {
        this.isHirschgeistSummon = value;
        this.isHostile = false;
        this.shouldBeHostile = false;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getTarget() != null && this.getTarget().isInvulnerable()) {
            this.setTarget(null);
        }
        if ((this.targetPosition != null && this.blockPosition().distSqr(this.targetPosition) < 4) || this.targetPosition == null || !this.isHostile || !this.hasSoul() || this.isHirschgeistSummon()) {
            if (this.getTarget() == null && !this.isPassive()) {
                this.setTarget(level.getNearestEntity(Player.class, HOSTILE_TARGET_PREDICATE, null, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(25)));
            }
            if (!this.isPassive() && this.getTarget() != null) {
                this.targetPosition = this.getTarget().blockPosition();
            } else {
                this.targetPosition = new BlockPos(this.getX() + (double) this.random.nextInt(5) - (double) this.random.nextInt(5), this.getY() + (double) this.random.nextInt(4) - 0.1D, this.getZ() + (double) this.random.nextInt(5) - (double) this.random.nextInt(5));

            }
            if (this.hasSoul() && this.isHostile) {
                this.targetPosition = new BlockPos(this.getX() + (double) this.random.nextInt(60) - (double) this.random.nextInt(60), this.getY() + (double) this.random.nextInt(4), this.getZ() + (double) this.random.nextInt(60) - (double) this.random.nextInt(60));
            }
        }
        if (targetPosition != null) {
            double d0 = (double) this.targetPosition.getX() + 0.5D - this.getX();
            double d1 = (double) this.targetPosition.getY() + 0.1D - this.getY();
            double d2 = (double) this.targetPosition.getZ() + 0.5D - this.getZ();
            Vec3 vec3d = this.getDeltaMovement();
            Vec3 vec3d1 = vec3d.add((Math.signum(d0) * 0.5D - vec3d.x) * (double) 0.1F, (Math.signum(d1) * (double) 0.7F - vec3d.y) * (double) 0.1F, (Math.signum(d2) * 0.5D - vec3d.z) * (double) 0.1F);
            this.setDeltaMovement(vec3d1);
            float f = (float) (Mth.atan2(vec3d1.z, vec3d1.x) * (double) (180F / (float) Math.PI)) - 90.0F;
            float f1 = Mth.wrapDegrees(f - this.yRot);
            this.zza = 0.5F;
            this.yRot += f1;
        }
    }

    public boolean hasSoul() {
        return this.entityData.get(ATTACK_STATE) > 0;
    }

    protected void resetAttackState() {
        this.entityData.set(ATTACK_STATE, 0);
        this.entityData.set(TARGET_ID, "");
        this.entityData.set(TARGET_NAME, "");
        targetTexture = null;
    }

    @Environment(EnvType.CLIENT)
    public ResourceLocation getTargetTexture() {
        if (targetTexture == null) {
            UUID target = UUID.fromString(this.getEntityData().get(EntityWisp.TARGET_ID));
            String name = this.getEntityData().get(EntityWisp.TARGET_NAME);
            GameProfile profile = new GameProfile(target, name);
            profile = SkullBlockEntity.updateGameprofile(profile);
            if (profile != null) {
                Map<Type, MinecraftProfileTexture> map = Minecraft.getInstance().getSkinManager().getInsecureSkinInformation(profile);
                ResourceLocation skin;
                if (map.containsKey(Type.SKIN)) {
                    skin = Minecraft.getInstance().getSkinManager().registerTexture(map.get(Type.SKIN), Type.SKIN);
                } else {
                    skin = DefaultPlayerSkin.getDefaultSkin(target);
                    Minecraft.getInstance().getSkinManager().registerSkins(profile, null, false);
                }
                targetTexture = skin;
            }
        }
        return targetTexture;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    protected void doPush(Entity entity) {
        if (entity == this.getTarget() && this.getTarget() != null && entity instanceof Player && HOSTILE_TARGET_PREDICATE.test(this, (Player) entity) && !this.hasSoul() && !this.isHirschgeistSummon()) {
            Player player = (Player) entity;
            this.entityData.set(ATTACK_STATE, 1);
            this.entityData.set(TARGET_ID, player.getGameProfile().getId().toString());
            this.entityData.set(TARGET_NAME, player.getGameProfile().getName());
        }
    }

    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double range) {
        // always has a custom name, so override default behavior instead of super call
        return this.getContainer().despawns() && !this.hasSoul();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("is_hostile", isHostile);
        compound.putInt("color_variant", this.entityData.get(COLOR_VARIANT));
        compound.putBoolean("should_be_hostile", this.shouldBeHostile);
        compound.putBoolean("hirschgeist_summon", this.isHirschgeistSummon());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.isHostile = compound.getBoolean("is_hostile");
        this.entityData.set(COLOR_VARIANT, compound.getInt("color_variant"));
        this.shouldBeHostile = compound.getBoolean("should_be_hostile");
        this.setHirschgeistSummon(compound.getBoolean("hirschgeist_summon"));
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!level.isClientSide && !this.isBaby()) {
            if (this.random.nextInt(10) == 0 || this.hasSoul() || this.isHirschgeistSummon()) {
                ItemStack stack = new ItemStack(getItemForVariant(this.getEntityData().get(COLOR_VARIANT)));
                this.spawnAtLocation(stack, 0.5F);
            }
        }
    }

    private static Item getItemForVariant(int variant) {
        if (variant <= WispColors.values().length && variant > 0) {
            Block block = WispColors.values()[variant - 1].getGhostLight().get();
            return block.asItem();
        }
        return null;
    }

    public boolean getNewHostileChance() {
        return getContainer().getCustomConfiguration().getDouble("hostile_chance") / 100D > Math.random();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData livingdata, CompoundTag compound) {
        boolean hostile = this.getNewHostileChance();
        int colorVariant = this.getRandom().nextInt(WispColors.values().length) + 1;

        if (livingdata instanceof WispData) {
            hostile = ((WispData) livingdata).isHostile;
            colorVariant = ((WispData) livingdata).colorVariant;
        } else {
            livingdata = new WispData(hostile, colorVariant);
        }

        this.isHostile = hostile;
        this.entityData.set(COLOR_VARIANT, colorVariant);
        return livingdata;
    }

    public static class WispData extends AgableMobGroupData {
        public boolean isHostile;
        public int colorVariant;

        public WispData(boolean isHostile, int colorVariant) {
            super(false);
            this.isHostile = isHostile;
            this.colorVariant = colorVariant;
        }
    }

    @Override
    public AgableMob getBreedOffspring(ServerLevel world, AgableMob ageable) {
        return null;
    }

    @Override
    public EntityWisp getImplementation() {
        return this;
    }

    @Override
    public EntityTypeContainer<EntityWisp> getContainer() {
        return ModEntities.WISP;
    }
}
