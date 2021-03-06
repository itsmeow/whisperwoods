package dev.itsmeow.whisperwoods.init;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticles {

    private static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WhisperwoodsMod.MODID);

    public static final RegistryObject<ParticleType<WispParticleData>> WISP = r("wisp", () -> new ParticleType<WispParticleData>(false, WispParticleData.DESERIALIZER) {
        @Override
        public Codec<WispParticleData> func_230522_e_() {
            return WispParticleData.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<BasicParticleType>> FLAME = r("flame", () -> new BasicParticleType(false));

    private static <T extends IParticleData> RegistryObject<ParticleType<T>> r(String name, Supplier<ParticleType<T>> b) {
        return PARTICLES.register(name, b);
    }

    public static void subscribe(IEventBus modEventBus) {
        PARTICLES.register(modEventBus);
    }

}
