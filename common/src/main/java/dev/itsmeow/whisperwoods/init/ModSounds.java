package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.sql.Ref;

public class ModSounds {

    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(WhisperwoodsMod.MODID, Registry.SOUND_EVENT_REGISTRY);

    public static final RegistrySupplier<SoundEvent> HIDEBEHIND_SOUND = r("hidebehind");
    public static final RegistrySupplier<SoundEvent> HIRSCHGEIST_AMBIENT = r("entity.hirschgeist.ambient");
    public static final RegistrySupplier<SoundEvent> HIRSCHGEIST_HURT = r("entity.hirschgeist.hurt");
    public static final RegistrySupplier<SoundEvent> HIRSCHGEIST_DEATH = r("entity.hirschgeist.death");

    private static RegistrySupplier<SoundEvent> r(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(WhisperwoodsMod.MODID, name)));
    }

    public static void init() {
        SOUNDS.register();
    }

}
