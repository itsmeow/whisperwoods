package dev.itsmeow.whisperwoods.util;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;

public class StopSpinningClimberPathNavigator extends ClimberPathNavigator {

    public StopSpinningClimberPathNavigator(MobEntity entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    /**
     * See Forge PRs #7520 and #6091
     * Necessary because some users may not have the latest Forge.
     */
    @Override
    protected void pathFollow() {
        Vector3d vector3d = this.getEntityPosition();
        this.maxDistanceToWaypoint = this.entity.getWidth() > 0.75F ? this.entity.getWidth() / 2.0F : 0.75F - this.entity.getWidth() / 2.0F;
        Vector3i vector3i = this.currentPath.func_242948_g();
        double d0 = Math.abs(this.entity.getPosX() - ((double)vector3i.getX() + (this.entity.getWidth() + 1) / 2D));
        double d1 = Math.abs(this.entity.getPosY() - (double)vector3i.getY());
        double d2 = Math.abs(this.entity.getPosZ() - ((double)vector3i.getZ() + (this.entity.getWidth() + 1) / 2D));
        boolean flag = d0 < (double)this.maxDistanceToWaypoint && d2 < (double)this.maxDistanceToWaypoint && d1 < 1.0D;
        if (flag || this.entity.func_233660_b_(this.currentPath.func_237225_h_().nodeType) && this.func_234112_b_(vector3d)) {
            this.currentPath.incrementPathIndex();
        }

        this.checkForStuck(vector3d);
    }
}
