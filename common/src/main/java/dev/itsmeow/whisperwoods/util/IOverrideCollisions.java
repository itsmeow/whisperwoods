package dev.itsmeow.whisperwoods.util;

import dev.itsmeow.imdlib.entity.interfaces.IContainerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

public interface IOverrideCollisions<T extends Mob> extends IContainerEntity<T> {

    default boolean insideOpaque() {
        if (this.getImplementation().noPhysics) {
            return false;
        } else {
            float f = this.getImplementation().getType().getDimensions().width * 0.8F;
            AABB aABB = AABB.ofSize(this.getImplementation().getEyePosition(), f, 1.0E-6D, f);
            return BlockPos.betweenClosedStream(aABB).anyMatch((blockPos) -> {
                BlockState blockState = this.getImplementation().level().getBlockState(blockPos);
                return !blockState.isAir() && blockState.isSuffocating(this.getImplementation().level(), blockPos) && !preventSuffocation(blockState) && Shapes.joinIsNotEmpty(blockState.getCollisionShape(this.getImplementation().level(), blockPos).move(blockPos.getX(), blockPos.getY(), blockPos.getZ()), Shapes.create(aABB), BooleanOp.AND);
            });
        }
    }

    boolean canPassThrough(BlockState state);

    default boolean preventSuffocation(BlockState state) {
        return canPassThrough(state);
    }
}
