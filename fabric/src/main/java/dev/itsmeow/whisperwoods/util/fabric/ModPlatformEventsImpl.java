package dev.itsmeow.whisperwoods.util.fabric;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class ModPlatformEventsImpl {

    public static boolean mobGrief(Level level, Mob entity) {
        return level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }

}
