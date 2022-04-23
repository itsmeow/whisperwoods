package dev.itsmeow.whisperwoods.mixin;

import dev.itsmeow.whisperwoods.util.IOverrideCollisions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GroundPathNavigation.class)
public abstract class GroundPathNavigationMixin extends PathNavigation {

    public GroundPathNavigationMixin(Mob mob, Level level) {
        super(mob, level);
    }

    @Inject(at = @At("HEAD"), method = "canWalkAbove(IIIIIILnet/minecraft/world/phys/Vec3;DD)Z", cancellable = true)
    public void canWalkAbove(int i, int j, int k, int l, int m, int n, Vec3 vec3, double d, double e, CallbackInfoReturnable<Boolean> cir) {
        if(this.mob instanceof IOverrideCollisions) {
            for (BlockPos blockpos : BlockPos.betweenClosed(new BlockPos(i, j, k), new BlockPos(i + l - 1, j + m - 1, k + n - 1))) {
                double d0 = (double) blockpos.getX() + 0.5D - vec3.x;
                double d1 = (double) blockpos.getZ() + 0.5D - vec3.z;
                BlockState state = this.level.getBlockState(blockpos);
                if (!(d0 * d + d1 * e < 0.0D) && !(state.isPathfindable(this.level, blockpos, PathComputationType.LAND) || ((IOverrideCollisions)this.mob).canPassThrough(state))) {
                    cir.setReturnValue(false);
                    cir.cancel();
                    return;
                }
            }
            cir.setReturnValue(true);
            cir.cancel();
            return;
        }
    }
}
