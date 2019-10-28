package its_meow.whisperwoods;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import its_meow.whisperwoods.client.init.ClientLifecycleHandler;
import its_meow.whisperwoods.config.WhisperwoodsConfig;
import its_meow.whisperwoods.init.ModBlocks;
import its_meow.whisperwoods.init.ModEntities;
import its_meow.whisperwoods.util.EntityTypeContainer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID)
@Mod(value = WhisperwoodsMod.MODID)
public class WhisperwoodsMod {

    public static final String MODID = "whisperwoods";
    public static final Logger LOGGER = LogManager.getLogger();

    public WhisperwoodsMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().<FMLClientSetupEvent>addListener(e -> new ClientLifecycleHandler().clientSetup(e));
        WhisperwoodsConfig.setupConfig();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, WhisperwoodsConfig.SERVER_CONFIG);
        LOGGER.log(Level.INFO, "Spooking you...");
    }

    public static final ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.GHOST_LIGHT_FIERY_ORANGE.asItem());
        }

        @Override
        public void fill(NonNullList<ItemStack> toDisplay) {
            super.fill(toDisplay);
            for(EntityTypeContainer<?> cont : ModEntities.ENTITIES.values()) {
                ItemStack stack = new ItemStack(cont.egg);
                toDisplay.add(stack);
            }
        }
    };

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.log(Level.INFO, "Summoning a hidebehind to eat you...");
    }

}
