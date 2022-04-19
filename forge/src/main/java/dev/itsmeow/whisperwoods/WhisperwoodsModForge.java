package dev.itsmeow.whisperwoods;

import me.shedaniel.architectury.platform.forge.EventBuses;
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
    }
}
