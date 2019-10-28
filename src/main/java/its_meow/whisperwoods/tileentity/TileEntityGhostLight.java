package its_meow.whisperwoods.tileentity;

import its_meow.whisperwoods.init.ModTileEntities;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGhostLight extends TileEntity {

    public long lastSpawn;

    public TileEntityGhostLight() {
        super(ModTileEntities.GHOST_LIGHT);
    }

}
