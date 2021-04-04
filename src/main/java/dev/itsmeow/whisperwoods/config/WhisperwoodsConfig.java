package dev.itsmeow.whisperwoods.config;

import dev.itsmeow.imdlib.entity.EntityRegistrarHandler.ClientEntityConfiguration;
import dev.itsmeow.imdlib.entity.EntityRegistrarHandler.ServerEntityConfiguration;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.init.ModEntities;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class WhisperwoodsConfig {

    private static final Logger LOGGER = LogManager.getLogger();

    private static ClientEntityConfiguration CLIENT_CONFIG = null;
    public static ForgeConfigSpec CLIENT_CONFIG_SPEC = null;

    private static ServerEntityConfiguration SERVER_CONFIG = null;
    public static ForgeConfigSpec SERVER_CONFIG_SPEC = null;
    
    public static ForgeConfigSpec getClientSpec() {
        final Pair<ClientEntityConfiguration, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ModEntities.H::clientConfig);
        CLIENT_CONFIG_SPEC = specPair.getRight();
        CLIENT_CONFIG = specPair.getLeft();
        return CLIENT_CONFIG_SPEC;
    }

    public static ForgeConfigSpec getServerSpec() {
        final Pair<ServerEntityConfiguration, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ModEntities.H::serverConfig);
        SERVER_CONFIG_SPEC = specPair.getRight();
        SERVER_CONFIG = specPair.getLeft();
        return SERVER_CONFIG_SPEC;
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        LOGGER.debug("Loading {} {}", WhisperwoodsMod.MODID, configEvent.getConfig().getFileName());
        if(configEvent.getConfig().getSpec() == SERVER_CONFIG_SPEC) {
            SERVER_CONFIG.onLoad();
        } else if(configEvent.getConfig().getSpec() == CLIENT_CONFIG_SPEC) {
            CLIENT_CONFIG.onLoad();
        }
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Reloading configEvent) {
        LOGGER.debug("Reloading {} {}", WhisperwoodsMod.MODID, configEvent.getConfig().getFileName());
        if(configEvent.getConfig().getSpec() == SERVER_CONFIG_SPEC) {
            SERVER_CONFIG.onLoad();
        } else if(configEvent.getConfig().getSpec() == CLIENT_CONFIG_SPEC) {
            CLIENT_CONFIG.onLoad();
        }
    }
}