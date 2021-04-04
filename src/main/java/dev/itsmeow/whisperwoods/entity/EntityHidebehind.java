package dev.itsmeow.whisperwoods.entity;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.EntityVariant;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModSounds;
import dev.itsmeow.whisperwoods.util.IOverrideCollisions;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

public class EntityHidebehind extends EntityCreatureWithSelectiveTypes implements IOverrideCollisions<EntityCreatureWithTypes> {

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

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(15D);
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
                this.setPositionAndUpdate(destinationBlock.getX(), destinationBlock.getY(), destinationBlock.getZ());
            }
        }
        float atkTicks = attackSequenceTicks();
        if(this.getHiding() && !this.isBeingViewed()) {
            this.setHiding(false);
        }
        if(this.getAttackTarget() == null) {
            this.setAttackTarget(world.getClosestEntityWithinAABB(PlayerEntity.class, EntityPredicate.DEFAULT, null, this.getPosX(), this.getPosY(), this.getPosZ(), this.getBoundingBox().grow(25)));
        }
        if(this.getAttackTarget() != null && this.getAttackTarget().getDistanceSq(this) < 5D && atkTicks == 0 && !this.getHiding() && this.isEntityAttackable(this.getAttackTarget())) {
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

    public boolean isEntityAttackable(LivingEntity target) {
        Item mainItem = target.getHeldItem(Hand.MAIN_HAND).getItem();
        Item offItem = target.getHeldItem(Hand.OFF_HAND).getItem();
        return world.getLight(target.getPosition()) < 8 && !(mainItem instanceof BlockItem && ((BlockItem)mainItem).getBlock() instanceof TorchBlock) && !(offItem instanceof BlockItem && ((BlockItem)offItem).getBlock() instanceof TorchBlock);
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

    public boolean isBeingViewed() {
        return Math.abs(this.getTargetViewingAngle()) <= 50;
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
        return MathHelper.wrapDegrees(getRequiredViewingAngle() - MathHelper.wrapDegrees(getAttackTarget().rotationYaw));
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
        return MathHelper.wrapDegrees(90D + Math.toDegrees(Math.atan2(target.getPosZ() - this.getPosZ(), target.getPosX() - this.getPosX())));
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        float f = (float) this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
        float f1 = (float) this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).getValue();
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
                ((LivingEntity) entity).knockBack(this, f1 * 0.5F, (double) MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F))));
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

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        if (this.noClip) {
            return false;
        } else {
            try (BlockPos.PooledMutable blockpos$pooledmutable = BlockPos.PooledMutable.retain()) {
                for(int i = 0; i < 8; ++i) {
                    int j = MathHelper.floor(this.getPosY() + (double)(((float)((i >> 0) % 2) - 0.5F) * 0.1F) + (double)this.getEyeHeight());
                    int k = MathHelper.floor(this.getPosX() + (double)(((float)((i >> 1) % 2) - 0.5F) * this.getWidth() * 0.8F));
                    int l = MathHelper.floor(this.getPosZ() + (double)(((float)((i >> 2) % 2) - 0.5F) * this.getWidth() * 0.8F));
                    if (blockpos$pooledmutable.getX() != k || blockpos$pooledmutable.getY() != j || blockpos$pooledmutable.getZ() != l) {
                        blockpos$pooledmutable.setPos(k, j, l);
                        BlockState state = this.world.getBlockState(blockpos$pooledmutable);
                        if (state.isSuffocating(this.world, blockpos$pooledmutable) && !state.getBlock().isIn(BlockTags.LOGS)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }

    @Override
    public Vec3d getAllowedMovement(Vec3d vec) {
        return allowedMove(vec);
    }

    @Override
    public Vec3d transformMove(@Nullable Entity entity, Vec3d vec, AxisAlignedBB bb, World world, ISelectionContext context, ReuseableStream<VoxelShape> stream) {
        boolean flag = vec.x == 0.0D;
        boolean flag1 = vec.y == 0.0D;
        boolean flag2 = vec.z == 0.0D;
        if((!flag || !flag1) && (!flag || !flag2) && (!flag1 || !flag2)) { // if moving somehow
            ReuseableStream<VoxelShape> reusableStream = new ReuseableStream<>(Stream.concat(stream.createStream(), world.getCollisionShapes(entity, bb.expand(vec))).filter(shape -> {
                return !world.getBlockState(new BlockPos(shape.getBoundingBox().minX, shape.getBoundingBox().minY, shape.getBoundingBox().minZ)).getBlock().isIn(BlockTags.LEAVES);
            }));
            return collideBoundingBox(vec, bb, reusableStream);
        } else {
            return getAllowedMovement(vec, bb, world, context, stream);
        }
    }

    public static class HideFromTargetGoal extends Goal {
        private final EntityHidebehind hidebehind;

        public HideFromTargetGoal(EntityHidebehind hb) {
            this.hidebehind = hb;
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return hidebehind.getAttackTarget() != null && (hidebehind.isBeingViewed() || !hidebehind.isEntityAttackable(hidebehind.getAttackTarget())) && hidebehind.attackSequenceTicks() == 0;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return this.shouldExecute();
        }

        @Override
        public void resetTask() {
            hidebehind.setHiding(false);
        }

        @Override
        public void startExecuting() {
            hidebehind.setHiding(true);
            this.hidebehind.getNavigator().clearPath();
        }

        @Override
        public void tick() {
            this.hidebehind.getNavigator().clearPath();
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
                    hidebehind.setPositionAndUpdate(destinationBlock.getX(), destinationBlock.getY(), destinationBlock.getZ());
                }
            }
        }
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(HIDING, (byte) 0);
        this.dataManager.register(OPEN, (byte) 0);
        this.dataManager.register(ATTACK_SEQUENCE_TICKS, 0);
    }

    @Override
    protected PathNavigator createNavigator(World world) {
        return new HidebehindGroundNavigator(this, world);
    }

    public static class HidebehindGroundNavigator extends GroundPathNavigator {

        public HidebehindGroundNavigator(MobEntity entityliving, World world) {
            super(entityliving, world);
        }

        @Override
        protected PathFinder getPathFinder(int i1) {
            this.nodeProcessor = new HidebehindNodeProcessor();
            this.nodeProcessor.setCanEnterDoors(true);
            return new PathFinder(this.nodeProcessor, i1);
        }

        public static class HidebehindNodeProcessor extends WalkNodeProcessor {
            @Override
            protected PathNodeType getSpecificPathNodeType(IBlockReader reader, boolean b1, boolean b2, BlockPos pos, PathNodeType typeIn) {
                return typeIn == PathNodeType.LEAVES ? PathNodeType.OPEN : super.getSpecificPathNodeType(reader, b1, b2, pos, typeIn);
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

        @Override
        public boolean shouldExecute() {
            this.target = this.hidebehind.getAttackTarget();
            if(this.target == null || this.target.getDistanceSq(this.hidebehind) > (double) (this.maxTargetDistance * this.maxTargetDistance)) {
                return false;
            } else {
                Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.hidebehind, 16, 7, new Vec3d(this.target.getPosX(), this.target.getPosY(), this.target.getPosZ()));
                return vec3d != null && hidebehind.isEntityAttackable(target) && hidebehind.attackSequenceTicks() <= 0 && !hidebehind.getHiding();
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return !hidebehind.getHiding() && !this.hidebehind.getNavigator().noPath() && this.target.isAlive() && this.target.getDistanceSq(this.hidebehind) < (double) (this.maxTargetDistance * this.maxTargetDistance) && hidebehind.attackSequenceTicks() == 0 && hidebehind.isEntityAttackable(target);
        }

        @Override
        public void resetTask() {
            this.target = null;
            this.hidebehind.getNavigator().clearPath();
        }

        @Override
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
    public String[] getTypesFor(Biome biome, Set<BiomeDictionary.Type> types) {
        if(biome == Biomes.GIANT_SPRUCE_TAIGA || biome == Biomes.GIANT_SPRUCE_TAIGA_HILLS || biome == Biomes.GIANT_TREE_TAIGA || biome == Biomes.GIANT_TREE_TAIGA_HILLS) {
            return new String[] { "mega_taiga", "mega_taiga", "mega_taiga", "darkforest" };
        }
        if(types.contains(BiomeDictionary.Type.CONIFEROUS)) {
            return new String[] { "coniferous", "coniferous", "coniferous", "coniferous", "black", "darkforest" };
        }
        if(types.contains(BiomeDictionary.Type.FOREST)) {
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