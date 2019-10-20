package its_meow.whisperwoods.entity;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import its_meow.whisperwoods.init.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

public class EntityHidebehind extends MobEntity {

    public final DamageSource HIDEBEHIND = new EntityDamageSource("hidebehind", this).setDamageIsAbsolute().setDamageBypassesArmor();
    protected float nextStepDistance = 1.0F;

    protected EntityHidebehind(EntityType<? extends EntityHidebehind> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityHidebehind(World worldIn) {
        super(ModEntities.HIDEBEHIND.entityType, worldIn);
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));

    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
        float f1 = (float) this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).getValue();
        if(entityIn instanceof LivingEntity) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) entityIn).getCreatureAttribute());
            f1 += (float) EnchantmentHelper.getKnockbackModifier(this);
        }

        int i = EnchantmentHelper.getFireAspectModifier(this);
        if(i > 0) {
            entityIn.setFire(i * 4);
        }

        boolean flag = entityIn.attackEntityFrom(HIDEBEHIND, f);
        if(flag) {
            if(f1 > 0.0F && entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).knockBack(this, f1 * 0.5F, (double) MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F))));
                this.setMotion(this.getMotion().mul(0.6D, 1.0D, 0.6D));
            }

            if(entityIn instanceof PlayerEntity) {
                PlayerEntity playerentity = (PlayerEntity) entityIn;
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

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    @Override
    public boolean canDespawn(double range) {
        return ModEntities.ENTITIES.containsKey("hidebehind") ? ModEntities.ENTITIES.get("hidebehind").despawn : false;
    }

    @Override
    public void move(MoverType typeIn, Vec3d pos) {
        if(this.noClip) {
            this.setBoundingBox(this.getBoundingBox().offset(pos));
            this.resetPositionToBB();
        } else {
            if(typeIn == MoverType.PISTON) {
                pos = this.handlePistonMovement(pos);
                if(pos.equals(Vec3d.ZERO)) {
                    return;
                }
            }

            this.world.getProfiler().startSection("move");
            if(this.motionMultiplier.lengthSquared() > 1.0E-7D) {
                pos = pos.mul(this.motionMultiplier);
                this.motionMultiplier = Vec3d.ZERO;
                this.setMotion(Vec3d.ZERO);
            }

            pos = this.handleSneakMovement(pos, typeIn);
            Vec3d vec3d = this.getAllowedMovement(pos);
            if(vec3d.lengthSquared() > 1.0E-7D) {
                this.setBoundingBox(this.getBoundingBox().offset(vec3d));
                this.resetPositionToBB();
            }

            this.world.getProfiler().endSection();
            this.world.getProfiler().startSection("rest");
            this.collidedHorizontally = !MathHelper.epsilonEquals(pos.x, vec3d.x) || !MathHelper.epsilonEquals(pos.z, vec3d.z);
            this.collidedVertically = pos.y != vec3d.y;
            this.onGround = this.collidedVertically && pos.y < 0.0D;
            this.collided = this.collidedHorizontally || this.collidedVertically;
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY - (double) 0.2F);
            int k = MathHelper.floor(this.posZ);
            BlockPos blockpos = new BlockPos(i, j, k);
            BlockState blockstate = this.world.getBlockState(blockpos);
            if(blockstate.isAir(this.world, blockpos)) {
                BlockPos blockpos1 = blockpos.down();
                BlockState blockstate1 = this.world.getBlockState(blockpos1);
                if(blockstate.collisionExtendsVertically(this.world, blockpos, this)) {
                    blockstate = blockstate1;
                    blockpos = blockpos1;
                }
            }

            this.updateFallState(vec3d.y, this.onGround, blockstate, blockpos);
            Vec3d vec3d2 = this.getMotion();
            if(pos.x != vec3d.x) {
                this.setMotion(0.0D, vec3d2.y, vec3d2.z);
            }

            if(pos.z != vec3d.z) {
                this.setMotion(vec3d2.x, vec3d2.y, 0.0D);
            }

            Block block1 = blockstate.getBlock();
            if(pos.y != vec3d.y) {
                block1.onLanded(this.world, this);
            }

            if(this.canTriggerWalking() && (!this.onGround || !this.isSneaking()) && !this.isPassenger()) {
                double d2 = vec3d.x;
                double d0 = vec3d.y;
                double d1 = vec3d.z;
                if(block1 != Blocks.LADDER && block1 != Blocks.SCAFFOLDING) {
                    d0 = 0.0D;
                }

                if(this.onGround) {
                    block1.onEntityWalk(this.world, blockpos, this);
                }

                this.distanceWalkedModified = (float) ((double) this.distanceWalkedModified + (double) MathHelper.sqrt(func_213296_b(vec3d)) * 0.6D);
                this.distanceWalkedOnStepModified = (float) ((double) this.distanceWalkedOnStepModified + (double) MathHelper.sqrt(d2 * d2 + d0 * d0 + d1 * d1) * 0.6D);
                if(this.distanceWalkedOnStepModified > this.nextStepDistance && !blockstate.isAir(this.world, blockpos)) {
                    this.nextStepDistance = this.determineNextStepDistance();
                    if(this.isInWater()) {
                        Entity entity = this.isBeingRidden() && this.getControllingPassenger() != null ? this.getControllingPassenger() : this;
                        float f = entity == this ? 0.35F : 0.4F;
                        Vec3d vec3d1 = entity.getMotion();
                        float f1 = MathHelper.sqrt(vec3d1.x * vec3d1.x * (double) 0.2F + vec3d1.y * vec3d1.y + vec3d1.z * vec3d1.z * (double) 0.2F) * f;
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
                this.inLava = false;
                this.doBlockCollisions();
            } catch(Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.fillCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            this.world.getProfiler().endSection();
        }
    }

    private Vec3d getAllowedMovement(Vec3d vec) {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        ISelectionContext iselectioncontext = ISelectionContext.forEntity(this);
        VoxelShape voxelshape = this.world.getWorldBorder().getShape();
        // get world shape as stream
        Stream<VoxelShape> stream = VoxelShapes.compare(voxelshape, VoxelShapes.create(axisalignedbb.shrink(1.0E-7D)), IBooleanFunction.AND) ? Stream.empty() : Stream.of(voxelshape);
        // get entity shape as stream
        Stream<VoxelShape> stream1 = this.world.getEmptyCollisionShapes(this, axisalignedbb.expand(vec), ImmutableSet.of());
        // concatenate and make reusable
        ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(Stream.concat(stream1, stream));
        // return movement vector if it is 0, otherwise
        Vec3d vec3d = vec.lengthSquared() == 0.0D ? vec : transformMove(this, vec, axisalignedbb, this.world, iselectioncontext, reuseablestream);
        boolean flag = vec.x != vec3d.x; // if vector is transformed over x
        boolean flag1 = vec.y != vec3d.y; // if vector is transformed over y
        boolean flag2 = vec.z != vec3d.z; // if vector is transformed over z
        boolean flag3 = this.onGround || flag1 && vec.y < 0.0D; // if on ground or transformed over y, and non-transformed vector is 0 (no
        // planned y movement?)
        if(this.stepHeight > 0.0F && flag3 && (flag || flag2)) { // if can step up and ^ and original vector transformed over either x or y
            Vec3d vec3d1 = transformMove(this, new Vec3d(vec.x, (double) this.stepHeight, vec.z), axisalignedbb, this.world, iselectioncontext, reuseablestream);
            Vec3d vec3d2 = transformMove(this, new Vec3d(0.0D, (double) this.stepHeight, 0.0D), axisalignedbb.expand(vec.x, 0.0D, vec.z), this.world, iselectioncontext, reuseablestream);
            if(vec3d2.y < (double) this.stepHeight) {
                Vec3d vec3d3 = transformMove(this, new Vec3d(vec.x, 0.0D, vec.z), axisalignedbb.offset(vec3d2), this.world, iselectioncontext, reuseablestream).add(vec3d2);
                if(func_213296_b(vec3d3) > func_213296_b(vec3d1)) {
                    vec3d1 = vec3d3;
                }
            }

            if(func_213296_b(vec3d1) > func_213296_b(vec3d)) {
                return vec3d1.add(transformMove(this, new Vec3d(0.0D, -vec3d1.y + vec.y, 0.0D), axisalignedbb.offset(vec3d1), this.world, iselectioncontext, reuseablestream));
            }
        }

        return vec3d;
    }

    public static Vec3d transformMove(@Nullable Entity entity, Vec3d vec, AxisAlignedBB bb, World world, ISelectionContext context, ReuseableStream<VoxelShape> stream) {
        boolean flag = vec.x == 0.0D;
        boolean flag1 = vec.y == 0.0D;
        boolean flag2 = vec.z == 0.0D;
        if((!flag || !flag1) && (!flag || !flag2) && (!flag1 || !flag2)) { // if moving somehow
            ReuseableStream<VoxelShape> reuseablestream = new ReuseableStream<>(Stream.concat(stream.createStream(), world.getCollisionShapes(entity, bb.expand(vec))).filter(shape -> {
                return !world.getBlockState(new BlockPos(shape.getBoundingBox().minX, shape.getBoundingBox().minY, shape.getBoundingBox().minZ)).getBlock().getTags().contains(BlockTags.LEAVES.getId());
            }));
            return func_223310_a(vec, bb, reuseablestream);
        } else {
            return getAllowedMovement(vec, bb, world, context, stream);
        }
    }

}
