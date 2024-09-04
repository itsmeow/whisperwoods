package dev.itsmeow.whisperwoods.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import dev.itsmeow.whisperwoods.util.TaskQueue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.joml.Vector3f;

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

    public static void encode(HOFEffectPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.type.ordinal());
        buf.writeFloat(pkt.pos.x());
        buf.writeFloat(pkt.pos.y());
        buf.writeFloat(pkt.pos.z());
        buf.writeInt(pkt.color);
    }

    public static HOFEffectPacket decode(FriendlyByteBuf buf) {
        int i = buf.readInt();
        if(i < 0 || i >= HOFEffectType.values().length) {
            return null;
        }
        return new HOFEffectPacket(HOFEffectType.values()[i], new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat()), buf.readInt());
    }

    public static void handle(HOFEffectPacket msg, Supplier<NetworkManager.PacketContext> ctx) {
        if(ctx.get().getEnvironment() == Env.CLIENT) {
            ctx.get().queue(() -> {
                if (Minecraft.getInstance().level != null && msg != null) {
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
                    for (int i = 0; i < amt; i++) {
                        float pAngle = (float) Math.toRadians(angle * (float) i);
                        Minecraft.getInstance().level.addParticle(data, pos.x(), pos.y(), pos.z(), speed * Math.cos(pAngle), 0F, speed * Math.sin(pAngle));
                    }
                    if (type == HOFEffectType.HIRSCHGEIST) {
                        TaskQueue.QUEUE_CLIENT.schedule(21, () -> {
                            for (int i = 0; i < amt; i++) {
                                float pAngle = (float) Math.toRadians(angle * (float) i);
                                Minecraft.getInstance().level.addParticle(data, pos.x() + 16 * speed * Math.cos(pAngle), pos.y(), pos.z() + 16 * speed * Math.sin(pAngle), -speed * Math.cos(pAngle), 0F, -speed * Math.sin(pAngle));
                            }
                            TaskQueue.QUEUE_CLIENT.schedule(25, () -> {
                                Minecraft.getInstance().level.playLocalSound(pos.x(), pos.y(), pos.z(), SoundEvents.RAVAGER_ROAR, SoundSource.BLOCKS, 1F, 0.25F, false);
                                final WispParticleData data2 = new WispParticleData(r, g, b, 0.25F);
                                for (int i = 0; i < 500; i++) {
                                    float xOff = (float) ((Math.random() - 0.5F) * 5F);
                                    float yOff = (float) (Math.random() * 2.5F);
                                    float zOff = (float) ((Math.random() - 0.5F) * 5F);
                                    Minecraft.getInstance().level.addParticle(data2, pos.x() + xOff, Math.floor(pos.y()) - 1F + yOff, pos.z() + zOff, xOff / 16F, yOff / 16F, zOff / 16F);
                                }
                            });
                        });
                    }
                }
            });
        }
    }

    public enum HOFEffectType {
        CIRCLE,
        HIRSCHGEIST
    }
}
