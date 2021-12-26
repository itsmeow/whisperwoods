package dev.itsmeow.whisperwoods.network;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import me.shedaniel.architectury.networking.NetworkChannel;
import net.minecraft.resources.ResourceLocation;

public class WWNetwork {

    public static final NetworkChannel HANDLER = NetworkChannel.create(new ResourceLocation(WhisperwoodsMod.MODID, "main_channel"));

    public static void init() {
        HANDLER.register(HOFEffectPacket.class, HOFEffectPacket::encode, HOFEffectPacket::decode, HOFEffectPacket::handle);
        HANDLER.register(WispAttackPacket.class, WispAttackPacket::encode, WispAttackPacket::decode, WispAttackPacket::handle);
    }

}
