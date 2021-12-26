package dev.itsmeow.whisperwoods.forge;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class WhisperwoodsModImpl {
    public static CreativeModeTab getPlatformTab() {
        return new CreativeModeTab(WhisperwoodsMod.MODID + ".main") {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(ModItems.GHOST_LIGHT_FIERY_ORANGE.get());
            }

            @Override
            public void fillItemList(NonNullList<ItemStack> toDisplay) {
                super.fillItemList(toDisplay);
                ModEntities.getEntities().values().forEach(cont -> toDisplay.add(new ItemStack(cont.getEggItem().get())));
            }
        };
    }
}
