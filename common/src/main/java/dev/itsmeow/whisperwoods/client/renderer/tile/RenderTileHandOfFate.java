package dev.itsmeow.whisperwoods.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.block.GhostLightBlock;
import dev.itsmeow.whisperwoods.block.HandOfFateBlock;
import dev.itsmeow.whisperwoods.block.HandOfFateBlock.Orientation;
import dev.itsmeow.whisperwoods.blockentity.HandOfFateBlockEntity;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHandOfFate;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderTileHandOfFate implements BlockEntityRenderer<HandOfFateBlockEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(WhisperwoodsMod.MODID, "textures/block/hand_of_fate.png");
    private final ModelHandOfFate model;
    private ItemStack istack = null;

    public RenderTileHandOfFate(BlockEntityRendererProvider.Context ctx) {
        this.model = new ModelHandOfFate(ctx.bakeLayer(new ModelLayerLocation(new ResourceLocation(WhisperwoodsMod.MODID, "hand_of_fate"), "main")));
    }

    @Override
    public void render(HandOfFateBlockEntity te, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180F));
        Orientation rot = te.getBlockState().getValue(HandOfFateBlock.ROTATION);
        float rotation = (float) rot.getHorizontalAngle();
        stack.mulPose(Axis.YP.rotationDegrees(rotation));
        model.renderToBuffer(stack, bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), combinedLightIn, combinedOverlayIn, 1F, 1F, 1F, 1F);
        if(!Minecraft.getInstance().isPaused() && Minecraft.getInstance().player != null) {
            if(te.isLit()) {
                GhostLightBlock light = (GhostLightBlock) te.getLevel().getBlockState(te.getBlockPos().above()).getBlock();
                int color = light.getColor();
                float r = (color >> 16) & 0xFF;
                float g = (color >> 8) & 0xFF;
                float b = color & 0xFF;
                te.getLevel().addParticle(new WispParticleData(r, g, b, 0.5F), te.getBlockPos().getX() + (te.getLevel().getRandom().nextFloat() + 0.5F) / 2, te.getBlockPos().getY() + te.getLevel().getRandom().nextFloat(), te.getBlockPos().getZ() + (te.getLevel().getRandom().nextFloat() + 0.5F) / 2, 0, 0.02F, 0);
            } else if(te.hasBlaze()) {
                te.getLevel().addParticle(ParticleTypes.FLAME, te.getBlockPos().getX() + (te.getLevel().getRandom().nextFloat() + 0.5F) / 2, te.getBlockPos().getY() + te.getLevel().getRandom().nextFloat(), te.getBlockPos().getZ() + (te.getLevel().getRandom().nextFloat() + 0.5F) / 2, 0, 0.02F, 0);
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
                stack.mulPose(Axis.YP.rotationDegrees(rotation + (rot.getHorizontalAngle() % 90F == 0 ? 0F : 90F)));
                stack.scale(0.25F, 0.25F, 0.25F);
                if(istack == null || istack.getItem() != display) {
                    istack = new ItemStack(display);
                }
                Minecraft.getInstance().getItemRenderer().renderStatic(istack, ItemDisplayContext.NONE, combinedLightIn, combinedOverlayIn, stack, bufferIn, null, ItemDisplayContext.NONE.ordinal());
            }
            stack.popPose();
        }
    }

}
