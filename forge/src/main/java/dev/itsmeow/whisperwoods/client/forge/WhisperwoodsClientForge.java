package dev.itsmeow.whisperwoods.client.forge;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.client.init.ClientLifecycleHandler;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WhisperwoodsClientForge {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientLifecycleHandler.clientInit();
    }

    @SubscribeEvent
    public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
        ClientLifecycleHandler.registerParticles((type, provider) -> event.registerSpriteSet(type, (ParticleEngine.SpriteParticleRegistration) spriteSet -> provider.apply(spriteSet)));
    }
}
