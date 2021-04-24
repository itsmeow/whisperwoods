package dev.itsmeow.whisperwoods.util;

import dev.itsmeow.imdlib.entity.interfaces.IContainerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ReuseableStream;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public interface IOverrideCollisions<T extends MobEntity> extends IContainerEntity<T> {
    default Vector3d allowedMove(Vector3d vec) {
        AxisAlignedBB axisalignedbb = getImplementation().getBoundingBox();
        ISelectionContext iselectioncontext = ISelectionContext.forEntity(getImplementation());
        VoxelShape voxelshape = getImplementation().world.getWorldBorder().getShape();
        // get world shape as stream
        Stream<VoxelShape> stream = VoxelShapes.compare(voxelshape, VoxelShapes.create(axisalignedbb.shrink(1.0E-7D)), IBooleanFunction.AND) ? Stream.empty() : Stream.of(voxelshape);
        // get entity shape as stream
        Stream<VoxelShape> stream1 = getImplementation().world.func_230318_c_(getImplementation(), axisalignedbb.expand(vec), (e) -> true);
        // concatenate and make reusable
        ReuseableStream<VoxelShape> reusableStream = new ReuseableStream<>(Stream.concat(stream1, stream));
        // return movement vector if it is 0, otherwise
        Vector3d vec3d = vec.lengthSquared() == 0.0D ? vec : transformMove(getImplementation(), vec, axisalignedbb, getImplementation().world, iselectioncontext, reusableStream);
        boolean flag = vec.x != vec3d.x; // if vector is transformed over x
        boolean flag1 = vec.y != vec3d.y; // if vector is transformed over y
        boolean flag2 = vec.z != vec3d.z; // if vector is transformed over z
        boolean flag3 = getImplementation().isOnGround() || flag1 && vec.y < 0.0D; // if on ground or transformed over y, and non-transformed vector is 0 (no planned y movement?)
        if (getImplementation().stepHeight > 0.0F && flag3 && (flag || flag2)) { // if can step up and ^ and original vector transformed over either x or y
            Vector3d vec3d1 = transformMove(getImplementation(), new Vector3d(vec.x, getImplementation().stepHeight, vec.z), axisalignedbb, getImplementation().world, iselectioncontext, reusableStream);
            Vector3d vec3d2 = transformMove(getImplementation(), new Vector3d(0.0D, getImplementation().stepHeight, 0.0D), axisalignedbb.expand(vec.x, 0.0D, vec.z), getImplementation().world, iselectioncontext, reusableStream);
            if (vec3d2.y < (double) getImplementation().stepHeight) {
                Vector3d vec3d3 = transformMove(getImplementation(), new Vector3d(vec.x, 0.0D, vec.z), axisalignedbb.offset(vec3d2), getImplementation().world, iselectioncontext, reusableStream).add(vec3d2);
                if (Entity.horizontalMag(vec3d3) > Entity.horizontalMag(vec3d1)) {
                    vec3d1 = vec3d3;
                }
            }

            if (Entity.horizontalMag(vec3d1) > Entity.horizontalMag(vec3d)) {
                return vec3d1.add(transformMove(getImplementation(), new Vector3d(0.0D, -vec3d1.y + vec.y, 0.0D), axisalignedbb.offset(vec3d1), getImplementation().world, iselectioncontext, reusableStream));
            }
        }

        return vec3d;
    }

    default Vector3d transformMove(@Nullable Entity entity, Vector3d vec, AxisAlignedBB bb, World world, ISelectionContext context, ReuseableStream<VoxelShape> stream) {
        boolean flag = vec.x == 0.0D;
        boolean flag1 = vec.y == 0.0D;
        boolean flag2 = vec.z == 0.0D;
        if ((!flag || !flag1) && (!flag || !flag2) && (!flag1 || !flag2)) { // if moving somehow
            ReuseableStream<VoxelShape> reusableStream = new ReuseableStream<>(Stream.concat(stream.createStream(), world.getBlockCollisionShapes(entity, bb.expand(vec))).filter(shape -> !canPassThrough(world.getBlockState(new BlockPos(shape.getBoundingBox().minX, shape.getBoundingBox().minY, shape.getBoundingBox().minZ)))));
            return Entity.collideBoundingBox(vec, bb, reusableStream);
        } else {
            return Entity.getAllowedMovement(vec, bb, world, context, stream);
        }
    }

    default boolean insideOpaque() {
        if (this.getImplementation().noClip) {
            return false;
        } else {
            float f1 = this.getImplementation().getType().getSize().width * 0.8F;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.withSizeAtOrigin(f1, 0.1F, f1).offset(this.getImplementation().getPosX(), this.getImplementation().getPosYEye(), this.getImplementation().getPosZ());
            return this.getImplementation().getEntityWorld().func_241457_a_(this.getImplementation(), axisalignedbb, (state, pos) -> state.isSuffocating(this.getImplementation().getEntityWorld(), pos) && !preventSuffocation(state)).findAny().isPresent();
        }
    }

    boolean canPassThrough(BlockState state);

    default boolean preventSuffocation(BlockState state) {
        return canPassThrough(state);
    }
}
