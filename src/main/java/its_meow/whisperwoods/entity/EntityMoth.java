package its_meow.whisperwoods.entity;

import its_meow.whisperwoods.init.ModEntities;
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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class EntityMoth extends EntityAnimalWithTypesAndSize {

    private static final DataParameter<Byte> LANDED = EntityDataManager.createKey(EntityMoth.class, DataSerializers.BYTE);
    private static final EntityPredicate playerPredicate = (new EntityPredicate()).setDistance(4.0D).allowFriendlyFire();
    private BlockPos targetPosition;

    public EntityMoth(World worldIn) {
        super(ModEntities.MOTH.entityType, worldIn);
    }

    protected EntityMoth(EntityType<? extends EntityAnimalWithTypesAndSize> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(LANDED, (byte)0);
    }

    public boolean canBePushed() {
        return false;
    }

    protected void collideWithEntity(Entity entityIn) {}

    protected void collideWithNearbyEntities() {}

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2.0D);
    }

    public boolean isLanded() {
        return (this.dataManager.get(LANDED) & 1) != 0;
    }

    public void setLanded(boolean landed) {
        byte b0 = this.dataManager.get(LANDED);
        if (landed) {
            this.dataManager.set(LANDED, (byte)(b0 | 1));
        } else {
            this.dataManager.set(LANDED, (byte)(b0 & -2));
        }

    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (this.isLanded()) {
            this.setMotion(Vec3d.ZERO);
            this.posY = (double)MathHelper.floor(this.posY) + 1.0D - (double)this.getHeight();
        } else {
            this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
        }

    }

    protected void updateAITasks() {
        super.updateAITasks();
        BlockPos blockpos = new BlockPos(this);
        BlockPos blockpos1 = blockpos.add(this.getRNG().nextInt(3) - 1, 0, this.getRNG().nextInt(3) - 1);
        if (this.isLanded()) {
            if (this.world.getBlockState(blockpos1).isNormalCube(this.world, blockpos)) {
                if (this.rand.nextInt(200) == 0) {
                    this.rotationYawHead = (float)this.rand.nextInt(360);
                }

                if (this.world.getClosestPlayer(playerPredicate, this) != null) {
                    this.setLanded(false);
                }
            } else {
                this.setLanded(false);
            }
        } else {
            if (this.targetPosition != null && (!this.world.isAirBlock(this.targetPosition) || this.targetPosition.getY() < 1)) {
                this.targetPosition = null;
            }

            if (this.targetPosition == null || this.rand.nextInt(30) == 0 || (this.targetPosition.withinDistance(this.getPositionVec(), 1.0D) && !isLightBlock(world.getBlockState(this.targetPosition)))) {
                /* Map<BlockPos, Integer> lightValues = new HashMap<BlockPos, Integer>();
                for(Direction direction : Direction.values()) {
                    BlockPos pos = blockpos.offset(direction);
                    pos = pos.subtract(blockpos).add(pos.subtract(blockpos)).add(blockpos);
                    lightValues.put(pos, world.getLightValue(pos));
                }

                Map.Entry<BlockPos, Integer> maxEntry = null;
                for(Map.Entry<BlockPos, Integer> entry : lightValues.entrySet()) {
                    if(maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0 || isLightBlock(world.getBlockState(entry.getKey())) && !isLightBlock(world.getBlockState(maxEntry.getKey()))) {
                        maxEntry = entry;
                    }
                }
                if(maxEntry != null && maxEntry.getValue() > 0 && maxEntry.getValue() > world.getLightValue(blockpos)) {
                    this.targetPosition = maxEntry.getKey().subtract(blockpos).add(maxEntry.getKey().subtract(blockpos)).add(blockpos);
                } else {
                    this.targetPosition = new BlockPos(this.posX + (double)this.rand.nextInt(5) - (double)this.rand.nextInt(5), this.posY + (double)this.rand.nextInt(4) - 1.0D, this.posZ + (double)this.rand.nextInt(5) - (double)this.rand.nextInt(5));
                }*/
                int i = 12;
                int j = 2;
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                BlockPos destinationBlock = null;
                long time = this.world.getDayTime() % 24000L;
                if(world.getLightFor(LightType.SKY, this.getPosition()) < 10 || (time >= 13000L && time <= 23000L)) {
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
                } else {
                    this.targetPosition = new BlockPos(this.posX + (double)this.rand.nextInt(5) - (double)this.rand.nextInt(5), this.posY + (double)this.rand.nextInt(4) - 1.0D, this.posZ + (double)this.rand.nextInt(5) - (double)this.rand.nextInt(5));
                }
            }
            if(targetPosition != null) {
                double d0 = (double)this.targetPosition.getX() + 0.5D - this.posX;
                double d1 = (double)this.targetPosition.getY() + 0.1D - this.posY;
                double d2 = (double)this.targetPosition.getZ() + 0.5D - this.posZ;
                Vec3d vec3d = this.getMotion();
                Vec3d vec3d1 = vec3d.add((Math.signum(d0) * 0.5D - vec3d.x) * (double)0.1F, (Math.signum(d1) * (double)0.7F - vec3d.y) * (double)0.1F, (Math.signum(d2) * 0.5D - vec3d.z) * (double)0.1F);
                this.setMotion(vec3d1);
                float f = (float)(MathHelper.atan2(vec3d1.z, vec3d1.x) * (double)(180F / (float)Math.PI)) - 90.0F;
                float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
                this.moveForward = 0.5F;
                this.rotationYaw += f1;
                if (this.rand.nextInt(100) == 0 && this.world.getBlockState(blockpos1).isNormalCube(this.world, blockpos1)) {
                    this.setLanded(true);
                }
            }
            if(world.getBlockState(this.getPosition()).getBlock() instanceof TorchBlock && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this) && world.getEntitiesWithinAABB(EntityMoth.class, this.getBoundingBox()).size() >= 5) {
                BlockState state = world.getBlockState(this.getPosition());
                Block.spawnDrops(state,  world, this.getPosition());
                world.setBlockState(this.getPosition(), Blocks.AIR.getDefaultState());
            }
        }

    }

    private static boolean isLightBlock(BlockState blockState) {
        Block block = blockState.getBlock();
        return block instanceof LanternBlock || block instanceof TorchBlock || (block instanceof CampfireBlock && blockState.get(CampfireBlock.LIT)) || block == Blocks.LAVA || block == Blocks.GLOWSTONE || block == Blocks.SEA_LANTERN || block == Blocks.JACK_O_LANTERN || block == Blocks.FIRE || (block instanceof RedstoneLampBlock && blockState.get(RedstoneLampBlock.LIT));
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public void fall(float distance, float damageMultiplier) {}

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {}

    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.world.isRemote && this.isLanded()) {
                this.setLanded(false);
            }
            return super.attackEntityFrom(source, amount);
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(LANDED, compound.getByte("Landed"));
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putByte("Landed", this.dataManager.get(LANDED));
    }

    @Override
    public int getVariantMax() {
        return 8;
    }

    @Override
    protected IVariantTypes getBaseChild() {
        return null;
    }

    @Override
    protected String getContainerName() {
        return "moth";
    }

    @Override
    public ITextComponent getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    protected float getRandomizedSize() {
        return (this.rand.nextInt(30) + 1F) / 100F + 0.15F;
    }

    public boolean canBeLeashedTo(PlayerEntity player) {
        return false;
    }

}
