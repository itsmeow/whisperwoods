package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.imdlib.IMDLib;
import dev.itsmeow.imdlib.entity.EntityRegistrarHandler;
import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.EntityTypeContainer.Builder;
import dev.itsmeow.imdlib.entity.util.EntityTypeContainer.CustomConfigurationHolder;
import dev.itsmeow.imdlib.entity.util.EntityTypeContainerContainable;
import dev.itsmeow.imdlib.entity.util.IContainable;
import dev.itsmeow.imdlib.item.IContainerItem;
import dev.itsmeow.imdlib.item.ItemModEntityContainer;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.entity.*;
import dev.itsmeow.whisperwoods.entity.EntityHidebehind.HidebehindVariant;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.LinkedHashMap;
import java.util.function.Function;

public class ModEntities {

    public static final EntityRegistrarHandler H = IMDLib.entityHandler(WhisperwoodsMod.MODID);

    public static final EntityTypeContainerContainable<EntityMoth, ItemModEntityContainer<EntityMoth>> MOTH = H.add(ModEntities.<EntityMoth, ItemModEntityContainer<EntityMoth>>entityContainable(EntityMoth.class, EntityMoth::new, "moth")
    .spawn(EntityClassification.AMBIENT, 10, 1, 3)
    .egg(0x442516, 0xc66121)
    .size(0.35F, 0.35F)
    .despawn()
    .config(new CustomConfigurationHolder() {
        private ForgeConfigSpec.IntValue requiredMoths;

        @Override
        public void customConfigurationInit(ForgeConfigSpec.Builder builder) {
            this.requiredMoths = builder.comment("How many moths required to destroy a torch - Disabled if set to 0").worldRestart().defineInRange("moths_to_destroy_torch", 5, 0, Integer.MAX_VALUE);
        }

        @Override
        public void customConfigurationLoad() {
            EntityMoth.MOTHS_REQUIRED_TO_DESTROY = requiredMoths.get();
        }
    })
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

    public static final EntityTypeContainer<EntityHidebehind> HIDEBEHIND = H.add(entity(EntityHidebehind.class, EntityHidebehind::new, "hidebehind")
    .spawn(EntityClassification.CREATURE, 5, 1, 1)
    .defaultPlacement((t, w, e, p, r) -> w.getDifficulty() != Difficulty.PEACEFUL && MonsterEntity.isValidLightLevel(w, p, r) && MobEntity.canSpawnOn(t, w, e, p, r))
    .egg(0x473123, 0xfff494)
    .size(1F, 5.2F)
    .variants(
    new HidebehindVariant("black"),
    new HidebehindVariant("coniferous"),
    new HidebehindVariant("darkforest"),
    new HidebehindVariant("forest"),
    new HidebehindVariant("mega_taiga"))
    .biomes(Type.FOREST));

    public static final EntityTypeContainer<EntityWisp> WISP = H.add(entity(EntityWisp.class, EntityWisp::new, "wisp")
    .spawn(EntityClassification.CREATURE, 13, 1, 3)
    .egg(0xc36406, 0xffc008)
    .size(0.75F, 0.9F)
    .config(new CustomConfigurationHolder() {
        private ForgeConfigSpec.IntValue hostileChance;

        @Override
        public void customConfigurationInit(ForgeConfigSpec.Builder builder) {
            this.hostileChance = builder.comment("Chance of wisp being hostile (soul stealer). Chance is 1/x, where x is the value specified. 0 is no chance, 1 is 100% chance, 2 is 50% chance, etc").worldRestart().defineInRange("hostile_chance", 8, 0, Integer.MAX_VALUE);
        }

        @Override
        public void customConfigurationLoad() {
            EntityWisp.HOSTILE_CHANCE = hostileChance.get();
        }
    })
    .biomes(Type.FOREST, Type.SWAMP));

    public static final EntityTypeContainer<EntityHirschgeist> HIRSCHGEIST = H.add(entity(EntityHirschgeist.class, EntityHirschgeist::new, "hirschgeist")
    .spawn(EntityClassification.CREATURE, 2, 1, 1)
    .egg(0xfffff, 0x00000)
    .size(3F, 4F)
    .biomes(Type.FOREST));

    public static final EntityTypeContainer<EntityZotzpyre> ZOTZPYRE = H.add(entity(EntityZotzpyre.class, EntityZotzpyre::new, "zotzpyre")
    .spawn(EntityClassification.MONSTER, 30, 1, 1)
    .defaultPlacement(EntityZotzpyre::canSpawn)
    .egg(0x321e13, 0x543a28).size(1F, 1F)
    .despawn()
    .biomes(Type.FOREST, Type.JUNGLE, Type.BEACH, Type.CONIFEROUS, Type.LUSH, Type.WASTELAND, Type.SWAMP, Type.HILLS, Type.MOUNTAIN)
    .variants(5));

    public static LinkedHashMap<String, EntityTypeContainer<? extends MobEntity>> getEntities() {
        return H.ENTITIES;
    }

    private static <T extends MobEntity> Builder<T> entity(Class<T> entityClass, Function<World, T> func, String entityNameIn) {
        return EntityTypeContainer.Builder.create(entityClass, func, entityNameIn, WhisperwoodsMod.MODID);
    }

    private static <T extends MobEntity & IContainable, I extends Item & IContainerItem<T>> EntityTypeContainerContainable.Builder<T, I> entityContainable(Class<T> EntityClass, Function<World, T> func, String entityNameIn) {
        return EntityTypeContainerContainable.Builder.create(EntityClass, func, entityNameIn, WhisperwoodsMod.MODID);
    }

    public static void subscribe(IEventBus modBus) {
        H.subscribe(modBus);
    }
}
