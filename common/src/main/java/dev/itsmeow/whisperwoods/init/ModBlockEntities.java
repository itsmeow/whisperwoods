package dev.itsmeow.whisperwoods.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.blockentity.GhostLightBlockEntity;
import dev.itsmeow.whisperwoods.blockentity.HGSkullBlockEntity;
import dev.itsmeow.whisperwoods.blockentity.HandOfFateBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(WhisperwoodsMod.MODID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<GhostLightBlockEntity>> GHOST_LIGHT = r("ghost_light_tile", () -> BlockEntityType.Builder.of(GhostLightBlockEntity::new, ModBlocks.GHOST_LIGHT_ELECTRIC_BLUE.get(), ModBlocks.GHOST_LIGHT_FIERY_ORANGE.get(), ModBlocks.GHOST_LIGHT_GOLD.get(), ModBlocks.GHOST_LIGHT_MAGIC_PURPLE.get(), ModBlocks.GHOST_LIGHT_TOXIC_GREEN.get(), ModBlocks.WISP_LANTERN_BLUE.get(), ModBlocks.WISP_LANTERN_GREEN.get(), ModBlocks.WISP_LANTERN_ORANGE.get(), ModBlocks.WISP_LANTERN_PURPLE.get(), ModBlocks.WISP_LANTERN_YELLOW.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<HGSkullBlockEntity>> HG_SKULL = r("hirschgeist_skull", () -> BlockEntityType.Builder.of(HGSkullBlockEntity::new, ModBlocks.HIRSCHGEIST_SKULL.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<HandOfFateBlockEntity>> HAND_OF_FATE = r("hand_of_fate", () -> BlockEntityType.Builder.of(HandOfFateBlockEntity::new, ModBlocks.HAND_OF_FATE.get()).build(null));

    private static <T extends BlockEntity> RegistrySupplier<BlockEntityType<T>> r(String name, Supplier<BlockEntityType<T>> b) {
        return BLOCK_ENTITIES.register(name, b);
    }

    public static void init() {
        BLOCK_ENTITIES.register();
    }
}
