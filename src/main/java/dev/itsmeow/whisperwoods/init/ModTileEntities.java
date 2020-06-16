package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.tileentity.TileEntityGhostLight;
import net.minecraft.tileentity.TileEntityType;

public class ModTileEntities {
    
    public static final TileEntityType<TileEntityGhostLight> GHOST_LIGHT = TileEntityType.Builder.create(TileEntityGhostLight::new, 
    ModBlocks.GHOST_LIGHT_ELECTRIC_BLUE, ModBlocks.GHOST_LIGHT_FIERY_ORANGE, ModBlocks.GHOST_LIGHT_GOLD, 
    ModBlocks.GHOST_LIGHT_MAGIC_PURPLE, ModBlocks.GHOST_LIGHT_TOXIC_GREEN).build(null);
    static {
        GHOST_LIGHT.setRegistryName(WhisperwoodsMod.MODID, "ghost_light_tile");
    }
}
