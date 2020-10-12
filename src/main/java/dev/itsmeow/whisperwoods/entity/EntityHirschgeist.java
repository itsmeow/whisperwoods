package dev.itsmeow.whisperwoods.entity;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import dev.itsmeow.whisperwoods.init.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityHirschgeist extends MonsterEntity {

    protected int flameIndex = 0;
    protected float nextStepDistance = 1.0F;

    public EntityHirschgeist(World p_i48553_2_) {
        super(ModEntities.HIRSCHGEIST.entityType, p_i48553_2_);
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
    }

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
                Block block = world.getBlockState(new BlockPos(shape.getBoundingBox().minX, shape.getBoundingBox().minY, shape.getBoundingBox().minZ)).getBlock();
                return !block.isIn(BlockTags.LEAVES) && !block.isIn(BlockTags.LOGS);
            }));
            return collideBoundingBox(vec, bb, reuseablestream);
        } else {
            return getAllowedMovement(vec, bb, world, context, stream);
        }
    }

}
