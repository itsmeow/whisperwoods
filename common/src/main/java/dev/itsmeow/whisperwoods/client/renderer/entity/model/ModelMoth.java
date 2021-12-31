package dev.itsmeow.whisperwoods.client.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.itsmeow.whisperwoods.entity.EntityMoth;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Pose;

public class ModelMoth extends EntityModel<EntityMoth> {

    public ModelPart thorax;
    public ModelPart head;
    public ModelPart lAntenna;
    public ModelPart rAntenna;
    public ModelPart abdomin;
    public ModelPart lLeg01;
    public ModelPart lLeg02;
    public ModelPart lLeg03;
    public ModelPart rLeg01;
    public ModelPart rLeg02;
    public ModelPart rLeg03;
    public ModelPart lWing;
    public ModelPart rWing;
    private boolean isOffset = false;
    private double xOff = 0;
    private double zOff = 0;

    public ModelMoth() {
        texWidth = 64;
        texHeight = 32;

        thorax = new ModelPart(this);
        thorax.setPos(0.0F, 20.9F, 0.0F);
        thorax.texOffs(0, 7).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPos(0.0F, 0.2F, -1.15F);
        thorax.addChild(head);
        setRotateAngle(head, 0.7854F, 0.0F, 0.0F);
        head.texOffs(0, 0).addBox(-1.5F, -3.0F, -3.0F, 3.0F, 3.0F, 3.0F, -0.1F, false);

        lAntenna = new ModelPart(this);
        lAntenna.setPos(0.4F, -3.0F, -2.0F);
        head.addChild(lAntenna);
        setRotateAngle(lAntenna, -0.7854F, -0.2793F, 0.0F);
        lAntenna.texOffs(11, 0).addBox(0.0F, 0.0F, -5.0F, 2.0F, 0.0F, 5.0F, 0.0F, false);

        rAntenna = new ModelPart(this);
        rAntenna.setPos(-0.4F, -3.0F, -2.0F);
        head.addChild(rAntenna);
        setRotateAngle(rAntenna, -0.7854F, 0.2793F, 0.0F);
        rAntenna.texOffs(11, 0).addBox(-2.0F, 0.0F, -5.0F, 2.0F, 0.0F, 5.0F, 0.0F, true);

        abdomin = new ModelPart(this);
        abdomin.setPos(0.0F, 0.2F, 1.8F);
        thorax.addChild(abdomin);
        setRotateAngle(abdomin, -0.1222F, 0.0F, 0.0F);
        abdomin.texOffs(0, 16).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, 0.1F, false);

        lLeg01 = new ModelPart(this);
        lLeg01.setPos(0.9F, 1.2F, -0.5F);
        thorax.addChild(lLeg01);
        setRotateAngle(lLeg01, -1.0472F, -0.8727F, 0.0F);
        lLeg01.texOffs(17, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, true);

        lLeg02 = new ModelPart(this);
        lLeg02.setPos(0.9F, 1.2F, 0.0F);
        thorax.addChild(lLeg02);
        setRotateAngle(lLeg02, 0.0F, 0.0F, -1.0472F);
        lLeg02.texOffs(20, 5).addBox(0.0F, 0.0F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, true);

        lLeg03 = new ModelPart(this);
        lLeg03.setPos(0.9F, 1.2F, 0.5F);
        thorax.addChild(lLeg03);
        setRotateAngle(lLeg03, 1.0472F, 0.8727F, 0.0F);
        lLeg03.texOffs(17, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, true);

        rLeg01 = new ModelPart(this);
        rLeg01.setPos(-0.9F, 1.2F, -0.5F);
        thorax.addChild(rLeg01);
        setRotateAngle(rLeg01, -1.0472F, 0.8727F, 0.0F);
        rLeg01.texOffs(17, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);

        rLeg02 = new ModelPart(this);
        rLeg02.setPos(-0.9F, 1.2F, 0.0F);
        thorax.addChild(rLeg02);
        setRotateAngle(rLeg02, 0.0F, 0.0F, 1.0472F);
        rLeg02.texOffs(20, 5).addBox(0.0F, 0.0F, -0.5F, 0.0F, 4.0F, 1.0F, 0.0F, false);

        rLeg03 = new ModelPart(this);
        rLeg03.setPos(-0.9F, 1.2F, 0.5F);
        thorax.addChild(rLeg03);
        setRotateAngle(rLeg03, 1.0472F, -0.8727F, 0.0F);
        rLeg03.texOffs(17, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, 0.0F, false);

        lWing = new ModelPart(this);
        lWing.setPos(1.2F, -1.0F, -1.0F);
        thorax.addChild(lWing);
        lWing.texOffs(5, 0).addBox(0.0F, 0.0F, -4.5F, 16.0F, 0.0F, 27.0F, 0.0F, false);

        rWing = new ModelPart(this);
        rWing.setPos(-1.2F, -1.0F, -1.0F);
        thorax.addChild(rWing);
        rWing.texOffs(5, 0).addBox(-16.0F, 0.0F, -4.5F, 16.0F, 0.0F, 27.0F, 0.0F, true);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(this.isOffset) {
            matrixStackIn.translate(-this.xOff, 0D, this.zOff);
        }
        this.thorax.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    @Override
    public void setupAnim(EntityMoth entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if(entity.isLanded()) {
            this.setRotateAngle(head, 0.7854F, 0.0F, 0.0F);
            this.setRotateAngle(lAntenna, -0.7854F, -0.2793F, 0.0F);
            this.setRotateAngle(rAntenna, -0.7854F, 0.2793F, 0.0F);
            this.setRotateAngle(abdomin, -0.1222F, 0.0F, 0.0F);
            this.setRotateAngle(lLeg01, -1.0472F, -0.8727F, 0.0F);
            this.setRotateAngle(lLeg02, 0.0F, 0.0F, -1.0472F);
            this.setRotateAngle(lLeg03, 1.0472F, 0.8727F, 0.0F);
            this.setRotateAngle(rLeg01, -1.0472F, 0.8727F, 0.0F);
            this.setRotateAngle(rLeg02, 0.0F, 0.0F, 1.0472F);
            this.setRotateAngle(rLeg03, 1.0472F, -0.8727F, 0.0F);
            this.rWing.zRot = (float) Math.toRadians(3);
            this.lWing.zRot = (float) Math.toRadians(-3);
            this.lWing.yRot = (float) Math.toRadians(-30);
            this.rWing.yRot = (float) Math.toRadians(30);
            this.isOffset = Direction.from3DDataValue(entity.getLandedInteger()) != Direction.DOWN;
            if(isOffset) {
                this.thorax.xRot = (float) Math.toRadians(-90);
                this.thorax.yRot = (float) Math.toRadians(Direction.from3DDataValue(entity.getLandedInteger()).toYRot());
                double x = Math.floor(entity.getX()) + 0.5D;
                double z = Math.floor(entity.getZ()) + 0.5D;
                BlockPos pos = new BlockPos(x, entity.getY(), z);
                BlockPos offset = pos.relative(Direction.from3DDataValue(entity.getLandedInteger()));
                BlockPos diff = pos.subtract(offset);
                this.xOff = ((double) diff.getX()) / (13D * entity.getDimensions(Pose.STANDING).width);
                this.zOff = ((double) diff.getZ()) / (13D * entity.getDimensions(Pose.STANDING).width);
            } else {
                this.thorax.xRot = 0;
                this.thorax.yRot = 0;
            }
        } else {
            this.rWing.zRot = (float) Math.sin(ageInTicks);
            this.lWing.zRot = (float) -Math.sin(ageInTicks);
            this.lWing.yRot = 0;
            this.rWing.yRot = 0;
            this.thorax.xRot = 0;
            this.thorax.yRot = 0;
        }
    }

    public void setRotateAngle(ModelPart modelPart, float x, float y, float z) {
        modelPart.xRot = x;
        modelPart.yRot = y;
        modelPart.zRot = z;
    }
}
