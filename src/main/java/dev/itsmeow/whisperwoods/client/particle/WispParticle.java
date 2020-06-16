package dev.itsmeow.whisperwoods.client.particle;

import dev.itsmeow.whisperwoods.particle.WispParticleData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WispParticle extends SpriteTexturedParticle {
    private final IAnimatedSprite spriteSet;

    private WispParticle(World world, double x, double y, double z, double moveX, double moveY, double moveZ, WispParticleData type, IAnimatedSprite sprites) {
        super(world, x, y, z);
        this.multipleParticleScaleBy(1.5F * type.getScale());
        this.setSize(0.05F, 0.05F);
        this.maxAge = this.rand.nextInt(5) + 15;
        this.spriteSet = sprites;
        this.particleRed = 256F - type.getRed();
        this.particleGreen = 256F - type.getGreen();
        this.particleBlue = 256F - type.getBlue();
        this.selectSpriteWithAge(spriteSet);
        this.particleGravity = 3.0E-6F;
        this.motionX = moveX;
        this.motionY = moveY + (double) (this.rand.nextFloat() / 500.0F);
        this.motionZ = moveZ;
        this.particleAlpha = 0.4F;
    }
    
    @Override
    protected int getBrightnessForRender(float partialTick) {
        return 240;
     }

    @Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }



    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if(this.age++ < this.maxAge && !(this.particleAlpha <= 0.0F)) {
            this.motionX += (double) (this.rand.nextFloat() / 5000.0F * (float) (this.rand.nextBoolean() ? 1 : -1));
            this.motionZ += (double) (this.rand.nextFloat() / 5000.0F * (float) (this.rand.nextBoolean() ? 1 : -1));
            this.motionY -= (double) this.particleGravity;
            this.move(this.motionX, this.motionY, this.motionZ);
            if(this.age >= this.maxAge - 60 && this.particleAlpha > 0.01F) {
                this.particleAlpha -= 0.015F;
            }
            this.selectSpriteWithAge(spriteSet);
        } else {
            this.setExpired();
        }
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class WispFactory implements IParticleFactory<WispParticleData> {
        private final IAnimatedSprite spriteSet;

        public WispFactory(IAnimatedSprite sprites) {
            this.spriteSet = sprites;
        }

        public Particle makeParticle(WispParticleData type, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WispParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, type, spriteSet);
        }
    }
}