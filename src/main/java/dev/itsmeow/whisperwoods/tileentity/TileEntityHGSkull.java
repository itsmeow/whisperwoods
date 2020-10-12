package dev.itsmeow.whisperwoods.tileentity;

import dev.itsmeow.imdlib.block.BlockAnimalSkull;
import dev.itsmeow.whisperwoods.init.ModTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class TileEntityHGSkull extends TileEntity {

    public TileEntityHGSkull() {
        super(ModTileEntities.HG_SKULL.get());
    }

    public Direction getDirection() {
        return this.getBlockState().get(BlockAnimalSkull.FACING_EXCEPT_DOWN);
    }

    public Direction getTopDirection() {
        return this.getBlockState().get(BlockAnimalSkull.TOP_FACING);
    }

    public float getTopRotation() {
        return this.getTopDirection().getHorizontalAngle();
    }
}
