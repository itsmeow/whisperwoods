package its_meow.whisperwoods.entity;

import javax.annotation.Nullable;

import its_meow.whisperwoods.init.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class EntityWisp extends AnimalEntity {

    public static int HOSTILE_CHANCE = 10;
    public boolean isHostile = false;
    public long lastSpawn = 0;
    private BlockPos targetPosition;

    protected EntityWisp(EntityType<? extends EntityWisp> entityType, World world) {
        super(entityType, world);
    }

    public EntityWisp(World world) {
        this(ModEntities.WISP.entityType, world);
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.5D);
        // this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        // this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.3D);
    }

    public void tick() {
        super.tick();
        this.setMotion(this.getMotion().mul(0.5D, 0.6D, 0.5D));
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if(this.getAttackTarget() == null && this.isHostile) {
            this.setAttackTarget(world.getClosestEntityWithinAABB(PlayerEntity.class, EntityPredicate.DEFAULT, null, this.posX, this.posY, this.posZ, this.getBoundingBox().grow(25)));
        }
        if(this.isHostile && this.getAttackTarget() != null) {
            this.targetPosition = this.getAttackTarget().getPosition();
        } else {
            this.targetPosition = new BlockPos(this.posX + (double)this.rand.nextInt(5) - (double)this.rand.nextInt(5), this.posY + (double)this.rand.nextInt(4), this.posZ + (double)this.rand.nextInt(5) - (double)this.rand.nextInt(5));
        }
        if(targetPosition != null) {
            double d0 = (double) this.targetPosition.getX() + 0.5D - this.posX;
            double d1 = (double) this.targetPosition.getY() + 0.1D - this.posY;
            double d2 = (double) this.targetPosition.getZ() + 0.5D - this.posZ;
            Vec3d vec3d = this.getMotion();
            Vec3d vec3d1 = vec3d.add((Math.signum(d0) * 0.5D - vec3d.x) * (double) 0.1F, (Math.signum(d1) * (double) 0.7F - vec3d.y) * (double) 0.1F, (Math.signum(d2) * 0.5D - vec3d.z) * (double) 0.1F);
            this.setMotion(vec3d1);
            float f = (float) (MathHelper.atan2(vec3d1.z, vec3d1.x) * (double) (180F / (float) Math.PI)) - 90.0F;
            float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
            this.moveForward = 0.5F;
            this.rotationYaw += f1;
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
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
        if(entity == this.getAttackTarget() && this.getAttackTarget() != null && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
        }
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    @Override
    public boolean canDespawn(double range) {
        return ModEntities.ENTITIES.containsKey("wisp") ? ModEntities.ENTITIES.get("wisp").despawn : false;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("is_hostile", isHostile);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.isHostile = compound.getBoolean("is_hostile");
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, CompoundNBT compound) {
        if(!this.isChild()) {
            boolean hostile = this.getRNG().nextInt(HOSTILE_CHANCE) == 0;

            if(livingdata instanceof HostileData) {
                hostile = ((HostileData) livingdata).isHostile;
            } else {
                livingdata = new HostileData(hostile);
            }

            this.isHostile = hostile;

        }

        return livingdata;
    }

    public static class HostileData implements ILivingEntityData {
        public boolean isHostile;

        public HostileData(boolean isHostile) {
            this.isHostile = isHostile;
        }
    }

    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }
}
