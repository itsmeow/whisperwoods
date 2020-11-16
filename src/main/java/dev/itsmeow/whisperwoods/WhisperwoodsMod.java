package dev.itsmeow.whisperwoods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.whisperwoods.config.WhisperwoodsConfig;
import dev.itsmeow.whisperwoods.init.ModBlocks;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModItems;
import dev.itsmeow.whisperwoods.init.ModParticles;
import dev.itsmeow.whisperwoods.init.ModSounds;
import dev.itsmeow.whisperwoods.init.ModTileEntities;
import dev.itsmeow.whisperwoods.network.WWNetwork;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = WhisperwoodsMod.MODID)
public class WhisperwoodsMod {

    public static final String MODID = "whisperwoods";
    private static final Logger LOGGER = LogManager.getLogger();

    public WhisperwoodsMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::setup);
        modBus.addListener(this::loadComplete);
        ModBlocks.subscribe(modBus);
        ModItems.subscribe(modBus);
        ModSounds.subscribe(modBus);
        ModTileEntities.subscribe(modBus);
        ModParticles.subscribe(modBus);
        WWNetwork.subscribe(modBus);
        MinecraftForge.EVENT_BUS.addListener(WhisperwoodsConfig::onBiomeLoad);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, WhisperwoodsConfig.getClientSpec());
        LOGGER.info("Spooking you...");
    }

    public static final ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.GHOST_LIGHT_FIERY_ORANGE.get().asItem());
        }

        @Override
        public void fill(NonNullList<ItemStack> toDisplay) {
            super.fill(toDisplay);
            for(EntityTypeContainer<?> cont : ModEntities.getEntities().values()) {
                ItemStack stack = new ItemStack(cont.egg);
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
