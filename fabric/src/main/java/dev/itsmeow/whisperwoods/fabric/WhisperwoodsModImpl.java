package dev.itsmeow.whisperwoods.fabric;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModItems;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Collectors;

public class WhisperwoodsModImpl {
    public static CreativeModeTab getPlatformTab() {
        return FabricItemGroupBuilder.create(new ResourceLocation(WhisperwoodsMod.MODID, "main"))
                .icon(() -> new ItemStack(ModItems.GHOST_LIGHT_FIERY_ORANGE.get()))
                .appendItems(list -> {
                    NonNullList<ItemStack> tabList = NonNullList.create();
                    Registry.ITEM.forEach(item -> item.fillItemCategory(WhisperwoodsMod.TAB, tabList));
                    list.addAll(tabList);
                    list.addAll(ModEntities.getEntities().values().stream().map(cont -> new ItemStack(cont.getEggItem().get())).collect(Collectors.toList()));
                })
                .build();
    }
}
