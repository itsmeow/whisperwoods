package dev.itsmeow.whisperwoods.entity;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.itsmeow.imdlib.entity.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.BiomeTypes;
import dev.itsmeow.imdlib.entity.util.variant.EntityVariant;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModSounds;
import dev.itsmeow.whisperwoods.util.IOverrideCollisions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class EntityHidebehind extends EntityCreatureWithSelectiveTypes implements IOverrideCollisions<EntityCreatureWithTypes> {
    public static final ResourceKey<DamageType> HIDEBEHIND = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(WhisperwoodsMod.MODID, "hidebehind"));
    protected static final EntityDataAccessor<Integer> HIDING = SynchedEntityData.defineId(EntityHidebehind.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> OPEN = SynchedEntityData.defineId(EntityHidebehind.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> ATTACK_SEQUENCE_TICKS = SynchedEntityData.defineId(EntityHidebehind.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Optional<UUID>> TARGET_UUID = SynchedEntityData.defineId(EntityHidebehind.class, EntityDataSerializers.OPTIONAL_UUID);

    public EntityHidebehind(EntityType<? extends EntityHidebehind> type, Level world) {
        super(type, world);
        this.setMaxUpStep(2F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new HideFromTargetGoal(this));
        this.goalSelector.addGoal(3, new StalkTargetGoal(this, 0.5D, 35F));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, this::isEntityAttackable));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.HIDEBEHIND_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.HIDEBEHIND_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.HIDEBEHIND_DEATH.get();
    }

    public boolean hasTargetUUID() {
        return this.getEntityData().get(TARGET_UUID).isPresent();
    }

    public boolean isSequenceTarget(Player player) {
        Optional<UUID> opt = this.getEntityData().get(TARGET_UUID);
        return player != null && player.getGameProfile().getId() != null && opt.isPresent() && player.getGameProfile().getId().equals(opt.get());
    }

    public void setSequenceTarget(Player player) {
        this.getEntityData().set(TARGET_UUID, player == null || player.getGameProfile().getId() == null ? Optional.empty() : Optional.of(player.getGameProfile().getId()));
    }

    public int attackSequenceTicks() {
        return this.entityData.get(ATTACK_SEQUENCE_TICKS);
    }

    public void attackSequenceTicksDecrement() {
        this.entityData.set(ATTACK_SEQUENCE_TICKS, attackSequenceTicks() - 1);
    }

    public void setAttackSequenceTicks(int value) {
        this.entityData.set(ATTACK_SEQUENCE_TICKS, value);
        if(value == 0) {
            this.setSequenceTarget(null);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.getEntity() == this.getTarget() && this.attackSequenceTicks() > 0) {
            this.setAttackSequenceTicks(0);
        }
        if (!level().isClientSide()) {
            boolean isImmediate = source.getDirectEntity() instanceof Player;
            Player player = isImmediate ? (Player) source.getDirectEntity() : (source.getEntity() instanceof Player ? (Player) source.getEntity() : null);
            if (player != null) {
                int hiding = this.getHidingInt();
                boolean attackable = this.isEntityAttackable(player);
                if (!attackable || hiding > 0) {
                    // retaliate attacks if you can't chase due to light
                    if (!player.isCreative()) {
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 15 * (hiding == 1 ? 5 : 20), 0));
                        if (player.distanceTo(this) < 3)
                            player.hurt(hideBehindDamageSource(), 1F);
                    }
                    HideFromTargetGoal.doTreeTick(this);
                    return super.hurt(source, amount * (hiding == 1 ? 0.5F : 0.25F));
                }
            }
        }
        return super.hurt(source, amount);
    }

    private  DamageSource hideBehindDamageSource() {
        return new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(HIDEBEHIND), this);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getTarget() != null && !this.getTarget().isAlive()) {
            this.setTarget(null);
        }
        if(this.isInWater()) {
            int i = 12;
            int j = 2;
            BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();
            BlockPos destinationBlock = null;
            for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                for(int l = 0; l < i; ++l) {
                    for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                        for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                            bp.set(this.blockPosition()).move(i1, k - 1, j1);
                            if(this.level().getBlockState(bp).is(BlockTags.LOGS)) {
                                destinationBlock = bp.immutable();
                            }
                        }
                    }
                }
            }
            boolean fixed = false;
            if(destinationBlock != null) {
                for(Direction dir : Direction.values()) {
                    if(!fixed) {
                        if(this.level().isEmptyBlock(destinationBlock.relative(dir)) || this.level().getBlockState(destinationBlock.relative(dir)).is(BlockTags.LEAVES)) {
                            destinationBlock = destinationBlock.relative(dir);
                            fixed = true;
                        }
                    }
                }
            }
            if(fixed) {
                this.teleportTo(destinationBlock.getX(), destinationBlock.getY(), destinationBlock.getZ());
            }
        }
        if(!level().isClientSide()) {
            if (level().isDay() && level().getBrightness(LightLayer.SKY, this.blockPosition()) > 10) {
                this.setHiding(2);
            } else if (this.getHidingInt() == 2) {
                this.setHiding(1);
            }
            if (!this.isBeingViewed() && this.getHidingInt() == 1) {
                this.setHiding(false);
            }
        }
        float atkTicks = attackSequenceTicks();
        if(this.getTarget() != null && this.getTarget().distanceToSqr(this) < 5D && atkTicks == 0 && !this.getHiding() && this.isEntityAttackable(this.getTarget())) {
            if(this.getTarget() instanceof Player player) {
                if(!this.level().isClientSide && this.getRandom().nextInt(20) == 0) {
                    if(player.getHealth() > this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()) {
                        this.doHurtTarget(player);
                    } else {
                        this.setAttackSequenceTicks(40);
                        this.setSequenceTarget(player);
                    }
                }
            }
        }

        if(atkTicks > 0) {
            if(!this.getOpen()) {
                this.setOpen(true);
            }
            Player target = null;
            if(this.getTarget() instanceof ServerPlayer) {
                target = (ServerPlayer) this.getTarget();
            } else if(Platform.getEnvironment() == Env.CLIENT) {
                Supplier<Supplier<Player>> s = () -> ClientSafeLogic::getTargetClient;
                target = s.get().get();
            }
            if(this.isSequenceTarget(target)) {
                target.setDeltaMovement(0, 0, 0);
                double d0 = this.getX() - target.getX();
                double d1 = this.getZ() - target.getZ();
                float angle = (float) (Mth.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                target.absMoveTo(target.getX(), target.getY(), target.getZ(), angle, 0);
                double e0 = target.getX() - this.getX();
                double e1 = target.getZ() - this.getZ();
                float angle1 = (float) (Mth.atan2(e1, e0) * (double) (180F / (float) Math.PI)) - 90.0F;
                this.absMoveTo(this.getX(), this.getY(), this.getZ(), angle1, 0);
                this.setYRot(angle1);
                if (this.attackSequenceTicks() == 40) {
                    target.playSound(ModSounds.HIDEBEHIND_SCARE.get(), 2F, 1F);

                }
                this.getLookControl().setLookAt(target, 360F, 360F);
            }
            this.attackSequenceTicksDecrement();
            if(atkTicks - 1 == 0 && !level().isClientSide()) {
                this.setOpen(false);
                if(this.getTarget() != null) {
                    this.doHurtTarget(target);
                    this.setTarget(null);
                    this.getEntityData().set(TARGET_UUID, Optional.empty());
                }
            }
        }
        if(atkTicks == 0 && this.getOpen()) {
            this.setOpen(false);
        }
    }

    public static class ClientSafeLogic {
        public static Player getTargetClient() {
            return Minecraft.getInstance().player;
        }
    }

    public boolean isEntityAttackable(LivingEntity target) {
        Item mainItem = target.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        Item offItem = target.getItemInHand(InteractionHand.OFF_HAND).getItem();
        return level().getMaxLocalRawBrightness(target.blockPosition()) < 8 && !(mainItem instanceof BlockItem && ((BlockItem)mainItem).getBlock() instanceof TorchBlock) && !(offItem instanceof BlockItem && ((BlockItem)offItem).getBlock() instanceof TorchBlock);
    }

    @Override
    protected float getWaterSlowDown() {
        return 1.0F;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public boolean getHiding() {
        return this.getHidingInt() != 0;
    }

    public int getHidingInt() {
        return this.getEntityData().get(HIDING);
    }

    public void setHiding(boolean hiding) {
        this.getEntityData().set(HIDING, hiding ? 1 : 0);
    }

    public void setHiding(int hiding) {
        this.getEntityData().set(HIDING, hiding);
    }

    public boolean getOpen() {
        return this.getEntityData().get(OPEN);
    }

    public void setOpen(boolean open) {
        this.getEntityData().set(OPEN, open);
    }

    public boolean isBeingViewed() {
        return Math.abs(this.getTargetViewingAngle()) <= 50;
    }

    /**
     * @return The viewing angle of the attack target (0 when looking directly at
     *         HB, 180 if opposite). If there is no target, returns -1000
     */
    public double getTargetViewingAngle() {
        LivingEntity target = this.getTarget();
        if(target == null) {
            return -1000;
        }
        return Mth.wrapDegrees(getRequiredViewingAngle() - Mth.wrapDegrees(getTarget().getYRot()));
    }

    /**
     * @return The required viewing angle of the attack target to be looking at it.
     *         If there is no target, returns -1000
     */
    public double getRequiredViewingAngle() {
        LivingEntity target = this.getTarget();
        if(target == null) {
            return -1000;
        }
        return Mth.wrapDegrees(90D + Math.toDegrees(Math.atan2(target.getZ() - this.getZ(), target.getX() - this.getX())));
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        float f1 = (float) this.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue();
        if(entity instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entity).getMobType());
            f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if(i > 0) {
            entity.setSecondsOnFire(i * 4);
        }

        boolean flag = entity.hurt(hideBehindDamageSource(), f);
        if (flag) {
            if (f1 > 0.0F && entity instanceof LivingEntity) {
                ((LivingEntity)entity).knockback(f1 * 0.5F, (double)Mth.sin(this.getYRot() * 0.017453292F), (double)(-Mth.cos(this.getYRot() * 0.017453292F)));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }
            this.doEnchantDamageEffects(this, entity);
            this.setLastHurtMob(entity);
        }

        return flag;
    }

    @Override
    public boolean isInWall() {
        return insideOpaque();
    }

    @Override
    public boolean canPassThrough(BlockState state) {
        return state.is(BlockTags.LEAVES);
    }

    @Override
    public boolean preventSuffocation(BlockState state) {
        return state.is(BlockTags.LOGS) || canPassThrough(state);
    }

    public static class HideFromTargetGoal extends Goal {
        private final EntityHidebehind hidebehind;

        public HideFromTargetGoal(EntityHidebehind hb) {
            this.hidebehind = hb;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return hidebehind.getTarget() != null && (hidebehind.isBeingViewed() || !hidebehind.isEntityAttackable(hidebehind.getTarget())) && hidebehind.attackSequenceTicks() == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }

        @Override
        public void stop() {
            if(hidebehind.getHidingInt() == 1)
                hidebehind.setHiding(false);
        }

        @Override
        public void start() {
            if(!hidebehind.getHiding())
                hidebehind.setHiding(true);
            this.hidebehind.getNavigation().stop();
        }

        @Override
        public void tick() {
            doTreeTick(hidebehind);
        }

        public static void doTreeTick(EntityHidebehind hidebehind) {
            hidebehind.getNavigation().stop();
            boolean nearTree = false;
            for(Direction dir : Direction.values()) {
                if(!nearTree) {
                    if(hidebehind.level().getBlockState(hidebehind.blockPosition().relative(dir)).is(BlockTags.LOGS)) {
                        nearTree = true;
                    }
                    if(hidebehind.level().getBlockState(hidebehind.blockPosition().above(3).relative(dir)).is(BlockTags.LEAVES)) {
                        nearTree = true;
                    }
                }
            }
            if(!nearTree && hidebehind.getRandom().nextInt(5) == 0) {
                int i = 12;
                int j = 2;
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                BlockPos destinationBlock = null;
                for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for(int l = 0; l < i; ++l) {
                        for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                            for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                                blockpos$mutableblockpos.set(hidebehind.blockPosition()).move(i1, k - 1, j1);
                                if(hidebehind.level().getBlockState(blockpos$mutableblockpos).is(BlockTags.LOGS)) {
                                    destinationBlock = blockpos$mutableblockpos.immutable();
                                }
                            }
                        }
                    }
                }
                boolean fixed = false;
                if(destinationBlock != null) {
                    for(Direction dir : Direction.values()) {
                        if(!fixed) {
                            if(hidebehind.level().isEmptyBlock(destinationBlock.relative(dir)) || hidebehind.level().getBlockState(destinationBlock.relative(dir)).is(BlockTags.LEAVES)) {
                                destinationBlock = destinationBlock.relative(dir);
                                fixed = true;
                            }
                        }
                    }
                }
                if(fixed) {
                    hidebehind.teleportTo(destinationBlock.getX(), destinationBlock.getY(), destinationBlock.getZ());
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HIDING, 0);
        this.entityData.define(OPEN, false);
        this.entityData.define(ATTACK_SEQUENCE_TICKS, 0);
        this.entityData.define(TARGET_UUID, Optional.empty());
    }

    @Override
    public boolean removeWhenFarAway(double range) {
        return level().isDay() && super.removeWhenFarAway(range);
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new HidebehindGroundNavigator(this, world);
    }

    public static class HidebehindGroundNavigator extends GroundPathNavigation {

        public HidebehindGroundNavigator(Mob entityliving, Level world) {
            super(entityliving, world);
        }

        @Override
        protected PathFinder createPathFinder(int i1) {
            this.nodeEvaluator = new HidebehindNodeProcessor();
            this.nodeEvaluator.setCanPassDoors(true);
            return new PathFinder(this.nodeEvaluator, i1);
        }

        public static class HidebehindNodeProcessor extends WalkNodeEvaluator {
            @Override
            protected BlockPathTypes evaluateBlockPathType(BlockGetter reader, BlockPos pos, BlockPathTypes typeIn) {
                return typeIn == BlockPathTypes.LEAVES ? BlockPathTypes.OPEN : super.evaluateBlockPathType(reader, pos, typeIn);
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
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.target = this.hidebehind.getTarget();
            if(this.target == null || this.target.distanceToSqr(this.hidebehind) > (double) (this.maxTargetDistance * this.maxTargetDistance)) {
                return false;
            } else {
                Vec3 vec3d = DefaultRandomPos.getPosTowards(this.hidebehind, 16, 7, new Vec3(this.target.getX(), this.target.getY(), this.target.getZ()), Math.PI / 2D);
                return vec3d != null && hidebehind.isEntityAttackable(target) && hidebehind.attackSequenceTicks() <= 0 && !hidebehind.getHiding();
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !hidebehind.getHiding() && !this.hidebehind.getNavigation().isDone() && this.target.isAlive() && this.target.distanceToSqr(this.hidebehind) < (double) (this.maxTargetDistance * this.maxTargetDistance) && hidebehind.attackSequenceTicks() == 0 && hidebehind.isEntityAttackable(target);
        }

        @Override
        public void stop() {
            this.target = null;
            this.hidebehind.getNavigation().stop();
        }

        @Override
        public void start() {
            this.hidebehind.lookControl.setLookAt(this.target, 1000, 1000);
            this.hidebehind.getNavigation().moveTo(this.hidebehind.getTarget(), this.speed);
        }
    }

    @Override
    public EntityTypeContainer<EntityHidebehind> getContainer() {
        return ModEntities.HIDEBEHIND;
    }

    @Override
    public String[] getTypesFor(ResourceKey<Biome> biomeKey, Biome biome, Set<BiomeTypes.Type> types, MobSpawnType reason) {
        if(biomeKey == Biomes.OLD_GROWTH_SPRUCE_TAIGA) {
            return new String[] { "mega_taiga", "mega_taiga", "mega_taiga", "darkforest" };
        }
        if(types.contains(BiomeTypes.CONIFEROUS)) {
            return new String[] { "coniferous", "coniferous", "coniferous", "coniferous", "black", "darkforest" };
        }
        if(types.contains(BiomeTypes.FOREST)) {
            return new String[] { "forest", "black", "darkforest" };
        }
        return new String[] { "black", "coniferous", "darkforest", "forest", "mega_taiga" };
    }

    public static class HidebehindVariant extends EntityVariant {

        private final ResourceLocation openTexture;

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