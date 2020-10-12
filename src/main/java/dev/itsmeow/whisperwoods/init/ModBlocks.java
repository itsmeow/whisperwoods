package dev.itsmeow.whisperwoods.init;

import java.util.function.Supplier;

import dev.itsmeow.imdlib.block.BlockAnimalSkull;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.block.BlockGhostLight;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHGSkull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WhisperwoodsMod.MODID);

    public static RegistryObject<Block> GHOST_LIGHT_ELECTRIC_BLUE = r("ghost_light_electric_blue", () -> new BlockGhostLight(0x00efef));
    public static RegistryObject<Block> GHOST_LIGHT_FIERY_ORANGE = r("ghost_light_fiery_orange", () -> new BlockGhostLight(0xf28900));
    public static RegistryObject<Block> GHOST_LIGHT_GOLD = r("ghost_light_gold", () -> new BlockGhostLight(0xffc61c));
    public static RegistryObject<Block> GHOST_LIGHT_TOXIC_GREEN = r("ghost_light_toxic_green", () -> new BlockGhostLight(0x2bff39));
    public static RegistryObject<Block> GHOST_LIGHT_MAGIC_PURPLE = r("ghost_light_magic_purple", () -> new BlockGhostLight(0xca27ea));
    public static RegistryObject<Block> HIRSCHGEIST_SKULL = r("hirschgeist_skull", () -> new BlockAnimalSkull() {
        @Override
        public TileEntity createTileEntity(BlockState state, IBlockReader world) {
            return new TileEntityHGSkull();
        }
    });

    private static RegistryObject<Block> r(String name, Supplier<Block> b) {
        return BLOCKS.register(name, b);
    }

    public static void subscribe(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }

}
