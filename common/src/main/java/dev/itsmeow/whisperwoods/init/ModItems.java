package dev.itsmeow.whisperwoods.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.item.ItemBlockHirschgeistSkull;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Supplier;

public class ModItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(WhisperwoodsMod.MODID, Registries.ITEM);

    public static RegistrySupplier<BlockItem> GHOST_LIGHT_ELECTRIC_BLUE = rIB(ModBlocks.GHOST_LIGHT_ELECTRIC_BLUE);
    public static RegistrySupplier<BlockItem> GHOST_LIGHT_FIERY_ORANGE = rIB(ModBlocks.GHOST_LIGHT_FIERY_ORANGE);
    public static RegistrySupplier<BlockItem> GHOST_LIGHT_GOLD = rIB(ModBlocks.GHOST_LIGHT_GOLD);
    public static RegistrySupplier<BlockItem> GHOST_LIGHT_TOXIC_GREEN = rIB(ModBlocks.GHOST_LIGHT_TOXIC_GREEN);
    public static RegistrySupplier<BlockItem> GHOST_LIGHT_MAGIC_PURPLE = rIB(ModBlocks.GHOST_LIGHT_MAGIC_PURPLE);
    public static RegistrySupplier<ItemBlockHirschgeistSkull> HIRSCHGEIST_SKULL = r("hirschgeist_skull", () -> new ItemBlockHirschgeistSkull(ModBlocks.HIRSCHGEIST_SKULL.get()));
    public static RegistrySupplier<BlockItem> WISP_LANTERN_BLUE = rIB(ModBlocks.WISP_LANTERN_BLUE);
    public static RegistrySupplier<BlockItem> WISP_LANTERN_ORANGE = rIB(ModBlocks.WISP_LANTERN_ORANGE);
    public static RegistrySupplier<BlockItem> WISP_LANTERN_YELLOW = rIB(ModBlocks.WISP_LANTERN_YELLOW);
    public static RegistrySupplier<BlockItem> WISP_LANTERN_GREEN = rIB(ModBlocks.WISP_LANTERN_GREEN);
    public static RegistrySupplier<BlockItem> WISP_LANTERN_PURPLE = rIB(ModBlocks.WISP_LANTERN_PURPLE);
    public static RegistrySupplier<BlockItem> HAND_OF_FATE = rIB(ModBlocks.HAND_OF_FATE);

    protected static <T extends Item> RegistrySupplier<T> r(String name, Supplier<T> b) {
        return ITEMS.register(name, b);
    }

    protected static RegistrySupplier<BlockItem> rIB(RegistrySupplier<? extends Block> parent) {
        return ITEMS.register(parent.getId().getPath(), () -> new BlockItem(parent.get(), new Item.Properties()));
    }

    public static Iterator<RegistrySupplier<Item>> getTabItems() {
        return ITEMS.iterator();
    }

    public static void init() {
        ITEMS.register();
    }

}
