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
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class EntityWisp extends AnimalEntity implements IContainerEntity<EntityWisp> {

    public final DamageSource WISP = new EntityDamageSource("wisp", this).setDamageIsAbsolute().setDamageBypassesArmor();
    public boolean isHostile = false;
    public long lastSpawn = 0;
    private BlockPos targetPosition;
    public static final DataParameter<Integer> ATTACK_STATE = EntityDataManager.createKey(EntityWisp.class, DataSerializers.VARINT);
    public static final DataParameter<String> TARGET_ID = EntityDataManager.createKey(EntityWisp.class, DataSerializers.STRING);
    public static final DataParameter<String> TARGET_NAME = EntityDataManager.createKey(EntityWisp.class, DataSerializers.STRING);
    public static final DataParameter<Float> PASSIVE_SCALE = EntityDataManager.createKey(EntityWisp.class, DataSerializers.FLOAT);
    public static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityWisp.class, DataSerializers.VARINT);
    private static final EntityPredicate PASSIVE_SCALE_PREDICATE = new EntityPredicate().allowInvulnerable().allowFriendlyFire().setSkipAttackChecks();
    private static final EntityPredicate HOSTILE_TARGET_PREDICATE = EntityPredicate.DEFAULT.setCustomPredicate(e -> EntityPredicates.CAN_HOSTILE_AI_TARGET.test(e) && e.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() != ModItems.HIRSCHGEIST_SKULL.get());
    protected ResourceLocation targetTexture;
    private boolean shouldBeHostile = false;
    private int attackCooldown = 0;
    private boolean isHirschgeistSummon = false;

    public EntityWisp(EntityType<? extends EntityWisp> entityType, World world) {
        super(entityType, world);
    }

    public WispColor getWispColor() {
        int c = this.dataManager.get(COLOR_VARIANT);
        if (c <= WispColors.values().length && c > 0) {
            return WispColors.values()[c - 1];
        }
        return WispColors.BLUE;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ATTACK_STATE, 0);
        this.dataManager.register(TARGET_ID, "");
        this.dataManager.register(TARGET_NAME, "");
        this.dataManager.register(PASSIVE_SCALE, 1F);
        this.dataManager.register(COLOR_VARIANT, 0);
    }

    public void tick() {
        super.tick();
        if (this.isHostile && world.getDifficulty() == Difficulty.PEACEFUL) {
            this.isHostile = false;
            this.shouldBeHostile = true;
        } else if (this.shouldBeHostile && world.getDifficulty() != Difficulty.PEACEFUL) {
            this.isHostile = true;
            this.shouldBeHostile = false;
        }
        int state = this.dataManager.get(ATTACK_STATE);
        if (!this.hasSoul()) {
            this.setMotion(this.getMotion().mul(0.5D, 0.6D, 0.5D));
            this.noClip = false;
        } else {
            this.setMotion(this.getMotion().mul(1D, 0.6D, 1D));
            this.noClip = true;
            this.dataManager.set(ATTACK_STATE, state + 1);
        }
        if(this.getAttackTarget() != null && !HOSTILE_TARGET_PREDICATE.canTarget(this, this.getAttackTarget())) {
            this.setAttackTarget(null);
        }
        if (!this.world.isRemote && this.isHirschgeistSummon() && this.getAttackTarget() != null) {
            double distance = this.getDistance(this.getAttackTarget());
            if (this.attackCooldown <= 0) {
                if (distance < 10D) {
                    WWNetwork.HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new WispAttackPacket(this.getPositionVec().add(0F, this.getHeight(), 0F), this.getWispColor().getColor()));
                    this.getAttackTarget().attackEntityFrom(DamageSource.MAGIC, 1F);
                    this.attackCooldown = 40 + this.getRNG().nextInt(6);
                }
            } else {
                this.attackCooldown--;
            }
        }
        if (state == 400 && !world.isRemote && world.getServer() != null) {
            PlayerEntity soul = null;
            if (this.getAttackTarget() instanceof PlayerEntity) {
                soul = (PlayerEntity) this.getAttackTarget();
            }
            if (soul == null) {
                soul = world.getServer().getPlayerList().getPlayerByUUID(UUID.fromString(this.dataManager.get(TARGET_ID)));
            }
            if (soul == null) {
                soul = world.getServer().getPlayerList().getPlayerByUsername(this.dataManager.get(TARGET_NAME));
            }
            resetAttackState();
            if (soul != null && HOSTILE_TARGET_PREDICATE.canTarget(this, soul)) {
                soul.attackEntityFrom(WISP, 3000F);
            }
            this.targetPosition = null;
            this.setAttackTarget(null);
        }
        if (this.isPassive() && !world.isRemote) {
            if (world.getEntitiesWithinAABB(PlayerEntity.class, this.getBoundingBox().grow(10)).size() > 0) {
                PlayerEntity nearest = world.getClosestEntityWithinAABB(PlayerEntity.class, PASSIVE_SCALE_PREDICATE, null, this.getPosX(), this.getPosY(), this.getPosZ(), this.getBoundingBox().grow(10));
                if (nearest != null) {
                    this.dataManager.set(PASSIVE_SCALE, nearest.getDistance(this) / 12F);
                } else {
                    this.dataManager.set(PASSIVE_SCALE, 0.3F);
                }
            } else {
                this.dataManager.set(PASSIVE_SCALE, 1.0F);
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source.getTrueSource() == this || source == DamageSource.MAGIC || source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE || source.getTrueSource() instanceof EntityHirschgeist;
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
    protected void updateAITasks() {
        super.updateAITasks();
        if (this.getAttackTarget() != null && this.getAttackTarget().isInvulnerable()) {
            this.setAttackTarget(null);
        }
        if ((this.targetPosition != null && this.getPosition().distanceSq(this.targetPosition) < 4) || this.targetPosition == null || !this.isHostile || !this.hasSoul() || this.isHirschgeistSummon()) {
            if (this.getAttackTarget() == null && !this.isPassive()) {
                this.setAttackTarget(world.getClosestEntityWithinAABB(PlayerEntity.class, HOSTILE_TARGET_PREDICATE, null, this.getPosX(), this.getPosY(), this.getPosZ(), this.getBoundingBox().grow(25)));
            }
            if (!this.isPassive() && this.getAttackTarget() != null) {
                this.targetPosition = this.getAttackTarget().getPosition();
            } else {
                this.targetPosition = new BlockPos(this.getPosX() + (double) this.rand.nextInt(5) - (double) this.rand.nextInt(5), this.getPosY() + (double) this.rand.nextInt(4) - 0.1D, this.getPosZ() + (double) this.rand.nextInt(5) - (double) this.rand.nextInt(5));

            }
            if (this.hasSoul() && this.isHostile) {
                this.targetPosition = new BlockPos(this.getPosX() + (double) this.rand.nextInt(60) - (double) this.rand.nextInt(60), this.getPosY() + (double) this.rand.nextInt(4), this.getPosZ() + (double) this.rand.nextInt(60) - (double) this.rand.nextInt(60));
            }
        }
        if (targetPosition != null) {
            double d0 = (double) this.targetPosition.getX() + 0.5D - this.getPosX();
            double d1 = (double) this.targetPosition.getY() + 0.1D - this.getPosY();
            double d2 = (double) this.targetPosition.getZ() + 0.5D - this.getPosZ();
            Vector3d vec3d = this.getMotion();
            Vector3d vec3d1 = vec3d.add((Math.signum(d0) * 0.5D - vec3d.x) * (double) 0.1F, (Math.signum(d1) * (double) 0.7F - vec3d.y) * (double) 0.1F, (Math.signum(d2) * 0.5D - vec3d.z) * (double) 0.1F);
            this.setMotion(vec3d1);
            float f = (float) (MathHelper.atan2(vec3d1.z, vec3d1.x) * (double) (180F / (float) Math.PI)) - 90.0F;
            float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
            this.moveForward = 0.5F;
            this.rotationYaw += f1;
        }
    }

    public boolean hasSoul() {
        return this.dataManager.get(ATTACK_STATE) > 0;
    }

    protected void resetAttackState() {
        this.dataManager.set(ATTACK_STATE, 0);
        this.dataManager.set(TARGET_ID, "");
        this.dataManager.set(TARGET_NAME, "");
        targetTexture = null;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTargetTexture() {
        if (targetTexture == null) {
            UUID target = UUID.fromString(this.getDataManager().get(EntityWisp.TARGET_ID));
            String name = this.getDataManager().get(EntityWisp.TARGET_NAME);
            GameProfile profile = new GameProfile(target, name);
            profile = SkullTileEntity.updateGameProfile(profile);
            if (profile != null) {
                Map<Type, MinecraftProfileTexture> map = Minecraft.getInstance().getSkinManager().loadSkinFromCache(profile);
                ResourceLocation skin;
                if (map.containsKey(Type.SKIN)) {
                    skin = Minecraft.getInstance().getSkinManager().loadSkin(map.get(Type.SKIN), Type.SKIN);
                } else {
                    skin = DefaultPlayerSkin.getDefaultSkin(target);
                    Minecraft.getInstance().getSkinManager().loadProfileTextures(profile, null, false);
                }
                targetTexture = skin;
            }
        }
        return targetTexture;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    protected void collideWithEntity(Entity entity) {
        if (entity == this.getAttackTarget() && this.getAttackTarget() != null && entity instanceof PlayerEntity && HOSTILE_TARGET_PREDICATE.canTarget(this, (PlayerEntity) entity) && !this.hasSoul() && !this.isHirschgeistSummon()) {
            PlayerEntity player = (PlayerEntity) entity;
            this.dataManager.set(ATTACK_STATE, 1);
            this.dataManager.set(TARGET_ID, player.getGameProfile().getId().toString());
            this.dataManager.set(TARGET_NAME, player.getGameProfile().getName());
        }
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    @Override
    public boolean canDespawn(double range) {
        // always has a custom name, so override default behavior instead of super call
        return this.getContainer().despawns() && !this.hasSoul();
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("is_hostile", isHostile);
        compound.putInt("color_variant", this.dataManager.get(COLOR_VARIANT));
        compound.putBoolean("should_be_hostile", this.shouldBeHostile);
        compound.putBoolean("hirschgeist_summon", this.isHirschgeistSummon());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.isHostile = compound.getBoolean("is_hostile");
        this.dataManager.set(COLOR_VARIANT, compound.getInt("color_variant"));
        this.shouldBeHostile = compound.getBoolean("should_be_hostile");
        this.setHirschgeistSummon(compound.getBoolean("hirschgeist_summon"));
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!world.isRemote && !this.isChild()) {
            if (this.rand.nextInt(10) == 0 || this.hasSoul() || this.isHirschgeistSummon()) {
                ItemStack stack = new ItemStack(getItemForVariant(this.getDataManager().get(COLOR_VARIANT)));
                this.entityDropItem(stack, 0.5F);
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
    @Nullable
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, CompoundNBT compound) {
        boolean hostile = this.getNewHostileChance();
        int colorVariant = this.getRNG().nextInt(WispColors.values().length) + 1;

        if (livingdata instanceof WispData) {
            hostile = ((WispData) livingdata).isHostile;
            colorVariant = ((WispData) livingdata).colorVariant;
        } else {
            livingdata = new WispData(hostile, colorVariant);
        }

        this.isHostile = hostile;
        this.dataManager.set(COLOR_VARIANT, colorVariant);
        return livingdata;
    }

    public static class WispData extends AgeableData {
        public boolean isHostile;
        public int colorVariant;

        public WispData(boolean isHostile, int colorVariant) {
            super(false);
            this.isHostile = isHostile;
            this.colorVariant = colorVariant;
        }
    }

    @Override
    public AgeableEntity createChild(ServerWorld world, AgeableEntity ageable) {
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
