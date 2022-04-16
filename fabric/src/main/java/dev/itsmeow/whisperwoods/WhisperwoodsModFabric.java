package dev.itsmeow.whisperwoods;

import net.fabricmc.api.ModInitializer;

public class WhisperwoodsModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        WhisperwoodsMod.construct();
        WhisperwoodsMod.init(Runnable::run);
    }

}
