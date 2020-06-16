package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.particles.ParticleType;

public class ModParticles {
    
    public static final ParticleType<WispParticleData> WISP = new ParticleType<WispParticleData>(false, WispParticleData.DESERIALIZER);
    static {
        WISP.setRegistryName(WhisperwoodsMod.MODID, "wisp");
    }

}
