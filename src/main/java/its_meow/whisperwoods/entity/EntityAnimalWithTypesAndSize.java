package its_meow.whisperwoods.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public abstract class EntityAnimalWithTypesAndSize extends EntityAnimalWithTypes {
    
    protected static final DataParameter<Float> SIZE = EntityDataManager.<Float>createKey(EntityAnimalWithTypesAndSize.class, DataSerializers.FLOAT);
    
    public EntityAnimalWithTypesAndSize(EntityType<? extends EntityAnimalWithTypes> entityType, World worldIn) {
        super(entityType, worldIn);
        this.setSize(0.35F, 0.35F);
    }
    
    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SIZE, Float.valueOf(1));
    }

    @Override
    public EntitySize getSize(Pose pose) {
        float size = this.dataManager.get(SIZE).floatValue();
        return EntitySize.flexible(size, size).scale(this.getRenderScale());
    }

    public void setSize(float width, float height) {
        this.dataManager.set(SIZE, Float.valueOf(width));
    }

    @Override
    public boolean writeUnlessRemoved(CompoundNBT compound) {
        compound.putFloat("Size", this.getSize(Pose.STANDING).width);
        return super.writeUnlessRemoved(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        float size = compound.getFloat("Size");
        this.setSize(size, size);
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, CompoundNBT compound) {
        livingdata = super.onInitialSpawn(world, difficulty, reason, livingdata, compound);
        if (!this.isChild()) {
            int i = this.getRandomType();
            float rand = this.getRandomizedSize();

            if (livingdata instanceof SizeTypeData) {
                i = ((SizeTypeData) livingdata).typeData;
                rand = ((SizeTypeData) livingdata).size;
            } else {
                livingdata = new SizeTypeData(i, rand);
            }

            this.setType(i);
            this.setSize(rand, rand);
        }
        this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0F);
        return livingdata;
    }

    protected abstract float getRandomizedSize();

    public static class SizeTypeData implements ILivingEntityData {

        public int typeData;
        public float size;

        public SizeTypeData(int type, float size) {
            this.typeData = type;
            this.size = size;
        }
    }

}
