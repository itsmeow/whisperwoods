package dev.itsmeow.whisperwoods.client.renderer.tile;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.block.BlockGhostLight;
import dev.itsmeow.whisperwoods.block.BlockHandOfFate;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHandOfFate;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHandOfFate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class RenderTileHandOfFate extends TileEntityRenderer<TileEntityHandOfFate> {
    private static final ModelHandOfFate MODEL = new ModelHandOfFate();
    private static final ResourceLocation TEXTURE = new ResourceLocation(WhisperwoodsMod.MODID, "textures/blocks/hand_of_fate.png");
    private Random rand = new Random();

    public RenderTileHandOfFate(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TileEntityHandOfFate te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5F, 1.5F, 0.5F);
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180F));
        float rotation = (float) te.getBlockState().get(BlockHandOfFate.ROTATION).getHorizontalAngle();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rotation));
        MODEL.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(TEXTURE)), combinedLightIn, combinedOverlayIn, 1F, 1F, 1F, 1F);
        if(te.isLit() && !Minecraft.getInstance().isGamePaused() && Minecraft.getInstance().player != null) {
            BlockGhostLight light = (BlockGhostLight) te.getWorld().getBlockState(te.getPos().up()).getBlock();
            int color = light.getColor();
            float r = (color >> 16) & 0xFF;
            float g = (color >> 8) & 0xFF;
            float b = color & 0xFF;
            te.getWorld().addParticle(new WispParticleData(r, g, b, 0.5F), te.getPos().getX() + (this.rand.nextFloat() + 0.5F) / 2, te.getPos().getY() + this.rand.nextFloat(), te.getPos().getZ() + (this.rand.nextFloat() + 0.5F) / 2, 0, 0.02F, 0);
        }
        matrixStackIn.pop();
    }

}
