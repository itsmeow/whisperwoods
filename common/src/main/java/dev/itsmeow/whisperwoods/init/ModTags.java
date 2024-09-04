package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static final class Blocks {
        public static final TagKey<Block> GHOST_LIGHT = tag("ghost_light");
        public static final TagKey<Block> WISP_LANTERN = tag("wisp_lantern");
        public static final TagKey<Block> MOTH_BREAKABLE = tag("moth_breakable");


        public static void loadTags() {
            // This is a classloading dummy.
        }

        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(WhisperwoodsMod.MODID, name));
        }
    }

    public static final class Items {
        public static final TagKey<Item> GHOST_LIGHT = tag("ghost_light");
        public static final TagKey<Item> WISP_LANTERN = tag("wisp_lantern");
        public static final TagKey<Item> MOTH_TARGET_HELD_LIGHT_ITEMS = tag("moth_target_held_light_items");

        public static void loadTags() {
            // This is a classloading dummy.
        }

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(WhisperwoodsMod.MODID, name));
        }
    }
}
