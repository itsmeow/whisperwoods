package dev.itsmeow.whisperwoods.init;

import com.mojang.serialization.Codec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class ModParticles {

    private static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(WhisperwoodsMod.MODID, Registries.PARTICLE_TYPE);

    public static final RegistrySupplier<ParticleType<WispParticleData>> WISP = r("wisp", () -> new ParticleType<>(false, WispParticleData.DESERIALIZER) {
        @Override
        public Codec<WispParticleData> codec() {
            return WispParticleData.CODEC;
        }
    });
    public static final RegistrySupplier<SimpleParticleType> FLAME = rSimple("flame", false);
    public static final RegistrySupplier<SimpleParticleType> SOUL_FLAME = rSimple("soul_flame", false);

    private static <T extends ParticleOptions> RegistrySupplier<ParticleType<T>> r(String name, Supplier<ParticleType<T>> b) {
        return PARTICLES.register(name, b);
    }

    private static RegistrySupplier<SimpleParticleType> rSimple(String name, boolean override) {
        return PARTICLES.register(name, () -> new SimpleParticleType(override));
    }

    public static void init() {
        PARTICLES.register();
    }

}
