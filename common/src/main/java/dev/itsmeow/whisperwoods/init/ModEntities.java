package dev.itsmeow.whisperwoods.init;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.itsmeow.imdlib.IMDLib;
import dev.itsmeow.imdlib.entity.EntityRegistrarHandler;
import dev.itsmeow.imdlib.entity.EntityTypeContainer;
import dev.itsmeow.imdlib.entity.util.BiomeTypes;
import dev.itsmeow.imdlib.entity.util.EntityTypeContainerContainable;
import dev.itsmeow.imdlib.item.ItemModEntityContainer;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.entity.*;
import dev.itsmeow.whisperwoods.entity.EntityHidebehind.HidebehindVariant;
import dev.itsmeow.whisperwoods.entity.projectile.EntityHirschgeistFireball;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

import java.util.LinkedHashMap;

public class ModEntities {

    public static final EntityRegistrarHandler H = IMDLib.entityHandler(WhisperwoodsMod.MODID);

    public static final EntityTypeContainerContainable<EntityMoth, ItemModEntityContainer<EntityMoth>> MOTH = H.addContainable(EntityMoth.class, EntityMoth::new, "moth", () -> Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D), b -> b
    .spawn(MobCategory.AMBIENT, 10, 1, 3)
    .egg(0x442516, 0xc66121)
    .size(0.35F, 0.35F)
    .despawn()
    .config((holder, builder) -> holder.put("moths_to_destroy_torch", Integer.class, builder.defineInRange("moths_to_destroy_torch", "How many moths required to destroy a torch - Disabled if set to 0",5, 0, Integer.MAX_VALUE)))
    .variants(
    "garden_tiger",
    "luna",
    "creeper_sphinx",
    "grey_spotted_hawk",
    "brown_spotted_hawk",
    "black_white_deaths_head",
    "brown_grey_deaths_head",
    "brown_orange_deaths_head",
    "black_witch",
    "brahmin",
    "dappled_wood",
    "owl",
    "vampire")
    .biomesOverworld(BiomeTypes.FOREST, BiomeTypes.SWAMP)
    .containers("bottled_%s", ItemModEntityContainer.get(ModCreativeTabs.WHISPERWOODS), "", c -> Items.GLASS_BOTTLE, EntityMoth::bottleTooltip));

    public static final EntityTypeContainer<EntityHidebehind> HIDEBEHIND = H.add(EntityHidebehind.class, EntityHidebehind::new, "hidebehind", () -> Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20D).add(Attributes.ATTACK_DAMAGE).add(Attributes.ATTACK_DAMAGE, 15D), b -> b
    .spawn(MobCategory.MONSTER, 8, 1, 1)
    .defaultPlacement((t, w, e, p, r) -> w.getDifficulty() != Difficulty.PEACEFUL && Mob.checkMobSpawnRules(t, w, e, p, r) && Monster.isDarkEnoughToSpawn(w, p, r))
    .egg(0x473123, 0xfff494)
    .size(1F, 5.2F)
    .despawn()
    .variants(
    new HidebehindVariant("black"),
    new HidebehindVariant("coniferous"),
    new HidebehindVariant("darkforest"),
    new HidebehindVariant("forest"),
    new HidebehindVariant("mega_taiga"))
    .biomesOverworld(BiomeTypes.FOREST));

    public static final EntityTypeContainer<EntityWisp> WISP = H.add(EntityWisp.class, EntityWisp::new, "wisp", () -> Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.5D), b -> b
    .spawn(MobCategory.CREATURE, 13, 1, 3)
    .egg(0xc36406, 0xffc008)
    .size(0.75F, 0.9F)
    .config((holder, builder) -> holder.put("hostile_chance", Double.class, builder.defineInRange("hostile_chance", "Chance of wisp being hostile (soul stealer). Chance is a percentage out of 100. 0 is never, 100 is always", 12.5D, 0D, 100D)))
    .biomes(BiomeTypes.FOREST, BiomeTypes.SWAMP));


    public static final EntityTypeContainer<EntityHirschgeist> HIRSCHGEIST = H.add(EntityHirschgeist.class, EntityHirschgeist::new, "hirschgeist", () -> Mob.createMobAttributes()
    .add(Attributes.MAX_HEALTH, 150.0D)
    .add(Attributes.FOLLOW_RANGE, 100.0D)
    .add(Attributes.MOVEMENT_SPEED, 0.5D)
    .add(Attributes.ARMOR, 10D)
    .add(Attributes.ARMOR_TOUGHNESS, 5D)
    .add(Attributes.ATTACK_DAMAGE)
    .add(Attributes.ATTACK_DAMAGE, 6.0D), b -> b
    .spawn(MobCategory.CREATURE, 2, 1, 1)
    .defaultPlacement((t, w, e, p, r) -> w.getEntitiesOfClass(EntityHirschgeist.class, new AABB(p).inflate(300D)).isEmpty())
    .egg(0xfffff, 0x00000)
    .size(3F, 4F)
    .biomesOverworld(BiomeTypes.FOREST));

    public static final EntityTypeContainer<EntityZotzpyre> ZOTZPYRE = H.add(EntityZotzpyre.class, EntityZotzpyre::new, "zotzpyre", () -> Mob.createMobAttributes()
    .add(Attributes.MAX_HEALTH, 20.0D)
    .add(Attributes.ATTACK_DAMAGE, 3.0D)
    .add(Attributes.FLYING_SPEED)
    .add(Attributes.FLYING_SPEED, 1D)
    .add(Attributes.FOLLOW_RANGE, 32D), b -> b
    .spawn(MobCategory.MONSTER, 30, 1, 1)
    .defaultPlacement(EntityZotzpyre::canSpawn)
    .egg(0x321e13, 0x543a28).size(1F, 1F)
    .despawn()
    .biomesOverworld(BiomeTypes.FOREST, BiomeTypes.JUNGLE, BiomeTypes.BEACH, BiomeTypes.CONIFEROUS, BiomeTypes.LUSH, BiomeTypes.WASTELAND, BiomeTypes.SWAMP, BiomeTypes.HILL, BiomeTypes.MOUNTAIN)
    .variants(6));


    public static LinkedHashMap<String, EntityTypeContainer<? extends Mob>> getEntities() {
        return H.ENTITIES;
    }

    public static final RegistrySupplier<EntityType<EntityHirschgeistFireball>> PROJECTILE_HIRSCHGEIST_FIREBALL = projectile(EntityHirschgeistFireball::new, "hirschgeist_fireball", 0.8F, 0.8F);

    public static void init() {
        H.init();
    }

    private static <T extends Projectile> RegistrySupplier<EntityType<T>> projectile(EntityType.EntityFactory<T> factory, String name, float width, float height) {
        return IMDLib.getRegistry(Registries.ENTITY_TYPE).register(new ResourceLocation(WhisperwoodsMod.MODID, name), () -> H.createEntityType(factory, name, MobCategory.MISC, 64, 1, true, width, height));
    }
}
