package dev.itsmeow.whisperwoods.entity;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.IContainerEntity;
import dev.itsmeow.whisperwoods.init.ModBlocks;
import dev.itsmeow.whisperwoods.init.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityWisp extends AnimalEntity implements IContainerEntity<EntityWisp> {

    public final DamageSource WISP = new EntityDamageSource("wisp", this).setDamageIsAbsolute().setDamageBypassesArmor();
    public static int HOSTILE_CHANCE = 8;
    public boolean isHostile = false;
    public long lastSpawn = 0;
    private BlockPos targetPosition;
    public static final DataParameter<Integer> ATTACK_STATE = EntityDataManager.createKey(EntityWisp.class, DataSerializers.VARINT);
    public static final DataParameter<String> TARGET = EntityDataManager.createKey(EntityWisp.class, DataSerializers.STRING);
    public static final DataParameter<String> TARGET_NAME = EntityDataManager.createKey(EntityWisp.class, DataSerializers.STRING);
    public static final DataParameter<Float> PASSIVE_SCALE = EntityDataManager.createKey(EntityWisp.class, DataSerializers.FLOAT);
    public static final DataParameter<Integer> COLOR_VARIANT = EntityDataManager.createKey(EntityWisp.class, DataSerializers.VARINT);
    protected ResourceLocation targetTexture;

    protected EntityWisp(EntityType<? extends EntityWisp> entityType, World world) {
        super(entityType, world);
    }

    public EntityWisp(World world) {
        this(ModEntities.WISP.entityType, world);
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.5D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(ATTACK_STATE, 0);
        this.dataManager.register(TARGET, "");
        this.dataManager.register(TARGET_NAME, "");
        this.dataManager.register(PASSIVE_SCALE, 1F);
        this.dataManager.register(COLOR_VARIANT, 0);
    }

    public void tick() {
        super.tick();
        int state = this.dataManager.get(ATTACK_STATE);
        if(state == 0) {
            this.setMotion(this.getMotion().mul(0.5D, 0.6D, 0.5D));
            this.noClip = false;
        } else {
            this.setMotion(this.getMotion().mul(1D, 0.6D, 1D));
            this.noClip = true;
            this.dataManager.set(ATTACK_STATE, state + 1);
        }
        if(state == 400 && !world.isRemote) {
            PlayerEntity soul = null;
            if(this.getAttackTarget() instanceof PlayerEntity) {
                soul = (PlayerEntity) this.getAttackTarget();
            }
            if(soul == null) {
                soul = world.getServer().getPlayerList().getPlayerByUUID(UUID.fromString(this.dataManager.get(TARGET)));
            }
            if(soul == null) {
                soul = world.getServer().getPlayerList().getPlayerByUsername(this.dataManager.get(TARGET_NAME));
            }
            resetAttackState();
            soul.attackEntityFrom(WISP, 3000F);
            this.targetPosition = null;
            this.setAttackTarget(null);
        }
        if(!this.isHostile && !world.isRemote) {
            if(world.getEntitiesWithinAABB(PlayerEntity.class, this.getBoundingBox().grow(10)).size() > 0) {
                PlayerEntity nearest = world.getClosestEntityWithinAABB(PlayerEntity.class, new EntityPredicate().allowInvulnerable().allowFriendlyFire().setSkipAttackChecks(), null, this.posX, this.posY, this.posZ, this.getBoundingBox().grow(10));
                if(nearest != null) {
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
    protected void updateAITasks() {
        super.updateAITasks();
        int state = this.dataManager.get(ATTACK_STATE);
        if(this.getAttackTarget() != null && this.getAttackTarget().isInvulnerable()) {
            this.setAttackTarget(null);
        }
        if(this.getAttackTarget() == null && this.isHostile) {
            
        }
        if((this.targetPosition != null && this.getPosition().distanceSq(this.targetPosition) < 4) || this.targetPosition == null || !this.isHostile || (this.isHostile && state == 0)) {
            if(this.getAttackTarget() == null && this.isHostile) {
                this.setAttackTarget(world.getClosestEntityWithinAABB(PlayerEntity.class, EntityPredicate.DEFAULT, null, this.posX, this.posY, this.posZ, this.getBoundingBox().grow(25)));
            }
            if(this.isHostile && this.getAttackTarget() != null) {
                this.targetPosition = this.getAttackTarget().getPosition();
            } else {
                this.targetPosition = new BlockPos(this.posX + (double)this.rand.nextInt(5) - (double)this.rand.nextInt(5), this.posY + (double)this.rand.nextInt(4) - 0.1D, this.posZ + (double)this.rand.nextInt(5) - (double)this.rand.nextInt(5));
                
            }
            if(state > 0 && this.isHostile) {
                this.targetPosition = new BlockPos(this.posX + (double)this.rand.nextInt(60) - (double)this.rand.nextInt(60), this.posY + (double)this.rand.nextInt(4), this.posZ + (double)this.rand.nextInt(60) - (double)this.rand.nextInt(60));
            }
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

    protected void resetAttackState() {
        this.dataManager.set(ATTACK_STATE, 0);
        this.dataManager.set(TARGET, "");
        this.dataManager.set(TARGET_NAME, "");
        targetTexture = null;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTargetTexture() {
        if(targetTexture == null) {
            UUID target = UUID.fromString(this.getDataManager().get(EntityWisp.TARGET));
            String name = this.getDataManager().get(EntityWisp.TARGET_NAME);
            GameProfile profile = new GameProfile(target, name);
            profile = SkullTileEntity.updateGameProfile(profile);
            Map<Type, MinecraftProfileTexture> map = Minecraft.getInstance().getSkinManager().loadSkinFromCache(profile);
            ResourceLocation skin;
            if(map.containsKey(Type.SKIN)) {
                skin = Minecraft.getInstance().getSkinManager().loadSkin(map.get(Type.SKIN), Type.SKIN);
            } else {
                skin = DefaultPlayerSkin.getDefaultSkin(target);
                Minecraft.getInstance().getSkinManager().loadProfileTextures(profile, null, false);
            }
            targetTexture = skin;
        }
        return targetTexture;
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
        if(entity == this.getAttackTarget() && this.getAttackTarget() != null && entity instanceof PlayerEntity && this.dataManager.get(ATTACK_STATE) == 0) {
            PlayerEntity player = (PlayerEntity) entity;
            this.dataManager.set(ATTACK_STATE, Integer.valueOf(1));
            this.dataManager.set(TARGET, player.getGameProfile().getId().toString());
            this.dataManager.set(TARGET_NAME, player.getGameProfile().getName());
        }
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    @Override
    public boolean canDespawn(double range) {
        return getContainer().despawn && this.dataManager.get(ATTACK_STATE) == 0;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("is_hostile", isHostile);
        compound.putInt("color_variant", this.dataManager.get(COLOR_VARIANT));
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.isHostile = compound.getBoolean("is_hostile");
        this.dataManager.set(COLOR_VARIANT, compound.getInt("color_variant"));
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        int variant = this.getDataManager().get(COLOR_VARIANT);
        if (!world.isRemote && !this.isChild()) {
            if (this.rand.nextInt(20) < 2 || this.dataManager.get(ATTACK_STATE) > 0) {
                ItemStack stack = new ItemStack(getItemForVariant(variant));
                this.entityDropItem(stack, 0.5F);
            }
        }
    }

    private static Item getItemForVariant(int variant) {
        Block block = null;
        switch(variant) {
        case 1: block = ModBlocks.GHOST_LIGHT_ELECTRIC_BLUE; break;
        case 2: block = ModBlocks.GHOST_LIGHT_FIERY_ORANGE; break;
        case 3: block = ModBlocks.GHOST_LIGHT_GOLD; break;
        case 4: block = ModBlocks.GHOST_LIGHT_TOXIC_GREEN; break;
        case 5: block = ModBlocks.GHOST_LIGHT_MAGIC_PURPLE; break;
        }
        if(block == null) {
            return null;
        }
        return block.asItem();
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, CompoundNBT compound) {
        if(!this.isChild()) {
            boolean hostile = this.getRNG().nextInt(HOSTILE_CHANCE) == 0;
            int colorVariant = this.getRNG().nextInt(5) + 1;

            if(livingdata instanceof WispData) {
                hostile = ((WispData) livingdata).isHostile;
                colorVariant = ((WispData) livingdata).colorVariant;
            } else {
                livingdata = new WispData(hostile, colorVariant);
            }

            this.isHostile = hostile;
            this.dataManager.set(COLOR_VARIANT, colorVariant);

        }

        return livingdata;
    }

    public static class WispData implements ILivingEntityData {
        public boolean isHostile;
        public int colorVariant;

        public WispData(boolean isHostile, int colorVariant) {
            this.isHostile = isHostile;
            this.colorVariant = colorVariant;
        }
    }

    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    @Override
    public EntityWisp getImplementation() {
        return this;
    }

    @Override
    public EntityTypeContainer<?> getContainer() {
        return ModEntities.WISP;
    }
}
