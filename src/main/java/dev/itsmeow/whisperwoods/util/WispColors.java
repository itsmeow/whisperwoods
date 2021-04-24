package dev.itsmeow.whisperwoods.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import dev.itsmeow.whisperwoods.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;

public class WispColors {

    private static final List<WispColor> ARRAY = new ArrayList<>();
    private static final Map<Integer, WispColor> BY_COLOR = new HashMap<>();
    public static final WispColor BLUE = r(new WispColor("BLUE", 0x00efef, ModBlocks.GHOST_LIGHT_ELECTRIC_BLUE, ModBlocks.WISP_LANTERN_BLUE));
    public static final WispColor ORANGE = r(new WispColor("ORANGE", 0xf28900, ModBlocks.GHOST_LIGHT_FIERY_ORANGE, ModBlocks.WISP_LANTERN_ORANGE));
    public static final WispColor YELLOW = r(new WispColor("YELLOW", 0xffc61c, ModBlocks.GHOST_LIGHT_GOLD, ModBlocks.WISP_LANTERN_YELLOW));   
    public static final WispColor PURPLE = r(new WispColor("PURPLE", 0xca27ea, ModBlocks.GHOST_LIGHT_MAGIC_PURPLE, ModBlocks.WISP_LANTERN_PURPLE));
    public static final WispColor GREEN = r(new WispColor("GREEN", 0x2bff39, ModBlocks.GHOST_LIGHT_TOXIC_GREEN, ModBlocks.WISP_LANTERN_GREEN));

    private static WispColor r(WispColor c) {
        ARRAY.add(c);
        BY_COLOR.put(c.getColor(), c);
        return c;
    }

    @Nullable
    public static WispColor byColor(int color) {
        return BY_COLOR.get(color);
    }

    public static WispColor[] values() {
        return ARRAY.toArray(new WispColor[0]);
    }

    public static final class WispColor {
        private final String name;
        private final int color;
        private final RegistryObject<Block> ghostLight;
        private final RegistryObject<Block> lantern;

        private WispColor(String name, int color, RegistryObject<Block> ghostLight, RegistryObject<Block> lantern) {
            this.name = name;
            this.color = color;
            this.ghostLight = ghostLight;
            this.lantern = lantern;
        }

        public RegistryObject<Block> getGhostLight() {
            return ghostLight;
        }

        public RegistryObject<Block> getLantern() {
            return lantern;
        }

        public String name() {
            return name;
        }

        public int ordinal() {
            return WispColors.ARRAY.indexOf(this);
        }

        public int getColor() {
            return this.color;
        }

        @Override
        public String toString() {
            return name + "/" + Integer.toHexString(color);
        }
    }

}
