package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.imdlib.IMDLib;
import dev.itsmeow.imdlib.entity.EntityRegistrarHandler;
import dev.itsmeow.imdlib.entity.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.EntityTypeContainerContainable;
import dev.itsmeow.imdlib.item.ItemModEntityContainer;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.entity.*;
import dev.itsmeow.whisperwoods.entity.EntityHidebehind.HidebehindVariant;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.Difficulty;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.LinkedHashMap;

public class ModEntities {

    public static final EntityRegistrarHandler H = IMDLib.entityHandler(WhisperwoodsMod.MODID);

    public static LinkedHashMap<String, EntityTypeContainer<? extends MobEntity>> getEntities() {
        return H.ENTITIES;
    }

    public static void subscribe(IEventBus modBus) {
        H.subscribe(modBus);
    }

    public static final EntityTypeContainerContainable<EntityMoth, ItemModEntityContainer<EntityMoth>> MOTH = H.addContainable(EntityMoth.class, EntityMoth::new, "moth", b -> b
    .spawn(EntityClassification.AMBIENT, 10, 1, 3)
    .egg(0x442516, 0xc66121)
    .size(0.35F, 0.35F)
    .despawn()
    .config((holder, builder) -> holder.put(builder.comment("How many moths required to destroy a torch - Disabled if set to 0").worldRestart().defineInRange("moths_to_destroy_torch", 5, 0, Integer.MAX_VALUE)))
    .variants(
    "garden_tiger",
    "luna",
    "creeper_sphinx",
    "grey_spotted_hawk",
    "brown_spotted_hawk",
    "black_white_deaths_head",
    "brown_grey_deaths_head",
    "brown_orange_deaths_head")
    .biomes(Type.FOREST, Type.SWAMP)
    .containers(ItemModEntityContainer.get("bottled_%s", WhisperwoodsMod.TAB), c -> Items.GLASS_BOTTLE, EntityMoth::bottleTooltip));

    public static final EntityTypeContainer<EntityHidebehind> HIDEBEHIND = H.add(EntityHidebehind.class, EntityHidebehind::new, "hidebehind", b -> b
    .spawn(EntityClassification.MONSTER, 8, 1, 1)
    .defaultPlacement((t, w, e, p, r) -> w.getDifficulty() != Difficulty.PEACEFUL && MobEntity.canSpawnOn(t, w, e, p, r) && MonsterEntity.isValidLightLevel(w, p, r))
    .egg(0x473123, 0xfff494)
    .size(1F, 5.2F)
    .despawn()
    .variants(
    new HidebehindVariant("black"),
    new HidebehindVariant("coniferous"),
    new HidebehindVariant("darkforest"),
    new HidebehindVariant("forest"),
    new HidebehindVariant("mega_taiga"))
    .biomes(Type.FOREST));

    public static final EntityTypeContainer<EntityWisp> WISP = H.add(EntityWisp.class, EntityWisp::new, "wisp", b -> b
    .spawn(EntityClassification.CREATURE, 13, 1, 3)
    .egg(0xc36406, 0xffc008)
    .size(0.75F, 0.9F)
    .config((holder, builder) -> holder.put(builder.comment("Chance of wisp being hostile (soul stealer). Chance is a percentage out of 100. 0 is never, 100 is always").worldRestart().defineInRange("hostile_chance", 12.5D, 0D, 100D)))
    .biomes(Type.FOREST, Type.SWAMP));

    public static final EntityTypeContainer<EntityHirschgeist> HIRSCHGEIST = H.add(EntityHirschgeist.class, EntityHirschgeist::new, "hirschgeist", b -> b
    .spawn(EntityClassification.CREATURE, 2, 1, 1)
    .defaultPlacement((t, w, e, p, r) -> w.getEntitiesWithinAABB(EntityHirschgeist.class, new AxisAlignedBB(p).grow(300D)).size() == 0)
    .egg(0xfffff, 0x00000)
    .size(3F, 4F)
    .biomes(Type.FOREST));

    public static final EntityTypeContainer<EntityZotzpyre> ZOTZPYRE = H.add(EntityZotzpyre.class, EntityZotzpyre::new, "zotzpyre", b -> b
    .spawn(EntityClassification.MONSTER, 30, 1, 1)
    .defaultPlacement(EntityZotzpyre::canSpawn)
    .egg(0x321e13, 0x543a28).size(1F, 1F)
    .despawn()
    .biomes(Type.FOREST, Type.JUNGLE, Type.BEACH, Type.CONIFEROUS, Type.LUSH, Type.WASTELAND, Type.SWAMP, Type.HILLS, Type.MOUNTAIN)
    .variants(5));

}
