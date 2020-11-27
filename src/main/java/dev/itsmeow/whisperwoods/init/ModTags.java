package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags {

    public static final class Blocks {
        public static final Tag<Block> GHOST_LIGHT = tag("ghost_light");
        public static final Tag<Block> WISP_LANTERN = tag("wisp_lantern");

        private static Tag<Block> tag(String name) {
            return new BlockTags.Wrapper(new ResourceLocation(WhisperwoodsMod.MODID, name));
        }
    }

    public static final class Items {
        public static final Tag<Item> GHOST_LIGHT = tag("ghost_light");
        public static final Tag<Item> WISP_LANTERN = tag("wisp_lantern");

        private static Tag<Item> tag(String name) {
            return new ItemTags.Wrapper(new ResourceLocation(WhisperwoodsMod.MODID, name));
        }
    }
}
