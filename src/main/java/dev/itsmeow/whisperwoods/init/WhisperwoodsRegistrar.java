
package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WhisperwoodsRegistrar {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for(EntityTypeContainer<?> container : ModEntities.getEntities().values()) {
            event.getRegistry().register(container.egg);
        }
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        for(EntityTypeContainer<?> container : ModEntities.getEntities().values()) {
            event.getRegistry().register(container.entityType);
            container.registerAttributes();
        }
    }

}
