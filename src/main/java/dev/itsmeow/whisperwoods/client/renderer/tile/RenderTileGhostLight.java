package dev.itsmeow.whisperwoods.client.renderer.tile;

import java.util.Random;

import dev.itsmeow.whisperwoods.block.BlockGhostLight;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import dev.itsmeow.whisperwoods.tileentity.TileEntityGhostLight;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

public class RenderTileGhostLight extends TileEntityRenderer<TileEntityGhostLight> {

    private final Random rand = new Random();

    @Override
    public void render(TileEntityGhostLight tileentity, double x, double y, double z, float partialTicks, int destroyStage) {
        if(tileentity.hasWorld()) {
            Block block = tileentity.getBlockState().getBlock();
            if(block instanceof BlockGhostLight) {
                BlockGhostLight light = (BlockGhostLight) block;
                if(!Minecraft.getInstance().isGamePaused()) {
                    if(System.nanoTime() - tileentity.lastSpawn >= 100_000_000L) {
                        float scale = 0.3F;
                        int color = light.getColor();
                        float r = (color >> 16) & 0xFF;
                        float g = (color >> 8) & 0xFF;
                        float b = color & 0xFF;
                        // spawn bottom particles
                        for(int i = 0; i < 2; i++) {
                            double xO = (rand.nextFloat() * 2F - 1F) / 5;
                            double yO = (rand.nextFloat() * 2F - 1F) / 6 + 0.5F;
                            double zO = (rand.nextFloat() * 2F - 1F) / 5;
                            tileentity.getWorld().addParticle(new WispParticleData(r, g, b, scale),
                            tileentity.getPos().getX() + 0.5F + xO,
                            tileentity.getPos().getY() + yO,
                            tileentity.getPos().getZ() + 0.5F + zO, 0, 0.005F, 0);
                        }
                        float offset = 100;
                        double xO = (rand.nextFloat() * 2F - 1F) / 20;
                        double yO = (rand.nextFloat() * 2F - 1F) / 20;
                        double zO = (rand.nextFloat() * 2F - 1F) / 20;
                        tileentity.getWorld().addParticle(new WispParticleData(Math.min(r + offset, 255), Math.min(g + offset, 255), Math.min(b + offset, 255), scale),
                        tileentity.getPos().getX() + 0.5F + xO,
                        tileentity.getPos().getY() + 0.5F + yO,
                        tileentity.getPos().getZ() + 0.5F + zO, 0, 0.01F, 0);
                        // spawn upper particle
                        tileentity.getWorld().addParticle(new WispParticleData(r, g, b, scale),
                        tileentity.getPos().getX() + 0.5F + (rand.nextFloat() * 2F - 1F) / 10,
                        tileentity.getPos().getY() + (rand.nextFloat() * 2F - 1F) / 5 + 0.8F,
                        tileentity.getPos().getZ() + 0.5F + (rand.nextFloat() * 2F - 1F) / 10, 0, 0.01F, 0);
                    }
                }
            }
        }
    }

}
