package dev.itsmeow.whisperwoods.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;

public class ModCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(WhisperwoodsMod.MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> WHISPERWOODS = CREATIVE_MODE_TABS.register("whisperwoods", () ->
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 7)
                    .title(Component.translatable("itemGroup.whisperwoods.main"))
                    .icon(() -> new ItemStack(ModItems.GHOST_LIGHT_FIERY_ORANGE.get()))
                    .displayItems((itemDisplayParameters, output) -> {
                        for (Iterator<RegistrySupplier<Item>> it = ModItems.getTabItems(); it.hasNext(); ) {
                            RegistrySupplier<Item> item = it.next();
                            logCreativeTabEntry(item.getId(), item.get());
                            output.accept(item.get());
                        }
                        ModEntities.getEntities().values().forEach(cont -> {
                            logCreativeTabEntry(cont.getEggItem().getId(), cont.getEggItem().get());
                            output.accept(new ItemStack(cont.getEggItem().get()));
                        });
                    }).build());
    private static final boolean LOG_CREATIVE_TAB_ENTRIES = false;

    private static void logCreativeTabEntry(ResourceLocation key, Item value) {
        if(LOG_CREATIVE_TAB_ENTRIES)
            WhisperwoodsMod.LOGGER.info("Registering {} with value {} to whisperwoods tab", key, value);
    }

    public static void init() {
        CREATIVE_MODE_TABS.register();
    }
}
