package dev.itsmeow.whisperwoods.entity;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.util.IOverrideCollisions;
import dev.itsmeow.whisperwoods.util.StopSpinningGroundPathNavigator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.stream.Stream;

public class EntityHirschgeist extends MonsterEntity implements IMob, IOverrideCollisions<EntityHirschgeist> {

    protected int flameIndex = 0;
    public static final DataParameter<Float> DISTANCE_TO_TARGET = EntityDataManager.createKey(EntityHirschgeist.class, DataSerializers.FLOAT);
    public static final DataParameter<Boolean> DAYTIME = EntityDataManager.createKey(EntityHirschgeist.class, DataSerializers.BOOLEAN);
    private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
    private boolean wasSummoned = false;

    public EntityHirschgeist(World p_i48553_2_) {
        super(ModEntities.HIRSCHGEIST.entityType, p_i48553_2_);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.7D, true) {

            @Override
            public boolean shouldExecute() {
                return !EntityHirschgeist.this.isDaytime() && super.shouldExecute();
            }

            @Override
            public boolean shouldContinueExecuting() {
                return !EntityHirschgeist.this.isDaytime() && super.shouldContinueExecuting();
            }

        });
        this.goalSelector.addGoal(2, new FlameAttackGoal(this));
        this.goalSelector.addGoal(3, new SummonWispsGoal(this));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 20F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if(reason == SpawnReason.EVENT || reason == SpawnReason.MOB_SUMMONED) {
            this.setWasSummoned();
        }
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        return new StopSpinningGroundPathNavigator(this, worldIn) {
            @Override
            protected PathFinder getPathFinder(int i1) {
                this.nodeProcessor = new WalkNodeProcessor() {
                    @Override
                    protected PathNodeType func_215744_a(IBlockReader reader, boolean b1, boolean b2, BlockPos pos, PathNodeType typeIn) {
                        return typeIn == PathNodeType.LEAVES || reader.getBlockState(pos).getBlock().isIn(BlockTags.LOGS) || reader.getBlockState(pos).getBlock().isIn(BlockTags.LEAVES) ? PathNodeType.OPEN : super.func_215744_a(reader, b1, b2, pos, typeIn);
                    }
                };
                this.nodeProcessor.setCanEnterDoors(true);
                return new PathFinder(this.nodeProcessor, i1);
            }

            @Override
            public boolean isPositionClear(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vector3d vec, double p_179692_8_, double p_179692_10_) {
                for (BlockPos blockpos : BlockPos.getAllInBoxMutable(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1))) {
                    double d0 = (double) blockpos.getX() + 0.5D - vec.x;
                    double d1 = (double) blockpos.getZ() + 0.5D - vec.z;
                    BlockState state = this.world.getBlockState(blockpos);
                    if (!(d0 * p_179692_8_ + d1 * p_179692_10_ < 0.0D) && !(state.allowsMovement(this.world, blockpos, PathType.LAND) || state.getBlock().isIn(BlockTags.LEAVES) && state.getBlock().isIn(BlockTags.LOGS))) {
                        return false;
                    }
                }

                return true;
            }
        };
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(DISTANCE_TO_TARGET, -1F);
        this.getDataManager().register(DAYTIME, true);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        if(compound.getBoolean("summoned")) {
            this.setWasSummoned();
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("summoned", this.wasSummoned());
    }

    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public float getTargetDistance() {
        return this.getDataManager().get(DISTANCE_TO_TARGET);
    }

    @Override
    public int getArmSwingAnimationEnd() {
        return 10;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (super.attackEntityAsMob(entityIn)) {
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).applyKnockback(2F, this.getPosX() - entityIn.getPosX(), this.getPosZ() - entityIn.getPosZ());
                entityIn.setFire(2 + this.getRNG().nextInt(2));
            }
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public int getFlameAnimationIndex() {
        return flameIndex;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.ticksExisted % 2 == 0) {
            if (flameIndex >= 7) {
                flameIndex = 0;
            } else {
                flameIndex++;
            }
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if (!world.isRemote) {
            this.getDataManager().set(DISTANCE_TO_TARGET, this.getAttackTarget() == null ? -1F : this.getAttackTarget().getDistance(this));
            this.getDataManager().set(DAYTIME, this.isDaytime());
        }
    }

    public boolean isDaytime() {
        return this.world.isDaytime();
    }

    public boolean isDaytimeClient() {
        return this.getDataManager().get(DAYTIME);
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
        return (this.isDaytime() && source != DamageSource.OUT_OF_WORLD && !source.isCreativePlayer()) || source.getTrueSource() instanceof EntityHirschgeist || source == DamageSource.MAGIC || source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE || source == DamageSource.LAVA;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isDaytime() && !this.world.isRemote && !source.isCreativePlayer()) {
            if (source.getTrueSource() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) source.getTrueSource();
                player.sendMessage(new TranslationTextComponent("entity.whisperwoods.hirschgeist.message.invulnerable"), Util.DUMMY_UUID);
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void setAttackTarget(LivingEntity entityIn) {
        super.setAttackTarget(this.isDaytime() ? null : entityIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getRNG().nextBoolean() ? SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT : SoundEvents.ENTITY_RAVAGER_ROAR;
    }

    @Override
    protected float getSoundPitch() {
        return 0.3F; // Lower pitch of skeleton horse sound
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_SHEEP_STEP, 0.5F, 0.6F);
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        } else {
            float f1 = this.getWidth() * 0.8F;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.withSizeAtOrigin(f1, 0.1F, f1).offset(this.getPosX(), this.getPosYEye(), this.getPosZ());
            return this.world.func_241457_a_(this, axisalignedbb, (state, pos) -> !state.isIn(BlockTags.LOGS) && state.isSuffocating(this.world, pos)).findAny().isPresent();
        }
    }

    @Override
    public Vector3d getAllowedMovement(Vector3d vec) {
        return this.allowedMove(vec);
    }

    @Override
    public Vector3d transformMove(@Nullable Entity entity, Vector3d vec, AxisAlignedBB bb, World world, ISelectionContext context, ReuseableStream<VoxelShape> stream) {
        boolean flag = vec.x == 0.0D;
        boolean flag1 = vec.y == 0.0D;
        boolean flag2 = vec.z == 0.0D;
        if ((!flag || !flag1) && (!flag || !flag2) && (!flag1 || !flag2)) { // if moving somehow
            ReuseableStream<VoxelShape> reusableStream = new ReuseableStream<>(Stream.concat(stream.createStream(), world.getCollisionShapes(entity, bb.expand(vec))).filter(shape -> {
                Block block = world.getBlockState(new BlockPos(shape.getBoundingBox().minX, shape.getBoundingBox().minY, shape.getBoundingBox().minZ)).getBlock();
                return !block.isIn(BlockTags.LEAVES) && !block.isIn(BlockTags.LOGS);
            }));
            return collideBoundingBox(vec, bb, reusableStream);
        } else {
            return getAllowedMovement(vec, bb, world, context, stream);
        }
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return despawn(distanceToClosestPlayer) && !this.wasSummoned();
    }

    public boolean wasSummoned() {
        return this.wasSummoned;
    }

    public void setWasSummoned() {
        this.wasSummoned = true;
    }

    @Override
    public EntityTypeContainer<?> getContainer() {
        return ModEntities.HIRSCHGEIST;
    }

    @Override
    public EntityHirschgeist getImplementation() {
        return this;
    }

    public static class FlameAttackGoal extends Goal {
        private int flameTicks;
        private final EntityHirschgeist attacker;
        private final World world;

        public FlameAttackGoal(EntityHirschgeist creature) {
            this.attacker = creature;
            this.world = creature.world;
            this.setMutexFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean shouldExecute() {
            return this.attacker.getAttackTarget() != null && !this.attacker.isDaytime() && this.attacker.getAttackTarget().isAlive();
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.flameTicks <= 200 && this.attacker.getAttackTarget() != null;
        }

        @Override
        public void tick() {
            ++this.flameTicks;
            LivingEntity target = this.attacker.getAttackTarget();
            if (this.flameTicks == 10 && target != null && target.getDistanceSq(this.attacker) <= 100) {
                AreaEffectCloudEntity areaEffectCloud = new AreaEffectCloudEntity(target.world, target.getPosX(), target.getPosY(), target.getPosZ());
                areaEffectCloud.setOwner(this.attacker);
                areaEffectCloud.setRadius(3.0F);
                areaEffectCloud.setDuration(2000);
                areaEffectCloud.setParticleData(ParticleTypes.FLAME);
                areaEffectCloud.addEffect(new EffectInstance(Effects.INSTANT_DAMAGE));
                this.attacker.world.addEntity(areaEffectCloud);
            }

            if (this.world.isRemote) {
                this.doClientRenderEffects();
            }
        }

        public void doClientRenderEffects() {
            if (this.flameTicks % 2 == 0 && this.flameTicks < 10 && this.attacker.getAttackTarget() != null) {
                LivingEntity target = this.attacker.getAttackTarget();
                Vector3d vec3d = this.attacker.getLook(1.0F).normalize();
                vec3d.rotateYaw(-((float) Math.PI / 4F));
                double d0 = target.getPosX();
                double d1 = target.getPosY();
                double d2 = target.getPosZ();

                for (int i = 0; i < 8; ++i) {
                    double d3 = d0 + this.attacker.getRNG().nextGaussian() / 2.0D;
                    double d4 = d1 + this.attacker.getRNG().nextGaussian() / 2.0D;
                    double d5 = d2 + this.attacker.getRNG().nextGaussian() / 2.0D;

                    for (int j = 0; j < 6; ++j) {
                        this.attacker.world.addParticle(ParticleTypes.FLAME, d3, d4, d5, -vec3d.x * 0.07999999821186066D * j, -vec3d.y * 0.6000000238418579D, -vec3d.z * 0.07999999821186066D * j);
                    }

                    vec3d.rotateYaw(0.19634955F);
                }
            }
        }

        @Override
        public void startExecuting() {
            this.flameTicks = 0;
        }

    }

    public static class SummonWispsGoal extends Goal {
        private final EntityHirschgeist parent;

        public SummonWispsGoal(EntityHirschgeist parent) {
            this.parent = parent;
        }

        @Override
        public boolean shouldExecute() {
            return this.parent.getAttackTarget() != null && this.parent.getRNG().nextInt(500) == 0 && this.parent.world.getEntitiesWithinAABB(EntityWisp.class, this.parent.getBoundingBox().grow(10D), wisp -> wisp.isHirschgeistSummon()).size() == 0;
        }

        @Override
        public void startExecuting() {
            if(parent.world instanceof ServerWorld) {
                for(int i = 0; i < 3; i++) {
                    EntityWisp wisp = ModEntities.WISP.entityType.create((ServerWorld) parent.world, null, null, null, parent.getPosition().add(parent.getRNG().nextInt(8) - 4 + 0.5D, parent.getRNG().nextInt(4) + 1 + 0.5D, parent.getRNG().nextInt(8) - 4 + 0.5D), SpawnReason.REINFORCEMENT, false, false);
                    wisp.setHirschgeistSummon(true);
                    if (parent.getAttackTarget() != null) {
                        wisp.setAttackTarget(parent.getAttackTarget());
                    }
                    parent.world.addEntity(wisp);
                }
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }
    }
}
