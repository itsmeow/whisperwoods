package dev.itsmeow.whisperwoods.util.forge;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

public class ModPlatformEventsImpl {

    public static boolean mobGrief(Level level, Mob entity) {
        return ForgeEventFactory.getMobGriefingEvent(level, entity);
    }

}
