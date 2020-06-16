
package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.item.ItemWhisperwoodsEgg;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WhisperwoodsRegistrar {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (EntityTypeContainer<?> container : ModEntities.getEntities().values()) {
            ItemWhisperwoodsEgg egg = new ItemWhisperwoodsEgg(container);
            egg.setRegistryName(container.entityName.toLowerCase().toString() + "_spawn_egg");
            event.getRegistry().register(egg);
            container.egg = egg;
        }
        registerItemBlocks(event.getRegistry(), ModBlocks.GHOST_LIGHT_ELECTRIC_BLUE, ModBlocks.GHOST_LIGHT_FIERY_ORANGE, ModBlocks.GHOST_LIGHT_GOLD, ModBlocks.GHOST_LIGHT_TOXIC_GREEN, ModBlocks.GHOST_LIGHT_MAGIC_PURPLE);
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.GHOST_LIGHT_ELECTRIC_BLUE, ModBlocks.GHOST_LIGHT_FIERY_ORANGE, ModBlocks.GHOST_LIGHT_GOLD, ModBlocks.GHOST_LIGHT_TOXIC_GREEN, ModBlocks.GHOST_LIGHT_MAGIC_PURPLE);
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        for(EntityTypeContainer<?> container : ModEntities.getEntities().values()) {
            event.getRegistry().register(container.entityType);
        }
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(ModSounds.HIDEBEHIND_SOUND);
    }

    @SubscribeEvent
    public static void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> event) {
        event.getRegistry().register(ModParticles.WISP);
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().register(ModTileEntities.GHOST_LIGHT);
    }
    
    private static void registerItemBlocks(IForgeRegistry<Item> registry, Block... blocks) {
        for(Block block : blocks) {
            registry.register(new BlockItem(block, new Item.Properties().group(WhisperwoodsMod.TAB)).setRegistryName(block.getRegistryName()));
        }
    }

}
