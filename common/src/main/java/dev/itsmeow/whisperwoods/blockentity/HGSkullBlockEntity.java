package dev.itsmeow.whisperwoods.blockentity;

import dev.itsmeow.imdlib.block.AnimalSkullBlock;
import dev.itsmeow.whisperwoods.init.ModBlockEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HGSkullBlockEntity extends BlockEntity {

    public HGSkullBlockEntity() {
        super(ModBlockEntities.HG_SKULL.get());
    }

    public Direction getDirection() {
        return this.getBlockState().getValue(AnimalSkullBlock.FACING_EXCEPT_DOWN);
    }

    public Direction getTopDirection() {
        return this.getBlockState().getValue(AnimalSkullBlock.TOP_FACING);
    }

    public float getTopRotation() {
        return this.getTopDirection().toYRot();
    }
}
