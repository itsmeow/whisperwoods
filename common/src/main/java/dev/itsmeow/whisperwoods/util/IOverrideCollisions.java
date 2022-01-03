package dev.itsmeow.whisperwoods.util;

import dev.itsmeow.imdlib.entity.interfaces.IContainerEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public interface IOverrideCollisions<T extends Mob> extends IContainerEntity<T> {

    default boolean insideOpaque() {
        if (this.getImplementation().noPhysics) {
            return false;
        } else {
            float f1 = this.getImplementation().getType().getDimensions().width * 0.8F;
            AABB axisalignedbb = AABB.ofSize(this.getImplementation().getEyePosition(), f1, 0.1F, f1);
            return this.getImplementation().getCommandSenderWorld().getBlockCollisions(this.getImplementation(), axisalignedbb, (state, pos) -> state.isSuffocating(this.getImplementation().getCommandSenderWorld(), pos) && !preventSuffocation(state)).findAny().isPresent();
        }
    }

    boolean canPassThrough(BlockState state);

    default boolean preventSuffocation(BlockState state) {
        return canPassThrough(state);
    }
}
