package dev.itsmeow.whisperwoods.entity;

import dev.itsmeow.imdlib.entity.util.ISelectiveVariantTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public abstract class EntityCreatureWithSelectiveTypes extends EntityCreatureWithTypes implements ISelectiveVariantTypes<EntityCreatureWithTypes> {

    public EntityCreatureWithSelectiveTypes(EntityType<? extends EntityCreatureWithSelectiveTypes> type, World world) {
        super(type, world);
    }

}
