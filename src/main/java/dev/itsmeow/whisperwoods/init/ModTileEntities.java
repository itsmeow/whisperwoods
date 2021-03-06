package dev.itsmeow.whisperwoods.init;

import java.util.function.Supplier;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.tileentity.TileEntityGhostLight;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHGSkull;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHandOfFate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, WhisperwoodsMod.MODID);

    public static final RegistryObject<TileEntityType<TileEntityGhostLight>> GHOST_LIGHT = r("ghost_light_tile", () -> TileEntityType.Builder.create(TileEntityGhostLight::new, ModBlocks.GHOST_LIGHT_ELECTRIC_BLUE.get(), ModBlocks.GHOST_LIGHT_FIERY_ORANGE.get(), ModBlocks.GHOST_LIGHT_GOLD.get(), ModBlocks.GHOST_LIGHT_MAGIC_PURPLE.get(), ModBlocks.GHOST_LIGHT_TOXIC_GREEN.get(), ModBlocks.WISP_LANTERN_BLUE.get(), ModBlocks.WISP_LANTERN_GREEN.get(), ModBlocks.WISP_LANTERN_ORANGE.get(), ModBlocks.WISP_LANTERN_PURPLE.get(), ModBlocks.WISP_LANTERN_YELLOW.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityHGSkull>> HG_SKULL = r("hirschgeist_skull", () -> TileEntityType.Builder.create(TileEntityHGSkull::new, ModBlocks.HIRSCHGEIST_SKULL.get()).build(null));
    public static final RegistryObject<TileEntityType<TileEntityHandOfFate>> HAND_OF_FATE = r("hand_of_fate", () -> TileEntityType.Builder.create(TileEntityHandOfFate::new, ModBlocks.HAND_OF_FATE.get()).build(null));

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> r(String name, Supplier<TileEntityType<T>> b) {
        return TILE_ENTITIES.register(name, b);
    }

    public static void subscribe(IEventBus modEventBus) {
        TILE_ENTITIES.register(modEventBus);
    }
}
