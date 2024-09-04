package dev.itsmeow.whisperwoods.entity;

import dev.itsmeow.imdlib.entity.EntityTypeContainer;
import dev.itsmeow.whisperwoods.entity.projectile.EntityHirschgeistFireball;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModSounds;
import dev.itsmeow.whisperwoods.util.IOverrideCollisions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public class EntityHirschgeist extends Monster implements Enemy, IOverrideCollisions<EntityHirschgeist> {

    protected int flameIndex = 0;
    public static final EntityDataAccessor<Float> DISTANCE_TO_TARGET = SynchedEntityData.defineId(EntityHirschgeist.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> DAYTIME = SynchedEntityData.defineId(EntityHirschgeist.class, EntityDataSerializers.BOOLEAN);
    private final ServerBossEvent bossInfo = (ServerBossEvent) (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(false);
    private boolean wasSummoned = false;

    public EntityHirschgeist(EntityType<? extends EntityHirschgeist> entityType, Level worldIn) {
        super(entityType, worldIn);
        this.xpReward = 150;
        this.setMaxUpStep(1.5F);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.7D, true) {

            @Override
            public boolean canUse() {
                return !EntityHirschgeist.this.isDaytime() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !EntityHirschgeist.this.isDaytime() && super.canContinueToUse();
            }

        });
        this.goalSelector.addGoal(2, new FlameAttackGoal(this));
        this.goalSelector.addGoal(3, new SummonWispsGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 20F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
        if(reason == MobSpawnType.EVENT || reason == MobSpawnType.MOB_SUMMONED) {
            this.setWasSummoned();
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new GroundPathNavigation(this, worldIn) {
            @Override
            protected PathFinder createPathFinder(int i1) {
                this.nodeEvaluator = new WalkNodeEvaluator() {
                    @Override
                    protected BlockPathTypes evaluateBlockPathType(BlockGetter reader, BlockPos pos, BlockPathTypes typeIn) {
                        return typeIn == BlockPathTypes.LEAVES || reader.getBlockState(pos).is(BlockTags.LOGS) || reader.getBlockState(pos).is(BlockTags.LEAVES) ? BlockPathTypes.OPEN : super.evaluateBlockPathType(reader, pos, typeIn);
                    }
                };
                this.nodeEvaluator.setCanPassDoors(true);
                return new PathFinder(this.nodeEvaluator, i1);
            }
        };
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DISTANCE_TO_TARGET, -1F);
        this.getEntityData().define(DAYTIME, true);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        if(compound.getBoolean("summoned")) {
            this.setWasSummoned();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("summoned", this.wasSummoned());
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public float getTargetDistance() {
        return this.getEntityData().get(DISTANCE_TO_TARGET);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (super.doHurtTarget(entityIn)) {
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).knockback(2F, this.getX() - entityIn.getX(), this.getZ() - entityIn.getZ());
                entityIn.setSecondsOnFire(2 + this.getRandom().nextInt(2));
                if(level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.position().x() + ((entityIn.position().x() - this.position().x()) / 2D), this.position().y() + ((entityIn.position().y() - this.position().y()) / 2D), this.position().z() + ((entityIn.position().z() - this.position().z()) / 2D), 500, Math.abs((entityIn.position().x() - this.position().x()) / 25D), 0, Math.abs((entityIn.position().z() - this.position().z()) / 25D), 0.1D);
                }
            }
            return true;
        }
        return false;
    }

    @Environment(EnvType.CLIENT)
    public int getFlameAnimationIndex() {
        return flameIndex;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 2 == 0) {
            if (flameIndex >= 7) {
                flameIndex = 0;
            } else {
                flameIndex++;
            }
        }
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
        if (!level().isClientSide) {
            this.getEntityData().set(DISTANCE_TO_TARGET, this.getTarget() == null ? -1F : this.getTarget().distanceTo(this));
            this.getEntityData().set(DAYTIME, this.isDaytime());
        }
    }

    public boolean isDaytime() {
        return this.level().isDay();
    }

    public boolean isDaytimeClient() {
        return this.getEntityData().get(DAYTIME);
    }

    @Override
    public boolean attackable() {
        return !this.isDaytime();
    }

    @Override
    public boolean isInvulnerable() {
        return this.isDaytime();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return (this.isDaytime() && !source.is(DamageTypes.FELL_OUT_OF_WORLD) && !source.is(DamageTypes.OUTSIDE_BORDER) && !source.isCreativePlayer()) || source.getEntity() instanceof EntityHirschgeist || source.is(DamageTypes.MAGIC) || source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!this.level().isClientSide && source.getEntity() instanceof Player && !source.isCreativePlayer()) {
            if (this.isDaytime()) {
                Player player = (Player) source.getEntity();
                player.sendSystemMessage(Component.translatable("entity.whisperwoods.hirschgeist.message.invulnerable"));
                return false;
            } else if (this.getRandom().nextInt(4) == 0) {
                this.level().playSound(null, source.getEntity(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.MASTER, 1F, 2F);
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void setTarget(LivingEntity entityIn) {
        super.setTarget(this.isDaytime() ? null : entityIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.HIRSCHGEIST_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.HIRSCHGEIST_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.HIRSCHGEIST_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.5F, 0.6F);
    }

    @Override
    public boolean isInWall() {
        return insideOpaque();
    }

    @Override
    public boolean canPassThrough(BlockState state) {
        return state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return despawn(distanceToClosestPlayer) && !this.wasSummoned();
    }

    public boolean wasSummoned() {
        return this.wasSummoned;
    }

    public void setWasSummoned() {
        this.wasSummoned = true;
    }

    @Override
    public EntityTypeContainer<EntityHirschgeist> getContainer() {
        return ModEntities.HIRSCHGEIST;
    }

    @Override
    public EntityHirschgeist getImplementation() {
        return this;
    }

    public static class FlameAttackGoal extends Goal {
        private final EntityHirschgeist attacker;

        public FlameAttackGoal(EntityHirschgeist creature) {
            this.attacker = creature;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.attacker.getTarget() != null && this.attacker.getRandom().nextInt(500) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            this.attacker.lookAt(this.attacker.getTarget(), 20, 20);
            LivingEntity target = this.attacker.getTarget();
            double d0 = target.getX() - this.attacker.getX();
            double d1 = target.getY() - this.attacker.getEyeY() - 0.1D;
            double d2 = target.getZ() - this.attacker.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            EntityHirschgeistFireball ball = new EntityHirschgeistFireball(ModEntities.PROJECTILE_HIRSCHGEIST_FIREBALL.get(), this.attacker.level(), this.attacker);
            ball.setPos(target.getX(), target.getEyeY() - 0.1D, target.getZ());
            ball.shoot(d0, d1 + d3 * 0.2D, d2, 0.5F, 2);
            this.attacker.level().playSound(null, this.attacker, SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 1F, 1F);
            this.attacker.level().addFreshEntity(ball);
        }

    }

    public static class SummonWispsGoal extends Goal {
        private final EntityHirschgeist parent;

        public SummonWispsGoal(EntityHirschgeist parent) {
            this.parent = parent;
        }

        @Override
        public boolean canUse() {
            return this.parent.getTarget() != null && this.parent.getRandom().nextInt(500) == 0 && this.parent.level().getEntitiesOfClass(EntityWisp.class, this.parent.getBoundingBox().inflate(10D), EntityWisp::isHirschgeistSummon).isEmpty();
        }

        @Override
        public void start() {
            if(parent.level() instanceof ServerLevel) {
                this.parent.level().playSound(null, this.parent, SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 1F, 1F);
                for(int i = 0; i < 3; i++) {
                    EntityWisp wisp = ModEntities.WISP.getEntityType().create((ServerLevel) parent.level(), null, null, parent.blockPosition().offset(Mth.floor(parent.getRandom().nextInt(8) - 4 + 0.5D), Mth.floor(parent.getRandom().nextInt(4) + 1 + 0.5D), Mth.floor(parent.getRandom().nextInt(8) - 4 + 0.5D)), MobSpawnType.REINFORCEMENT, false, false);
                    wisp.setHirschgeistSummon(true);
                    if (parent.getTarget() != null) {
                        wisp.setTarget(parent.getTarget());
                    }
                    parent.level().addFreshEntity(wisp);
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }
    }
}
