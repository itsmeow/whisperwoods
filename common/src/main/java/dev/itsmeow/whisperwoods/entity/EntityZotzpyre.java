package dev.itsmeow.whisperwoods.entity;

import dev.itsmeow.imdlib.entity.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.BiomeTypes;
import dev.itsmeow.whisperwoods.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class EntityZotzpyre extends EntityMonsterWithTypes implements FlyingAnimal {

    private static final EntityDataAccessor<Boolean> HANGING = SynchedEntityData.defineId(EntityZotzpyre.class, EntityDataSerializers.BOOLEAN);
    protected FastFlyingMoveControl moveSwoop;
    protected FlyingMoveControl moveNormal;

    public EntityZotzpyre(EntityType<? extends EntityZotzpyre> entityType, Level worldIn) {
        super(entityType, worldIn);
        this.setNoGravity(true);
        this.moveSwoop = new FastFlyingMoveControl(this);
        this.moveNormal = new FlyingMoveControl(this, 20, false);
        this.moveControl = moveNormal;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwoopingAttackGoal<>(this));
        this.goalSelector.addGoal(1, new HangFromCeilingGoal<>(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1D));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, e -> true));
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new FlyingPathNavigation(this, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(HANGING, false);
    }

    public boolean isHanging() {
        return this.getEntityData().get(HANGING);
    }

    public void setHanging(boolean hanging) {
        this.getEntityData().set(HANGING, hanging);
    }

    @Override
    protected float getSoundVolume() {
        return 0.5F;
    }

    @Override
    public float getVoicePitch() {
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

    @SuppressWarnings("deprecation")
    public static boolean canSpawn(EntityType<EntityZotzpyre> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource rand) {
        if (pos.getY() >= world.getSeaLevel() && !BiomeTypes.getTypes(world.getBiome(pos).unwrapKey().get()).contains(BiomeTypes.JUNGLE)) {
            return false;
        } else {
            return checkAnyLightMonsterSpawnRules(type, world, reason, pos, rand);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        boolean flag = entityIn.hurt(this.level().damageSources().mobAttack(this), f);
        if (flag) {
            if(entityIn instanceof Player player) {
                int slowTicks = 0;
                if (this.level().getDifficulty() == Difficulty.EASY) {
                    slowTicks = 200; // 10s
                } else if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    slowTicks = 300; // 15s
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    slowTicks = 600; // 30s
                }
                if (slowTicks > 0)
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slowTicks, 1, false, false));
            }
            this.setLastHurtMob(entityIn);
        }
        return flag;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return entity != this.getTarget() && super.canCollideWith(entity);
    }

    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    public void travel(Vec3 vec3) {
        if(this.isHanging() || this.moveControl == this.moveSwoop) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, vec3);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.800000011920929D));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, vec3);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else {
                float f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(BlockPos.containing(this.getX(), this.getY() - 1.0D, this.getZ())).getBlock().getFriction() * 0.91F;
                }
                float g = 0.16277137F / (f * f * f);
                this.moveRelative(this.onGround() ? 0.1F * g : 0.02F, vec3);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(f));
            }
            this.calculateEntityAnimation(false);
        } else {
            super.travel(vec3);
        }
    }

    @Override
    public boolean onClimbable() {
        return !this.isHanging();
    }

    @Override
    public EntityTypeContainer<EntityZotzpyre> getContainer() {
        return ModEntities.ZOTZPYRE;
    }

    @Override
    public boolean isFlying() {
        return this.isNoGravity() && !this.isHanging();
    }

    public static class HangFromCeilingGoal<T extends Mob> extends Goal {

        protected T parentEntity;

        public HangFromCeilingGoal(T parentEntity) {
            this.parentEntity = parentEntity;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.parentEntity.getTarget() == null && this.parentEntity.level().getBlockState(this.parentEntity.blockPosition().above()).isFaceSturdy(this.parentEntity.level(), this.parentEntity.blockPosition().above(), Direction.DOWN);
        }

        @Override
        public void tick() {
            this.parentEntity.getNavigation().moveTo(this.parentEntity.getX(), this.parentEntity.blockPosition().getY(), this.parentEntity.getZ(), 1D);
        }

        @Override
        public void start() {
            if(parentEntity instanceof EntityZotzpyre) {
                ((EntityZotzpyre) this.parentEntity).setHanging(true);
            }
        }

        @Override
        public void stop() {
            ((EntityZotzpyre) this.parentEntity).setHanging(false);
        }
    }

    public static class SwoopingAttackGoal<T extends PathfinderMob> extends Goal {

        public enum MovementPhase {
            SWOOP_TO,
            HIT,
            SWOOP_AWAY
        }

        protected MovementPhase phase;
        protected T parentEntity;
        private int phaseTicks;
        private Vec3 startPos;

        private Vec3 targetPosition;

        public SwoopingAttackGoal(T parentEntity) {
            this.parentEntity = parentEntity;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.phase = MovementPhase.SWOOP_TO;
            this.phaseTicks = 0;
            this.startPos = null;
            this.targetPosition = null;
        }

        @Override
        public void stop() {
            this.phase = MovementPhase.SWOOP_TO;
            this.phaseTicks = 0;
            this.startPos = null;
            this.targetPosition = null;
            if (this.parentEntity.getMoveControl() instanceof FastFlyingMoveControl) {
                ((FastFlyingMoveControl) this.parentEntity.getMoveControl()).stop();
            }
            if(this.parentEntity instanceof EntityZotzpyre zotzpyre) {
                zotzpyre.moveControl = zotzpyre.moveNormal;
            }
        }

        @Override
        public boolean canUse() {
            return this.parentEntity.getTarget() != null && this.parentEntity.canAttack(this.parentEntity.getTarget());
        }

        @Override
        public void start() {
            if(this.parentEntity instanceof EntityZotzpyre zotzpyre) {
                zotzpyre.moveControl = zotzpyre.moveSwoop;
            }
            this.startPos = this.parentEntity.position();
            this.phaseTicks = 0;
            LivingEntity target = this.parentEntity.getTarget();
            if(this.parentEntity.distanceTo(target) < 1D) {
                this.phase = MovementPhase.HIT;
                this.startPos = null;
            }
            this.targetPosition = target.getEyePosition(1F);
        }

        private boolean isAtPosition() {
            return this.targetPosition != null ? this.parentEntity.position().distanceTo(this.targetPosition) < 1.5D : false;
        }

        @Override
        public void tick() {
            LivingEntity target = this.parentEntity.getTarget();
            if(this.phase == MovementPhase.SWOOP_TO) {
                this.targetPosition = target.getEyePosition(1F);
                if(this.targetPosition != null && !this.isAtPosition() && this.phaseTicks++ > 20F && this.parentEntity.getDeltaMovement().length() < 0.01D) {
                    this.parentEntity.getNavigation().moveTo(targetPosition.x(), targetPosition.y(), targetPosition.z(), 1D);
                } else {
                    this.parentEntity.getNavigation().stop();
                }
                this.parentEntity.lookAt(target, 30F, 30F);
                if(this.isAtPosition()) {
                    this.phase = MovementPhase.HIT;
                    this.phaseTicks = 0;
                }
            } else if(this.phase == MovementPhase.HIT) {
                this.targetPosition = target.getEyePosition(1F);
                this.parentEntity.lookAt(target, 30F, 30F);
                if((this.parentEntity.tickCount - this.parentEntity.getLastHurtMobTimestamp() > 40 && this.parentEntity.doHurtTarget(target)) || this.phaseTicks++ > 45) {
                    this.phaseTicks = 0;
                    this.phase = MovementPhase.SWOOP_AWAY;
                    this.targetPosition = null;
                }
            } else if(this.phase == MovementPhase.SWOOP_AWAY) {
                if(this.targetPosition != null) {
                    if(this.isAtPosition() || this.phaseTicks++ > 45 || this.parentEntity.getNavigation().createPath(targetPosition.x(), targetPosition.y(), targetPosition.z(), 1) == null) {
                        this.phase = MovementPhase.SWOOP_TO;
                        this.phaseTicks = 0;
                        this.startPos = this.parentEntity.position();
                        this.targetPosition = null;
                    }
                } else {
                    if (startPos != null) {
                        Vec3 oScale = startPos.subtract(target.position()).multiply(-2D, 1D, -2D);
                        if (oScale.length() > 10D) {
                            oScale = oScale.multiply(0.5D, 1D, 0.5D);
                        }
                        if(oScale.y() > 5) {
                            oScale = oScale.subtract(0, 2.5, 0);
                        }
                        Vec3 opposite = target.position().add(oScale);
                        this.targetPosition = opposite;
                    } else {
                        Vec3 newPos = DefaultRandomPos.getPos(this.parentEntity, 10, 10);
                        this.targetPosition = newPos;
                    }
                }
            } else {
                this.phase = MovementPhase.SWOOP_TO;
                this.phaseTicks = 0;
                this.startPos = null;
                this.targetPosition = null;
            }
            if(this.targetPosition != null && !this.isAtPosition()) {
                this.parentEntity.getMoveControl().setWantedPosition(this.targetPosition.x(), this.targetPosition.y(), this.targetPosition.z(), 1D);
            }
        }
    }

    public static class FastFlyingMoveControl extends MoveControl {

        private boolean cantReach = false;

        public FastFlyingMoveControl(Mob mob) {
            super(mob);
        }

        @Override
        public void setWantedPosition(double d, double e, double f, double g) {
            super.setWantedPosition(d, e, f, g);
            this.cantReach = false;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                this.mob.setNoGravity(true);
                Vec3 diff = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
                Vec3 diffNorm = diff.normalize();
                if (this.canReach(diffNorm, Mth.ceil(diff.length()))) {
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(diffNorm.scale(0.1D)));
                } else {
                    this.cantReach = true;
                    this.stop();
                }

                double yawAngle = Math.atan2(diff.z(), diff.x()) * (180D / Math.PI) - 90D;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), (float) yawAngle, 35.0F));

                double pitchAngle = -Math.atan2(diff.y(), Math.sqrt(diff.x() * diff.x() + diff.z() * diff.z())) * (180D / Math.PI);
                this.mob.setXRot(this.rotlerp(this.mob.getXRot(), (float) pitchAngle, 35.0F));
            } else {
                this.mob.setNoGravity(false);
            }
        }

        public boolean cantReach() {
            return this.cantReach;
        }

        public void stop() {
            this.operation = Operation.WAIT;
        }

        private boolean canReach(Vec3 vec3, int i) {
            AABB box = this.mob.getBoundingBox();
            for (int j = 1; j < i; ++j) {
                box = box.move(vec3);
                if (this.mob.level().getBlockCollisions(this.mob, box).iterator().hasNext()) {
                    return false;
                }
            }
            return true;
        }
    }
}
