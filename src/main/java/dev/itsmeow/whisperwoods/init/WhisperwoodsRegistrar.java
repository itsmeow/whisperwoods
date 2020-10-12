
package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.EntityTypeContainerContainable;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WhisperwoodsRegistrar {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for(EntityTypeContainer<?> container : ModEntities.getEntities().values()) {
            event.getRegistry().register(container.egg);
            if(container instanceof EntityTypeContainerContainable<?, ?>) {
                EntityTypeContainerContainable<?, ?> c = (EntityTypeContainerContainable<?, ?>) container;
                if(!ForgeRegistries.ITEMS.containsValue(c.getContainerItem()) && c.getContainerItem().getRegistryName().getNamespace().equals(WhisperwoodsMod.MODID)) {
                    event.getRegistry().register(c.getContainerItem());
                }
                if(!ForgeRegistries.ITEMS.containsValue(c.getEmptyContainerItem()) && c.getEmptyContainerItem().getRegistryName().getNamespace().equals(WhisperwoodsMod.MODID)) {
                    event.getRegistry().register(c.getEmptyContainerItem());
                }
            }
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
