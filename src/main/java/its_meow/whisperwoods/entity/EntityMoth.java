package its_meow.whisperwoods.entity;

import its_meow.whisperwoods.init.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class EntityMoth extends EntityAnimalWithTypesAndSize {

    public EntityMoth(World worldIn) {
        super(ModEntities.MOTH.entityType, worldIn);
    }

    protected EntityMoth(EntityType<? extends EntityAnimalWithTypesAndSize> type, World worldIn) {
        super(type, worldIn);
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

}
