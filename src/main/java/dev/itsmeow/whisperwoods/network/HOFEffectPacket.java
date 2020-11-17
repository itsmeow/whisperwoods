package dev.itsmeow.whisperwoods.network;

import java.util.function.Supplier;

import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class HOFEffectPacket {

    public Vector3f pos;
    public int color;

    public HOFEffectPacket(Vector3f pos, int color) {
        this.pos = pos;
        this.color = color;
    }

    public static void encode(HOFEffectPacket pkt, PacketBuffer buf) {
        buf.writeFloat(pkt.pos.getX());
        buf.writeFloat(pkt.pos.getY());
        buf.writeFloat(pkt.pos.getZ());
        buf.writeInt(pkt.color);
    }

    public static HOFEffectPacket decode(PacketBuffer buf) {
        return new HOFEffectPacket(new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat()), buf.readInt());
    }

    @SuppressWarnings("resource")
    public static void handle(HOFEffectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT)
            return;
        ctx.get().enqueueWork(() -> {
            if(Minecraft.getInstance().world != null) {
                Vector3f pos = msg.pos;
                int color = msg.color;
                float r = (color >> 16) & 0xFF;
                float g = (color >> 8) & 0xFF;
                float b = color & 0xFF;

                final int amt = 90;
                final float angle = 360F / (float) amt;
                final float speed = 0.5F;
                for(int i = 0; i < amt; i++) {
                    float pAngle = (float) Math.toRadians(angle * (float) i);
                    Minecraft.getInstance().world.addParticle(new WispParticleData(r, g, b, 1), pos.getX(), pos.getY(), pos.getZ(), speed * Math.cos(pAngle), 0F, speed * Math.sin(pAngle));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
