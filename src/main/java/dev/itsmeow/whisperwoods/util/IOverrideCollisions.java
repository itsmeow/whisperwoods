package dev.itsmeow.whisperwoods.util;

import dev.itsmeow.imdlib.entity.interfaces.IContainerEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RewindableStream;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.stream.Stream;

public interface IOverrideCollisions<T extends Mob> extends IContainerEntity<T> {
    default Vec3 allowedMove(Vec3 vec) {
        AABB axisalignedbb = getImplementation().getBoundingBox();
        CollisionContext iselectioncontext = CollisionContext.of(getImplementation());
        VoxelShape voxelshape = getImplementation().level.getWorldBorder().getCollisionShape();
        // get world shape as stream
        Stream<VoxelShape> stream = Shapes.joinIsNotEmpty(voxelshape, Shapes.create(axisalignedbb.deflate(1.0E-7D)), BooleanOp.AND) ? Stream.empty() : Stream.of(voxelshape);
        // get entity shape as stream
        Stream<VoxelShape> stream1 = getImplementation().level.getEntityCollisions(getImplementation(), axisalignedbb.expandTowards(vec), (e) -> true);
        // concatenate and make reusable
        RewindableStream<VoxelShape> reusableStream = new RewindableStream<>(Stream.concat(stream1, stream));
        // return movement vector if it is 0, otherwise
        Vec3 vec3d = vec.lengthSqr() == 0.0D ? vec : transformMove(getImplementation(), vec, axisalignedbb, getImplementation().level, iselectioncontext, reusableStream);
        boolean flag = vec.x != vec3d.x; // if vector is transformed over x
        boolean flag1 = vec.y != vec3d.y; // if vector is transformed over y
        boolean flag2 = vec.z != vec3d.z; // if vector is transformed over z
        boolean flag3 = getImplementation().isOnGround() || flag1 && vec.y < 0.0D; // if on ground or transformed over y, and non-transformed vector is 0 (no planned y movement?)
        if (getImplementation().maxUpStep > 0.0F && flag3 && (flag || flag2)) { // if can step up and ^ and original vector transformed over either x or y
            Vec3 vec3d1 = transformMove(getImplementation(), new Vec3(vec.x, getImplementation().maxUpStep, vec.z), axisalignedbb, getImplementation().level, iselectioncontext, reusableStream);
            Vec3 vec3d2 = transformMove(getImplementation(), new Vec3(0.0D, getImplementation().maxUpStep, 0.0D), axisalignedbb.expandTowards(vec.x, 0.0D, vec.z), getImplementation().level, iselectioncontext, reusableStream);
            if (vec3d2.y < (double) getImplementation().maxUpStep) {
                Vec3 vec3d3 = transformMove(getImplementation(), new Vec3(vec.x, 0.0D, vec.z), axisalignedbb.move(vec3d2), getImplementation().level, iselectioncontext, reusableStream).add(vec3d2);
                if (Entity.getHorizontalDistanceSqr(vec3d3) > Entity.getHorizontalDistanceSqr(vec3d1)) {
                    vec3d1 = vec3d3;
                }
            }

            if (Entity.getHorizontalDistanceSqr(vec3d1) > Entity.getHorizontalDistanceSqr(vec3d)) {
                return vec3d1.add(transformMove(getImplementation(), new Vec3(0.0D, -vec3d1.y + vec.y, 0.0D), axisalignedbb.move(vec3d1), getImplementation().level, iselectioncontext, reusableStream));
            }
        }

        return vec3d;
    }

    default Vec3 transformMove(@Nullable Entity entity, Vec3 vec, AABB bb, Level world, CollisionContext context, RewindableStream<VoxelShape> stream) {
        boolean flag = vec.x == 0.0D;
        boolean flag1 = vec.y == 0.0D;
        boolean flag2 = vec.z == 0.0D;
        if ((!flag || !flag1) && (!flag || !flag2) && (!flag1 || !flag2)) { // if moving somehow
            RewindableStream<VoxelShape> reusableStream = new RewindableStream<>(Stream.concat(stream.getStream(), world.getBlockCollisions(entity, bb.expandTowards(vec))).filter(shape -> !canPassThrough(world.getBlockState(new BlockPos(shape.bounds().minX, shape.bounds().minY, shape.bounds().minZ)))));
            return Entity.collideBoundingBoxLegacy(vec, bb, reusableStream);
        } else {
            return Entity.collideBoundingBox(vec, bb, world, context, stream);
        }
    }

    default boolean insideOpaque() {
        if (this.getImplementation().noPhysics) {
            return false;
        } else {
            float f1 = this.getImplementation().getType().getDimensions().width * 0.8F;
            AABB axisalignedbb = AABB.ofSize(f1, 0.1F, f1).move(this.getImplementation().getX(), this.getImplementation().getEyeY(), this.getImplementation().getZ());
            return this.getImplementation().getCommandSenderWorld().getBlockCollisions(this.getImplementation(), axisalignedbb, (state, pos) -> state.isSuffocating(this.getImplementation().getCommandSenderWorld(), pos) && !preventSuffocation(state)).findAny().isPresent();
        }
    }

    boolean canPassThrough(BlockState state);

    default boolean preventSuffocation(BlockState state) {
        return canPassThrough(state);
    }
}
