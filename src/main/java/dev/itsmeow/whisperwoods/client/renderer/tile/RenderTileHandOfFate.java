package dev.itsmeow.whisperwoods.client.renderer.tile;

import java.util.Random;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.block.BlockGhostLight;
import dev.itsmeow.whisperwoods.block.BlockHandOfFate;
import dev.itsmeow.whisperwoods.block.BlockHandOfFate.Orientation;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHandOfFate;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHandOfFate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderTileHandOfFate extends BlockEntityRenderer<TileEntityHandOfFate> {
    private static final ModelHandOfFate MODEL = new ModelHandOfFate();
    private static final ResourceLocation TEXTURE = new ResourceLocation(WhisperwoodsMod.MODID, "textures/blocks/hand_of_fate.png");
    private Random rand = new Random();
    private ItemStack istack = null;

    public RenderTileHandOfFate(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TileEntityHandOfFate te, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Vector3f.ZP.rotationDegrees(180F));
        Orientation rot = te.getBlockState().getValue(BlockHandOfFate.ROTATION);
        float rotation = (float) rot.getHorizontalAngle();
        stack.mulPose(Vector3f.YP.rotationDegrees(rotation));
        MODEL.renderToBuffer(stack, bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), combinedLightIn, combinedOverlayIn, 1F, 1F, 1F, 1F);
        if(!Minecraft.getInstance().isPaused() && Minecraft.getInstance().player != null) {
            if(te.isLit()) {
                BlockGhostLight light = (BlockGhostLight) te.getLevel().getBlockState(te.getBlockPos().above()).getBlock();
                int color = light.getColor();
                float r = (color >> 16) & 0xFF;
                float g = (color >> 8) & 0xFF;
                float b = color & 0xFF;
                te.getLevel().addParticle(new WispParticleData(r, g, b, 0.5F), te.getBlockPos().getX() + (this.rand.nextFloat() + 0.5F) / 2, te.getBlockPos().getY() + this.rand.nextFloat(), te.getBlockPos().getZ() + (this.rand.nextFloat() + 0.5F) / 2, 0, 0.02F, 0);
            } else if(te.hasBlaze()) {
                te.getLevel().addParticle(ParticleTypes.FLAME, te.getBlockPos().getX() + (this.rand.nextFloat() + 0.5F) / 2, te.getBlockPos().getY() + this.rand.nextFloat(), te.getBlockPos().getZ() + (this.rand.nextFloat() + 0.5F) / 2, 0, 0.02F, 0);
            }
        }
        stack.popPose();
        Item display = te.getDisplayItem();
        if(display != null) {
            stack.pushPose();
            {
                stack.translate(0.5F, 1.5F, 0.5F);
                @SuppressWarnings("resource")
                float y = Mth.sin((float) Minecraft.getInstance().level.getGameTime() * 0.05F) / 8F;
                stack.translate(0F, Mth.lerp(partialTicks, te.lastAnimationY, y), 0F);
                te.lastAnimationY = y;
                stack.mulPose(Vector3f.YP.rotationDegrees(rotation + (rot.getHorizontalAngle() % 90F == 0 ? 0F : 90F)));
                stack.scale(0.25F, 0.25F, 0.25F);
                if(istack == null || istack.getItem() != display) {
                    istack = new ItemStack(display);
                }
                Minecraft.getInstance().getItemRenderer().renderStatic(istack, TransformType.NONE, combinedLightIn, combinedOverlayIn, stack, bufferIn);
            }
            stack.popPose();
        }
    }

}
