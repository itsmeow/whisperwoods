package its_meow.whisperwoods.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.function.Function;

import its_meow.whisperwoods.WhisperwoodsMod;
import its_meow.whisperwoods.entity.EntityHidebehind;
import its_meow.whisperwoods.entity.EntityMoth;
import its_meow.whisperwoods.entity.EntityWisp;
import its_meow.whisperwoods.util.EntityTypeContainer;
import its_meow.whisperwoods.util.EntityTypeContainer.CustomConfigurationHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ModEntities {

    public static final LinkedHashMap<String, EntityTypeContainer<? extends LivingEntity>> ENTITIES = new LinkedHashMap<>();

    /*
     * ##########################################################
     * 
     * ##########################################################
     */

    public static final EntityTypeContainer<EntityMoth> MOTH = setupContainer(new EntityTypeContainer<EntityMoth>(EntityMoth.class, EntityMoth::new, "moth", EntityClassification.AMBIENT, 0x442516, 0xc66121, 10, 1, 3, 0.35F, 0.35F, true, new CustomConfigurationHolder() {
        private ForgeConfigSpec.IntValue requiredMoths;
        @Override
        protected void customConfigurationInit(ForgeConfigSpec.Builder builder) {
            this.requiredMoths = builder.comment("How many moths required to destroy a torch - Disabled if set to 0").worldRestart().defineInRange("moths_to_destroy_torch", 5, 0, Integer.MAX_VALUE);
        }
        @Override
        protected void customConfigurationLoad() {
            EntityMoth.MOTHS_REQUIRED_TO_DESTROY = requiredMoths.get();
        }
    }, Type.FOREST, Type.SWAMP));

    public static final EntityTypeContainer<EntityHidebehind> HIDEBEHIND = setupContainer(new EntityTypeContainer<EntityHidebehind>(EntityHidebehind.class, EntityHidebehind::new, "hidebehind", EntityClassification.MONSTER, 0x473123, 0xfff494, 7, 1, 1, 1F, 5.2F, true, null, Type.FOREST));
    
    public static final EntityTypeContainer<EntityWisp> WISP = setupContainer(new EntityTypeContainer<EntityWisp>(EntityWisp.class, EntityWisp::new, "wisp", EntityClassification.CREATURE, 0xc36406, 0xffc008, 8, 1, 3, 0.75F, 0.75F, true, new CustomConfigurationHolder() {
        private ForgeConfigSpec.IntValue hostileChance;
        @Override
        protected void customConfigurationInit(ForgeConfigSpec.Builder builder) {
            this.hostileChance = builder.comment("Chance of wisp being hostile (soul stealer). Chance is 1/x, where x is the value specified. 0 is no chance, 1 is 100% chance, 2 is 50% chance, etc").worldRestart().defineInRange("hostile_chance", 10, 0, Integer.MAX_VALUE);
        }
        @Override
        protected void customConfigurationLoad() {
            EntityWisp.HOSTILE_CHANCE = hostileChance.get();
        }
    }, Type.FOREST, Type.SWAMP));

    /*
     * ##########################################################
     * 
     * ##########################################################
     */

    @SuppressWarnings("unchecked")
    public static <T extends LivingEntity>EntityTypeContainer<T> getEntityTypeContainer(String name) {
        return (EntityTypeContainer<T>) ENTITIES.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <T extends LivingEntity>EntityType<T> getEntityType(String name) {
        return (EntityType<T>) ENTITIES.get(name).entityType;
    }

    private static <T extends LivingEntity>EntityTypeContainer<T> setupContainer(EntityTypeContainer<T> c) {
        c.entityType = ModEntities.<T>createEntityType(c);
        ENTITIES.put(c.entityName, c);
        return c;
    }

    private static Field type$serializable = null;

    public static <T extends LivingEntity> EntityType<T> createEntityType(EntityTypeContainer<T> container) {
        return createEntityType(container.entityClass, container.factory, container.entityName, container.spawnType, 64, 1, true, container.width, container.height);
    }

    public static <T extends Entity> EntityType<T> createEntityType(Class<T> EntityClass, Function<World, T> func, String entityNameIn, EntityClassification classification, int trackingRange, int updateInterval, boolean velUpdates, float width, float height) {
        EntityType<T> type =  EntityType.Builder.<T>create((etype, world) -> func.apply(world), classification).setTrackingRange(trackingRange).setUpdateInterval(updateInterval).setShouldReceiveVelocityUpdates(velUpdates).size(width, height).setCustomClientFactory((e, world) -> func.apply(world)).disableSerialization().build(WhisperwoodsMod.MODID + ":" + entityNameIn.toLowerCase());
        type.setRegistryName(WhisperwoodsMod.MODID + ":" + entityNameIn.toLowerCase());

        // Workaround for "no datafixer registered" log spam
        try {
            if(type$serializable == null) {
                type$serializable = ObfuscationReflectionHelper.findField(EntityType.class, "field_200733_aL");
            }
            setFinalField(type$serializable, type, true);
        } catch(Exception e) {
            WhisperwoodsMod.LOGGER.warn("Unable to set serializable for " + entityNameIn + ". This could result in possible saving issues with entities!");
        }
        return type;
    }

    private static void setFinalField(Field field, Object object, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(object, newValue);
    }

}
