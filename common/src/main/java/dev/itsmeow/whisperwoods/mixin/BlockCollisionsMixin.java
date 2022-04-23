package dev.itsmeow.whisperwoods.mixin;

import dev.itsmeow.whisperwoods.util.IOverrideCollisions;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockCollisions.class)
public class BlockCollisionsMixin {

    @Shadow
    @Final
    private CollisionContext context;

    @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/BlockGetter;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"), method = "computeNext()Lnet/minecraft/world/phys/shapes/VoxelShape;", cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void computeNext(CallbackInfoReturnable<VoxelShape> cir, int i, int j, int k, int l, BlockGetter blockGetter, BlockState blockState) {
        if(this.context instanceof EntityCollisionContext eContext) {
            if(eContext.getEntity() instanceof IOverrideCollisions o) {
                if(o.canPassThrough(blockState)) {
                    cir.setReturnValue(Shapes.empty());
                }
            }
        }
    }

}
