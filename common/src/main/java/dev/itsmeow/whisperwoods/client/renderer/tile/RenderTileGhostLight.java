package dev.itsmeow.whisperwoods.client.renderer.tile;

import java.util.Random;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.itsmeow.whisperwoods.block.GhostLightBlock;
import dev.itsmeow.whisperwoods.block.WispLanternBlock;
import dev.itsmeow.whisperwoods.init.ModBlocks;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import dev.itsmeow.whisperwoods.blockentity.GhostLightBlockEntity;
import dev.itsmeow.whisperwoods.util.IHaveColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RenderTileGhostLight extends BlockEntityRenderer<GhostLightBlockEntity> {

    private final Random rand = new Random();

    public RenderTileGhostLight(BlockEntityRenderDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(GhostLightBlockEntity tileentity, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if(tileentity.hasLevel()) {
            Block block = tileentity.getBlockState().getBlock();
            if(block instanceof IHaveColor) {
                IHaveColor light = (IHaveColor) block;
                if(!Minecraft.getInstance().isPaused()) {
                    if(System.nanoTime() - tileentity.lastSpawn >= 100_000_000L) {
                        float scale = 0.3F;
                        int color = light.getColor();
                        float r = (color >> 16) & 0xFF;
                        float g = (color >> 8) & 0xFF;
                        float b = color & 0xFF;
                        
                        double gXO = 0F;
                        double gYO = 0F;
                        double gZO = 0F;
                        if(block instanceof WispLanternBlock) {
                            BlockState state = tileentity.getBlockState();
                            Direction dir = state.getValue(WispLanternBlock.FACING);
                            gYO = -0.35F;
                            final float off = 0.25F;
                            if(dir.getAxis() != Axis.Y) {
                                gXO = dir.getStepX() * off;
                                gZO = dir.getStepZ() * off;
                            }
                        }

                        if(block instanceof GhostLightBlock && tileentity.getLevel() != null && tileentity.getBlockPos() != null && tileentity.getLevel().isLoaded(tileentity.getBlockPos().below())) {
                            BlockState down = tileentity.getLevel().getBlockState(tileentity.getBlockPos().below());
                            if(down.getBlock() == ModBlocks.HAND_OF_FATE.get()) {
                                gYO = -0.35F;
                            }
                        }

                        // spawn bottom particles
                        for(int i = 0; i < 2; i++) {
                            double xO = gXO + (rand.nextFloat() * 2F - 1F) / 5;
                            double yO = gYO + (rand.nextFloat() * 2F - 1F) / 6 + 0.5F;
                            double zO = gZO + (rand.nextFloat() * 2F - 1F) / 5;
                            tileentity.getLevel().addParticle(new WispParticleData(r, g, b, scale),
                            tileentity.getBlockPos().getX() + 0.5F + xO,
                            tileentity.getBlockPos().getY() + yO,
                            tileentity.getBlockPos().getZ() + 0.5F + zO, 0, 0.005F, 0);
                        }
                        float offset = 100;
                        double xO = gXO + (rand.nextFloat() * 2F - 1F) / 20;
                        double yO = gYO + (rand.nextFloat() * 2F - 1F) / 20;
                        double zO = gZO + (rand.nextFloat() * 2F - 1F) / 20;
                        tileentity.getLevel().addParticle(new WispParticleData(Math.min(r + offset, 255), Math.min(g + offset, 255), Math.min(b + offset, 255), scale),
                        tileentity.getBlockPos().getX() + 0.5F + xO,
                        tileentity.getBlockPos().getY() + 0.5F + yO,
                        tileentity.getBlockPos().getZ() + 0.5F + zO, 0, 0.01F, 0);
                        // spawn upper particle
                        tileentity.getLevel().addParticle(new WispParticleData(r, g, b, scale),
                        tileentity.getBlockPos().getX() + gXO + 0.5F + (rand.nextFloat() * 2F - 1F) / 10,
                        tileentity.getBlockPos().getY() + gYO + (rand.nextFloat() * 2F - 1F) / 5 + 0.8F,
                        tileentity.getBlockPos().getZ() + gZO + 0.5F + (rand.nextFloat() * 2F - 1F) / 10, 0, 0.01F, 0);
                    }
                }
            }
        }
    }

}
