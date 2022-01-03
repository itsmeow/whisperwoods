package dev.itsmeow.whisperwoods.client.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.itsmeow.whisperwoods.entity.EntityZotzpyre;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class ModelZotzpyre<T extends LivingEntity> extends EntityModel<T> {

    public ModelPart chest;
    public ModelPart stomach;
    public ModelPart lLeg01;
    public ModelPart lLeg02;
    public ModelPart lClawsRotator;
    public ModelPart lClaw01;
    public ModelPart lClaw02;
    public ModelPart lClaw03;
    public ModelPart lClaw04;
    public ModelPart lClaw05;
    public ModelPart rLeg01;
    public ModelPart rLeg02;
    public ModelPart rClawsRotator;
    public ModelPart rClaw01;
    public ModelPart rClaw02;
    public ModelPart rClaw03;
    public ModelPart rClaw04;
    public ModelPart rClaw05;
    public ModelPart tail01;
    public ModelPart tail02;
    public ModelPart tail02Membrane;
    public ModelPart tail01Membrane;
    public ModelPart neck;
    public ModelPart head;
    public ModelPart lEar01;
    public ModelPart lEar02;
    public ModelPart rEar01;
    public ModelPart rEar02;
    public ModelPart snout;
    public ModelPart upperJaw01;
    public ModelPart lTeeth;
    public ModelPart upperJaw02;
    public ModelPart rTeeth;
    public ModelPart nose;
    public ModelPart lowerJaw;
    public ModelPart lWing01;
    public ModelPart lWing02;
    public ModelPart lWing03;
    public ModelPart lWing04;
    public ModelPart lWingMembrane01;
    public ModelPart lWingMembrane02;
    public ModelPart lFinger;
    public ModelPart rWing01;
    public ModelPart rWing02;
    public ModelPart rWing03;
    public ModelPart rWing04;
    public ModelPart rWingMembrane01;
    public ModelPart rWingMembrane02;
    public ModelPart rFinger;
    public ModelPart mane01;
    public ModelPart mane02;
    public ModelPart mane03;
    public ModelPart mane04;

    private boolean wasHanging = false;

    public ModelZotzpyre(ModelPart root) {
        this.chest = root.getChild("chest");
        this.stomach = chest.getChild("stomach");
        this.lLeg01 = stomach.getChild("lLeg01");
        this.lLeg02 = lLeg01.getChild("lLeg02");
        this.lClawsRotator = lLeg02.getChild("lClawsRotator");
        this.lClaw01 = lClawsRotator.getChild("lClaw01");
        this.lClaw02 = lClawsRotator.getChild("lClaw02");
        this.lClaw03 = lClawsRotator.getChild("lClaw03");
        this.lClaw04 = lClawsRotator.getChild("lClaw04");
        this.lClaw05 = lClawsRotator.getChild("lClaw05");
        this.rLeg01 = stomach.getChild("rLeg01");
        this.rLeg02 = rLeg01.getChild("rLeg02");
        this.rClawsRotator = rLeg02.getChild("rClawsRotator");
        this.rClaw01 = rClawsRotator.getChild("rClaw01");
        this.rClaw02 = rClawsRotator.getChild("rClaw02");
        this.rClaw03 = rClawsRotator.getChild("rClaw03");
        this.rClaw04 = rClawsRotator.getChild("rClaw04");
        this.rClaw05 = rClawsRotator.getChild("rClaw05");
        this.tail01 = stomach.getChild("tail01");
        this.tail02 = tail01.getChild("tail02");
        this.tail02Membrane = tail02.getChild("tail02Membrane");
        this.tail01Membrane = tail01.getChild("tail01Membrane");
        this.neck = chest.getChild("neck");
        this.head = neck.getChild("head");
        this.lEar01 = head.getChild("lEar01");
        this.lEar02 = lEar01.getChild("lEar02");
        this.rEar01 = head.getChild("rEar01");
        this.rEar02 = rEar01.getChild("rEar02");
        this.snout = head.getChild("snout");
        this.upperJaw01 = snout.getChild("upperJaw01");
        this.lTeeth = upperJaw01.getChild("lTeeth");
        this.upperJaw02 = snout.getChild("upperJaw02");
        this.rTeeth = upperJaw02.getChild("rTeeth");
        this.nose = snout.getChild("nose");
        this.lowerJaw = head.getChild("lowerJaw");
        this.lWing01 = chest.getChild("lWing01");
        this.lWing02 = lWing01.getChild("lWing02");
        this.lWing03 = lWing02.getChild("lWing03");
        this.lWing04 = lWing03.getChild("lWing04");
        this.lWingMembrane01 = lWing03.getChild("lWingMembrane01");
        this.lWingMembrane02 = lWing02.getChild("lWingMembrane02");
        this.lFinger = lWing02.getChild("lFinger");
        this.rWing01 = chest.getChild("rWing01");
        this.rWing02 = rWing01.getChild("rWing02");
        this.rWing03 = rWing02.getChild("rWing03");
        this.rWing04 = rWing03.getChild("rWing04");
        this.rWingMembrane01 = rWing03.getChild("rWingMembrane01");
        this.rWingMembrane02 = rWing02.getChild("rWingMembrane02");
        this.rFinger = rWing02.getChild("rFinger");
        this.mane01 = chest.getChild("mane01");
        this.mane02 = chest.getChild("mane02");
        this.mane03 = chest.getChild("mane03");
        this.mane04 = chest.getChild("mane04");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition chest = partdefinition.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.5F, -10.0F, 10.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.8F, 4.5F, -0.2182F, 0.0F, 0.0F));
        PartDefinition stomach = chest.addOrReplaceChild("stomach", CubeListBuilder.create().texOffs(0, 23).addBox(-4.5F, -4.0F, 0.0F, 9.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.2F, -0.8F, 0.0785F, 0.0F, 0.0F));
        PartDefinition lLeg01 = stomach.addOrReplaceChild("lLeg01", CubeListBuilder.create().texOffs(73, 0).addBox(-0.5F, -2.0F, -1.5F, 9.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.2F, 0.4F, 5.6F, 0.3491F, 0.2182F, 0.2618F));
        PartDefinition lLeg02 = lLeg01.addOrReplaceChild("lLeg02", CubeListBuilder.create().texOffs(85, 7).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.7F, 0.4F, 0.0F, -0.2182F, 0.0F, 0.0F));
        PartDefinition lClawsRotator = lLeg02.addOrReplaceChild("lClawsRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(1.1F, 7.0F, 0.0F, 0.0873F, 0.0F, -0.2618F));
        PartDefinition lClaw01 = lClawsRotator.addOrReplaceChild("lClaw01", CubeListBuilder.create().texOffs(97, 0).addBox(-0.3F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3F, -0.2F, -0.8F, 0.1396F, 0.6109F, 0.0436F));
        PartDefinition lClaw02 = lClawsRotator.addOrReplaceChild("lClaw02", CubeListBuilder.create().texOffs(97, 0).addBox(-0.3F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -0.2F, -0.6F, 0.0F, 0.2618F, 0.0436F));
        PartDefinition lClaw03 = lClawsRotator.addOrReplaceChild("lClaw03", CubeListBuilder.create().texOffs(97, 0).addBox(-0.3F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, -0.3F, 0.0F, 0.0F, 0.0F, 0.0436F));
        PartDefinition lClaw04 = lClawsRotator.addOrReplaceChild("lClaw04", CubeListBuilder.create().texOffs(97, 0).addBox(-0.3F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -0.2F, 0.6F, 0.0F, -0.2618F, 0.0436F));
        PartDefinition lClaw05 = lClawsRotator.addOrReplaceChild("lClaw05", CubeListBuilder.create().texOffs(97, 0).addBox(-0.3F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3F, -0.2F, 0.8F, -0.1396F, -0.6109F, 0.0436F));
        PartDefinition rLeg01 = stomach.addOrReplaceChild("rLeg01", CubeListBuilder.create().texOffs(73, 0).mirror().addBox(-8.5F, -2.0F, -1.5F, 9.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.2F, 0.4F, 5.6F, 0.3491F, -0.2182F, -0.2618F));
        PartDefinition rLeg02 = rLeg01.addOrReplaceChild("rLeg02", CubeListBuilder.create().texOffs(85, 7).mirror().addBox(-1.5F, -2.0F, -1.0F, 3.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.7F, 0.4F, 0.0F, -0.2182F, 0.0F, 0.0F));
        PartDefinition rClawsRotator = rLeg02.addOrReplaceChild("rClawsRotator", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.1F, 7.0F, 0.0F, 0.0873F, 0.0F, 0.2618F));
        PartDefinition rClaw01 = rClawsRotator.addOrReplaceChild("rClaw01", CubeListBuilder.create().texOffs(97, 0).mirror().addBox(-3.7F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.3F, -0.2F, -0.8F, 0.1396F, -0.6109F, -0.0436F));
        PartDefinition rClaw02 = rClawsRotator.addOrReplaceChild("rClaw02", CubeListBuilder.create().texOffs(97, 0).mirror().addBox(-3.7F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.1F, -0.2F, -0.6F, 0.0F, -0.2618F, -0.0436F));
        PartDefinition rClaw03 = rClawsRotator.addOrReplaceChild("rClaw03", CubeListBuilder.create().texOffs(97, 0).mirror().addBox(-3.7F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.2F, -0.3F, 0.0F, 0.0F, 0.0F, -0.0436F));
        PartDefinition rClaw04 = rClawsRotator.addOrReplaceChild("rClaw04", CubeListBuilder.create().texOffs(97, 0).mirror().addBox(-3.7F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.1F, -0.2F, 0.6F, 0.0F, 0.2618F, -0.0436F));
        PartDefinition rClaw05 = rClawsRotator.addOrReplaceChild("rClaw05", CubeListBuilder.create().texOffs(97, 0).mirror().addBox(-3.7F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.3F, -0.2F, 0.8F, -0.1396F, 0.6109F, -0.0436F));
        PartDefinition tail01 = stomach.addOrReplaceChild("tail01", CubeListBuilder.create().texOffs(30, 19).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.3F, 7.9F, -0.5473F, 0.0F, 0.0F));
        PartDefinition tail02 = tail01.addOrReplaceChild("tail02", CubeListBuilder.create().texOffs(31, 24).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.8F));
        PartDefinition tail02Membrane = tail02.addOrReplaceChild("tail02Membrane", CubeListBuilder.create().texOffs(56, 56).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition tail01Membrane = tail01.addOrReplaceChild("tail01Membrane", CubeListBuilder.create().texOffs(58, 52).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition neck = chest.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 41).addBox(-3.5F, -3.5F, -2.0F, 7.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.8F, -9.6F, 0.1309F, 0.0F, 0.0F));
        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 52).addBox(-4.0F, -3.5F, -4.5F, 8.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.2F, -1.5F, 0.0873F, 0.0F, 0.0F));
        PartDefinition lEar01 = head.addOrReplaceChild("lEar01", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -2.6F, -1.0F, 0.0F, -0.1396F, 0.3491F));
        PartDefinition lEar02 = lEar01.addOrReplaceChild("lEar02", CubeListBuilder.create().texOffs(33, 0).addBox(-1.5F, -5.5F, -0.5F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9F, -0.5F, 0.8F, 0.2094F, 0.0F, -0.1745F));
        PartDefinition rEar01 = head.addOrReplaceChild("rEar01", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, -4.0F, -0.5F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.5F, -2.6F, -1.0F, 0.0F, 0.1396F, -0.3491F));
        PartDefinition rEar02 = rEar01.addOrReplaceChild("rEar02", CubeListBuilder.create().texOffs(33, 0).mirror().addBox(-1.5F, -5.5F, -0.5F, 3.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.9F, -0.5F, 0.8F, 0.2094F, 0.0F, 0.1745F));
        PartDefinition snout = head.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(47, 0).addBox(-1.5F, -0.5F, -3.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.8F, -4.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition upperJaw01 = snout.addOrReplaceChild("upperJaw01", CubeListBuilder.create().texOffs(47, 6).addBox(-1.0F, -1.0F, -3.8F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2F, 1.8F, 0.6F, -0.2618F, 0.1396F, 0.0F));
        PartDefinition lTeeth = upperJaw01.addOrReplaceChild("lTeeth", CubeListBuilder.create().texOffs(63, 0).addBox(-0.5F, -1.1F, -2.8F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, 1.3F, -0.8F, 0.1396F, 0.0F, 0.0F));
        PartDefinition upperJaw02 = snout.addOrReplaceChild("upperJaw02", CubeListBuilder.create().texOffs(47, 6).mirror().addBox(-1.0F, -1.0F, -3.8F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.2F, 1.8F, 0.6F, -0.2618F, -0.1396F, 0.0F));
        PartDefinition rTeeth = upperJaw02.addOrReplaceChild("rTeeth", CubeListBuilder.create().texOffs(63, 0).mirror().addBox(-0.5F, -1.1F, -2.8F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.4F, 1.3F, -0.8F, 0.1396F, 0.0F, 0.0F));
        PartDefinition nose = snout.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(47, 14).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.6F, -3.0F, 0.1047F, 0.0F, 0.0F));
        PartDefinition lowerJaw = head.addOrReplaceChild("lowerJaw", CubeListBuilder.create().texOffs(62, 8).addBox(-1.5F, 0.0F, -2.7F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.6F, -3.8F));
        PartDefinition lWing01 = chest.addOrReplaceChild("lWing01", CubeListBuilder.create().texOffs(43, 20).addBox(0.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -2.2F, -7.0F, 0.7418F, -0.1309F, 0.48F));
        PartDefinition lWing02 = lWing01.addOrReplaceChild("lWing02", CubeListBuilder.create().texOffs(43, 27).addBox(0.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.7F, 0.0F, 0.0F, 0.0F, 0.829F, 0.3491F));
        PartDefinition lWing03 = lWing02.addOrReplaceChild("lWing03", CubeListBuilder.create().texOffs(43, 32).addBox(0.0F, -0.5F, -1.0F, 13.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, 0.0F, -0.1F, 0.0F, -2.1719F, 0.0F));
        PartDefinition lWing04 = lWing03.addOrReplaceChild("lWing04", CubeListBuilder.create().texOffs(43, 36).addBox(0.0F, -0.5F, -0.5F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.7F, 0.0F, -0.2F, 0.0F, -0.3491F, 0.0F));
        PartDefinition lWingMembrane01 = lWing03.addOrReplaceChild("lWingMembrane01", CubeListBuilder.create().texOffs(7, 38).addBox(-1.3F, -0.01F, -2.8F, 29.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.1F, 0.0F, -0.2618F, 0.0F));
        PartDefinition lWingMembrane02 = lWing02.addOrReplaceChild("lWingMembrane02", CubeListBuilder.create().texOffs(64, 46).addBox(-10.0F, -0.03F, 0.0F, 16.0F, 0.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, 0.0F, 1.0F, 0.1745F, -0.6545F, -0.6981F));
        PartDefinition lFinger = lWing02.addOrReplaceChild("lFinger", CubeListBuilder.create().texOffs(63, 18).addBox(-1.0F, -0.5F, -5.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.5F, 0.1F, -0.6F, -0.6981F, -0.3054F, 0.6545F));
        PartDefinition rWing01 = chest.addOrReplaceChild("rWing01", CubeListBuilder.create().texOffs(43, 20).mirror().addBox(-6.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -2.2F, -7.0F, 0.7418F, 0.1309F, -0.48F));
        PartDefinition rWing02 = rWing01.addOrReplaceChild("rWing02", CubeListBuilder.create().texOffs(43, 27).mirror().addBox(-12.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-5.7F, 0.0F, 0.0F, 0.0F, -0.829F, -0.3491F));
        PartDefinition rWing03 = rWing02.addOrReplaceChild("rWing03", CubeListBuilder.create().texOffs(43, 32).mirror().addBox(-13.0F, -0.5F, -1.0F, 13.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-12.0F, 0.0F, -0.1F, 0.0F, 2.1719F, 0.0F));
        PartDefinition rWing04 = rWing03.addOrReplaceChild("rWing04", CubeListBuilder.create().texOffs(43, 36).mirror().addBox(-11.0F, -0.5F, -0.5F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-12.7F, 0.0F, -0.2F, 0.0F, 0.3491F, 0.0F));
        PartDefinition rWingMembrane01 = rWing03.addOrReplaceChild("rWingMembrane01", CubeListBuilder.create().texOffs(7, 38).mirror().addBox(-27.7F, -0.01F, -2.8F, 29.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.1F, 0.0F, 0.2618F, 0.0F));
        PartDefinition rWingMembrane02 = rWing02.addOrReplaceChild("rWingMembrane02", CubeListBuilder.create().texOffs(64, 46).mirror().addBox(-6.0F, -0.03F, 0.0F, 16.0F, 0.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.2F, 0.0F, 1.0F, 0.1745F, 0.6545F, 0.6981F));
        PartDefinition rFinger = rWing02.addOrReplaceChild("rFinger", CubeListBuilder.create().texOffs(63, 18).mirror().addBox(-1.0F, -0.5F, -5.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-11.5F, 0.1F, -0.6F, -0.6981F, 0.3054F, -0.6545F));
        PartDefinition mane01 = chest.addOrReplaceChild("mane01", CubeListBuilder.create().texOffs(102, 13).addBox(-2.5F, -0.8F, 0.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.6F, -10.3F, 0.2618F, 0.0F, 0.0F));
        PartDefinition mane02 = chest.addOrReplaceChild("mane02", CubeListBuilder.create().texOffs(98, 3).addBox(-3.5F, -0.7F, 0.0F, 7.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -9.0F, 0.1222F, 0.0F, 0.0F));
        PartDefinition mane03 = chest.addOrReplaceChild("mane03", CubeListBuilder.create().texOffs(30, 51).addBox(-3.5F, 0.0F, -4.0F, 7.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.4F, -5.2F, -0.0524F, 0.0F, 0.0F));
        PartDefinition mane04 = chest.addOrReplaceChild("mane04", CubeListBuilder.create().texOffs(102, 25).addBox(-2.5F, -0.8F, -0.5F, 5.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.1F, -10.6F, 0.0873F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.chest.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if(entity instanceof EntityZotzpyre) {
            EntityZotzpyre zotz = (EntityZotzpyre) entity;
            if(zotz.isHanging()) {
                this.hangingPose();
                this.wasHanging = true;
            } else {
                if(wasHanging) {
                    wasHanging = false;
                    this.resetHangingPose();
                }
                if (zotz.isNoGravity()) {
                    this.setRotateAngle(lLeg01, 1.3962634015954636F, 0.08726646259971647F, 0.40142572795869574F);
                    this.setRotateAngle(lWing03, 0.0F, -0.9599310885968813F, 0.0F);
                    this.setRotateAngle(lWing02, 0.0F, 0.5235987755982988F, 0.0F);
                    this.setRotateAngle(rLeg01, 1.3962634015954636F, -0.08726646259971647F, -0.40142572795869574F);
                    this.setRotateAngle(rWing03, 0.0F, 0.9599310885968813F, 0.0F);
                    this.setRotateAngle(rWing02, 0.0F, -0.5235987755982988F, 0.0F);
                    this.setRotateAngle(tail01, 0.0F, 0.0F, 0.0F);
                    this.setRotateAngle(tail02, 0.0F, 0.0F, 0.0F);
                    this.setRotateAngle(rWingMembrane02, 0.0F, 0.0F, 0.0F);
                    this.setRotateAngle(lWingMembrane02, 0.0F, 0.0F, 0.0F);
                    this.setRotateAngle(lWing01, 0.0F, 0.0F, 0.0F);
                    this.setRotateAngle(rWing01, 0.0F, 0.0F, 0.0F);
                    this.setRotateAngle(head, -0.08726646259971647F, 0.0F, 0.0F);
                    float limbSwingAmountM = Math.max(limbSwingAmount, 0.5F);
                    this.lWing01.xRot = Mth.cos(ageInTicks * 0.1F) * 0.4F - 0.14F;
                    this.rWing01.xRot = this.lWing01.xRot;
                    this.lWing01.zRot = Mth.cos(ageInTicks) * limbSwingAmountM * 0.4F - 0.14F;
                    this.rWing01.zRot = -this.lWing01.zRot;
                    this.lWing03.zRot = Mth.cos(ageInTicks) * limbSwingAmountM - 0.14F;
                    this.rWing03.zRot = -this.lWing03.zRot;
                    this.chest.xRot = -Mth.cos(ageInTicks * 0.45F) * limbSwingAmountM * 0.02F;
                    this.chest.zRot = -Mth.cos(ageInTicks * 0.3F) * 0.1F * limbSwingAmountM;
                    this.stomach.zRot = -Mth.cos(ageInTicks * 0.3F) * 0.1F * limbSwingAmountM;
                    this.lLeg01.zRot = Mth.cos(ageInTicks * 0.6F) * 0.1F * limbSwingAmountM;
                    this.rLeg01.zRot = this.lLeg01.zRot;
                    this.lowerJaw.xRot = 0.9F + Mth.cos(ageInTicks * 0.2F) * 0.25F;
                } else {
                    this.setRotateAngle(lWing02, 0.3490658503988659F, 0.6981317007977318F, 0.7853981633974483F);
                    this.setRotateAngle(rWing03, 0.0F, 1.9547687622336491F, 0.0F);
                    this.setRotateAngle(rWing01, -0.13962634015954636F, 0.5759586531581287F, -0.40142572795869574F);
                    this.setRotateAngle(tail01, -0.6283185307179586F, 0.0F, 0.0F);
                    this.setRotateAngle(rWingMembrane02, 0.0F, 0.5759586531581287F, 0.0F);
                    this.setRotateAngle(lLeg01, 0.0F, 0.45378560551852565F, -0.03490658503988659F);
                    this.setRotateAngle(lWingMembrane02, 0.0F, -0.5759586531581287F, 0.0F);
                    this.setRotateAngle(tail02, -0.3141592653589793F, 0.0F, 0.0F);
                    this.setRotateAngle(rWing02, 0.3490658503988659F, -0.6981317007977318F, -0.7853981633974483F);
                    this.setRotateAngle(lWing01, -0.13962634015954636F, -0.5759586531581287F, 0.40142572795869574F);
                    this.setRotateAngle(lWing03, 0.0F, -1.9547687622336491F, 0.0F);
                    this.setRotateAngle(rLeg01, 0.0F, -0.45378560551852565F, 0.03490658503988659F);
                    this.lLeg01.xRot = Mth.sin(limbSwing * 0.8665F + (float) Math.PI) * limbSwingAmount;
                    this.rLeg01.xRot = Mth.cos(limbSwing * 0.8665F) * limbSwingAmount;
                    this.lWing01.xRot = Mth.sin(limbSwing * 0.8665F + (float) Math.PI) * limbSwingAmount - 0.13962634015954636F;
                    this.rWing01.xRot = Mth.cos(limbSwing * 0.8665F) * limbSwingAmount - 0.13962634015954636F;
                    this.lowerJaw.xRot = 0F;
                }
                this.neck.xRot = headPitch * 0.017453292F;
                this.neck.yRot = netHeadYaw * 0.017453292F;
            }
        }
    }

    public void hangingPose() {
        this.setRotateAngle(chest, 1.5708F, 0.0F, 0.0F);
        this.setRotateAngle(head, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(lClawsRotator, 0.0873F, 0.0F, 1.6144F);
        this.setRotateAngle(lFinger, -2.0944F, -0.7854F, 2.8798F);
        this.setRotateAngle(lLeg01, 1.5708F, -1.3963F, 0.0F);
        this.setRotateAngle(lLeg02, 0.0F, -0.2094F, -1.4399F);
        this.setRotateAngle(lWing01, 0.2618F, -0.1309F, 1.309F);
        this.setRotateAngle(lWing02, 0.0F, 0.829F, 1.3526F);
        this.setRotateAngle(lWing03, 0.0F, -1.9199F, 0.0F);
        this.setRotateAngle(lWingMembrane02, 0.6109F, -0.2182F, -1.309F);
        this.setRotateAngle(neck, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(rClawsRotator, 0.0873F, 0.0F, -1.6144F);
        this.setRotateAngle(rFinger, -2.0944F, 0.7854F, -2.8798F);
        this.setRotateAngle(rLeg01, 1.5708F, 1.4399F, 0.0F);
        this.setRotateAngle(rLeg02, 0.0F, 0.2094F, 1.4399F);
        this.setRotateAngle(rWing01, 0.2618F, 0.1309F, -1.5272F);
        this.setRotateAngle(rWing02, 0.0F, -0.829F, -1.3526F);
        this.setRotateAngle(rWing03, 0.0F, 1.9199F, 0.0F);
        this.setRotateAngle(rWingMembrane02, 0.6109F, 0.2182F, 1.309F);
        this.setRotateAngle(stomach, 0.0F, 0.0F, 0.0F);
        this.setRotateAngle(tail01, 0.0F, 0.0F, 0.0F);
    }

    public void resetHangingPose() {
        this.setRotateAngle(chest, -0.2182F, 0.0F, 0.0F);
        this.setRotateAngle(head, 0.0873F, 0.0F, 0.0F);
        this.setRotateAngle(lClawsRotator, 0.0873F, 0.0F, -0.2618F);
        this.setRotateAngle(lFinger, -0.6981F, -0.3054F, 0.6545F);
        this.setRotateAngle(lLeg01, 0.3491F, 0.2182F, 0.2618F);
        this.setRotateAngle(lLeg02, -0.2182F, 0.0F, 0.0F);
        this.setRotateAngle(lWing01, 0.7418F, -0.1309F, 0.48F);
        this.setRotateAngle(lWing02, 0.0F, 0.829F, 0.3491F);
        this.setRotateAngle(lWing03, 0.0F, -2.1719F, 0.0F);
        this.setRotateAngle(lWingMembrane02, 0.1745F, -0.6545F, -0.6981F);
        this.setRotateAngle(neck, 0.1309F, 0.0F, 0.0F);
        this.setRotateAngle(rClawsRotator, 0.0873F, 0.0F, 0.2618F);
        this.setRotateAngle(rFinger, -0.6981F, 0.3054F, -0.6545F);
        this.setRotateAngle(rLeg01, 0.3491F, -0.2182F, -0.2618F);
        this.setRotateAngle(rLeg02, -0.2182F, 0.0F, 0.0F);
        this.setRotateAngle(rWing01, 0.7418F, 0.1309F, -0.48F);
        this.setRotateAngle(rWing02, 0.0F, -0.829F, -0.3491F);
        this.setRotateAngle(rWing03, 0.0F, 2.1719F, 0.0F);
        this.setRotateAngle(rWingMembrane02, 0.1745F, 0.6545F, 0.6981F);
        this.setRotateAngle(stomach, 0.0785F, 0.0F, 0.0F);
        this.setRotateAngle(tail01, -0.5473F, 0.0F, 0.0F);
    }

    public void setRotateAngle(ModelPart ModelPart, float x, float y, float z) {
        ModelPart.xRot = x;
        ModelPart.yRot = y;
        ModelPart.zRot = z;
    }
}
