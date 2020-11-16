package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class ModTags {

    public static final class Blocks {
        public static final IOptionalNamedTag<Block> GHOST_LIGHT = tag("ghost_light");
        public static final IOptionalNamedTag<Block> WISP_LANTERN = tag("wisp_lantern");

        private static IOptionalNamedTag<Block> tag(String name) {
            return BlockTags.createOptional(new ResourceLocation(WhisperwoodsMod.MODID, name));
        }
    }

    public static final class Items {
        public static final IOptionalNamedTag<Item> GHOST_LIGHT = tag("ghost_light");
        public static final IOptionalNamedTag<Item> WISP_LANTERN = tag("wisp_lantern");

        private static IOptionalNamedTag<Item> tag(String name) {
            return ItemTags.createOptional(new ResourceLocation(WhisperwoodsMod.MODID, name));
        }
    }
}
