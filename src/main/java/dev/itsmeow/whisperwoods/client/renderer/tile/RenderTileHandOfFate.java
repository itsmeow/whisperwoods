package dev.itsmeow.whisperwoods.client.renderer.tile;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.block.BlockGhostLight;
import dev.itsmeow.whisperwoods.block.BlockHandOfFate;
import dev.itsmeow.whisperwoods.block.BlockHandOfFate.Orientation;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHandOfFate;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHandOfFate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class RenderTileHandOfFate extends TileEntityRenderer<TileEntityHandOfFate> {
    private static final ModelHandOfFate MODEL = new ModelHandOfFate();
    private static final ResourceLocation TEXTURE = new ResourceLocation(WhisperwoodsMod.MODID, "textures/blocks/hand_of_fate.png");
    private Random rand = new Random();
    private ItemStack istack = null;

    public RenderTileHandOfFate(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TileEntityHandOfFate te, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        stack.push();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.rotate(Vector3f.ZP.rotationDegrees(180F));
        Orientation rot = te.getBlockState().get(BlockHandOfFate.ROTATION);
        float rotation = (float) rot.getHorizontalAngle();
        stack.rotate(Vector3f.YP.rotationDegrees(rotation));
        MODEL.render(stack, bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(TEXTURE)), combinedLightIn, combinedOverlayIn, 1F, 1F, 1F, 1F);
        if(!Minecraft.getInstance().isGamePaused() && Minecraft.getInstance().player != null) {
            if(te.isLit()) {
                BlockGhostLight light = (BlockGhostLight) te.getWorld().getBlockState(te.getPos().up()).getBlock();
                int color = light.getColor();
                float r = (color >> 16) & 0xFF;
                float g = (color >> 8) & 0xFF;
                float b = color & 0xFF;
                te.getWorld().addParticle(new WispParticleData(r, g, b, 0.5F), te.getPos().getX() + (this.rand.nextFloat() + 0.5F) / 2, te.getPos().getY() + this.rand.nextFloat(), te.getPos().getZ() + (this.rand.nextFloat() + 0.5F) / 2, 0, 0.02F, 0);
            } else if(te.hasBlaze()) {
                te.getWorld().addParticle(ParticleTypes.FLAME, te.getPos().getX() + (this.rand.nextFloat() + 0.5F) / 2, te.getPos().getY() + this.rand.nextFloat(), te.getPos().getZ() + (this.rand.nextFloat() + 0.5F) / 2, 0, 0.02F, 0);
            }
        }
        stack.pop();
        Item display = te.getDisplayItem();
        if(display != null) {
            stack.push();
            {
                stack.translate(0.5F, 1.5F, 0.5F);
                @SuppressWarnings("resource")
                float y = MathHelper.sin((float) Minecraft.getInstance().world.getGameTime() * 0.05F) / 8F;
                stack.translate(0F, MathHelper.lerp(partialTicks, te.lastAnimationY, y), 0F);
                te.lastAnimationY = y;
                stack.rotate(Vector3f.YP.rotationDegrees(rotation + (rot.getHorizontalAngle() % 90F == 0 ? 0F : 90F)));
                stack.scale(0.25F, 0.25F, 0.25F);
                if(istack == null || istack.getItem() != display) {
                    istack = new ItemStack(display);
                }
                Minecraft.getInstance().getItemRenderer().renderItem(istack, TransformType.NONE, combinedLightIn, combinedOverlayIn, stack, bufferIn);
            }
            stack.pop();
        }
    }

}
