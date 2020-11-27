package dev.itsmeow.whisperwoods.client.renderer.entity;

import java.util.UUID;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import dev.itsmeow.whisperwoods.entity.EntityWisp;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.entity.model.HumanoidHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RenderWisp extends LivingRenderer<EntityWisp, EntityModel<EntityWisp>> {

    GenericHeadModel head = new HumanoidHeadModel();

    public RenderWisp(EntityRendererManager mgr) {
        super(mgr, null, 0F);
    }

    @Override
    public void render(EntityWisp entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        int color = entity.getWispColor().getColor();
        float r = (color >> 16) & 0xFF;
        float g = (color >> 8) & 0xFF;
        float b = color & 0xFF;
        float scale = 1;
        if(entity.hasSoul()) {
            UUID target = UUID.fromString(entity.getDataManager().get(EntityWisp.TARGET_ID));
            String name = entity.getDataManager().get(EntityWisp.TARGET_NAME);
            if(target != null && name != null && !name.equals("")) {
                stack.push();
                {
                    stack.translate(0F, 0.8F, 0F);
                    //stack.translate(entity.getPosX(), entity.getPosY() + 0.8F, entity.getPosZ());
                    IVertexBuilder vertex = bufferIn.getBuffer(RenderType.getEntityTranslucent(entity.getTargetTexture()));
                    head.func_225603_a_(0F, -entity.rotationYawHead, 180F + entity.rotationPitch);
                    head.render(stack, vertex, packedLightIn, OverlayTexture.NO_OVERLAY, r / 255F, g / 255F, b / 255F, 0.6F);
                    stack.translate(0F, 0.4F, 0F);
                    this.renderName(entity, name + "'s soul", stack, bufferIn, packedLightIn);
                }
                stack.pop();
            }
        }
        if(!Minecraft.getInstance().isGamePaused()) {
            if(System.nanoTime() - entity.lastSpawn >= 100_000_000L) {
                entity.lastSpawn = System.nanoTime();
                if(entity.getDataManager().get(EntityWisp.ATTACK_STATE) > 0) {
                    scale = ((400F - ((float) entity.getDataManager().get(EntityWisp.ATTACK_STATE))) / 400F) * 5F;
                } else {
                    scale = entity.getDataManager().get(EntityWisp.PASSIVE_SCALE);
                }
                // spawn bottom particles
                for(int i = 0; i < 2; i++) {
                    double xO = (entity.getRNG().nextFloat() * 2F - 1F) / 3.5;
                    double yO = (entity.getRNG().nextFloat() * 2F - 1F) / 6 + 0.8F;
                    double zO = (entity.getRNG().nextFloat() * 2F - 1F) / 3.5;
                    entity.world.addParticle(new WispParticleData(r, g, b, scale),
                    entity.getPosX() + xO,
                    entity.getPosY() + yO,
                    entity.getPosZ() + zO, 0, 0.005F, 0);
                }
                // spawn upper particle
                entity.world.addParticle(new WispParticleData(r, g, b, scale),
                entity.getPosX() + (entity.getRNG().nextFloat() * 2F - 1F) / 10,
                entity.getPosY() + (entity.getRNG().nextFloat() * 2F - 1F) / 5 + 1.1F,
                entity.getPosZ() + (entity.getRNG().nextFloat() * 2F - 1F) / 10, 0, 0.02F, 0);
            }
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EntityWisp entity) {
        return null;
    }

    @Override
    protected boolean canRenderName(EntityWisp entity) {
        return false;
    }
}
