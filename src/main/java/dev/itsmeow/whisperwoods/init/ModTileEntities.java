package dev.itsmeow.whisperwoods.init;

import java.util.function.Supplier;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.tileentity.TileEntityGhostLight;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHGSkull;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHandOfFate;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    private static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, WhisperwoodsMod.MODID);

    public static final RegistryObject<BlockEntityType<TileEntityGhostLight>> GHOST_LIGHT = r("ghost_light_tile", () -> BlockEntityType.Builder.of(TileEntityGhostLight::new, ModBlocks.GHOST_LIGHT_ELECTRIC_BLUE.get(), ModBlocks.GHOST_LIGHT_FIERY_ORANGE.get(), ModBlocks.GHOST_LIGHT_GOLD.get(), ModBlocks.GHOST_LIGHT_MAGIC_PURPLE.get(), ModBlocks.GHOST_LIGHT_TOXIC_GREEN.get(), ModBlocks.WISP_LANTERN_BLUE.get(), ModBlocks.WISP_LANTERN_GREEN.get(), ModBlocks.WISP_LANTERN_ORANGE.get(), ModBlocks.WISP_LANTERN_PURPLE.get(), ModBlocks.WISP_LANTERN_YELLOW.get()).build(null));
    public static final RegistryObject<BlockEntityType<TileEntityHGSkull>> HG_SKULL = r("hirschgeist_skull", () -> BlockEntityType.Builder.of(TileEntityHGSkull::new, ModBlocks.HIRSCHGEIST_SKULL.get()).build(null));
    public static final RegistryObject<BlockEntityType<TileEntityHandOfFate>> HAND_OF_FATE = r("hand_of_fate", () -> BlockEntityType.Builder.of(TileEntityHandOfFate::new, ModBlocks.HAND_OF_FATE.get()).build(null));

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> r(String name, Supplier<BlockEntityType<T>> b) {
        return TILE_ENTITIES.register(name, b);
    }

    public static void subscribe(IEventBus modEventBus) {
        TILE_ENTITIES.register(modEventBus);
    }
}
