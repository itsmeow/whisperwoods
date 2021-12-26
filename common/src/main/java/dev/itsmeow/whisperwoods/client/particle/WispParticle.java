package dev.itsmeow.whisperwoods.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

public class WispParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    public static final ParticleRenderType PARTICLE_SHEET_TRANSLUCENT_114 = new ParticleRenderType() {
        @SuppressWarnings("deprecation")
        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            textureManager.bind(TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.alphaFunc(516, 0.003921569F);
            bufferBuilder.begin(7, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        @Override
        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT_114";
        }
    };

    private WispParticle(ClientLevel world, double x, double y, double z, double moveX, double moveY, double moveZ, WispParticleData type, SpriteSet sprites) {
        super(world, x, y, z);
        this.scale(1.5F * type.getScale());
        this.setSize(0.05F, 0.05F);
        this.lifetime = this.random.nextInt(5) + 15;
        this.spriteSet = sprites;
        this.rCol = 256F - type.getRed();
        this.gCol = 256F - type.getGreen();
        this.bCol = 256F - type.getBlue();
        this.setSpriteFromAge(spriteSet);
        this.gravity = 3.0E-6F;
        this.xd = moveX;
        this.yd = moveY + (double) (this.random.nextFloat() / 500.0F);
        this.zd = moveZ;
        this.alpha = 0.4F;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 240;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ < this.lifetime && !(this.alpha <= 0.0F)) {
            this.xd += (this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1));
            this.zd += (this.random.nextFloat() / 5000.0F * (float) (this.random.nextBoolean() ? 1 : -1));
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            if (this.age >= this.lifetime - 60 && this.alpha > 0.01F) {
                this.alpha -= 0.015F;
            }
            this.setSpriteFromAge(spriteSet);
        } else {
            this.remove();
        }
    }

    public ParticleRenderType getRenderType() {
        return PARTICLE_SHEET_TRANSLUCENT_114;
    }

    public static class WispFactory implements ParticleProvider<WispParticleData> {
        private final SpriteSet spriteSet;

        public WispFactory(SpriteSet sprites) {
            this.spriteSet = sprites;
        }

        @Override
        public Particle createParticle(WispParticleData type, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WispParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, type, spriteSet);
        }
    }
}