package its_meow.whisperwoods.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import its_meow.whisperwoods.WhisperwoodsMod;
import its_meow.whisperwoods.init.ModEntities;
import its_meow.whisperwoods.util.EntityTypeContainer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class WhisperwoodsConfig {

    private static EntityConfig ENTITY_CONFIG = null;

    public static ForgeConfigSpec SERVER_CONFIG = null;

    public static void setupConfig() {
        final Pair<EntityConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(EntityConfig::new);
        SERVER_CONFIG = specPair.getRight();
        ENTITY_CONFIG = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        WhisperwoodsMod.LOGGER.debug("Loading {} {}", WhisperwoodsMod.MODID, configEvent.getConfig().getFileName());
        if (configEvent.getConfig().getSpec() == SERVER_CONFIG) {
            ENTITY_CONFIG.onWorldLoad();
        }
    }
    
    @SubscribeEvent
    public static void onLoad(final ModConfig.ConfigReloading configEvent) {
        WhisperwoodsMod.LOGGER.debug("Reloading {} {}", WhisperwoodsMod.MODID, configEvent.getConfig().getFileName());
        if (configEvent.getConfig().getSpec() == SERVER_CONFIG) {
            ENTITY_CONFIG.loadEntityData();
        }
    }
    
    public static class EntityConfig {
        public ForgeConfigSpec.Builder builder;

        EntityConfig(ForgeConfigSpec.Builder builder) {
            this.builder = builder;
            for(EntityTypeContainer<?> cont : ModEntities.ENTITIES.values()) {
                cont.initConfiguration(builder);
            }
            builder.build();
        }

        public void loadEntityData() {
            // Replace entity data

            for (EntityTypeContainer<?> container : ModEntities.ENTITIES.values()) {
                EntityTypeContainer<?>.EntityConfiguration section = container.getConfiguration();
                container.spawnMaxGroup = section.spawnMaxGroup.get();
                container.spawnMinGroup = section.spawnMinGroup.get();
                container.spawnWeight = section.spawnWeight.get();
                container.doSpawning = section.doSpawning.get();
                container.despawn = section.doDespawn.get();
                container.customConfigurationLoad();

                // Parse biomes

                List<Biome> biomesList = new ArrayList<Biome>();
                for (String biomeID : section.biomesList.get()) {
                    Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeID));
                    if (biome == null) { // Could not get biome with ID
                        WhisperwoodsMod.LOGGER.error("Invalid biome configuration entered for entity \"" + container.entityName + "\" (biome was mistyped or a biome mod was removed?): " + biomeID);
                    } else { // Valid biome
                        biomesList.add(biome);
                    }
                }

                container.setBiomes(biomesList.toArray(new Biome[0]));
            }
        }

        @SuppressWarnings("unchecked")
        public void onWorldLoad() {

            // Fill containers with proper values from their config sections
            this.loadEntityData();

            // Add spawns based on new container data
            if (!ModEntities.ENTITIES.values().isEmpty()) {
                for(EntityTypeContainer<?> entry : ModEntities.ENTITIES.values()) {
                    EntityType<?> type = entry.entityType;
                    if (entry.doSpawning) {
                        if (entry.spawnType == EntityClassification.WATER_CREATURE && EntitySpawnPlacementRegistry.getPlacementType((EntityType<? extends MobEntity>) type) == null) {
                            // This thing breaks every other day
                            try {
                                EntitySpawnPlacementRegistry.<MobEntity>register((EntityType<MobEntity>) type, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (t, w, s, b, r) -> b.getY() > 45 && b.getY() < w.getSeaLevel());
                            } catch(Exception e) { // Just in case
                                e.printStackTrace();
                            }
                        }
                        Method addSpawn = ObfuscationReflectionHelper.findMethod(Biome.class, "func_201866_a",
                        EntityClassification.class, SpawnListEntry.class);
                        for (Biome biome : entry.getBiomes()) {
                            try {
                                addSpawn.invoke(biome, entry.spawnType, new SpawnListEntry((EntityType<? extends MobEntity>) type, entry.spawnWeight, entry.spawnMinGroup, entry.spawnMaxGroup));
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

}