package dev.itsmeow.whisperwoods.init;

import dev.architectury.hooks.tags.TagHooks;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final class Blocks {
        public static final Tag.Named<Block> GHOST_LIGHT = tag("ghost_light");
        public static final Tag.Named<Block> WISP_LANTERN = tag("wisp_lantern");


        public static void loadTags() {
            // This is a classloading dummy.
        }

        private static Tag.Named<Block> tag(String name) {
            return TagHooks.optionalBlock(new ResourceLocation(WhisperwoodsMod.MODID, name));
        }
    }

    public static final class Items {
        public static final Tag.Named<Item> GHOST_LIGHT = tag("ghost_light");
        public static final Tag.Named<Item> WISP_LANTERN = tag("wisp_lantern");

        public static void loadTags() {
            // This is a classloading dummy.
        }

        private static Tag.Named<Item> tag(String name) {
            return TagHooks.optionalItem(new ResourceLocation(WhisperwoodsMod.MODID, name));
        }
    }
}
