package dev.itsmeow.whisperwoods.network;

import dev.itsmeow.whisperwoods.particle.WispParticleData;
import dev.itsmeow.whisperwoods.util.WWClientTaskQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class HOFEffectPacket {

    public HOFEffectType type;
    public Vector3f pos;
    public int color;

    public HOFEffectPacket(HOFEffectType type, Vector3f pos, int color) {
        this.type = type;
        this.pos = pos;
        this.color = color;
    }

    public static void encode(HOFEffectPacket pkt, PacketBuffer buf) {
        buf.writeInt(pkt.type.ordinal());
        buf.writeFloat(pkt.pos.getX());
        buf.writeFloat(pkt.pos.getY());
        buf.writeFloat(pkt.pos.getZ());
        buf.writeInt(pkt.color);
    }

    public static HOFEffectPacket decode(PacketBuffer buf) {
        int i = buf.readInt();
        if(i < 0 || i >= HOFEffectType.values().length) {
            return null;
        }
        return new HOFEffectPacket(HOFEffectType.values()[i], new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat()), buf.readInt());
    }

    @SuppressWarnings("resource")
    public static void handle(HOFEffectPacket msg, Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT)
            return;
        ctx.get().enqueueWork(() -> {
            if(Minecraft.getInstance().world != null && msg != null) {
                HOFEffectType type = msg.type;
                Vector3f pos = msg.pos;
                int color = msg.color;
                float r = (color >> 16) & 0xFF;
                float g = (color >> 8) & 0xFF;
                float b = color & 0xFF;

                final int amt = 90;
                final float angle = 360F / (float) amt;
                final float speed = 0.5F;
                final WispParticleData data = new WispParticleData(r, g, b, 1);
                for(int i = 0; i < amt; i++) {
                    float pAngle = (float) Math.toRadians(angle * (float) i);
                    Minecraft.getInstance().world.addParticle(data, pos.getX(), pos.getY(), pos.getZ(), speed * Math.cos(pAngle), 0F, speed * Math.sin(pAngle));
                }
                if(type == HOFEffectType.HIRSCHGEIST) {
                    WWClientTaskQueue.schedule(21, () -> {
                        for(int i = 0; i < amt; i++) {
                            float pAngle = (float) Math.toRadians(angle * (float) i);
                            Minecraft.getInstance().world.addParticle(data, pos.getX() + 16 * speed * Math.cos(pAngle), pos.getY(), pos.getZ() + 16 * speed * Math.sin(pAngle), -speed * Math.cos(pAngle), 0F, -speed * Math.sin(pAngle));
                        }
                        WWClientTaskQueue.schedule(25, () -> {
                            Minecraft.getInstance().world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_RAVAGER_ROAR, SoundCategory.BLOCKS, 1F, 0.25F, false);
                            final WispParticleData data2 = new WispParticleData(r, g, b, 0.25F);
                            for(int i = 0; i < 500; i++) {
                                float xOff = (float) ((Math.random() - 0.5F) * 5F);
                                float yOff = (float) (Math.random() * 2.5F);
                                float zOff = (float) ((Math.random() - 0.5F) * 5F);
                                Minecraft.getInstance().world.addParticle(data2, pos.getX() + xOff, Math.floor(pos.getY()) - 1F + yOff, pos.getZ() + zOff, xOff / 16F, yOff / 16F, zOff / 16F);
                            }
                        });
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static enum HOFEffectType {
        CIRCLE,
        HIRSCHGEIST
    }
}
