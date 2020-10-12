package dev.itsmeow.whisperwoods.init;

import java.util.function.Supplier;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.item.ItemBlockHirschgeistSkull;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WhisperwoodsMod.MODID);

    public static RegistryObject<Item> GHOST_LIGHT_ELECTRIC_BLUE = rIB(ModBlocks.GHOST_LIGHT_ELECTRIC_BLUE);
    public static RegistryObject<Item> GHOST_LIGHT_FIERY_ORANGE = rIB(ModBlocks.GHOST_LIGHT_FIERY_ORANGE);
    public static RegistryObject<Item> GHOST_LIGHT_GOLD = rIB(ModBlocks.GHOST_LIGHT_GOLD);
    public static RegistryObject<Item> GHOST_LIGHT_TOXIC_GREEN = rIB(ModBlocks.GHOST_LIGHT_TOXIC_GREEN);
    public static RegistryObject<Item> GHOST_LIGHT_MAGIC_PURPLE = rIB(ModBlocks.GHOST_LIGHT_MAGIC_PURPLE);
    public static RegistryObject<Item> HIRSCHGEIST_SKULL = r("hirschgeist_skull", () -> new ItemBlockHirschgeistSkull(ModBlocks.HIRSCHGEIST_SKULL.get()));

    private static RegistryObject<Item> r(String name, Supplier<Item> b) {
        return ITEMS.register(name, b);
    }

    private static RegistryObject<Item> rIB(RegistryObject<Block> parent) {
        return ITEMS.register(parent.getId().getPath(), () -> new BlockItem(parent.get(), new Item.Properties().group(WhisperwoodsMod.TAB)));
    }

    public static void subscribe(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

}
