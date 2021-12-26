package dev.itsmeow.whisperwoods.blockentity;

import dev.itsmeow.whisperwoods.init.ModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GhostLightBlockEntity extends BlockEntity {

    public long lastSpawn;

    public GhostLightBlockEntity() {
        super(ModBlockEntities.GHOST_LIGHT.get());
    }

}
