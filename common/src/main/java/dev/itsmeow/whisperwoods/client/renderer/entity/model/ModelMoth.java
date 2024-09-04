package dev.itsmeow.whisperwoods.client.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.itsmeow.whisperwoods.entity.EntityMoth;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
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

    public ModelMoth(ModelPart root) {
        this.thorax = root.getChild("thorax");
        this.head = thorax.getChild("head");
        this.lAntenna = head.getChild("lAntenna");
        this.rAntenna = head.getChild("rAntenna");
        this.abdomin = thorax.getChild("abdomin");
        this.lLeg01 = thorax.getChild("lLeg01");
        this.lLeg02 = thorax.getChild("lLeg02");
        this.lLeg03 = thorax.getChild("lLeg03");
        this.rLeg01 = thorax.getChild("rLeg01");
        this.rLeg02 = thorax.getChild("rLeg02");
        this.rLeg03 = thorax.getChild("rLeg03");
        this.lWing = thorax.getChild("lWing");
        this.rWing = thorax.getChild("rWing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition thorax = partdefinition.addOrReplaceChild("thorax", CubeListBuilder.create().texOffs(0, 7).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.9F, 0.0F));
        PartDefinition head = thorax.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -3.0F, -3.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.2F, -1.15F, 0.7854F, 0.0F, 0.0F));
        PartDefinition lAntenna = head.addOrReplaceChild("lAntenna", CubeListBuilder.create().texOffs(11, 0).addBox(0.0F, 0.0F, -5.0F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, -3.0F, -2.0F, -0.7854F, -0.2793F, 0.0F));
        PartDefinition rAntenna = head.addOrReplaceChild("rAntenna", CubeListBuilder.create().texOffs(11, 0).mirror().addBox(-2.0F, 0.0F, -5.0F, 2.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.4F, -3.0F, -2.0F, -0.7854F, 0.2793F, 0.0F));
        PartDefinition abdomin = thorax.addOrReplaceChild("abdomin", CubeListBuilder.create().texOffs(0, 16).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.2F, 1.8F, -0.1222F, 0.0F, 0.0F));
        PartDefinition lLeg01 = thorax.addOrReplaceChild("lLeg01", CubeListBuilder.create().texOffs(17, 6).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.9F, 1.2F, -0.5F, -1.0472F, -0.8727F, 0.0F));
        PartDefinition lLeg02 = thorax.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(20, 5).mirror().addBox(0.0F, 0.0F, -0.5F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.9F, 1.2F, 0.0F, 0.0F, 0.0F, -1.0472F));
        PartDefinition lLeg03 = thorax.addOrReplaceChild("lLeg03", CubeListBuilder.create().texOffs(17, 6).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.9F, 1.2F, 0.5F, 1.0472F, 0.8727F, 0.0F));
        PartDefinition rLeg01 = thorax.addOrReplaceChild("rLeg01", CubeListBuilder.create().texOffs(17, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9F, 1.2F, -0.5F, -1.0472F, 0.8727F, 0.0F));
        PartDefinition rLeg02 = thorax.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(20, 5).addBox(0.0F, 0.0F, -0.5F, 0.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9F, 1.2F, 0.0F, 0.0F, 0.0F, 1.0472F));
        PartDefinition rLeg03 = thorax.addOrReplaceChild("rLeg03", CubeListBuilder.create().texOffs(17, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9F, 1.2F, 0.5F, 1.0472F, -0.8727F, 0.0F));
        PartDefinition lWing = thorax.addOrReplaceChild("lWing", CubeListBuilder.create().texOffs(5, 0).addBox(0.0F, 0.0F, -4.5F, 16.0F, 0.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offset(1.2F, -1.0F, -1.0F));
        PartDefinition rWing = thorax.addOrReplaceChild("rWing", CubeListBuilder.create().texOffs(5, 0).mirror().addBox(-16.0F, 0.0F, -4.5F, 16.0F, 0.0F, 27.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.2F, -1.0F, -1.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(this.isOffset) {
            matrixStackIn.translate(-this.xOff, 0D, this.zOff);
        }
        this.thorax.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
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
                BlockPos pos = BlockPos.containing(x, entity.getY(), z);
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
