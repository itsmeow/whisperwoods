package dev.itsmeow.whisperwoods.entity.projectile;

import dev.itsmeow.whisperwoods.init.ModParticles;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityHirschgeistFireball extends ThrowableProjectile {

    public LivingEntity thrower;
    public long lastSpawn;

    public EntityHirschgeistFireball(EntityType<? extends EntityHirschgeistFireball> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public EntityHirschgeistFireball(EntityType<? extends EntityHirschgeistFireball> entityType, Level worldIn, LivingEntity throwerIn) {
        super(entityType, worldIn);
        this.thrower = throwerIn;
    }

    public void shoot(double d, double e, double f, float g, float h) {
        Vec3 vec3 = (new Vec3(d, e, f)).normalize().add(this.random.nextGaussian() * 0.0075D * (double)h, this.random.nextGaussian() * 0.0075D * (double)h, this.random.nextGaussian() * 0.0075D * (double)h).scale(g);
        this.setDeltaMovement(vec3);
    }

    @Override
    protected void onHit(HitResult result) {
        if (!this.level().isClientSide) {
            AreaEffectCloud areaEffectCloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            areaEffectCloud.setOwner(this.thrower);
            areaEffectCloud.setRadius(3.0F);
            areaEffectCloud.setDuration(2000);
            areaEffectCloud.setParticle(ModParticles.SOUL_FLAME.get());
            areaEffectCloud.addEffect(new MobEffectInstance(MobEffects.HARM));
            this.level().addFreshEntity(areaEffectCloud);
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected void defineSynchedData() {}

}
