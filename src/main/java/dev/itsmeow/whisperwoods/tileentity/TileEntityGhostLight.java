package dev.itsmeow.whisperwoods.tileentity;

import dev.itsmeow.whisperwoods.init.ModTileEntities;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGhostLight extends TileEntity {

    public long lastSpawn;

    public TileEntityGhostLight() {
        super(ModTileEntities.GHOST_LIGHT);
    }

}
