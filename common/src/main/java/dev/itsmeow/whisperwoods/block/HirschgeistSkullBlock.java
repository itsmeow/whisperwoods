package dev.itsmeow.whisperwoods.block;

import dev.itsmeow.imdlib.block.AnimalSkullBlock;
import dev.itsmeow.whisperwoods.blockentity.HGSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HirschgeistSkullBlock extends AnimalSkullBlock implements EntityBlock {
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HGSkullBlockEntity(pos, state);
    }
}
