package dev.itsmeow.whisperwoods.entity;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.whisperwoods.block.BlockGhostLight;
import dev.itsmeow.whisperwoods.init.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class EntityMoth extends EntityAnimalWithTypesAndSize {

    private static final DataParameter<Integer> LANDED = EntityDataManager.createKey(EntityMoth.class, DataSerializers.VARINT);
    private static final EntityPredicate playerPredicate = (new EntityPredicate()).setDistance(4.0D).allowFriendlyFire().allowInvulnerable();
    private BlockPos targetPosition;
    public static int MOTHS_REQUIRED_TO_DESTROY = 5;

    public EntityMoth(World worldIn) {
        this(ModEntities.MOTH.entityType, worldIn);
    }

    protected EntityMoth(EntityType<? extends EntityAnimalWithTypesAndSize> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(LANDED, 1);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0D);
    }

    public boolean isLanded() {
        return this.dataManager.get(LANDED) != 1;
    }

    public int getLandedInteger() {
        return this.dataManager.get(LANDED);
    }

    public void setLanded(Direction direction) {
        if(direction == Direction.UP) {
            throw new RuntimeException("Invalid landing direction!");
        }
        this.dataManager.set(LANDED, direction.ordinal());
    }

    public void setNotLanded() {
        this.dataManager.set(LANDED, 1);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isLanded()) {
            this.setMotion(Vec3d.ZERO);
            if(Direction.byIndex(this.getLandedInteger()) != Direction.DOWN) {
                double x = Math.floor(this.posX) + 0.5D;
                double z = Math.floor(this.posZ) + 0.5D;
                this.posY = Math.floor(this.posY) + 0.5D;
                BlockPos pos = new BlockPos(x, posY, z);
                BlockPos offset = pos.offset(Direction.byIndex(this.getLandedInteger()));
                BlockPos diff = pos.subtract(offset);
                this.posX = x - ((double) diff.getX()) / 2.778D;
                this.posZ = z - ((double) diff.getZ()) / 2.778D;
                this.rotationYaw = 0;
                this.rotationYawHead = 0;
            } else {
                this.posY = Math.floor(this.posY);
            }
        } else {
            this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
        }

    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        BlockPos blockpos = new BlockPos(this);
        if(this.isLanded()) {
            BlockPos offset = blockpos.offset(Direction.byIndex(this.getLandedInteger()));
            if(this.world.getBlockState(offset).isNormalCube(this.world, offset)) {
                if(this.world.getClosestPlayer(playerPredicate, this) != null || this.getRNG().nextInt(this.isAttractedToLight() ? 500 : 1000) == 0) {
                    this.setNotLanded();
                }
            } else {
                this.setNotLanded();
            }
        }
        if(this.targetPosition == null || this.rand.nextInt(30) == 0 || (this.targetPosition.withinDistance(this.getPositionVec(), 1.0D) && !isLightBlock(world.getBlockState(this.targetPosition)))) {
            int i = 12;
            int j = 2;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            BlockPos destinationBlock = null;
            if(this.isAttractedToLight()) {
                for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for(int l = 0; l < i; ++l) {
                        for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                            for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                                blockpos$mutableblockpos.setPos(this.getPosition()).move(i1, k - 1, j1);
                                if(isLightBlock(world.getBlockState(blockpos$mutableblockpos))) {
                                    destinationBlock = blockpos$mutableblockpos.toImmutable();
                                }
                            }
                        }
                    }
                }
            }
            if(destinationBlock != null) {
                this.targetPosition = destinationBlock;
                this.setNotLanded();
            } else {
                boolean found = false;
                if(this.isAttractedToLight()) {
                    for(LivingEntity entity : world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(10))) {
                        for(Hand hand : Hand.values()) {
                            Item held = entity.getHeldItem(hand).getItem();
                            if(held == Items.TORCH || held == Items.LANTERN) {
                                this.targetPosition = entity.getPosition().add(0, 1.5, 0);
                                found = true;
                                this.setNotLanded();
                            }
                        }
                    }
                }
                if(!found && this.world.getClosestPlayer(playerPredicate, this) == null && this.getRNG().nextInt(this.isAttractedToLight() ? 80 : 30) == 0) {
                    for(Direction direction : Direction.values()) {
                        if(direction != Direction.UP) {
                            BlockPos offset = blockpos.offset(direction);
                            if(world.getBlockState(offset).isNormalCube(world, offset)) {
                                this.setLanded(direction);
                                this.targetPosition = null;
                                found = true;
                            }
                        }
                    }
                }
                if(!found) {
                    this.targetPosition = new BlockPos(this.posX + (double) this.rand.nextInt(5) - (double) this.rand.nextInt(5), this.posY + (double) this.rand.nextInt(4) - 1.0D, this.posZ + (double) this.rand.nextInt(5) - (double) this.rand.nextInt(5));
                }
            }
        }
        if(!this.isLanded() && targetPosition != null) {
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
        if(MOTHS_REQUIRED_TO_DESTROY != 0 && world.getBlockState(this.getPosition()).getBlock() instanceof TorchBlock && world.getEntitiesWithinAABB(EntityMoth.class, this.getBoundingBox()).size() >= MOTHS_REQUIRED_TO_DESTROY && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
            BlockState state = world.getBlockState(this.getPosition());
            Block.spawnDrops(state, world, this.getPosition());
            world.setBlockState(this.getPosition(), Blocks.AIR.getDefaultState());
        }
    }

    public boolean isAttractedToLight() {
        long time = this.world.getDayTime() % 24000L;
        return world.getLightFor(LightType.SKY, this.getPosition()) < 10 || (time >= 13000L && time <= 23000L);
    }

    private static boolean isLightBlock(BlockState blockState) {
        Block block = blockState.getBlock();
        return block instanceof BlockGhostLight || block instanceof LanternBlock || block instanceof TorchBlock || (block instanceof CampfireBlock && blockState.get(CampfireBlock.LIT)) || block == Blocks.LAVA || block == Blocks.GLOWSTONE || block == Blocks.SEA_LANTERN || block == Blocks.JACK_O_LANTERN || block == Blocks.FIRE || (block instanceof RedstoneLampBlock && blockState.get(RedstoneLampBlock.LIT));
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(this.isInvulnerableTo(source)) {
            return false;
        } else {
            if(!this.world.isRemote && this.isLanded()) {
                this.setNotLanded();
            }
            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(LANDED, compound.getInt("Landed"));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Landed", this.dataManager.get(LANDED));
    }

    @Override
    protected float getRandomizedSize() {
        return (this.rand.nextInt(30) + 1F) / 100F + 0.15F;
    }

    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

    @Override
    public EntityTypeContainer<?> getContainer() {
        return ModEntities.MOTH;
    }

    @Override
    protected EntityAnimalWithTypes getBaseChild() {
        return null;
    }

}
