package dev.itsmeow.whisperwoods.tileentity;

import dev.itsmeow.imdlib.block.BlockAnimalSkull;
import dev.itsmeow.whisperwoods.init.ModTileEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TileEntityHGSkull extends BlockEntity {

    public TileEntityHGSkull() {
        super(ModTileEntities.HG_SKULL.get());
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(BlockAnimalSkull.FACING_EXCEPT_DOWN);
    }

    public Direction getTopDirection() {
        return this.getBlockState().getValue(BlockAnimalSkull.TOP_FACING);
    }

    public float getTopRotation() {
        return this.getTopDirection().toYRot();
    }
}
