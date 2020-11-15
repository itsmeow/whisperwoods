package dev.itsmeow.whisperwoods.client.renderer.tile;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.itsmeow.whisperwoods.block.BlockGhostLight;
import dev.itsmeow.whisperwoods.block.BlockWispLantern;
import dev.itsmeow.whisperwoods.init.ModBlocks;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import dev.itsmeow.whisperwoods.tileentity.TileEntityGhostLight;
import dev.itsmeow.whisperwoods.util.IHaveColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;

public class RenderTileGhostLight extends TileEntityRenderer<TileEntityGhostLight> {

    private final Random rand = new Random();

    public RenderTileGhostLight(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityGhostLight tileentity, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(tileentity.hasWorld()) {
            Block block = tileentity.getBlockState().getBlock();
            if(block instanceof IHaveColor) {
                IHaveColor light = (IHaveColor) block;
                if(!Minecraft.getInstance().isGamePaused()) {
                    if(System.nanoTime() - tileentity.lastSpawn >= 100_000_000L) {
                        float scale = 0.3F;
                        int color = light.getColor();
                        float r = (color >> 16) & 0xFF;
                        float g = (color >> 8) & 0xFF;
                        float b = color & 0xFF;
                        
                        double gXO = 0F;
                        double gYO = 0F;
                        double gZO = 0F;
                        if(block instanceof BlockWispLantern) {
                            BlockState state = tileentity.getBlockState();
                            Direction dir = state.get(BlockWispLantern.FACING);
                            gYO = -0.35F;
                            final float off = 0.25F;
                            if(dir.getAxis() != Axis.Y) {
                                gXO = dir.getXOffset() * off;
                                gZO = dir.getZOffset() * off;
                            }
                        }

                        if(block instanceof BlockGhostLight && tileentity.getWorld() != null && tileentity.getPos() != null && tileentity.getWorld().isBlockPresent(tileentity.getPos().down())) {
                            BlockState down = tileentity.getWorld().getBlockState(tileentity.getPos().down());
                            if(down.getBlock() == ModBlocks.HAND_OF_FATE.get()) {
                                gYO = -0.35F;
                            }
                        }

                        // spawn bottom particles
                        for(int i = 0; i < 2; i++) {
                            double xO = gXO + (rand.nextFloat() * 2F - 1F) / 5;
                            double yO = gYO + (rand.nextFloat() * 2F - 1F) / 6 + 0.5F;
                            double zO = gZO + (rand.nextFloat() * 2F - 1F) / 5;
                            tileentity.getWorld().addParticle(new WispParticleData(r, g, b, scale),
                            tileentity.getPos().getX() + 0.5F + xO,
                            tileentity.getPos().getY() + yO,
                            tileentity.getPos().getZ() + 0.5F + zO, 0, 0.005F, 0);
                        }
                        float offset = 100;
                        double xO = gXO + (rand.nextFloat() * 2F - 1F) / 20;
                        double yO = gYO + (rand.nextFloat() * 2F - 1F) / 20;
                        double zO = gZO + (rand.nextFloat() * 2F - 1F) / 20;
                        tileentity.getWorld().addParticle(new WispParticleData(Math.min(r + offset, 255), Math.min(g + offset, 255), Math.min(b + offset, 255), scale),
                        tileentity.getPos().getX() + 0.5F + xO,
                        tileentity.getPos().getY() + 0.5F + yO,
                        tileentity.getPos().getZ() + 0.5F + zO, 0, 0.01F, 0);
                        // spawn upper particle
                        tileentity.getWorld().addParticle(new WispParticleData(r, g, b, scale),
                        tileentity.getPos().getX() + gXO + 0.5F + (rand.nextFloat() * 2F - 1F) / 10,
                        tileentity.getPos().getY() + gYO + (rand.nextFloat() * 2F - 1F) / 5 + 0.8F,
                        tileentity.getPos().getZ() + gZO + 0.5F + (rand.nextFloat() * 2F - 1F) / 10, 0, 0.01F, 0);
                    }
                }
            }
        }
    }

}
