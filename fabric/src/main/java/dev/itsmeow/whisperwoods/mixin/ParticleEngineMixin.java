package dev.itsmeow.whisperwoods.mixin;

import dev.itsmeow.whisperwoods.client.init.ClientLifecycleHandler;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

    @Shadow
    protected abstract <T extends ParticleOptions> void register(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> spriteParticleRegistration);

    @Inject(at = @At("RETURN"), method = "registerProviders()V")
    private void registerProviders(CallbackInfo callback) {
        ClientLifecycleHandler.registerParticles((type, provider) -> this.register(type, (ParticleEngine.SpriteParticleRegistration) provider::apply));
    }

}
