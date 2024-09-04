package dev.itsmeow.whisperwoods.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.block.GhostLightBlock;
import dev.itsmeow.whisperwoods.block.HandOfFateBlock;
import dev.itsmeow.whisperwoods.block.HirschgeistSkullBlock;
import dev.itsmeow.whisperwoods.block.WispLanternBlock;
import dev.itsmeow.whisperwoods.util.WispColors;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(WhisperwoodsMod.MODID, Registries.BLOCK);

    public static RegistrySupplier<GhostLightBlock> GHOST_LIGHT_ELECTRIC_BLUE = r("ghost_light_electric_blue", () -> new GhostLightBlock(WispColors.BLUE.getColor()));
    public static RegistrySupplier<GhostLightBlock> GHOST_LIGHT_FIERY_ORANGE = r("ghost_light_fiery_orange", () -> new GhostLightBlock(WispColors.ORANGE.getColor()));
    public static RegistrySupplier<GhostLightBlock> GHOST_LIGHT_GOLD = r("ghost_light_gold", () -> new GhostLightBlock(WispColors.YELLOW.getColor()));
    public static RegistrySupplier<GhostLightBlock> GHOST_LIGHT_TOXIC_GREEN = r("ghost_light_toxic_green", () -> new GhostLightBlock(WispColors.GREEN.getColor()));
    public static RegistrySupplier<GhostLightBlock> GHOST_LIGHT_MAGIC_PURPLE = r("ghost_light_magic_purple", () -> new GhostLightBlock(WispColors.PURPLE.getColor()));
    public static RegistrySupplier<HirschgeistSkullBlock> HIRSCHGEIST_SKULL = r("hirschgeist_skull", HirschgeistSkullBlock::new);
    private static final BlockBehaviour.Properties LANTERN_PROPS = BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel(state -> 15).noOcclusion().pushReaction(PushReaction.DESTROY);
    public static RegistrySupplier<WispLanternBlock> WISP_LANTERN_BLUE = r("wisp_lantern_blue", () -> new WispLanternBlock(WispColors.BLUE.getColor(), LANTERN_PROPS));
    public static RegistrySupplier<WispLanternBlock> WISP_LANTERN_GREEN = r("wisp_lantern_green", () -> new WispLanternBlock(WispColors.GREEN.getColor(), LANTERN_PROPS));
    public static RegistrySupplier<WispLanternBlock> WISP_LANTERN_ORANGE = r("wisp_lantern_orange", () -> new WispLanternBlock(WispColors.ORANGE.getColor(), LANTERN_PROPS));
    public static RegistrySupplier<WispLanternBlock> WISP_LANTERN_PURPLE = r("wisp_lantern_purple", () -> new WispLanternBlock(WispColors.PURPLE.getColor(), LANTERN_PROPS));
    public static RegistrySupplier<WispLanternBlock> WISP_LANTERN_YELLOW = r("wisp_lantern_yellow", () -> new WispLanternBlock(WispColors.YELLOW.getColor(), LANTERN_PROPS));
    public static RegistrySupplier<HandOfFateBlock> HAND_OF_FATE = r("hand_of_fate", () -> new HandOfFateBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.0F, 2.0F)));

    private static <T extends Block> RegistrySupplier<T> r(String name, Supplier<T> b) {
        return BLOCKS.register(name, b);
    }

    public static void init() {
        BLOCKS.register();
    }

}
