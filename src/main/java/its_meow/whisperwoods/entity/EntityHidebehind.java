package its_meow.whisperwoods.entity;

import its_meow.whisperwoods.init.ModEntities;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

public class EntityHidebehind extends AnimalEntity {

    protected EntityHidebehind(EntityType<? extends EntityHidebehind> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityHidebehind(World worldIn) {
        super(ModEntities.HIDEBEHIND.entityType, worldIn);
    }

    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    @Override
    public boolean canDespawn(double range) {
        return ModEntities.ENTITIES.containsKey("hidebehind") ? ModEntities.ENTITIES.get("hidebehind").despawn : false;
    }

}
