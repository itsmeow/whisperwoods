package dev.itsmeow.whisperwoods.mixin;

import dev.itsmeow.whisperwoods.entity.EntityHirschgeist;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("HEAD"), method = "getCurrentSwingDuration()I", cancellable = true)
    public void getCurrentSwingDuration(CallbackInfoReturnable<Integer> cir) {
        if(((Object) this) instanceof EntityHirschgeist) {
            cir.setReturnValue(10);
            cir.cancel();
        }
    }

}
