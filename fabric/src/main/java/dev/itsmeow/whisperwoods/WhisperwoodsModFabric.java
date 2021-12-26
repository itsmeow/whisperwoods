package dev.itsmeow.whisperwoods;

import dev.itsmeow.imdlib.util.ClassLoadHacks;
import net.fabricmc.api.ModInitializer;

public class WhisperwoodsModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        WhisperwoodsMod.construct();
        WhisperwoodsMod.init(Runnable::run);
    }

}
