package its_meow.whisperwoods.client.init;

import its_meow.whisperwoods.WhisperwoodsMod;
import its_meow.whisperwoods.client.particle.WispParticle;
import its_meow.whisperwoods.client.renderer.entity.RenderHidebehind;
import its_meow.whisperwoods.client.renderer.entity.RenderMoth;
import its_meow.whisperwoods.client.renderer.entity.RenderWisp;
import its_meow.whisperwoods.entity.EntityHidebehind;
import its_meow.whisperwoods.entity.EntityMoth;
import its_meow.whisperwoods.entity.EntityWisp;
import its_meow.whisperwoods.init.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientLifecycleHandler {
    
    public void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityMoth.class, RenderMoth::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHidebehind.class, RenderHidebehind::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWisp.class, RenderWisp::new);
        Minecraft.getInstance().particles.registerFactory(ModParticles.WISP, WispParticle.WispFactory::new);
        WhisperwoodsMod.LOGGER.info("Increasing wispiness of wisps...");
    }

}
