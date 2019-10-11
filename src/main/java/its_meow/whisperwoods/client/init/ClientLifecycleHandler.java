package its_meow.whisperwoods.client.init;

import its_meow.whisperwoods.WhisperwoodsMod;
import its_meow.whisperwoods.client.renderer.entity.RenderMoth;
import its_meow.whisperwoods.entity.EntityMoth;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientLifecycleHandler {
    
    public void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityMoth.class, RenderMoth::new);
        WhisperwoodsMod.LOGGER.info("Increasing wispiness of wisps...");
    }

}
