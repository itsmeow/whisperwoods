package dev.itsmeow.whisperwoods.network;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class WWNetwork {

    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(WhisperwoodsMod.MODID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    private static int packets = 0;

    private static void setup(final FMLCommonSetupEvent event) {
        addPkt(HOFEffectPacket.class, HOFEffectPacket::encode, HOFEffectPacket::decode, HOFEffectPacket::handle, NetworkDirection.PLAY_TO_CLIENT);
        addPkt(WispAttackPacket.class, WispAttackPacket::encode, WispAttackPacket::decode, WispAttackPacket::handle, NetworkDirection.PLAY_TO_CLIENT);
    }

    protected static <MSG> void addPkt(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        HANDLER.registerMessage(packets++, messageType, encoder, decoder, messageConsumer);
    }

    protected static <MSG> void addPkt(Class<MSG> messageType, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer, NetworkDirection networkDirection) {
        HANDLER.registerMessage(packets++, messageType, encoder, decoder, messageConsumer, Optional.of(networkDirection));
    }

    public static void subscribe(IEventBus modBus) {
        modBus.addListener(WWNetwork::setup);
    }
}
