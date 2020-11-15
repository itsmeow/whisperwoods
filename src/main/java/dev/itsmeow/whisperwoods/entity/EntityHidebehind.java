package dev.itsmeow.whisperwoods.entity;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.EntityVariant;
import dev.itsmeow.imdlib.util.BiomeDictionary.Type;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

public class EntityHidebehind extends EntityCreatureWithSelectiveTypes {

    public final DamageSource HIDEBEHIND = new EntityDamageSource("hidebehind", this).setDamageIsAbsolute().setDamageBypassesArmor();
    protected static final DataParameter<Byte> HIDING = EntityDataManager.<Byte>createKey(EntityHidebehind.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> OPEN = EntityDataManager.<Byte>createKey(EntityHidebehind.class, DataSerializers.BYTE);
    protected float nextStepDistance = 1.0F;
    protected static final DataParameter<Integer> ATTACK_SEQUENCE_TICKS = EntityDataManager.<Integer>createKey(EntityHidebehind.class, DataSerializers.VARINT);

    protected EntityHidebehind(EntityType<? extends EntityHidebehind> type, World world) {
        super(type, world);
        this.stepHeight = 2F;
    }

    public EntityHidebehind(World world) {
        this(ModEntities.HIDEBEHIND.entityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new HideFromTargetGoal(this));
        this.goalSelector.addGoal(3, new StalkTargetGoal(this, 0.5D, 35F));
    }

    public int attackSequenceTicks() {
        return this.dataManager.get(ATTACK_SEQUENCE_TICKS);
    }

    public void attackSequenceTicksDecrement() {
        this.dataManager.set(ATTACK_SEQUENCE_TICKS, attackSequenceTicks() - 1);
    }

    public void setAttackSequenceTicks(int value) {
        this.dataManager.set(ATTACK_SEQUENCE_TICKS, value);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getTrueSource() == this.getAttackTarget() && this.attackSequenceTicks() > 0) {
            this.setAttackSequenceTicks(0);
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getAttackTarget() != null && !this.getAttackTarget().isAlive()) {
            this.setAttackTarget(null);
        }
        if(this.isInWater()) {
            int i = 12;
            int j = 2;
            BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();
            BlockPos destinationBlock = null;
            for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                for(int l = 0; l < i; ++l) {
                    for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                        for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                            blockpos$mutableblockpos.setPos(this.getPosition()).move(i1, k - 1, j1);
                            if(this.world.getBlockState(blockpos$mutableblockpos).getBlock().isIn(BlockTags.LOGS)) {
                                destinationBlock = blockpos$mutableblockpos.toImmutable();
                            }
                        }
                    }
                }
            }
            boolean fixed = false;
            if(destinationBlock != null) {
                for(Direction dir : Direction.values()) {
                    if(!fixed) {
                        if(this.world.isAirBlock(destinationBlock.offset(dir)) || this.world.getBlockState(destinationBlock.offset(dir)).getBlock().isIn(BlockTags.LEAVES)) {
                            destinationBlock = destinationBlock.offset(dir);
                            fixed = true;
                        }
                    }
                }
            }
            if(fixed) {
                this.setPosition(destinationBlock.getX(), destinationBlock.getY(), destinationBlock.getZ());
            }
        }
        float atkTicks = attackSequenceTicks();
        if(this.getHiding() && Math.abs(this.getTargetViewingAngle()) >= 70) {
            this.setHiding(false);
        }
        if(this.getAttackTarget() == null) {
            this.setAttackTarget(world.getClosestEntityWithinAABB(PlayerEntity.class, EntityPredicate.DEFAULT, null, this.getPosX(), this.getPosY(), this.getPosZ(), this.getBoundingBox().grow(25)));
        }
        if(this.getAttackTarget() != null && this.getAttackTarget().getDistanceSq(this) < 5D && atkTicks == 0 && !this.getHiding()) {
            if(this.getAttackTarget() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) this.getAttackTarget();
                if(!this.world.isRemote && this.getRNG().nextInt(20) == 0) {
                    if(player.getHealth() > 11) {
                        this.attackEntityAsMob(player);
                    } else {
                        this.setAttackSequenceTicks(40);
                    }
                }
            }
        }

        if(atkTicks > 0) {
            if(!this.getOpen()) {
                this.setOpen(true);
            }
            if(this.getAttackTarget() != null) {
                LivingEntity target = this.getAttackTarget();
                target.setMotion(0, 0, 0);
                double d0 = this.getPosX() - target.getPosX();
                double d1 = this.getPosZ() - target.getPosZ();
                float angle = (float) (MathHelper.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                target.setPositionAndRotation(target.getPosX(), target.getPosY(), target.getPosZ(), angle, 0);
                double e0 = target.getPosX() - this.getPosX();
                double e1 = target.getPosZ() - this.getPosZ();
                float angle1 = (float) (MathHelper.atan2(e1, e0) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.setPositionAndRotation(this.getPosX(), this.getPosY(), this.getPosZ(), angle1, 0);
                this.rotationYaw = angle1;
                if(atkTicks == 20) {
                    target.playSound(ModSounds.HIDEBEHIND_SOUND.get(), 2F, 1F);
                }
                this.lookController.setLookPositionWithEntity(target, 360F, 360F);
            }
            this.attackSequenceTicksDecrement();
            if(atkTicks - 1 == 0) {
                this.setOpen(false);
                if(this.getAttackTarget() != null) {
                    LivingEntity target = this.getAttackTarget();
                    this.attackEntityAsMob(target);
                    this.setAttackTarget(null);
                }
            }
        }
        if(atkTicks == 0 && this.getOpen()) {
            this.setOpen(false);
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return 1.0F;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    public boolean getHiding() {
        return (this.dataManager.get(HIDING) & 1) != 0;
    }

    public void setHiding(boolean hiding) {
        byte b0 = this.dataManager.get(HIDING);
        if(hiding) {
            this.dataManager.set(HIDING, (byte) (b0 | 1));
        } else {
            this.dataManager.set(HIDING, (byte) (b0 & -2));
        }
    }

    public boolean getOpen() {
        return (this.dataManager.get(OPEN) & 1) != 0;
    }

    public void setOpen(boolean open) {
        byte b0 = this.dataManager.get(OPEN);
        if(open) {
            this.dataManager.set(OPEN, (byte) (b0 | 1));
        } else {
            this.dataManager.set(OPEN, (byte) (b0 & -2));
        }
    }

    /**
     * @return The viewing angle of the attack target (0 when looking directly at
     *         HB, 180 if opposite). If there is no target, returns -1000
     */
    public double getTargetViewingAngle() {
        LivingEntity target = this.getAttackTarget();
        if(target == null) {
            return -1000;
        }
        float targetAngle = Math.abs(this.getAttackTarget().rotationYawHead % 360);
        boolean xNeg = target.getPosX() - this.getPosX() <= 0;
        boolean zNeg = target.getPosZ() - this.getPosZ() <= 0;
        double angleAdd = xNeg ? (zNeg ? 0 : 90) : (zNeg ? 180 : 0);
        if(!(!xNeg && !zNeg)) {
            double angle = (target.getPosZ() - this.getPosZ() == 0) ? ((target.getPosX() - target.getPosX() > 0) ? 90 : -90) : Math.toDegrees(Math.atan(Math.abs(target.getPosX() - this.getPosX()) / Math.abs(target.getPosZ() - this.getPosZ())));
            double ans = Math.abs(((angleAdd) - Math.abs(angle)) + angleAdd) - targetAngle;
            if(ans > 180 || ans < -180) {
                ans = (ans > 180 ? -1 : 1) * (180 - (Math.abs(ans) - 180));
            }
            return ans;
        } else {
            double angle = (target.getPosX() - this.getPosX() == 0) ? ((target.getPosZ() - target.getPosZ() > 0) ? 0 : 360) : Math.toDegrees(Math.atan(Math.abs(target.getPosZ() - this.getPosZ()) / Math.abs(target.getPosX() - this.getPosX())));
            double ans = Math.abs(angle);
            if(ans > 180 || ans < -180) {
                ans = (ans > 180 ? -1 : 1) * (180 - (Math.abs(ans) - 180));
            }
            return 90 - ans - (targetAngle - 270) - 90;
        }
    }

    /**
     * @return The required viewing angle of the attack target to be looking at it.
     *         If there is no target, returns -1000
     */
    public double getRequiredViewingAngle() {
        LivingEntity target = this.getAttackTarget();
        if(target == null) {
            return -1000;
        }
        float targetAngle = 0;
        boolean xNeg = target.getPosX() - this.getPosX() <= 0;
        boolean zNeg = target.getPosZ() - this.getPosZ() <= 0;
        double angleAdd = xNeg ? (zNeg ? 0 : 90) : (zNeg ? 180 : 0);
        if(!(!xNeg && !zNeg)) {
            double angle = (target.getPosZ() - this.getPosZ() == 0) ? ((target.getPosX() - target.getPosX() > 0) ? 90 : -90) : Math.toDegrees(Math.atan(Math.abs(target.getPosX() - this.getPosX()) / Math.abs(target.getPosZ() - this.getPosZ())));
            double ans = Math.abs(((angleAdd) - Math.abs(angle)) + angleAdd) - targetAngle;
            if(ans > 180 || ans < -180) {
                ans = (ans > 180 ? -1 : 1) * (180 - (Math.abs(ans) - 180));
            }
            return ans;
        } else {
            double angle = (target.getPosX() - this.getPosX() == 0) ? ((target.getPosZ() - target.getPosZ() > 0) ? 0 : 360) : Math.toDegrees(Math.atan(Math.abs(target.getPosZ() - this.getPosZ()) / Math.abs(target.getPosX() - this.getPosX())));
            double ans = Math.abs(angle);
            if(ans > 180 || ans < -180) {
                ans = (ans > 180 ? -1 : 1) * (180 - (Math.abs(ans) - 180));
            }
            return 90 - ans - (targetAngle - 270) - 90;
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        float f1 = (float) this.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
        if(entity instanceof LivingEntity) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) entity).getCreatureAttribute());
            f1 += (float) EnchantmentHelper.getKnockbackModifier(this);
        }

        int i = EnchantmentHelper.getFireAspectModifier(this);
        if(i > 0) {
            entity.setFire(i * 4);
        }

        boolean flag = entity.attackEntityFrom(HIDEBEHIND, f);
        if(flag) {
            if(f1 > 0.0F && entity instanceof LivingEntity) {
                ((LivingEntity) entity).applyKnockback(f1 * 0.5F, (double) MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F))));
                this.setMotion(this.getMotion().mul(0.6D, 1.0D, 0.6D));
            }

            if(entity instanceof PlayerEntity) {
                PlayerEntity playerentity = (PlayerEntity) entity;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = playerentity.isHandActive() ? playerentity.getActiveItemStack() : ItemStack.EMPTY;
                if(!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.canDisableShield(itemstack1, playerentity, this) && itemstack1.isShield(playerentity)) {
                    float f2 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
                    if(this.rand.nextFloat() < f2) {
                        playerentity.getCooldownTracker().setCooldown(itemstack.getItem(), 100);
                        this.world.setEntityState(playerentity, (byte) 30);
                    }
                }
            }

            this.applyEnchantments(this, entity);
        }

        return flag;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void move(MoverType type, Vector3d pos) {
        if(this.noClip) {
            this.setBoundingBox(this.getBoundingBox().offset(pos));
            this.resetPositionToBB();
        } else {
            if(type == MoverType.PISTON) {
                pos = this.handlePistonMovement(pos);
                if(pos.equals(Vector3d.ZERO)) {
                    return;
                }
            }

            this.world.getProfiler().startSection("move");
            if(this.motionMultiplier.lengthSquared() > 1.0E-7D) {
                pos = pos.mul(this.motionMultiplier);
                this.motionMultiplier = Vector3d.ZERO;
                this.setMotion(Vector3d.ZERO);
            }

            pos = this.maybeBackOffFromEdge(pos, type);
            Vector3d vector3d = this.getAllowedMovement(pos);
            if(vector3d.lengthSquared() > 1.0E-7D) {
                this.setBoundingBox(this.getBoundingBox().offset(vector3d));
                this.resetPositionToBB();
            }

            this.world.getProfiler().endSection();
            this.world.getProfiler().startSection("rest");
            this.collidedHorizontally = !MathHelper.epsilonEquals(pos.x, vector3d.x) || !MathHelper.epsilonEquals(pos.z, vector3d.z);
            this.collidedVertically = pos.y != vector3d.y;
            this.onGround = this.collidedVertically && pos.y < 0.0D;
            BlockPos blockpos = this.getOnPosition();
            BlockState blockstate = this.world.getBlockState(blockpos);
            this.updateFallState(vector3d.y, this.onGround, blockstate, blockpos);
            Vector3d vector3d1 = this.getMotion();
            if(pos.x != vector3d.x) {
                this.setMotion(0.0D, vector3d1.y, vector3d1.z);
            }

            if(pos.z != vector3d.z) {
                this.setMotion(vector3d1.x, vector3d1.y, 0.0D);
            }

            Block block = blockstate.getBlock();
            if(pos.y != vector3d.y) {
                block.onLanded(this.world, this);
            }

            if(this.onGround && !this.isSteppingCarefully()) {
                block.onEntityWalk(this.world, blockpos, this);
            }

            if(this.canTriggerWalking() && !this.isPassenger()) {
                double d0 = vector3d.x;
                double d1 = vector3d.y;
                double d2 = vector3d.z;
                if(!block.isIn(BlockTags.CLIMBABLE)) {
                    d1 = 0.0D;
                }

                this.distanceWalkedModified = (float) ((double) this.distanceWalkedModified + (double) MathHelper.sqrt(horizontalMag(vector3d)) * 0.6D);
                this.distanceWalkedOnStepModified = (float) ((double) this.distanceWalkedOnStepModified + (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 0.6D);
                if(this.distanceWalkedOnStepModified > this.nextStepDistance && !blockstate.isAir(this.world, blockpos)) {
                    this.nextStepDistance = this.determineNextStepDistance();
                    if(this.isInWater()) {
                        Entity entity = this.isBeingRidden() && this.getControllingPassenger() != null ? this.getControllingPassenger() : this;
                        float f = entity == this ? 0.35F : 0.4F;
                        Vector3d vector3d2 = entity.getMotion();
                        float f1 = MathHelper.sqrt(vector3d2.x * vector3d2.x * (double) 0.2F + vector3d2.y * vector3d2.y + vector3d2.z * vector3d2.z * (double) 0.2F) * f;
                        if(f1 > 1.0F) {
                            f1 = 1.0F;
                        }

                        this.playSwimSound(f1);
                    } else {
                        this.playStepSound(blockpos, blockstate);
                    }
                }
            }

            try {
                this.doBlockCollisions();
            } catch(Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.fillCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            float f2 = this.getSpeedFactor();
            this.setMotion(this.getMotion().mul((double) f2, 1.0D, (double) f2));
            if(BlockPos.getAllInBox(this.getBoundingBox().shrink(0.001D)).noneMatch((p_233572_0_) -> {
                BlockState state = world.getBlockState(p_233572_0_);
                return state.isIn(BlockTags.FIRE) || state.isIn(Blocks.LAVA) || state.isBurning(world, p_233572_0_);
            }) && this.getFireTimer() <= 0) {
                this.forceFireTicks(-this.getFireImmuneTicks());
            }

            if(this.isInWaterRainOrBubbleColumn() && this.isBurning()) {
                this.playSound(SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                this.forceFireTicks(-this.getFireImmuneTicks());
            }

            this.world.getProfiler().endSection();
        }
    }

    private Vector3d getAllowedMovement(Vector3d vec) {
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
        boolean flag3 = this.onGround || flag1 && vec.y < 0.0D; // if on ground or transformed over y, and non-transformed vector is 0 (no
        // planned y movement?)
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
                return !world.getBlockState(new BlockPos(shape.getBoundingBox().minX, shape.getBoundingBox().minY, shape.getBoundingBox().minZ)).getBlock().isIn(BlockTags.LEAVES);
            }));
            return collideBoundingBox(vec, bb, reuseablestream);
        } else {
            return getAllowedMovement(vec, bb, world, context, stream);
        }
    }

    public static class HideFromTargetGoal extends Goal {
        private final EntityHidebehind hidebehind;
        private LivingEntity target;

        public HideFromTargetGoal(EntityHidebehind hb) {
            this.hidebehind = hb;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            this.target = this.hidebehind.getAttackTarget();
            return target != null && Math.abs(hidebehind.getTargetViewingAngle()) < 70 && hidebehind.attackSequenceTicks() == 0;

        }

        public boolean shouldContinueExecuting() {
            this.target = this.hidebehind.getAttackTarget();
            return target != null && Math.abs(hidebehind.getTargetViewingAngle()) < 70 && hidebehind.attackSequenceTicks() == 0;
        }

        public void resetTask() {
            this.target = null;
            hidebehind.setHiding(false);
        }

        public void startExecuting() {
            hidebehind.setHiding(true);
        }

        @Override
        public void tick() {
            boolean nearTree = false;
            for(Direction dir : Direction.values()) {
                if(!nearTree) {
                    if(hidebehind.world.getBlockState(hidebehind.getPosition().offset(dir)).getBlock().isIn(BlockTags.LOGS)) {
                        nearTree = true;
                    }
                    if(hidebehind.world.getBlockState(hidebehind.getPosition().up(3).offset(dir)).getBlock().isIn(BlockTags.LEAVES)) {
                        nearTree = true;
                    }
                }
            }
            if(!nearTree && hidebehind.getRNG().nextInt(5) == 0) {
                int i = 12;
                int j = 2;
                BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();
                BlockPos destinationBlock = null;
                for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for(int l = 0; l < i; ++l) {
                        for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                            for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                                blockpos$mutableblockpos.setPos(hidebehind.getPosition()).move(i1, k - 1, j1);
                                if(hidebehind.world.getBlockState(blockpos$mutableblockpos).getBlock().isIn(BlockTags.LOGS)) {
                                    destinationBlock = blockpos$mutableblockpos.toImmutable();
                                }
                            }
                        }
                    }
                }
                boolean fixed = false;
                if(destinationBlock != null) {
                    for(Direction dir : Direction.values()) {
                        if(!fixed) {
                            if(hidebehind.world.isAirBlock(destinationBlock.offset(dir)) || hidebehind.world.getBlockState(destinationBlock.offset(dir)).getBlock().isIn(BlockTags.LEAVES)) {
                                destinationBlock = destinationBlock.offset(dir);
                                fixed = true;
                            }
                        }
                    }
                }
                if(fixed) {
                    hidebehind.setPosition(destinationBlock.getX(), destinationBlock.getY(), destinationBlock.getZ());
                }
            }
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(HIDING, (byte) 0);
        this.dataManager.register(OPEN, (byte) 0);
        this.dataManager.register(ATTACK_SEQUENCE_TICKS, Integer.valueOf(0));
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        return new HidebehindGroundNavigator(this, world);
    }

    public static class HidebehindGroundNavigator extends GroundPathNavigator {

        public HidebehindGroundNavigator(MobEntity entityliving, World world) {
            super(entityliving, world);
        }

        protected PathFinder getPathFinder(int i1) {
            this.nodeProcessor = new HidebehindNodeProcessor();
            this.nodeProcessor.setCanEnterDoors(true);
            return new PathFinder(this.nodeProcessor, i1);
        }

        public static class HidebehindNodeProcessor extends WalkNodeProcessor {
            protected PathNodeType func_215744_a(IBlockReader reader, boolean b1, boolean b2, BlockPos pos, PathNodeType typeIn) {
                return typeIn == PathNodeType.LEAVES ? PathNodeType.OPEN : super.func_215744_a(reader, b1, b2, pos, typeIn);
            }
        }

    }

    public static class StalkTargetGoal extends Goal {
        private final EntityHidebehind hidebehind;
        private LivingEntity target;
        private final double speed;
        private final float maxTargetDistance;

        public StalkTargetGoal(EntityHidebehind creature, double speedIn, float targetMaxDistance) {
            this.hidebehind = creature;
            this.speed = speedIn;
            this.maxTargetDistance = targetMaxDistance;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute() {
            this.target = this.hidebehind.getAttackTarget();
            if(this.target == null) {
                return false;
            } else if(this.target.getDistanceSq(this.hidebehind) > (double) (this.maxTargetDistance * this.maxTargetDistance)) {
                return false;
            } else {
                Vector3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.hidebehind, 16, 7, new Vector3d(this.target.getPosX(), this.target.getPosY(), this.target.getPosZ()));
                if(vec3d == null) {
                    return false;
                } else if(hidebehind.world.getLightValue(hidebehind.getPosition()) > 8) {
                    return false;
                } else if(hidebehind.attackSequenceTicks() > 0 || hidebehind.getHiding()) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        public boolean shouldContinueExecuting() {
            return !hidebehind.getHiding() && !this.hidebehind.getNavigator().noPath() && this.target.isAlive() && this.target.getDistanceSq(this.hidebehind) < (double) (this.maxTargetDistance * this.maxTargetDistance) && hidebehind.attackSequenceTicks() == 0;
        }

        public void resetTask() {
            this.target = null;
        }

        public void startExecuting() {
            this.hidebehind.lookController.setLookPositionWithEntity(this.target, 1000, 1000);
            this.hidebehind.getNavigator().tryMoveToEntityLiving(this.hidebehind.getAttackTarget(), this.speed);
        }
    }

    @Override
    public EntityTypeContainer<?> getContainer() {
        return ModEntities.HIDEBEHIND;
    }

    @Override
    public String[] getTypesFor(RegistryKey<Biome> biomeKey, Biome biome, Set<Type> types, SpawnReason reason) {
        if(biomeKey == Biomes.GIANT_SPRUCE_TAIGA || biomeKey == Biomes.GIANT_SPRUCE_TAIGA_HILLS || biomeKey == Biomes.GIANT_TREE_TAIGA || biomeKey == Biomes.GIANT_TREE_TAIGA_HILLS) {
            return new String[] { "mega_taiga", "mega_taiga", "mega_taiga", "darkforest" };
        }
        if(types.contains(Type.CONIFEROUS)) {
            return new String[] { "coniferous", "coniferous", "coniferous", "coniferous", "black", "darkforest" };
        }
        if(types.contains(Type.FOREST)) {
            return new String[] { "forest", "black", "darkforest" };
        }
        return new String[] { "black", "coniferous", "darkforest", "forest", "mega_taiga" };
    }

    public static class HidebehindVariant extends EntityVariant {

        private ResourceLocation openTexture;

        public HidebehindVariant(String nameTexture) {
            super(WhisperwoodsMod.MODID, nameTexture, "hidebehind_" + nameTexture);
            this.openTexture = new ResourceLocation(WhisperwoodsMod.MODID, "textures/entity/hidebehind_" + nameTexture + "_open.png");
        }

        @Override
        public ResourceLocation getTexture(Entity entity) {
            if(entity instanceof EntityHidebehind) {
                return ((EntityHidebehind) entity).getOpen() ? openTexture : texture;
            } else {
                return texture;
            }
        }
    }

}