package dev.itsmeow.whisperwoods.client.renderer.tile.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelHirschgeist;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

/**
 * hirschgeist_skull - CyberCat5555 Created using Tabula 8.0.0
 */
public class ModelHGSkull extends EntityModel<Entity> {
    public ModelPart head;
    public ModelPart lUpperJaw;
    public ModelPart lowerJaw;
    public ModelPart snout;
    public ModelPart rUpperJaw;
    public ModelPart lAntler01;
    public ModelPart rAntler01;
    public ModelPart lAntler02;
    public ModelPart lAntler03;
    public ModelPart lAntler08;
    public ModelPart lAntler09;
    public ModelPart lAntler10;
    public ModelPart lAntler04;
    public ModelPart lAntler07;
    public ModelPart lAntler05;
    public ModelPart lAntler06;
    public ModelPart lAntler05b;
    public ModelPart lAntler07b;
    public ModelPart lAntler08b;
    public ModelPart lAntler09b;
    public ModelPart lAntler09c;
    public ModelPart lAntler010b;
    public ModelPart rAntler02;
    public ModelPart rAntler03;
    public ModelPart rAntler08;
    public ModelPart rAntler09;
    public ModelPart rAntler10;
    public ModelPart rAntler04;
    public ModelPart rAntler07;
    public ModelPart rAntler05;
    public ModelPart rAntler06;
    public ModelPart rAntler05b;
    public ModelPart rAntler07b;
    public ModelPart rAntler08b;
    public ModelPart rAntler09b;
    public ModelPart rAntler09c;
    public ModelPart rAntler010b;

    public ModelHGSkull() {
        this.texWidth = 128;
        this.texHeight = 128;
        this.rAntler08 = new ModelPart(this, 70, 25);
        this.rAntler08.mirror = true;
        this.rAntler08.setPos(-0.4F, -1.2F, 0.0F);
        this.rAntler08.addBox(-0.5F, -3.0F, -0.5F, 0.9F, 3.0F, 0.9F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler08, 0.3839724354387525F, 0.0F, -0.6108652381980153F);
        this.rAntler07b = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.rAntler07b.mirror = true;
        this.rAntler07b.setPos(0.0F, -2.8F, 0.0F);
        this.rAntler07b.addBox(-0.4F, -2.0F, -0.4F, 0.7F, 2.0F, 0.7F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler07b, 0.0F, 0.0F, 0.5235987755982988F);
        this.lAntler05b = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.lAntler05b.setPos(0.0F, -2.5F, 0.0F);
        this.lAntler05b.addBox(-0.4F, -2.0F, -0.4F, 0.6F, 2.0F, 0.6F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler05b, 0.0F, 0.0F, 0.5235987755982988F);
        this.lAntler09c = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.lAntler09c.setPos(-0.1F, -1.3F, 0.0F);
        this.lAntler09c.addBox(-0.2F, -1.5F, -0.3F, 0.6F, 1.5F, 0.6F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler09c, 0.0F, 0.0F, 0.5235987755982988F);
        this.rAntler08b = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.rAntler08b.mirror = true;
        this.rAntler08b.setPos(0.0F, -2.8F, 0.0F);
        this.rAntler08b.addBox(-0.4F, -2.0F, -0.4F, 0.7F, 2.0F, 0.7F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler08b, 0.0F, 0.0F, 0.5235987755982988F);
        this.rAntler06 = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.rAntler06.mirror = true;
        this.rAntler06.setPos(-0.3F, -2.4F, 0.0F);
        this.rAntler06.addBox(-0.4F, -2.8F, -0.4F, 0.8F, 3.0F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler06, 0.0F, 0.0F, -0.8726646259971648F);
        this.rAntler09c = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.rAntler09c.mirror = true;
        this.rAntler09c.setPos(0.1F, -1.3F, 0.0F);
        this.rAntler09c.addBox(-0.2F, -1.5F, -0.3F, 0.6F, 1.5F, 0.6F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler09c, 0.0F, 0.0F, -0.5235987755982988F);
        this.lAntler01 = new ModelPart(this, 70, 25);
        this.lAntler01.setPos(1.5F, -4.5F, -1.0F);
        this.lAntler01.addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler01, -0.08726646259971647F, 0.0F, 0.5235987755982988F);
        this.rUpperJaw = new ModelPart(this, 86, 13);
        this.rUpperJaw.mirror = true;
        this.rUpperJaw.setPos(-2.0F, -2.0F, -4.8F);
        this.rUpperJaw.addBox(-1.0F, -1.5F, -6.0F, 2.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rUpperJaw, 0.13962634015954636F, -0.22689280275926282F, 0.0F);
        this.lAntler02 = new ModelPart(this, 70, 25);
        this.lAntler02.setPos(-0.3F, -2.5F, 0.0F);
        this.lAntler02.addBox(-0.7F, -4.0F, -0.7F, 1.5F, 4.0F, 1.5F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler02, -0.08726646259971647F, 0.0F, 0.7853981633974483F);
        this.rAntler05b = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.rAntler05b.mirror = true;
        this.rAntler05b.setPos(0.0F, -2.5F, 0.0F);
        this.rAntler05b.addBox(-0.2F, -2.0F, -0.4F, 0.6F, 2.0F, 0.6F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler05b, 0.0F, 0.0F, -0.5235987755982988F);
        this.head = new ModelPart(this, 98, 0);
        this.head.setPos(0.0F, 23.0F, 0.0F);
        this.head.addBox(-3.0F, -5.1F, -5.1F, 6.0F, 6.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.lAntler07 = new ModelPart(this, 70, 25);
        this.lAntler07.setPos(0.3F, -2.2F, 0.0F);
        this.lAntler07.addBox(-0.5F, -3.0F, -0.5F, 0.9F, 3.0F, 0.9F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler07, -0.2617993877991494F, 0.0F, 0.6981317007977318F);
        this.lAntler05 = new ModelPart(this, 70, 25);
        this.lAntler05.setPos(0.0F, -3.8F, 0.0F);
        this.lAntler05.addBox(-0.4F, -2.8F, -0.4F, 0.8F, 3.0F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler05, 0.0F, 0.0F, 0.5235987755982988F);
        this.rAntler010b = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.rAntler010b.mirror = true;
        this.rAntler010b.setPos(0.0F, -1.7F, 0.0F);
        this.rAntler010b.addBox(-0.3F, -1.5F, -0.4F, 0.6F, 1.5F, 0.6F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler010b, 0.0F, 0.0F, 0.593411945678072F);
        this.lUpperJaw = new ModelPart(this, 86, 13);
        this.lUpperJaw.setPos(2.0F, -2.0F, -4.8F);
        this.lUpperJaw.addBox(-1.0F, -1.5F, -6.0F, 2.0F, 3.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lUpperJaw, 0.13962634015954636F, 0.22689280275926282F, 0.0F);
        this.rAntler02 = new ModelPart(this, 70, 25);
        this.rAntler02.mirror = true;
        this.rAntler02.setPos(0.3F, -2.5F, 0.0F);
        this.rAntler02.addBox(-0.7F, -4.0F, -0.7F, 1.5F, 4.0F, 1.5F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler02, -0.08726646259971647F, 0.0F, -0.7853981633974483F);
        this.rAntler03 = new ModelPart(this, 70, 25);
        this.rAntler03.mirror = true;
        this.rAntler03.setPos(0.0F, -3.7F, 0.0F);
        this.rAntler03.addBox(-0.7F, -4.0F, -0.69F, 1.5F, 4.0F, 1.5F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler03, 0.0F, 0.0F, 0.5235987755982988F);
        this.lAntler08 = new ModelPart(this, 70, 25);
        this.lAntler08.setPos(0.4F, -1.2F, 0.0F);
        this.lAntler08.addBox(-0.5F, -3.0F, -0.5F, 0.9F, 3.0F, 0.9F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler08, 0.3839724354387525F, 0.0F, 0.6108652381980153F);
        this.lAntler08b = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.lAntler08b.setPos(0.0F, -2.8F, 0.0F);
        this.lAntler08b.addBox(-0.4F, -2.0F, -0.4F, 0.7F, 2.0F, 0.7F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler08b, 0.0F, 0.0F, -0.5235987755982988F);
        this.rAntler09 = new ModelPart(this, 70, 25);
        this.rAntler09.mirror = true;
        this.rAntler09.setPos(0.2F, -2.8F, 0.0F);
        this.rAntler09.addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler09, -0.3490658503988659F, 0.0F, 1.1344640137963142F);
        this.rAntler09b = new ModelPart(this, 70, 25);
        this.rAntler09b.mirror = true;
        this.rAntler09b.setPos(0.0F, -2.8F, 0.0F);
        this.rAntler09b.addBox(-0.3F, -1.5F, -0.4F, 0.8F, 1.5F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler09b, 0.22689280275926282F, 0.0F, 0.17453292519943295F);
        this.lAntler06 = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.lAntler06.setPos(0.3F, -2.4F, 0.0F);
        this.lAntler06.addBox(-0.4F, -2.8F, -0.4F, 0.8F, 3.0F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler06, 0.0F, 0.0F, 0.8726646259971648F);
        this.rAntler10 = new ModelPart(this, 70, 25);
        this.rAntler10.mirror = true;
        this.rAntler10.setPos(0.2F, -0.1F, 0.0F);
        this.rAntler10.addBox(-0.5F, -2.0F, -0.5F, 0.8F, 2.0F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler10, 0.3490658503988659F, 0.0F, 1.1344640137963142F);
        this.lAntler010b = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.lAntler010b.setPos(0.0F, -1.7F, 0.0F);
        this.lAntler010b.addBox(-0.3F, -1.5F, -0.4F, 0.6F, 1.5F, 0.6F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler010b, 0.0F, 0.0F, -0.593411945678072F);
        this.lAntler10 = new ModelPart(this, 70, 25);
        this.lAntler10.setPos(-0.2F, -0.1F, 0.0F);
        this.lAntler10.addBox(-0.5F, -2.0F, -0.5F, 0.8F, 2.0F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler10, 0.3490658503988659F, 0.0F, -1.1344640137963142F);
        this.rAntler01 = new ModelPart(this, 70, 25);
        this.rAntler01.mirror = true;
        this.rAntler01.setPos(-1.5F, -4.5F, -1.0F);
        this.rAntler01.addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler01, -0.08726646259971647F, 0.0F, -0.5235987755982988F);
        this.lAntler09b = new ModelPart(this, 70, 25);
        this.lAntler09b.setPos(0.0F, -2.8F, 0.0F);
        this.lAntler09b.addBox(-0.3F, -1.5F, -0.4F, 0.8F, 1.5F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler09b, 0.22689280275926282F, 0.0F, -0.17453292519943295F);
        this.rAntler04 = new ModelPart(this, 70, 25);
        this.rAntler04.mirror = true;
        this.rAntler04.setPos(0.0F, -3.7F, 0.0F);
        this.rAntler04.addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler04, 0.0F, 0.0F, 0.5759586531581287F);
        this.lowerJaw = new ModelPart(this, 107, 14);
        this.lowerJaw.setPos(0.0F, 0.3F, -5.0F);
        this.lowerJaw.addBox(-1.5F, -1.4F, -5.6F, 3.0F, 2.0F, 6.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lowerJaw, -0.08726646259971647F, 0.0F, 0.0F);
        this.lAntler04 = new ModelPart(this, 70, 25);
        this.lAntler04.setPos(0.0F, -3.7F, 0.0F);
        this.lAntler04.addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler04, 0.0F, 0.0F, -0.5759586531581287F);
        this.rAntler05 = new ModelPart(this, 70, 25);
        this.rAntler05.mirror = true;
        this.rAntler05.setPos(0.0F, -3.8F, 0.0F);
        this.rAntler05.addBox(-0.4F, -2.8F, -0.4F, 0.8F, 3.0F, 0.8F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler05, 0.0F, 0.0F, -0.5235987755982988F);
        this.lAntler09 = new ModelPart(this, 70, 25);
        this.lAntler09.setPos(-0.2F, -2.8F, 0.0F);
        this.lAntler09.addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler09, -0.3490658503988659F, 0.0F, -1.1344640137963142F);
        this.lAntler03 = new ModelPart(this, 70, 25);
        this.lAntler03.setPos(0.0F, -3.7F, 0.0F);
        this.lAntler03.addBox(-0.7F, -4.0F, -0.69F, 1.5F, 4.0F, 1.5F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler03, 0.0F, 0.0F, -0.5235987755982988F);
        this.lAntler07b = new ModelHirschgeist.FlameTipModelRenderer(this, 70, 25);
        this.lAntler07b.setPos(0.0F, -2.8F, 0.0F);
        this.lAntler07b.addBox(-0.4F, -2.0F, -0.4F, 0.7F, 2.0F, 0.7F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lAntler07b, 0.0F, 0.0F, -0.5235987755982988F);
        this.rAntler07 = new ModelPart(this, 70, 25);
        this.rAntler07.mirror = true;
        this.rAntler07.setPos(-0.3F, -2.2F, 0.0F);
        this.rAntler07.addBox(-0.5F, -3.0F, -0.5F, 0.9F, 3.0F, 0.9F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rAntler07, -0.2617993877991494F, 0.0F, -0.6981317007977318F);
        this.snout = new ModelPart(this, 66, 13);
        this.snout.setPos(0.0F, -3.94F, -5.0F);
        this.snout.addBox(-1.5F, -1.0F, -6.1F, 3.0F, 2.0F, 7.0F, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(snout, 0.20943951023931953F, 0.0F, 0.0F);
        this.rAntler02.addChild(this.rAntler08);
        this.rAntler07.addChild(this.rAntler07b);
        this.lAntler05.addChild(this.lAntler05b);
        this.lAntler09b.addChild(this.lAntler09c);
        this.rAntler08.addChild(this.rAntler08b);
        this.rAntler04.addChild(this.rAntler06);
        this.rAntler09b.addChild(this.rAntler09c);
        this.head.addChild(this.lAntler01);
        this.head.addChild(this.rUpperJaw);
        this.lAntler01.addChild(this.lAntler02);
        this.rAntler05.addChild(this.rAntler05b);
        this.lAntler03.addChild(this.lAntler07);
        this.lAntler04.addChild(this.lAntler05);
        this.rAntler10.addChild(this.rAntler010b);
        this.head.addChild(this.lUpperJaw);
        this.rAntler01.addChild(this.rAntler02);
        this.rAntler02.addChild(this.rAntler03);
        this.lAntler02.addChild(this.lAntler08);
        this.lAntler08.addChild(this.lAntler08b);
        this.rAntler02.addChild(this.rAntler09);
        this.rAntler09.addChild(this.rAntler09b);
        this.lAntler04.addChild(this.lAntler06);
        this.rAntler02.addChild(this.rAntler10);
        this.lAntler10.addChild(this.lAntler010b);
        this.lAntler02.addChild(this.lAntler10);
        this.head.addChild(this.rAntler01);
        this.lAntler09.addChild(this.lAntler09b);
        this.rAntler03.addChild(this.rAntler04);
        this.head.addChild(this.lowerJaw);
        this.lAntler03.addChild(this.lAntler04);
        this.rAntler04.addChild(this.rAntler05);
        this.lAntler02.addChild(this.lAntler09);
        this.lAntler02.addChild(this.lAntler03);
        this.lAntler07.addChild(this.lAntler07b);
        this.rAntler03.addChild(this.rAntler07);
        this.head.addChild(this.snout);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.head.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = (float) Math.toRadians(limbSwing);
        this.head.xRot = (float) Math.toRadians(limbSwingAmount);
        this.head.x = 0F;
        this.head.z = 0F;
        if(this.head.xRot > 0F) {
            this.head.y = 17.0F;
        } else {
            this.head.y = 23.0F;
            int angle = Math.round(limbSwing);
            if(angle == 0) {
                this.head.z = 4F;
            } else if(angle == 90) {
                this.head.x = 4F;
            } else if(angle == 180) {
                this.head.z = -4F;
            } else if(angle == 270) {
                this.head.x = -4F;
            }
        }
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
