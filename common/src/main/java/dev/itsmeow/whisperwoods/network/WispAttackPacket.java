package dev.itsmeow.whisperwoods.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class WispAttackPacket {

    public Vec3 fromPos;
    public int color;

    public WispAttackPacket(Vec3 fromPos, int color) {
        this.fromPos = fromPos;
        this.color = color;
    }

    public static void encode(WispAttackPacket pkt, FriendlyByteBuf buf) {
        buf.writeDouble(pkt.fromPos.x());
        buf.writeDouble(pkt.fromPos.y());
        buf.writeDouble(pkt.fromPos.z());
        buf.writeInt(pkt.color);
    }

    public static WispAttackPacket decode(FriendlyByteBuf buf) {
        return new WispAttackPacket(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readInt());
    }

    public static void handle(WispAttackPacket msg, Supplier<NetworkManager.PacketContext> ctx) {
        if(ctx.get().getEnvironment() == Env.CLIENT) {
            ctx.get().queue(() -> {
                if (Minecraft.getInstance().level != null && msg != null) {
                    Vec3 pos = msg.fromPos;
                    int color = msg.color;
                    float r = (color >> 16) & 0xFF;
                    float g = (color >> 8) & 0xFF;
                    float b = color & 0xFF;
                    LocalPlayer player = Minecraft.getInstance().player;
                    if (player != null) {
                        Vec3 destPos = player.position().add(0F, player.getEyeHeight(), 0F);
                        final double stops = Math.round(3 * pos.distanceTo(destPos));
                        double dirX = (destPos.x() - pos.x()) / stops;
                        double dirY = (destPos.y() - pos.y()) / stops;
                        double dirZ = (destPos.z() - pos.z()) / stops;
                        Vec3 dir = new Vec3(dirX, dirY, dirZ);
                        for (double i = 1; i <= stops; i++) {
                            Vec3 posOff = dir.scale(i).add(pos);
                            double sin = Math.sin((i / stops) * (Math.PI * 2D)) / 4D;
                            double cos = Math.cos((i / stops) * (Math.PI * 2D)) / 4D;
                            player.level().addParticle(new WispParticleData(r, g, b, 0.5F), posOff.x() + sin, posOff.y() + sin, posOff.z() - cos, 0F, 0F, 0F);
                        }
                    }
                }
            });
        }
    }
}
