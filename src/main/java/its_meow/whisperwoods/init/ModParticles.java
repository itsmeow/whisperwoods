package its_meow.whisperwoods.init;

import its_meow.whisperwoods.WhisperwoodsMod;
import its_meow.whisperwoods.particle.WispParticleData;
import net.minecraft.particles.ParticleType;

public class ModParticles {
    
    public static final ParticleType<WispParticleData> WISP = new ParticleType<WispParticleData>(false, WispParticleData.DESERIALIZER);
    static {
        WISP.setRegistryName(WhisperwoodsMod.MODID, "wisp");
    }

}
