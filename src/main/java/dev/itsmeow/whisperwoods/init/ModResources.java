package dev.itsmeow.whisperwoods.init;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.util.ResourceLocation;

public class ModResources {

    public static final ResourceLocation HIDEBEHIND_GLOW = t("hidebehind_glow");
    public static final ResourceLocation HIDEBEHIND_OPEN_GLOW = t("hidebehind_open_glow");
    public static final ResourceLocation ZOTZPYRE_EYES = t("zotzpyre_eyes");

    private static ResourceLocation t(String tex) {
        return new ResourceLocation(WhisperwoodsMod.MODID, "textures/entity/" + tex + ".png");
    }
}
