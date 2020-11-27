package dev.itsmeow.whisperwoods.network;

import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class WispAttackPacket {

    public Vec3d fromPos;
    public int color;

    public WispAttackPacket(Vec3d fromPos, int color) {
        this.fromPos = fromPos;
        this.color = color;
    }

    public static void encode(WispAttackPacket pkt, PacketBuffer buf) {
        buf.writeDouble(pkt.fromPos.getX());
        buf.writeDouble(pkt.fromPos.getY());
        buf.writeDouble(pkt.fromPos.getZ());
        buf.writeInt(pkt.color);
    }

    public static WispAttackPacket decode(PacketBuffer buf) {
        return new WispAttackPacket(new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readInt());
    }

    @SuppressWarnings("resource")
    public static void handle(WispAttackPacket msg, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT)
            return;
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().world != null && msg != null) {
                Vec3d pos = msg.fromPos;
                int color = msg.color;
                float r = (color >> 16) & 0xFF;
                float g = (color >> 8) & 0xFF;
                float b = color & 0xFF;
                ClientPlayerEntity player = Minecraft.getInstance().player;
                if (player != null) {
                    Vec3d destPos = player.getPositionVec().add(0F, player.getEyeHeight(), 0F);
                    final double stops = Math.round(3 * pos.distanceTo(destPos));
                    double dirX = (destPos.getX() - pos.getX()) / stops;
                    double dirY = (destPos.getY() - pos.getY()) / stops;
                    double dirZ = (destPos.getZ() - pos.getZ()) / stops;
                    Vec3d dir = new Vec3d(dirX, dirY, dirZ);
                    for (double i = 1; i <= stops; i++) {
                        Vec3d posOff = dir.scale(i).add(pos);
                        double sin = Math.sin((i / stops) * (Math.PI * 2D)) / 4D;
                        double cos = Math.cos((i / stops) * (Math.PI * 2D)) / 4D;
                        player.world.addParticle(new WispParticleData(r, g, b, 0.5F), posOff.getX() + sin, posOff.getY() + sin, posOff.getZ() - cos, 0F, 0F, 0F);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
