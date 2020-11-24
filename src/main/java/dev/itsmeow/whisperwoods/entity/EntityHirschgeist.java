package dev.itsmeow.whisperwoods.entity;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.IContainerEntity;
import dev.itsmeow.whisperwoods.init.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityHirschgeist extends MonsterEntity implements IMob, IContainerEntity<EntityHirschgeist> {

    protected int flameIndex = 0;
    protected float nextStepDistance = 1.0F;
    public static final DataParameter<Float> DISTANCE_TO_TARGET = EntityDataManager.createKey(EntityHirschgeist.class, DataSerializers.FLOAT);
    public static final DataParameter<Boolean> DAYTIME = EntityDataManager.createKey(EntityHirschgeist.class, DataSerializers.BOOLEAN);
    private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);

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
        //this.goalSelector.addGoal(2, new HirschgeistAIFlameAttack(this));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 20F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, false));
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        return new GroundPathNavigator(this, worldIn) {
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
                for(BlockPos blockpos : BlockPos.getAllInBoxMutable(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1))) {
                    double d0 = (double) blockpos.getX() + 0.5D - vec.x;
                    double d1 = (double) blockpos.getZ() + 0.5D - vec.z;
                    BlockState state = this.world.getBlockState(blockpos);
                    if(!(d0 * p_179692_8_ + d1 * p_179692_10_ < 0.0D) && !(state.allowsMovement(this.world, blockpos, PathType.LAND) || state.getBlock().isIn(BlockTags.LEAVES) && state.getBlock().isIn(BlockTags.LOGS))) {
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
        if(this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }

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
        if(super.attackEntityAsMob(entityIn)) {
            if(entityIn instanceof LivingEntity) {
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
        if(this.ticksExisted % 2 == 0) {
            if(flameIndex >= 7) {
                flameIndex = 0;
            } else {
                flameIndex++;
            }
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if(!world.isRemote) {
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
        return (this.isDaytime() && source != DamageSource.OUT_OF_WORLD && !source.isCreativePlayer()) || source == DamageSource.IN_FIRE || source == DamageSource.ON_FIRE || source == DamageSource.LAVA;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.isDaytime() && !this.world.isRemote && !source.isCreativePlayer()) {
            if(source.getTrueSource() instanceof PlayerEntity) {
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
        if(this.noClip) {
            return false;
        } else {
            float f1 = this.getWidth() * 0.8F;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.withSizeAtOrigin((double) f1, (double) 0.1F, (double) f1).offset(this.getPosX(), this.getPosYEye(), this.getPosZ());
            return this.world.func_241457_a_(this, axisalignedbb, (state, pos) -> {
                return !state.isIn(BlockTags.LOGS) && state.isSuffocating(this.world, pos);
            }).findAny().isPresent();
        }
    }

    @Override
    public Vector3d getAllowedMovement(Vector3d vec) {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        ISelectionContext iselectioncontext = ISelectionContext.forEntity(this);
        VoxelShape voxelshape = this.world.getWorldBorder().getShape();
        // get world shape as stream
        Stream<VoxelShape> stream = VoxelShapes.compare(voxelshape, VoxelShapes.create(axisalignedbb.shrink(1.0E-7D)), IBooleanFunction.AND) ? Stream.empty() : Stream.of(voxelshape);
        // get entity shape as stream
        Stream<VoxelShape> stream1 = this.world.func_230318_c_(this, axisalignedbb.expand(vec), (p_233561_0_) -> {
            return true;
        });
        // concatenate and make reusable
        ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(Stream.concat(stream1, stream));
        // return movement vector if it is 0, otherwise
        Vector3d vec3d = vec.lengthSquared() == 0.0D ? vec : transformMove(this, vec, axisalignedbb, this.world, iselectioncontext, reuseablestream);
        boolean flag = vec.x != vec3d.x; // if vector is transformed over x
        boolean flag1 = vec.y != vec3d.y; // if vector is transformed over y
        boolean flag2 = vec.z != vec3d.z; // if vector is transformed over z
        boolean flag3 = this.onGround || flag1 && vec.y < 0.0D; // if on ground or transformed over y, and non-transformed vector is 0 (no planned y movement?)
        if(this.stepHeight > 0.0F && flag3 && (flag || flag2)) { // if can step up and ^ and original vector transformed over either x or y
            Vector3d vec3d1 = transformMove(this, new Vector3d(vec.x, (double) this.stepHeight, vec.z), axisalignedbb, this.world, iselectioncontext, reuseablestream);
            Vector3d vec3d2 = transformMove(this, new Vector3d(0.0D, (double) this.stepHeight, 0.0D), axisalignedbb.expand(vec.x, 0.0D, vec.z), this.world, iselectioncontext, reuseablestream);
            if(vec3d2.y < (double) this.stepHeight) {
                Vector3d vec3d3 = transformMove(this, new Vector3d(vec.x, 0.0D, vec.z), axisalignedbb.offset(vec3d2), this.world, iselectioncontext, reuseablestream).add(vec3d2);
                if(horizontalMag(vec3d3) > horizontalMag(vec3d1)) {
                    vec3d1 = vec3d3;
                }
            }

            if(horizontalMag(vec3d1) > horizontalMag(vec3d)) {
                return vec3d1.add(transformMove(this, new Vector3d(0.0D, -vec3d1.y + vec.y, 0.0D), axisalignedbb.offset(vec3d1), this.world, iselectioncontext, reuseablestream));
            }
        }

        return vec3d;
    }

    public static Vector3d transformMove(@Nullable Entity entity, Vector3d vec, AxisAlignedBB bb, World world, ISelectionContext context, ReuseableStream<VoxelShape> stream) {
        boolean flag = vec.x == 0.0D;
        boolean flag1 = vec.y == 0.0D;
        boolean flag2 = vec.z == 0.0D;
        if((!flag || !flag1) && (!flag || !flag2) && (!flag1 || !flag2)) { // if moving somehow
            ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(Stream.concat(stream.createStream(), world.getCollisionShapes(entity, bb.expand(vec))).filter(shape -> {
                Block block = world.getBlockState(new BlockPos(shape.getBoundingBox().minX, shape.getBoundingBox().minY, shape.getBoundingBox().minZ)).getBlock();
                return !block.isIn(BlockTags.LEAVES) && !block.isIn(BlockTags.LOGS);
            }));
            return collideBoundingBox(vec, bb, reuseablestream);
        } else {
            return getAllowedMovement(vec, bb, world, context, stream);
        }
    }

    @Override
    public EntityTypeContainer<?> getContainer() {
        return ModEntities.HIRSCHGEIST;
    }

    @Override
    public EntityHirschgeist getImplementation() {
        return this;
    }

}
