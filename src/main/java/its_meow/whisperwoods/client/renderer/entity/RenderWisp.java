package its_meow.whisperwoods.client.renderer.entity;

import its_meow.whisperwoods.entity.EntityWisp;
import its_meow.whisperwoods.particle.WispParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class RenderWisp extends LivingRenderer<EntityWisp, EntityModel<EntityWisp>> {

    public RenderWisp(EntityRendererManager mgr) {
        super(mgr, null, 0F);
    }

    @Override
    public void doRender(EntityWisp entity, double x, double y, double z, float entityYaw, float partialTicks) {
        double renderViewX = Minecraft.getInstance().player.posX;
        double renderViewY = Minecraft.getInstance().player.posY;
        double renderViewZ = Minecraft.getInstance().player.posZ;

        if(!Minecraft.getInstance().isGamePaused()) {
            if(System.nanoTime() - entity.lastSpawn >= (500f - 500.0f * 120.00000F / 150.0f) * 1000000L) {
                entity.lastSpawn = System.nanoTime();
                float r = 0;
                float g = 239;
                float b = 239;
                if(entity.getAttackTarget() != null) {
                    r = 255;
                    g = 0;
                    b = 0;
                }
                entity.world.addParticle(new WispParticleData(r, g, b),
                entity.posX + (entity.getRNG().nextFloat() * 2F - 1F) / 3.5,
                entity.posY + (entity.getRNG().nextFloat() * 2F - 1F) / 6 + 0.8F,
                entity.posZ + (entity.getRNG().nextFloat() * 2F - 1F) / 3.5, 0, 0.005F, 0);
                entity.world.addParticle(new WispParticleData(r, g, b),
                entity.posX + (entity.getRNG().nextFloat() * 2F - 1F) / 3.5,
                entity.posY + (entity.getRNG().nextFloat() * 2F - 1F) / 6 + 0.8F,
                entity.posZ + (entity.getRNG().nextFloat() * 2F - 1F) / 3.5, 0, 0.005F, 0);
                entity.world.addParticle(new WispParticleData(r, g, b),
                entity.posX + (entity.getRNG().nextFloat() * 2F - 1F) / 10,
                entity.posY + (entity.getRNG().nextFloat() * 2F - 1F) / 5 + 1.1F,
                entity.posZ + (entity.getRNG().nextFloat() * 2F - 1F) / 10, 0, 0.02F, 0);
                /*
                BatchedParticleRenderer.INSTANCE.addParticle(DefaultParticleBatches.WISPS, BLParticles.WISP.create(tileEntity.getWorld(), 
                        x + 0.5 + renderViewX, 
                        y + 0.5 + renderViewY, 
                        z + 0.5 + renderViewZ, 
                        ParticleArgs.get().withColor(r, g, b, 1.0F).withScale(3.0F)));


                BatchedParticleRenderer.INSTANCE.addParticle(DefaultParticleBatches.WISPS, BLParticles.WISP.create(tileEntity.getWorld(), 
                        x + 0.5 + renderViewX, 
                        y + 0.5 + renderViewY, 
                        z + 0.5 + renderViewZ, 
                        ParticleArgs.get().withColor(r, g, b, 1.0F).withScale(2.0F)));
                 */
            }
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWisp entity) {
        return null;
    }

    protected boolean canRenderName(EntityWisp entity) {
        return false;
    }
}
