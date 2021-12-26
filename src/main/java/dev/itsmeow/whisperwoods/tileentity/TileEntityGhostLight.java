package dev.itsmeow.whisperwoods.tileentity;

import dev.itsmeow.whisperwoods.init.ModTileEntities;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TileEntityGhostLight extends BlockEntity {

    public long lastSpawn;

    public TileEntityGhostLight() {
        super(ModTileEntities.GHOST_LIGHT.get());
    }

}
