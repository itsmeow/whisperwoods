package dev.itsmeow.whisperwoods.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.itsmeow.whisperwoods.entity.EntityWisp;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class RenderWisp extends LivingEntityRenderer<EntityWisp, EntityModel<EntityWisp>> {

    private final SkullModel head;

    public RenderWisp(EntityRendererProvider.Context ctx) {
        super(ctx, null, 0F);
        this.head = new SkullModel(ctx.getModelSet().bakeLayer(ModelLayers.PLAYER_HEAD));
    }

    @Override
    public void render(EntityWisp entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        int color = entity.getWispColor().getColor();
        float r = (color >> 16) & 0xFF;
        float g = (color >> 8) & 0xFF;
        float b = color & 0xFF;
        float scale;
        if(entity.hasSoul()) {
            UUID target = UUID.fromString(entity.getEntityData().get(EntityWisp.TARGET_ID));
            String name = entity.getEntityData().get(EntityWisp.TARGET_NAME);
            if(target != null && name != null && !name.isEmpty()) {
                stack.pushPose();
                {
                    stack.translate(0F, 0.8F, 0F);
                    //stack.translate(entity.getPosX(), entity.getPosY() + 0.8F, entity.getPosZ());
                    VertexConsumer vertex = bufferIn.getBuffer(RenderType.entityTranslucent(entity.getTargetTexture()));
                    head.setupAnim(0F, -entity.yHeadRot, 180F + entity.getXRot());
                    head.renderToBuffer(stack, vertex, packedLightIn, OverlayTexture.NO_OVERLAY, r / 255F, g / 255F, b / 255F, 0.6F);
                    stack.translate(0F, 0.4F, 0F);
                    this.renderNameTag(entity, Component.literal(name + "'s soul"), stack, bufferIn, packedLightIn);
                }
                stack.popPose();
            }
        }
        if(!Minecraft.getInstance().isPaused()) {
            if(System.nanoTime() - entity.lastSpawn >= 100_000_000L) {
                entity.lastSpawn = System.nanoTime();
                if(entity.getEntityData().get(EntityWisp.ATTACK_STATE) > 0) {
                    scale = ((400F - ((float) entity.getEntityData().get(EntityWisp.ATTACK_STATE))) / 400F) * 5F;
                } else {
                    scale = entity.getEntityData().get(EntityWisp.PASSIVE_SCALE);
                }
                // spawn bottom particles
                for(int i = 0; i < 2; i++) {
                    double xO = (entity.getRandom().nextFloat() * 2F - 1F) / 3.5;
                    double yO = (entity.getRandom().nextFloat() * 2F - 1F) / 6 + 0.8F;
                    double zO = (entity.getRandom().nextFloat() * 2F - 1F) / 3.5;
                    entity.level().addParticle(new WispParticleData(r, g, b, scale),
                    entity.getX() + xO,
                    entity.getY() + yO,
                    entity.getZ() + zO, 0, 0.005F, 0);
                }
                // spawn upper particle
                entity.level().addParticle(new WispParticleData(r, g, b, scale),
                entity.getX() + (entity.getRandom().nextFloat() * 2F - 1F) / 10,
                entity.getY() + (entity.getRandom().nextFloat() * 2F - 1F) / 5 + 1.1F,
                entity.getZ() + (entity.getRandom().nextFloat() * 2F - 1F) / 10, 0, 0.02F, 0);
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWisp entity) {
        return null;
    }

    @Override
    protected boolean shouldShowName(EntityWisp livingEntity) {
        return false;
    }
}
