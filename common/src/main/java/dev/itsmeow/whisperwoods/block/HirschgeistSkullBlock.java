package dev.itsmeow.whisperwoods.block;

import dev.itsmeow.imdlib.block.AnimalSkullBlock;
import dev.itsmeow.whisperwoods.blockentity.HGSkullBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class HirschgeistSkullBlock extends AnimalSkullBlock implements EntityBlock {
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new HGSkullBlockEntity();
    }
}
