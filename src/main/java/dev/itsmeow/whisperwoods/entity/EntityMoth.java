package dev.itsmeow.whisperwoods.entity;

import dev.itsmeow.imdlib.entity.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.EntityTypeContainerContainable;
import dev.itsmeow.imdlib.item.ItemModEntityContainer;
import dev.itsmeow.whisperwoods.init.ModEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class EntityMoth extends EntityAnimalWithTypesAndSizeContainable {

    private static final DataParameter<Integer> LANDED = EntityDataManager.createKey(EntityMoth.class, DataSerializers.VARINT);
    private static final EntityPredicate playerPredicate = (new EntityPredicate()).setDistance(4.0D).allowFriendlyFire().allowInvulnerable();
    private BlockPos targetPosition;

    public EntityMoth(EntityType<? extends EntityAnimalWithTypesAndSizeContainable> type, World worldIn) {
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
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isLanded()) {
            this.setMotion(Vector3d.ZERO);
            if(Direction.byIndex(this.getLandedInteger()) != Direction.DOWN) {
                double x = Math.floor(this.getPosX()) + 0.5D;
                double z = Math.floor(this.getPosZ()) + 0.5D;
                BlockPos pos = new BlockPos(x, Math.floor(this.getPosY()) + 0.5D, z);
                BlockPos offset = pos.offset(Direction.byIndex(this.getLandedInteger()));
                BlockPos diff = pos.subtract(offset);
                this.setPositionAndUpdate(x - ((double) diff.getX()) / 2.778D, Math.floor(this.getPosY()) + 0.5D, z - ((double) diff.getZ()) / 2.778D);
                this.rotationYaw = 0;
                this.rotationYawHead = 0;
            } else {
                this.setPositionAndUpdate(this.getPosX(), Math.floor(this.getPosY()), this.getPosZ());
            }
        } else {
            this.setMotion(this.getMotion().mul(1.0D, 0.6D, 1.0D));
        }

    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        BlockPos blockpos = this.getPosition();
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
            BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();
            BlockPos destinationBlock = null;
            if(this.isAttractedToLight()) {
                for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                    for(int l = 0; l < i; ++l) {
                        for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                            for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                                blockpos$mutableblockpos.setPos(this.getPosition()).move(i1, k - 1, j1);
                                BlockState state = world.getBlockState(blockpos$mutableblockpos);
                                if(isLightBlock(state) && (destinationBlock == null || state.getLightValue() >= world.getBlockState(destinationBlock).getLightValue())) {
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
                    this.targetPosition = new BlockPos(this.getPosX() + (double) this.rand.nextInt(5) - (double) this.rand.nextInt(5), this.getPosY() + (double) this.rand.nextInt(4) - 1.0D, this.getPosZ() + (double) this.rand.nextInt(5) - (double) this.rand.nextInt(5));
                }
            }
        }
        if(!this.isLanded() && targetPosition != null) {
            double d0 = (double) this.targetPosition.getX() + 0.5D - this.getPosX();
            double d1 = (double) this.targetPosition.getY() + 0.1D - this.getPosY();
            double d2 = (double) this.targetPosition.getZ() + 0.5D - this.getPosZ();
            Vector3d vec3d = this.getMotion();
            Vector3d vec3d1 = vec3d.add((Math.signum(d0) * 0.5D - vec3d.x) * (double) 0.1F, (Math.signum(d1) * (double) 0.7F - vec3d.y) * (double) 0.1F, (Math.signum(d2) * 0.5D - vec3d.z) * (double) 0.1F);
            this.setMotion(vec3d1);
            float f = (float) (MathHelper.atan2(vec3d1.z, vec3d1.x) * (double) (180F / (float) Math.PI)) - 90.0F;
            float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
            this.moveForward = 0.5F;
            this.rotationYaw += f1;
        }
        int moths_req = getContainer().getCustomConfiguration().getInt("moths_to_destroy_torch");
        if(moths_req != 0 && world.getBlockState(this.getPosition()).getBlock() instanceof TorchBlock && world.getEntitiesWithinAABB(EntityMoth.class, this.getBoundingBox()).size() >= moths_req && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
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
        return blockState.getLightValue() > 0;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
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
    public EntityTypeContainer<EntityMoth> getContainer() {
        return ModEntities.MOTH;
    }

    @Override
    protected EntityAnimalWithTypes getBaseChild() {
        return null;
    }

    @Override
    public EntityTypeContainerContainable<EntityMoth, ItemModEntityContainer<EntityMoth>> getContainableContainer() {
        return ModEntities.MOTH;
    }

    public static void bottleTooltip(EntityTypeContainer<? extends MobEntity> container, ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip) {
        CompoundNBT tag = stack.getTag();
        if(tag != null) {
            if(tag.contains("SizeTag", Constants.NBT.TAG_FLOAT)) {
                tooltip.add(new StringTextComponent("Size: " + tag.getFloat("SizeTag")).setStyle(Style.EMPTY.mergeWithFormatting(TextFormatting.ITALIC, TextFormatting.GRAY)));
            }
        }
    }
}
