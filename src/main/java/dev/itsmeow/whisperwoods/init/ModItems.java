package dev.itsmeow.whisperwoods.init;

import java.util.Collection;
import java.util.function.Supplier;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.item.ItemBlockHirschgeistSkull;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
    public static RegistryObject<Item> WISP_LANTERN_BLUE = rIB(ModBlocks.WISP_LANTERN_BLUE);
    public static RegistryObject<Item> WISP_LANTERN_ORANGE = rIB(ModBlocks.WISP_LANTERN_ORANGE);
    public static RegistryObject<Item> WISP_LANTERN_YELLOW = rIB(ModBlocks.WISP_LANTERN_YELLOW);
    public static RegistryObject<Item> WISP_LANTERN_GREEN = rIB(ModBlocks.WISP_LANTERN_GREEN);
    public static RegistryObject<Item> WISP_LANTERN_PURPLE = rIB(ModBlocks.WISP_LANTERN_PURPLE);
    public static RegistryObject<Item> HAND_OF_FATE = rIB(ModBlocks.HAND_OF_FATE);

    private static RegistryObject<Item> r(String name, Supplier<Item> b) {
        return ITEMS.register(name, b);
    }

    private static RegistryObject<Item> rIB(RegistryObject<Block> parent) {
        return ITEMS.register(parent.getId().getPath(), () -> new BlockItem(parent.get(), new Item.Properties().tab(WhisperwoodsMod.TAB)));
    }

    public static void subscribe(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

    public static Collection<RegistryObject<Item>> getItems() {
        return ITEMS.getEntries();
    }

}
