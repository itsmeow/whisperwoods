
package its_meow.whisperwoods.init;

import its_meow.whisperwoods.WhisperwoodsMod;
import its_meow.whisperwoods.item.ItemWhisperwoodsEgg;
import its_meow.whisperwoods.util.EntityTypeContainer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WhisperwoodsRegistrar {
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (EntityTypeContainer<?> container : ModEntities.ENTITIES.values()) {
            ItemWhisperwoodsEgg egg = new ItemWhisperwoodsEgg(container);
            egg.setRegistryName(container.entityName.toLowerCase().toString() + "_spawn_egg");
            event.getRegistry().register(egg);
            container.egg = egg;
        }
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        for(EntityTypeContainer<?> container : ModEntities.ENTITIES.values()) {
            event.getRegistry().register(container.entityType);
        }
    }

}
