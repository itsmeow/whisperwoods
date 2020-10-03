package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {

    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WhisperwoodsMod.MODID);

    public static final RegistryObject<SoundEvent> HIDEBEHIND_SOUND = r("hidebehind");

    private static RegistryObject<SoundEvent> r(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(WhisperwoodsMod.MODID, name)));
    }

    public static void subscribe(IEventBus modEventBus) {
        SOUNDS.register(modEventBus);
    }

}
