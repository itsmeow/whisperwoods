package dev.itsmeow.whisperwoods.entity;

import dev.itsmeow.imdlib.entity.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.BiomeTypes;
import dev.itsmeow.whisperwoods.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.pathfinder.TurtleNodeEvaluator;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class EntityZotzpyre extends EntityMonsterWithTypes {

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(EntityZotzpyre.class, EntityDataSerializers.BYTE);
    protected int lastAttack = 0;
    private boolean isFromZotz = false;

    public EntityZotzpyre(EntityType<? extends EntityZotzpyre> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LeapAtTargetGoal(this, 0.5F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 0, true, true, entity -> !(entity instanceof EntityZotzpyre) && !(entity instanceof AbstractHorse) && !(entity instanceof AmbientCreature) && !(entity instanceof Enemy) && entity.getMobType() != MobType.UNDEAD));
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new WallClimberNavigation(this, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIMBING, (byte) 0);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entityIn) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public void tick() {
        if (this.isPassenger()) {
            this.setNoAi(true);
            // you'd think it wouldn't be null. except it is. on turtles in water?
            // explain MINECRAFT. EXPLAIN.
            if (this.getNavigation().getNodeEvaluator() == null) {
                this.getNavigation().nodeEvaluator = new TurtleNodeEvaluator();
            }
        } else if (this.isNoAi()) {
            this.setNoAi(false);
        }
        super.tick();
        if (!this.level.isClientSide && !this.isAlive() && this.getVehicle() != null || (!this.level.isClientSide && this.getVehicle() != null && !this.getVehicle().isAlive())) {
            this.dismountZotz();
        }
        if (!this.level.isClientSide) {
            this.setBesideClimbableBlock(this.horizontalCollision);
        }
    }

    /* prevent slowdown in air */
    @Override
    public void travel(Vec3 vec) {
        boolean flag = this.getDeltaMovement().y <= 0.0D;
        double d0 = 0.08D;
        double d1 = this.getY();
        float f = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
        float f1 = 0.02F;
        float f2 = (float) EnchantmentHelper.getDepthStrider(this);
        if (f2 > 3.0F) {
            f2 = 3.0F;
        }

        if (f2 > 0.0F) {
            f += (0.54600006F - f) * f2 / 3.0F;
            f1 += (this.getSpeed() - f1) * f2 / 3.0F;
        }

        if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
            f = 0.96F;
        }

        this.moveRelative(f1, vec);
        this.move(MoverType.SELF, this.getDeltaMovement());
        Vec3 vec3d1 = this.getDeltaMovement();
        if (this.horizontalCollision && this.onClimbable()) {
            vec3d1 = new Vec3(vec3d1.x, 0.2D, vec3d1.z);
        }

        this.setDeltaMovement(vec3d1.multiply(f, 0.8F, f));
        if (!this.isNoGravity() && !this.isSprinting()) {
            Vec3 vec3d2 = this.getDeltaMovement();
            double d2;
            if (flag && Math.abs(vec3d2.y - 0.005D) >= 0.003D && Math.abs(vec3d2.y - d0 / 16.0D) < 0.003D) {
                d2 = -0.003D;
            } else {
                d2 = vec3d2.y - d0 / 16.0D;
            }

            this.setDeltaMovement(vec3d2.x, d2, vec3d2.z);
        }

        Vec3 vec3d6 = this.getDeltaMovement();
        if (this.horizontalCollision && this.isFree(vec3d6.x, vec3d6.y + (double) 0.6F - this.getY() + d1, vec3d6.z)) {
            this.setDeltaMovement(vec3d6.x, 0.3F, vec3d6.z);
        }


        this.animationSpeedOld = this.animationSpeed;
        double d5 = this.getX() - this.xo;
        double d6 = this.getZ() - this.zo;
        double d8 = this instanceof FlyingAnimal ? this.getY() - this.yo : 0.0D;
        float f8 = Mth.sqrt(d5 * d5 + d8 * d8 + d6 * d6) * 4.0F;
        if (f8 > 1.0F) {
            f8 = 1.0F;
        }

        this.animationSpeed += (f8 - this.animationSpeed) * 0.4F;
        this.animationPosition += this.animationSpeed;
    }

    @Override
    public boolean onClimbable() {
        return this.isBesideClimbableBlock();
    }

    public boolean isBesideClimbableBlock() {
        return (this.entityData.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.entityData.get(CLIMBING);
        this.entityData.set(CLIMBING, climbing ? (byte) (b0 | 1) : (byte) (b0 & -2));
    }

    @Override
    protected float getSoundVolume() {
        return 0.5F;
    }

    @Override
    protected float getVoicePitch() {
        return super.getVoicePitch() * 0.05F;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.BAT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.BAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BAT_DEATH;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide && this.getTarget() != null && this.getTarget().isAlive()) {
            if (this.getVehicle() != null && this.getVehicle() == this.getTarget()) {
                float time = 20F;
                if (!this.wasTouchingWater) {
                    time *= 2F * (Math.random() + 1F);
                } else {
                    time += Math.random() * Math.random() * 2 * ((Math.random() < 0.5) ? -1 : 1);
                }
                if (this.lastAttack + time < this.tickCount) {
                    this.doHurtTarget(this.getTarget());
                }
            } else if (this.distanceToSqr(this.getTarget()) < 3) {
                this.grabTarget(this.getTarget());
            }
        }
    }

    @Override
    public boolean rideableUnderWater() {
        return true;
    }

    public void grabTarget(Entity entity) {
        if (!level.isClientSide) {
            if (this.getVehicle() == null) {
                this.startRiding(entity, true);
                for (ServerPlayer player : this.level.getServer().getPlayerList().getPlayers()) {
                    if (player.level == this.level && player.distanceTo(this) <= 128) {
                        player.connection.send(new ClientboundSetPassengersPacket(entity));
                    }
                }
            }
        }
    }

    public void dismountZotz() {
        Entity mount = this.getVehicle();
        this.isFromZotz = true;
        this.stopRiding();
        this.isFromZotz = false;
        if (!level.isClientSide && this.level.getServer() != null) {
            for (ServerPlayer player : this.level.getServer().getPlayerList().getPlayers()) {
                if (player.level == this.level && player.distanceTo(this) <= 128) {
                    player.connection.send(new ClientboundSetPassengersPacket(mount));
                }
            }
        }
    }

    @Override
    public void stopRiding() {
        if (this.getVehicle() != null && !this.getVehicle().rideableUnderWater()) {
            super.stopRiding();
        } else if (this.getTarget() == null || isFromZotz) {
            super.stopRiding();
        }
    }

    //@Override on Forge
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public double getMyRidingOffset() {
        if (getVehicle() != null && getVehicle() instanceof Player) {
            return getVehicle().getBbHeight() - 2.25F;
        } else if (getVehicle() != null) {
            return (getVehicle().getEyeHeight() / 2) - this.getBbHeight();
        } else {
            return super.getMyRidingOffset();
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean canSpawn(EntityType<EntityZotzpyre> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
        if (pos.getY() >= world.getSeaLevel() && !BiomeTypes.getTypes(ResourceKey.create(Registry.BIOME_REGISTRY, ((ServerLevel)world).getServer().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(world.getBiome(pos)))).contains(BiomeTypes.JUNGLE)) {
            return false;
        } else {
            return checkAnyLightMonsterSpawnRules(type, world, reason, pos, rand);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (amount > 3 && this.getVehicle() != null && !this.level.isClientSide) {
                this.dismountZotz();
            }

            return super.hurt(source, amount);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);
        if (flag) {
            this.lastAttack = this.tickCount;
            if (entityIn instanceof Player) {
                Player player = (Player) entityIn;
                int slowTicks = 0;
                if (this.level.getDifficulty() == Difficulty.EASY) {
                    slowTicks = 200; // 10s
                } else if (this.level.getDifficulty() == Difficulty.NORMAL) {
                    slowTicks = 300; // 15s
                } else if (this.level.getDifficulty() == Difficulty.HARD) {
                    slowTicks = 600; // 30s
                }
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slowTicks, 1, false, false));
            }
        }
        if (!this.level.isClientSide && !entityIn.isAlive() && entityIn == this.getVehicle()) {
            this.dismountZotz();
        }
        return flag;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public EntityTypeContainer<EntityZotzpyre> getContainer() {
        return ModEntities.ZOTZPYRE;
    }

}
