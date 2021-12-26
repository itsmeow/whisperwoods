package dev.itsmeow.whisperwoods;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.itsmeow.imdlib.IMDLib;
import dev.itsmeow.whisperwoods.init.*;
import dev.itsmeow.whisperwoods.network.WWNetwork;
import me.shedaniel.architectury.utils.PlatformExpectedError;
import net.minecraft.world.item.CreativeModeTab;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class WhisperwoodsMod {

    public static final String MODID = "whisperwoods";
    private static final Logger LOGGER = LogManager.getLogger();

    public static void construct() {
        IMDLib.setRegistry(MODID);
        ModTags.Blocks.loadTags();
        ModTags.Items.loadTags();
        ModSounds.init();
        ModParticles.init();
        ModEntities.init();
        ModBlocks.init();
        ModItems.init();
        ModBlockEntities.init();
        WWNetwork.init();
        LOGGER.info("Spooking you...");
    }

    public static void init(Consumer<Runnable> enqueue) {
        LOGGER.info("Summoning a hidebehind to eat you...");
    }

    public static final CreativeModeTab TAB = getPlatformTab();

    @ExpectPlatform
    public static CreativeModeTab getPlatformTab() {
        throw new PlatformExpectedError("getPlatformTab(): Expected Platform");
    }

}
