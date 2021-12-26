package dev.itsmeow.whisperwoods.entity;

import dev.itsmeow.imdlib.entity.interfaces.ISelectiveVariantTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class EntityCreatureWithSelectiveTypes extends EntityCreatureWithTypes implements ISelectiveVariantTypes<EntityCreatureWithTypes> {

    public EntityCreatureWithSelectiveTypes(EntityType<? extends EntityCreatureWithSelectiveTypes> type, Level world) {
        super(type, world);
    }

}
