package dev.itsmeow.whisperwoods.client.renderer.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.client.init.ClientLifecycleHandler;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelHirschgeist;
import dev.itsmeow.whisperwoods.entity.EntityHirschgeist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;

import java.util.List;

public class RenderHirschgeist extends LivingEntityRenderer<EntityHirschgeist, ModelHirschgeist> {

    private static ResourceLocation hg(String num) {
        return new ResourceLocation(WhisperwoodsMod.MODID, "textures/entity/hirschgeist_" + num + ".png");
    }

    public static final List<ResourceLocation> HG_TEXTURES = ImmutableList.of(hg("01"), hg("02"), hg("03"), hg("04"), hg("05"), hg("06"), hg("07"), hg("08"));
    public static final ResourceLocation BONE_TEXTURE = hg("bones");

    public RenderHirschgeist(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelHirschgeist(ctx.bakeLayer(new ModelLayerLocation(new ResourceLocation(WhisperwoodsMod.MODID, "hirschgeist"), "main"))), 0F);
    }

    @Override
    public void render(EntityHirschgeist livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        this.model.attackTime = this.getAttackAnim(livingEntity, g);
        this.model.riding = livingEntity.isPassenger();
        this.model.young = livingEntity.isBaby();
        float h = Mth.rotLerp(g, livingEntity.yBodyRotO, livingEntity.yBodyRot);
        float j = Mth.rotLerp(g, livingEntity.yHeadRotO, livingEntity.yHeadRot);
        float k = j - h;
        float l;
        if (livingEntity.isPassenger() && livingEntity.getVehicle() instanceof LivingEntity mount) {
            h = Mth.rotLerp(g, mount.yBodyRotO, mount.yBodyRot);
            k = j - h;
            l = Mth.wrapDegrees(k);
            if (l < -85.0F) {
                l = -85.0F;
            }
            if (l >= 85.0F) {
                l = 85.0F;
            }
            h = j - l;
            if (l * l > 2500.0F) {
                h += l * 0.2F;
            }
            k = j - h;
        }
        float m = Mth.lerp(g, livingEntity.xRotO, livingEntity.getXRot());
        float n;
        if (livingEntity.getPose() == Pose.SLEEPING) {
            Direction direction = livingEntity.getBedOrientation();
            if (direction != null) {
                n = livingEntity.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate(((float)(-direction.getStepX()) * n), 0.0D, ((float)(-direction.getStepZ()) * n));
            }
        }
        l = this.getBob(livingEntity, g);
        this.setupRotations(livingEntity, poseStack, l, h, g);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(livingEntity, poseStack, g);
        poseStack.scale(2F, 2F, 2F);
        poseStack.translate(0.0D, -1.5010000467300415D, 0.0D);
        n = 0.0F;
        float o = 0.0F;
        if (!livingEntity.isPassenger() && livingEntity.isAlive()) {
            n = livingEntity.walkAnimation.speed(g);
            o = livingEntity.walkAnimation.position(g);
            if (livingEntity.isBaby()) {
                o *= 3.0F;
            }

            if (n > 1.0F) {
                n = 1.0F;
            }
        }
        this.model.prepareMobModel(livingEntity, o, n, g);
        this.model.setupAnim(livingEntity, o, n, l, k, m);
        Minecraft minecraft = Minecraft.getInstance();
        boolean bl = this.isBodyVisible(livingEntity);
        boolean bl2 = !bl && !livingEntity.isInvisibleTo(minecraft.player);
        boolean bl3 = minecraft.shouldEntityAppearGlowing(livingEntity);
        RenderType renderType = this.getRenderType(livingEntity, bl, bl2, bl3);
        if (renderType != null) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(renderType);
            int p = getOverlayCoords(livingEntity, this.getWhiteOverlayProgress(livingEntity, g));
            this.model.renderToBuffer(poseStack, vertexConsumer, i, p, 1.0F, 1.0F, 1.0F, bl2 ? 0.15F : 1.0F);
        }
        if (renderType != null && !livingEntity.isDaytimeClient()) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(ClientLifecycleHandler.RenderTypeAddition.getEyesEntityCutoutNoCullDepthMaskOff(this.getEctoTexture(livingEntity)));
            int p = getOverlayCoords(livingEntity, this.getWhiteOverlayProgress(livingEntity, g));
            this.model.renderToBuffer(poseStack, vertexConsumer, i, p, 1.0F, 1.0F, 1.0F, bl2 ? 0.15F : 1.0F);
        }
        if (!livingEntity.isSpectator()) {
            for(RenderLayer<EntityHirschgeist, ModelHirschgeist> layer : layers) {
                layer.render(poseStack, multiBufferSource, i, livingEntity, o, n, g, l, k, m);
            }
        }

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHirschgeist entity) {
        return BONE_TEXTURE;
    }

    public ResourceLocation getEctoTexture(EntityHirschgeist entity) {
        return entity.getFlameAnimationIndex() < HG_TEXTURES.size() && entity.getFlameAnimationIndex() >= 0 ? HG_TEXTURES.get(entity.getFlameAnimationIndex()) : null;
    }

    @Override
    protected boolean shouldShowName(EntityHirschgeist livingEntity) {
        return false;
    }
}
