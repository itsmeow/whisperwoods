package dev.itsmeow.whisperwoods.mixin;

import dev.itsmeow.whisperwoods.util.IOverrideCollisions;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RewindableStream;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.stream.Stream;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(at = @At(value = "HEAD"), method = "collideBoundingBoxHeuristically(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/shapes/CollisionContext;Lnet/minecraft/util/RewindableStream;)Lnet/minecraft/world/phys/Vec3;", cancellable = true)
    private static void collideBoundingBoxHeuristically(Entity entity, Vec3 vec3, AABB aABB, Level level, CollisionContext collisionContext, RewindableStream<VoxelShape> rewindableStream, CallbackInfoReturnable<Vec3> cir) {
        if(entity instanceof IOverrideCollisions) {
            boolean bl = vec3.x == 0.0D;
            boolean bl2 = vec3.y == 0.0D;
            boolean bl3 = vec3.z == 0.0D;
            if ((!bl || !bl2) && (!bl || !bl3) && (!bl2 || !bl3)) {
                RewindableStream<VoxelShape> rewindableStream2 = new RewindableStream(Stream.concat(rewindableStream.getStream(), level.getBlockCollisions(entity, aABB.expandTowards(vec3)).filter(shape -> !((IOverrideCollisions) entity).canPassThrough(level.getBlockState(new BlockPos(shape.bounds().minX, shape.bounds().minY, shape.bounds().minZ))))));
                cir.setReturnValue(Entity.collideBoundingBoxLegacy(vec3, aABB, rewindableStream2));
                cir.cancel();
            }
        }
    }

}
