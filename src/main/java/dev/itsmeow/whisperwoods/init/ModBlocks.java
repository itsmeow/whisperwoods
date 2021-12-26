package dev.itsmeow.whisperwoods.init;

import java.util.Collection;
import java.util.function.Supplier;

import dev.itsmeow.imdlib.block.BlockAnimalSkull;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.block.BlockGhostLight;
import dev.itsmeow.whisperwoods.block.BlockHandOfFate;
import dev.itsmeow.whisperwoods.block.BlockWispLantern;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHGSkull;
import dev.itsmeow.whisperwoods.util.WispColors;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WhisperwoodsMod.MODID);

    public static RegistryObject<Block> GHOST_LIGHT_ELECTRIC_BLUE = r("ghost_light_electric_blue", () -> new BlockGhostLight(WispColors.BLUE.getColor()));
    public static RegistryObject<Block> GHOST_LIGHT_FIERY_ORANGE = r("ghost_light_fiery_orange", () -> new BlockGhostLight(WispColors.ORANGE.getColor()));
    public static RegistryObject<Block> GHOST_LIGHT_GOLD = r("ghost_light_gold", () -> new BlockGhostLight(WispColors.YELLOW.getColor()));
    public static RegistryObject<Block> GHOST_LIGHT_TOXIC_GREEN = r("ghost_light_toxic_green", () -> new BlockGhostLight(WispColors.GREEN.getColor()));
    public static RegistryObject<Block> GHOST_LIGHT_MAGIC_PURPLE = r("ghost_light_magic_purple", () -> new BlockGhostLight(WispColors.PURPLE.getColor()));
    public static RegistryObject<Block> HIRSCHGEIST_SKULL = r("hirschgeist_skull", () -> new BlockAnimalSkull() {
        @Override
        public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
            return new TileEntityHGSkull();
        }
    });
    private static final BlockBehaviour.Properties LANTERN_PROPS = BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(3.5F).sound(SoundType.LANTERN).lightLevel(state -> 15).noOcclusion();
    public static RegistryObject<Block> WISP_LANTERN_BLUE = r("wisp_lantern_blue", () -> new BlockWispLantern(WispColors.BLUE.getColor(), LANTERN_PROPS));
    public static RegistryObject<Block> WISP_LANTERN_GREEN = r("wisp_lantern_green", () -> new BlockWispLantern(WispColors.GREEN.getColor(), LANTERN_PROPS));
    public static RegistryObject<Block> WISP_LANTERN_ORANGE = r("wisp_lantern_orange", () -> new BlockWispLantern(WispColors.ORANGE.getColor(), LANTERN_PROPS));
    public static RegistryObject<Block> WISP_LANTERN_PURPLE = r("wisp_lantern_purple", () -> new BlockWispLantern(WispColors.PURPLE.getColor(), LANTERN_PROPS));
    public static RegistryObject<Block> WISP_LANTERN_YELLOW = r("wisp_lantern_yellow", () -> new BlockWispLantern(WispColors.YELLOW.getColor(), LANTERN_PROPS));
    public static RegistryObject<Block> HAND_OF_FATE = r("hand_of_fate", () -> new BlockHandOfFate(BlockBehaviour.Properties.of(Material.METAL).strength(3.0F, 2.0F)));

    private static RegistryObject<Block> r(String name, Supplier<Block> b) {
        return BLOCKS.register(name, b);
    }

    public static void subscribe(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }

    public static Collection<RegistryObject<Block>> getBlocks() {
        return BLOCKS.getEntries();
    }

}
