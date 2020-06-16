package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ModSounds {
    
    public static final SoundEvent HIDEBEHIND_SOUND = getSound("hidebehind");
    
    private static final SoundEvent getSound(ResourceLocation location) {
        return new SoundEvent(location).setRegistryName(location);
    }
    
    private static final SoundEvent getSound(String location) {
        return getSound(new ResourceLocation(WhisperwoodsMod.MODID, location));
    }
}
