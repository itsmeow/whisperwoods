package dev.itsmeow.whisperwoods.client.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.itsmeow.whisperwoods.entity.EntityHidebehind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ModelHidebehind extends EntityModel<EntityHidebehind> {

    public ModelPart chest;
    public ModelPart stomach;
    public ModelPart hips;
    public ModelPart lLeg01;
    public ModelPart lLeg02;
    public ModelPart lLeg03;
    public ModelPart lPaw;
    public ModelPart lToe00;
    public ModelPart lToe01;
    public ModelPart lToe02;
    public ModelPart lLeg02Fur;
    public ModelPart lLeg02FurMid;
    public ModelPart lLeg01Fur;
    public ModelPart rLeg01;
    public ModelPart rLeg02;
    public ModelPart rLeg03;
    public ModelPart rPaw;
    public ModelPart rToe00;
    public ModelPart rToe01;
    public ModelPart rToe02;
    public ModelPart rLeg02Fur;
    public ModelPart rLeg02FurMid;
    public ModelPart rLeg01Fur;
    public ModelPart tail00;
    public ModelPart tail01;
    public ModelPart crothFur;
    public ModelPart neck00;
    public ModelPart neck01;
    public ModelPart head;
    public ModelPart lowerJawOpen;
    public ModelPart lowerJawClosed;
    public ModelPart lEar;
    public ModelPart rEar;
    public ModelPart lArm01;
    public ModelPart lArm02;
    public ModelPart lClaw00;
    public ModelPart lClaw01;
    public ModelPart lClaw02;
    public ModelPart lArmFur;
    public ModelPart rArm01;
    public ModelPart rArm02;
    public ModelPart rClaw00;
    public ModelPart rClaw01;
    public ModelPart rClaw02;
    public ModelPart rArmFur;
    public ModelPart chestFur;
    public ModelPart chestFurLower;
    private double yTranslate = 0D;
    private float alpha = 1F;

    public ModelHidebehind(ModelPart root) {
        this.chest = root.getChild("chest");
        this.stomach = chest.getChild("stomach");
        this.hips = stomach.getChild("hips");
        this.lLeg01 = hips.getChild("lLeg01");
        this.lLeg02 = lLeg01.getChild("lLeg02");
        this.lLeg03 = lLeg02.getChild("lLeg03");
        this.lPaw = lLeg03.getChild("lPaw");
        this.lToe00 = lPaw.getChild("lToe00");
        this.lToe01 = lPaw.getChild("lToe01");
        this.lToe02 = lPaw.getChild("lToe02");
        this.lLeg02Fur = lLeg02.getChild("lLeg02Fur");
        this.lLeg02FurMid = lLeg02.getChild("lLeg02FurMid");
        this.lLeg01Fur = lLeg01.getChild("lLeg01Fur");
        this.rLeg01 = hips.getChild("rLeg01");
        this.rLeg02 = rLeg01.getChild("rLeg02");
        this.rLeg03 = rLeg02.getChild("rLeg03");
        this.rPaw = rLeg03.getChild("rPaw");
        this.rToe00 = rPaw.getChild("rToe00");
        this.rToe01 = rPaw.getChild("rToe01");
        this.rToe02 = rPaw.getChild("rToe02");
        this.rLeg02Fur = rLeg02.getChild("rLeg02Fur");
        this.rLeg02FurMid = rLeg02.getChild("rLeg02FurMid");
        this.rLeg01Fur = rLeg01.getChild("rLeg01Fur");
        this.tail00 = hips.getChild("tail00");
        this.tail01 = tail00.getChild("tail01");
        this.crothFur = hips.getChild("crothFur");
        this.neck00 = chest.getChild("neck00");
        this.neck01 = neck00.getChild("neck01");
        this.head = neck01.getChild("head");
        this.lowerJawOpen = head.getChild("lowerJawOpen");
        this.lowerJawClosed = head.getChild("lowerJawClosed");
        this.lEar = head.getChild("lEar");
        this.rEar = head.getChild("rEar");
        this.lArm01 = chest.getChild("lArm01");
        this.lArm02 = lArm01.getChild("lArm02");
        this.lClaw00 = lArm02.getChild("lClaw00");
        this.lClaw01 = lArm02.getChild("lClaw01");
        this.lClaw02 = lArm02.getChild("lClaw02");
        this.lArmFur = lArm02.getChild("lArmFur");
        this.rArm01 = chest.getChild("rArm01");
        this.rArm02 = rArm01.getChild("rArm02");
        this.rClaw00 = rArm02.getChild("rClaw00");
        this.rClaw01 = rArm02.getChild("rClaw01");
        this.rClaw02 = rArm02.getChild("rClaw02");
        this.rArmFur = rArm02.getChild("rArmFur");
        this.chestFur = chest.getChild("chestFur");
        this.chestFurLower = chest.getChild("chestFurLower");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition chest = partdefinition.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -10.0F, -4.5F, 14.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -31.5F, 0.0F, 0.4538F, 0.0F, 0.0F));
        PartDefinition stomach = chest.addOrReplaceChild("stomach", CubeListBuilder.create().texOffs(0, 23).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.3F, 0.6F, -0.182F, 0.0F, 0.0F));
        PartDefinition hips = stomach.addOrReplaceChild("hips", CubeListBuilder.create().texOffs(0, 47).addBox(-6.0F, 0.0F, -3.0F, 12.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.9F, 0.0F, -0.2618F, 0.0F, 0.0F));
        PartDefinition lLeg01 = hips.addOrReplaceChild("lLeg01", CubeListBuilder.create().texOffs(0, 62).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 24.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 2.0F, -0.4F, -0.6109F, -0.1396F, 0.0F));
        PartDefinition lLeg02 = lLeg01.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(0, 94).addBox(-2.0F, -2.5F, 0.0F, 4.0F, 5.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 20.0F, 1.3F, -0.3142F, 0.0F, 0.0F));
        PartDefinition lLeg03 = lLeg02.addOrReplaceChild("lLeg03", CubeListBuilder.create().texOffs(0, 94).addBox(-2.0F, -0.9F, -1.5F, 4.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.3F, 14.3F, 0.6981F, 0.0F, 0.0F));
        PartDefinition lPaw = lLeg03.addOrReplaceChild("lPaw", CubeListBuilder.create().texOffs(14, 116).addBox(-2.5F, 0.0F, -3.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.2F, 0.0F, 0.2269F, 0.0F, 0.0F));
        PartDefinition lToe00 = lPaw.addOrReplaceChild("lToe00", CubeListBuilder.create().texOffs(0, 116).addBox(-0.5F, -0.9F, -4.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4F, 0.0F, -2.5F, 0.1745F, -0.1396F, 0.0F));
        PartDefinition lToe01 = lPaw.addOrReplaceChild("lToe01", CubeListBuilder.create().texOffs(0, 116).addBox(-0.5F, -0.9F, -4.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.1745F, 0.0F, 0.0F));
        PartDefinition lToe02 = lPaw.addOrReplaceChild("lToe02", CubeListBuilder.create().texOffs(0, 116).addBox(-0.5F, -0.9F, -4.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4F, 0.0F, -2.5F, 0.1745F, 0.1396F, 0.0F));
        PartDefinition lLeg02Fur = lLeg02.addOrReplaceChild("lLeg02Fur", CubeListBuilder.create().texOffs(45, 98).addBox(-1.99F, 0.0F, -5.5F, 4.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.7F, 1.9F, 0.7854F, 0.0F, 0.0F));
        PartDefinition lLeg02FurMid = lLeg02.addOrReplaceChild("lLeg02FurMid", CubeListBuilder.create().texOffs(74, 91).addBox(0.0F, 0.0F, -5.5F, 0.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.6F, 3.4F, 0.3491F, 0.0F, 0.0F));
        PartDefinition lLeg01Fur = lLeg01.addOrReplaceChild("lLeg01Fur", CubeListBuilder.create().texOffs(44, 71).addBox(-2.49F, -9.0F, 0.0F, 5.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.5F, -1.5F, -0.7854F, 0.0F, 0.0F));
        PartDefinition rLeg01 = hips.addOrReplaceChild("rLeg01", CubeListBuilder.create().texOffs(0, 62).mirror().addBox(-2.5F, -1.5F, -2.5F, 5.0F, 24.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.5F, 2.0F, -0.4F, -0.6109F, 0.1396F, 0.0F));
        PartDefinition rLeg02 = rLeg01.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(0, 94).mirror().addBox(-2.0F, -2.5F, 0.0F, 4.0F, 5.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 20.0F, 1.1F, -0.3142F, 0.0F, 0.0F));
        PartDefinition rLeg03 = rLeg02.addOrReplaceChild("rLeg03", CubeListBuilder.create().texOffs(0, 94).mirror().addBox(-2.0F, -0.9F, -1.5F, 4.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.3F, 14.3F, 0.6981F, 0.0F, 0.0F));
        PartDefinition rPaw = rLeg03.addOrReplaceChild("rPaw", CubeListBuilder.create().texOffs(14, 116).mirror().addBox(-2.5F, 0.0F, -3.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 7.2F, 0.0F, 0.2269F, 0.0F, 0.0F));
        PartDefinition rToe00 = rPaw.addOrReplaceChild("rToe00", CubeListBuilder.create().texOffs(0, 116).mirror().addBox(-0.5F, -0.9F, -4.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.4F, 0.0F, -2.5F, 0.1745F, 0.1396F, 0.0F));
        PartDefinition rToe01 = rPaw.addOrReplaceChild("rToe01", CubeListBuilder.create().texOffs(0, 116).mirror().addBox(-0.5F, -0.9F, -4.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.1745F, 0.0F, 0.0F));
        PartDefinition rToe02 = rPaw.addOrReplaceChild("rToe02", CubeListBuilder.create().texOffs(0, 116).mirror().addBox(-0.5F, -0.9F, -4.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.4F, 0.0F, -2.5F, 0.1745F, -0.1396F, 0.0F));
        PartDefinition rLeg02Fur = rLeg02.addOrReplaceChild("rLeg02Fur", CubeListBuilder.create().texOffs(45, 98).mirror().addBox(-2.01F, 0.0F, -5.5F, 4.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -0.7F, 1.9F, 0.7854F, 0.0F, 0.0F));
        PartDefinition rLeg02FurMid = rLeg02.addOrReplaceChild("rLeg02FurMid", CubeListBuilder.create().texOffs(74, 91).mirror().addBox(0.0F, 0.0F, -5.5F, 0.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.6F, 3.4F, 0.3491F, 0.0F, 0.0F));
        PartDefinition rLeg01Fur = rLeg01.addOrReplaceChild("rLeg01Fur", CubeListBuilder.create().texOffs(44, 73).mirror().addBox(-2.51F, -9.0F, 0.0F, 5.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 7.5F, -1.5F, -0.7854F, 0.0F, 0.0F));
        PartDefinition tail00 = hips.addOrReplaceChild("tail00", CubeListBuilder.create().texOffs(25, 69).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.7F, 3.1F, 0.1222F, 0.0F, 0.0F));
        PartDefinition tail01 = tail00.addOrReplaceChild("tail01", CubeListBuilder.create().texOffs(25, 77).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.3F, 2.1F, 0.3142F, 0.0F, 0.0F));
        PartDefinition crothFur = hips.addOrReplaceChild("crothFur", CubeListBuilder.create().texOffs(88, 43).addBox(-5.0F, 0.0F, -4.5F, 10.0F, 18.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.8F, 0.0F));
        PartDefinition neck00 = chest.addOrReplaceChild("neck00", CubeListBuilder.create().texOffs(46, 0).addBox(-4.0F, -8.0F, -3.5F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.8F, 0.8F, 0.3142F, 0.0F, 0.0F));
        PartDefinition neck01 = neck00.addOrReplaceChild("neck01", CubeListBuilder.create().texOffs(46, 0).addBox(-4.0F, -8.0F, -3.5F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.8F, 0.0F, -0.3142F, 0.0F, 0.0F));
        PartDefinition head = neck01.addOrReplaceChild("head", CubeListBuilder.create().texOffs(41, 15).addBox(-5.5F, -6.5F, -7.1F, 11.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.4F, -0.1F, -0.4189F, 0.0F, 0.0F));
        PartDefinition lowerJawOpen = head.addOrReplaceChild("lowerJawOpen", CubeListBuilder.create().texOffs(38, 41).addBox(-5.0F, -1.9F, -8.4F, 10.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.7F, -4.5F));
        PartDefinition lowerJawClosed = head.addOrReplaceChild("lowerJawClosed", CubeListBuilder.create().texOffs(38, 55).addBox(-5.0F, -3.0F, -2.4F, 10.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.4F, -4.5F));
        PartDefinition lEar = head.addOrReplaceChild("lEar", CubeListBuilder.create().texOffs(75, 16).addBox(0.0F, -2.0F, -1.2F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.2F, -5.3F, 0.0F, 0.3491F, 0.3491F, -1.2217F));
        PartDefinition rEar = head.addOrReplaceChild("rEar", CubeListBuilder.create().texOffs(75, 16).mirror().addBox(-5.0F, -2.0F, -1.5F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.2F, -5.3F, 0.0F, 0.3491F, -0.3491F, 1.2217F));
        PartDefinition lArm01 = chest.addOrReplaceChild("lArm01", CubeListBuilder.create().texOffs(90, 0).addBox(-2.0F, -1.5F, -2.5F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, -9.4F, 0.5F, -0.0873F, 0.0F, -0.1222F));
        PartDefinition lArm02 = lArm01.addOrReplaceChild("lArm02", CubeListBuilder.create().texOffs(90, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, 0.0F, -0.5236F, 0.0F, 0.0F));
        PartDefinition lClaw00 = lArm02.addOrReplaceChild("lClaw00", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1F, -1.5F, -0.5F, 3.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6F, 19.8F, -1.5F, -0.1047F, 0.0F, 0.2269F));
        PartDefinition lClaw01 = lArm02.addOrReplaceChild("lClaw01", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1F, -1.5F, -0.5F, 3.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6F, 19.8F, 0.0F, 0.0F, 0.0F, 0.2269F));
        PartDefinition lClaw02 = lArm02.addOrReplaceChild("lClaw02", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1F, -1.5F, -0.5F, 3.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6F, 19.8F, 1.5F, 0.1047F, 0.0F, 0.2269F));
        PartDefinition lArmFur = lArm02.addOrReplaceChild("lArmFur", CubeListBuilder.create().texOffs(78, 75).addBox(-1.9F, -7.5F, 0.5F, 4.0F, 15.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.2F, -1.2F, -0.3491F, 0.0F, 0.0F));
        PartDefinition rArm01 = chest.addOrReplaceChild("rArm01", CubeListBuilder.create().texOffs(90, 0).mirror().addBox(-2.0F, -1.5F, -2.5F, 4.0F, 17.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.5F, -9.4F, 0.5F, -0.0873F, 0.0F, 0.1222F));
        PartDefinition rArm02 = rArm01.addOrReplaceChild("rArm02", CubeListBuilder.create().texOffs(90, 0).mirror().addBox(-1.9F, 0.0F, -2.0F, 4.0F, 20.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 15.0F, 0.0F, -0.5236F, 0.0F, 0.0F));
        PartDefinition rClaw00 = rArm02.addOrReplaceChild("rClaw00", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.8F, -1.5F, -0.5F, 3.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.6F, 19.8F, -1.5F, -0.1047F, 0.0F, -0.2269F));
        PartDefinition rClaw01 = rArm02.addOrReplaceChild("rClaw01", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.8F, -1.5F, -0.5F, 3.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.6F, 19.8F, 0.0F, 0.0F, 0.0F, -0.2269F));
        PartDefinition rClaw02 = rArm02.addOrReplaceChild("rClaw02", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.8F, -1.5F, -0.5F, 3.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.6F, 19.8F, 1.5F, 0.1047F, 0.0F, -0.2269F));
        PartDefinition rArmFur = rArm02.addOrReplaceChild("rArmFur", CubeListBuilder.create().texOffs(78, 75).mirror().addBox(-2.01F, -7.5F, 0.5F, 4.0F, 15.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 8.2F, -1.2F, -0.3491F, 0.0F, 0.0F));
        PartDefinition chestFur = chest.addOrReplaceChild("chestFur", CubeListBuilder.create().texOffs(81, 42).addBox(-7.0F, 0.0F, -4.5F, 14.0F, 18.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.8F, -0.4F, -0.4538F, 0.0F, 0.0F));
        PartDefinition chestFurLower = chest.addOrReplaceChild("chestFurLower", CubeListBuilder.create().texOffs(82, 42).addBox(-6.0F, 0.0F, -4.5F, 12.0F, 18.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.3F, 0.6F, -0.4538F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        
        stack.pushPose();
        {
            stack.translate(0D, yTranslate, 0D);
            this.chest.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha * this.alpha);
        }
        stack.popPose();
    }

    @Override
    public void setupAnim(EntityHidebehind entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.lowerJawOpen.visible = entity.getOpen();
        int h = entity.getHidingInt();
        this.alpha = h == 0 || h > 2 ? 1F : (h == 1 ? 0.5F : 0.05F);
        if(entity.attackSequenceTicks() > 0 && entity.isSequenceTarget(Minecraft.getInstance().player)) {
            LivingEntity target = Minecraft.getInstance().player;
            Vec3 targetEyes = target.getEyePosition(1F);
            Vec3 entityEyes = entity.getEyePosition(1F);
            this.head.yRot = 0;
            this.head.xRot = 0;
            this.yTranslate = entityEyes.subtract(targetEyes).y();
        } else {
            this.yTranslate = 0F;
            this.head.yRot = (float) Math.toRadians(netHeadYaw);
            this.head.xRot = (float) Math.toRadians(headPitch);
        }
        this.lLeg01.xRot = (float) Math.cos(0.6F * limbSwing) * limbSwingAmount * 0.7F - 0.6108652381980153F;
        this.rLeg01.xRot = (float) Math.cos(0.6F * limbSwing + (float) Math.PI) * limbSwingAmount * 0.7F - 0.6108652381980153F;
    }

}
