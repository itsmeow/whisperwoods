package its_meow.whisperwoods.client.init;

import its_meow.whisperwoods.WhisperwoodsMod;
import its_meow.whisperwoods.client.particle.WispParticle;
import its_meow.whisperwoods.client.renderer.entity.RenderHidebehind;
import its_meow.whisperwoods.client.renderer.entity.RenderMoth;
import its_meow.whisperwoods.client.renderer.entity.RenderWisp;
import its_meow.whisperwoods.client.renderer.tile.RenderTileGhostLight;
import its_meow.whisperwoods.entity.EntityHidebehind;
import its_meow.whisperwoods.entity.EntityMoth;
import its_meow.whisperwoods.entity.EntityWisp;
import its_meow.whisperwoods.init.ModParticles;
import its_meow.whisperwoods.tileentity.TileEntityGhostLight;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientLifecycleHandler {
    
    public void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityMoth.class, RenderMoth::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityHidebehind.class, RenderHidebehind::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWisp.class, RenderWisp::new);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGhostLight.class, new RenderTileGhostLight());
        WhisperwoodsMod.LOGGER.info("Increasing wispiness of wisps...");
    }
    
    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ModParticles.WISP, WispParticle.WispFactory::new);
    }

}
