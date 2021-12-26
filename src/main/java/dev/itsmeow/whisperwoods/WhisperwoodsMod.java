package dev.itsmeow.whisperwoods;

import dev.itsmeow.imdlib.entity.EntityTypeContainer;
import dev.itsmeow.whisperwoods.config.WhisperwoodsConfig;
import dev.itsmeow.whisperwoods.init.*;
import dev.itsmeow.whisperwoods.network.WWNetwork;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = WhisperwoodsMod.MODID)
public class WhisperwoodsMod {

    public static final String MODID = "whisperwoods";
    private static final Logger LOGGER = LogManager.getLogger();

    public WhisperwoodsMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setup);
        modBus.addListener(this::loadComplete);
        ModEntities.subscribe(modBus);
        ModBlocks.subscribe(modBus);
        ModItems.subscribe(modBus);
        ModSounds.subscribe(modBus);
        ModTileEntities.subscribe(modBus);
        ModParticles.subscribe(modBus);
        WWNetwork.subscribe(modBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, WhisperwoodsConfig.getClientSpec());
        LOGGER.info("Spooking you...");
    }

    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.GHOST_LIGHT_FIERY_ORANGE.get().asItem());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> toDisplay) {
            super.fillItemList(toDisplay);
            for(EntityTypeContainer<?> cont : ModEntities.getEntities().values()) {
                ItemStack stack = new ItemStack(cont.getEggItem());
                toDisplay.add(stack);
            }
        }
    };

    public void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Summoning a hidebehind to eat you...");
    }

    public void loadComplete(final FMLLoadCompleteEvent event) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WhisperwoodsConfig.getServerSpec());
    }

}
