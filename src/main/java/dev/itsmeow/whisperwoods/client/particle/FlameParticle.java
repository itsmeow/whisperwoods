package dev.itsmeow.whisperwoods.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FlameParticle extends SpriteTexturedParticle {

    private FlameParticle(World world, double x, double y, double z, double moveX, double moveY, double moveZ, IAnimatedSprite spriteSet) {
        super(world, x, y, z);
        this.setSize(0.05F, 0.05F);
        this.maxAge = this.rand.nextInt(5) + 15;
        this.selectSpriteRandomly(spriteSet);
        this.particleGravity = 3.0E-6F;
        this.motionX = moveX;
        this.motionY = moveY + (double) (this.rand.nextFloat() / 500.0F);
        this.motionZ = moveZ;
        this.particleAlpha = 0.8F;
    }

    @Override
    protected int getBrightnessForRender(float partialTick) {
        return 240;
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
        } else {
            this.setExpired();
        }
    }

    public IParticleRenderType getRenderType() {
        return WispParticle.PARTICLE_SHEET_TRANSLUCENT_114;
    }

    @OnlyIn(Dist.CLIENT)
    public static class FlameFactory implements IParticleFactory<BasicParticleType> {
        private final IAnimatedSprite spriteSet;

        public FlameFactory(IAnimatedSprite sprites) {
            this.spriteSet = sprites;
        }

        public Particle makeParticle(BasicParticleType type, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FlameParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet);
        }
    }
}