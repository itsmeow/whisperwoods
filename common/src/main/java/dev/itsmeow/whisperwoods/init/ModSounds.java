package dev.itsmeow.whisperwoods.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(WhisperwoodsMod.MODID, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> HIDEBEHIND_SCARE = r("entity.hidebehind.scare");
    public static final RegistrySupplier<SoundEvent> HIDEBEHIND_AMBIENT = r("entity.hidebehind.ambient");
    public static final RegistrySupplier<SoundEvent> HIDEBEHIND_HURT = r("entity.hidebehind.hurt");
    public static final RegistrySupplier<SoundEvent> HIDEBEHIND_DEATH = r("entity.hidebehind.death");
    public static final RegistrySupplier<SoundEvent> HIRSCHGEIST_AMBIENT = r("entity.hirschgeist.ambient");
    public static final RegistrySupplier<SoundEvent> HIRSCHGEIST_HURT = r("entity.hirschgeist.hurt");
    public static final RegistrySupplier<SoundEvent> HIRSCHGEIST_DEATH = r("entity.hirschgeist.death");

    private static RegistrySupplier<SoundEvent> r(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(WhisperwoodsMod.MODID, name)));
    }

    public static void init() {
        SOUNDS.register();
    }

}
