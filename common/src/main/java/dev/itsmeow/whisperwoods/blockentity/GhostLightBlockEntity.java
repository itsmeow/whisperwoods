package dev.itsmeow.whisperwoods.blockentity;

import dev.itsmeow.whisperwoods.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GhostLightBlockEntity extends BlockEntity {

    public long lastSpawn;

    public GhostLightBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GHOST_LIGHT.get(), pos, state);
    }

}
