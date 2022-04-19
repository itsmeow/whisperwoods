package dev.itsmeow.whisperwoods.forge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.itsmeow.imdlib.util.ClassLoadHacks;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.client.init.ClientLifecycleHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WhisperwoodsMod.MODID)
@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WhisperwoodsModForge {
    public WhisperwoodsModForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(WhisperwoodsMod.MODID, modBus);
        WhisperwoodsMod.construct();
        modBus.<FMLCommonSetupEvent>addListener(e -> {
            WhisperwoodsMod.init(e::enqueueWork);
        });
        ClassLoadHacks.runIf(Platform.getEnv() == Dist.CLIENT, () -> ClientLifecycleHandler::registerEntityRenders);
    }
}
