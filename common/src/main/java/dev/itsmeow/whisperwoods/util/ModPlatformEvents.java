package dev.itsmeow.whisperwoods.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import me.shedaniel.architectury.utils.PlatformExpectedError;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class ModPlatformEvents {

    /**
     * Internally checks mob grief game rule on both platforms
     * @return true if mob grief allowed, false if not
     */
    @ExpectPlatform
    public static boolean mobGrief(Level level, Mob entity) {
        throw new PlatformExpectedError("ExpectPlatform failed: ModPlatformEvents.mobGrief()");
    }

}
