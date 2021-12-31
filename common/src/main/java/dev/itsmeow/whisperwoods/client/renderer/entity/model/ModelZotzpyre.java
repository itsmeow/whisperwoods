package dev.itsmeow.whisperwoods.client.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.itsmeow.whisperwoods.entity.EntityZotzpyre;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
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

    public ModelZotzpyre() {
        texWidth = 128;
        texHeight = 64;

        chest = new ModelPart(this);
        chest.setPos(0.0F, 14.8F, 4.5F);
        setRotateAngle(chest, -0.2182F, 0.0F, 0.0F);
        chest.texOffs(0, 0).addBox(-5.0F, -4.5F, -10.0F, 10.0F, 8.0F, 10.0F, 0.0F, false);

        stomach = new ModelPart(this);
        stomach.setPos(0.0F, -0.2F, -0.8F);
        chest.addChild(stomach);
        setRotateAngle(stomach, 0.0785F, 0.0F, 0.0F);
        stomach.texOffs(0, 23).addBox(-4.5F, -4.0F, 0.0F, 9.0F, 7.0F, 8.0F, 0.0F, false);

        lLeg01 = new ModelPart(this);
        lLeg01.setPos(3.2F, 0.4F, 5.6F);
        stomach.addChild(lLeg01);
        setRotateAngle(lLeg01, 0.3491F, 0.2182F, 0.2618F);
        lLeg01.texOffs(73, 0).addBox(-0.5F, -2.0F, -1.5F, 9.0F, 4.0F, 3.0F, 0.0F, false);

        lLeg02 = new ModelPart(this);
        lLeg02.setPos(8.7F, 0.4F, 0.0F);
        lLeg01.addChild(lLeg02);
        setRotateAngle(lLeg02, -0.2182F, 0.0F, 0.0F);
        lLeg02.texOffs(85, 7).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 10.0F, 2.0F, 0.0F, false);

        lClawsRotator = new ModelPart(this);
        lClawsRotator.setPos(1.1F, 7.0F, 0.0F);
        lLeg02.addChild(lClawsRotator);
        setRotateAngle(lClawsRotator, 0.0873F, 0.0F, -0.2618F);


        lClaw01 = new ModelPart(this);
        lClaw01.setPos(-0.3F, -0.2F, -0.8F);
        lClawsRotator.addChild(lClaw01);
        setRotateAngle(lClaw01, 0.1396F, 0.6109F, 0.0436F);
        lClaw01.texOffs(97, 0).addBox(-0.3F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        lClaw02 = new ModelPart(this);
        lClaw02.setPos(0.1F, -0.2F, -0.6F);
        lClawsRotator.addChild(lClaw02);
        setRotateAngle(lClaw02, 0.0F, 0.2618F, 0.0436F);
        lClaw02.texOffs(97, 0).addBox(-0.3F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        lClaw03 = new ModelPart(this);
        lClaw03.setPos(0.2F, -0.3F, 0.0F);
        lClawsRotator.addChild(lClaw03);
        setRotateAngle(lClaw03, 0.0F, 0.0F, 0.0436F);
        lClaw03.texOffs(97, 0).addBox(-0.3F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        lClaw04 = new ModelPart(this);
        lClaw04.setPos(0.1F, -0.2F, 0.6F);
        lClawsRotator.addChild(lClaw04);
        setRotateAngle(lClaw04, 0.0F, -0.2618F, 0.0436F);
        lClaw04.texOffs(97, 0).addBox(-0.3F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        lClaw05 = new ModelPart(this);
        lClaw05.setPos(-0.3F, -0.2F, 0.8F);
        lClawsRotator.addChild(lClaw05);
        setRotateAngle(lClaw05, -0.1396F, -0.6109F, 0.0436F);
        lClaw05.texOffs(97, 0).addBox(-0.3F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, false);

        rLeg01 = new ModelPart(this);
        rLeg01.setPos(-3.2F, 0.4F, 5.6F);
        stomach.addChild(rLeg01);
        setRotateAngle(rLeg01, 0.3491F, -0.2182F, -0.2618F);
        rLeg01.texOffs(73, 0).addBox(-8.5F, -2.0F, -1.5F, 9.0F, 4.0F, 3.0F, 0.0F, true);

        rLeg02 = new ModelPart(this);
        rLeg02.setPos(-8.7F, 0.4F, 0.0F);
        rLeg01.addChild(rLeg02);
        setRotateAngle(rLeg02, -0.2182F, 0.0F, 0.0F);
        rLeg02.texOffs(85, 7).addBox(-1.5F, -2.0F, -1.0F, 3.0F, 10.0F, 2.0F, 0.0F, true);

        rClawsRotator = new ModelPart(this);
        rClawsRotator.setPos(-1.1F, 7.0F, 0.0F);
        rLeg02.addChild(rClawsRotator);
        setRotateAngle(rClawsRotator, 0.0873F, 0.0F, 0.2618F);


        rClaw01 = new ModelPart(this);
        rClaw01.setPos(0.3F, -0.2F, -0.8F);
        rClawsRotator.addChild(rClaw01);
        setRotateAngle(rClaw01, 0.1396F, -0.6109F, -0.0436F);
        rClaw01.texOffs(97, 0).addBox(-3.7F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        rClaw02 = new ModelPart(this);
        rClaw02.setPos(-0.1F, -0.2F, -0.6F);
        rClawsRotator.addChild(rClaw02);
        setRotateAngle(rClaw02, 0.0F, -0.2618F, -0.0436F);
        rClaw02.texOffs(97, 0).addBox(-3.7F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        rClaw03 = new ModelPart(this);
        rClaw03.setPos(-0.2F, -0.3F, 0.0F);
        rClawsRotator.addChild(rClaw03);
        setRotateAngle(rClaw03, 0.0F, 0.0F, -0.0436F);
        rClaw03.texOffs(97, 0).addBox(-3.7F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        rClaw04 = new ModelPart(this);
        rClaw04.setPos(-0.1F, -0.2F, 0.6F);
        rClawsRotator.addChild(rClaw04);
        setRotateAngle(rClaw04, 0.0F, 0.2618F, -0.0436F);
        rClaw04.texOffs(97, 0).addBox(-3.7F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        rClaw05 = new ModelPart(this);
        rClaw05.setPos(0.3F, -0.2F, 0.8F);
        rClawsRotator.addChild(rClaw05);
        setRotateAngle(rClaw05, -0.1396F, 0.6109F, -0.0436F);
        rClaw05.texOffs(97, 0).addBox(-3.7F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, 0.0F, true);

        tail01 = new ModelPart(this);
        tail01.setPos(0.0F, -3.3F, 7.9F);
        stomach.addChild(tail01);
        setRotateAngle(tail01, -0.5473F, 0.0F, 0.0F);
        tail01.texOffs(30, 19).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);

        tail02 = new ModelPart(this);
        tail02.setPos(0.0F, 0.0F, 3.8F);
        tail01.addChild(tail02);
        tail02.texOffs(31, 24).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        tail02Membrane = new ModelPart(this);
        tail02Membrane.setPos(0.0F, 0.0F, 0.0F);
        tail02.addChild(tail02Membrane);
        tail02Membrane.texOffs(56, 56).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 0.0F, 6.0F, 0.0F, false);

        tail01Membrane = new ModelPart(this);
        tail01Membrane.setPos(0.0F, 0.0F, 0.0F);
        tail01.addChild(tail01Membrane);
        tail01Membrane.texOffs(58, 52).addBox(-3.5F, 0.0F, 0.0F, 7.0F, 0.0F, 4.0F, 0.0F, false);

        neck = new ModelPart(this);
        neck.setPos(0.0F, -0.8F, -9.6F);
        chest.addChild(neck);
        setRotateAngle(neck, 0.1309F, 0.0F, 0.0F);
        neck.texOffs(0, 41).addBox(-3.5F, -3.5F, -2.0F, 7.0F, 6.0F, 2.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPos(0.0F, -0.2F, -1.5F);
        neck.addChild(head);
        setRotateAngle(head, 0.0873F, 0.0F, 0.0F);
        head.texOffs(0, 52).addBox(-4.0F, -3.5F, -4.5F, 8.0F, 6.0F, 5.0F, 0.0F, false);

        lEar01 = new ModelPart(this);
        lEar01.setPos(2.5F, -2.6F, -1.0F);
        head.addChild(lEar01);
        setRotateAngle(lEar01, 0.0F, -0.1396F, 0.3491F);
        lEar01.texOffs(0, 0).addBox(-2.0F, -4.0F, -0.5F, 4.0F, 4.0F, 1.0F, 0.0F, false);

        lEar02 = new ModelPart(this);
        lEar02.setPos(0.9F, -0.5F, 0.8F);
        lEar01.addChild(lEar02);
        setRotateAngle(lEar02, 0.2094F, 0.0F, -0.1745F);
        lEar02.texOffs(33, 0).addBox(-1.5F, -5.5F, -0.5F, 3.0F, 6.0F, 1.0F, 0.0F, false);

        rEar01 = new ModelPart(this);
        rEar01.setPos(-2.5F, -2.6F, -1.0F);
        head.addChild(rEar01);
        setRotateAngle(rEar01, 0.0F, 0.1396F, -0.3491F);
        rEar01.texOffs(0, 0).addBox(-2.0F, -4.0F, -0.5F, 4.0F, 4.0F, 1.0F, 0.0F, true);

        rEar02 = new ModelPart(this);
        rEar02.setPos(-0.9F, -0.5F, 0.8F);
        rEar01.addChild(rEar02);
        setRotateAngle(rEar02, 0.2094F, 0.0F, 0.1745F);
        rEar02.texOffs(33, 0).addBox(-1.5F, -5.5F, -0.5F, 3.0F, 6.0F, 1.0F, 0.0F, true);

        snout = new ModelPart(this);
        snout.setPos(0.0F, -0.8F, -4.0F);
        head.addChild(snout);
        setRotateAngle(snout, 0.2618F, 0.0F, 0.0F);
        snout.texOffs(47, 0).addBox(-1.5F, -0.5F, -3.0F, 3.0F, 1.0F, 3.0F, 0.0F, false);

        upperJaw01 = new ModelPart(this);
        upperJaw01.setPos(1.2F, 1.8F, 0.6F);
        snout.addChild(upperJaw01);
        setRotateAngle(upperJaw01, -0.2618F, 0.1396F, 0.0F);
        upperJaw01.texOffs(47, 6).addBox(-1.0F, -1.0F, -3.8F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        lTeeth = new ModelPart(this);
        lTeeth.setPos(0.4F, 1.3F, -0.8F);
        upperJaw01.addChild(lTeeth);
        setRotateAngle(lTeeth, 0.1396F, 0.0F, 0.0F);
        lTeeth.texOffs(63, 0).addBox(-0.5F, -1.1F, -2.8F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        upperJaw02 = new ModelPart(this);
        upperJaw02.setPos(-1.2F, 1.8F, 0.6F);
        snout.addChild(upperJaw02);
        setRotateAngle(upperJaw02, -0.2618F, -0.1396F, 0.0F);
        upperJaw02.texOffs(47, 6).addBox(-1.0F, -1.0F, -3.8F, 2.0F, 2.0F, 4.0F, 0.0F, true);

        rTeeth = new ModelPart(this);
        rTeeth.setPos(-0.4F, 1.3F, -0.8F);
        upperJaw02.addChild(rTeeth);
        setRotateAngle(rTeeth, 0.1396F, 0.0F, 0.0F);
        rTeeth.texOffs(63, 0).addBox(-0.5F, -1.1F, -2.8F, 1.0F, 2.0F, 2.0F, 0.0F, true);

        nose = new ModelPart(this);
        nose.setPos(0.0F, 0.6F, -3.0F);
        snout.addChild(nose);
        setRotateAngle(nose, 0.1047F, 0.0F, 0.0F);
        nose.texOffs(47, 14).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 0.0F, 0.0F, false);

        lowerJaw = new ModelPart(this);
        lowerJaw.setPos(0.0F, 1.6F, -3.8F);
        head.addChild(lowerJaw);
        lowerJaw.texOffs(62, 8).addBox(-1.5F, 0.0F, -2.7F, 3.0F, 1.0F, 3.0F, 0.0F, false);

        lWing01 = new ModelPart(this);
        lWing01.setPos(4.5F, -2.2F, -7.0F);
        chest.addChild(lWing01);
        setRotateAngle(lWing01, 0.7418F, -0.1309F, 0.48F);
        lWing01.texOffs(43, 20).addBox(0.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, 0.0F, false);

        lWing02 = new ModelPart(this);
        lWing02.setPos(5.7F, 0.0F, 0.0F);
        lWing01.addChild(lWing02);
        setRotateAngle(lWing02, 0.0F, 0.829F, 0.3491F);
        lWing02.texOffs(43, 27).addBox(0.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, 0.0F, false);

        lWing03 = new ModelPart(this);
        lWing03.setPos(12.0F, 0.0F, -0.1F);
        lWing02.addChild(lWing03);
        setRotateAngle(lWing03, 0.0F, -2.1719F, 0.0F);
        lWing03.texOffs(43, 32).addBox(0.0F, -0.5F, -1.0F, 13.0F, 1.0F, 2.0F, 0.0F, false);

        lWing04 = new ModelPart(this);
        lWing04.setPos(12.7F, 0.0F, -0.2F);
        lWing03.addChild(lWing04);
        setRotateAngle(lWing04, 0.0F, -0.3491F, 0.0F);
        lWing04.texOffs(43, 36).addBox(0.0F, -0.5F, -0.5F, 11.0F, 1.0F, 1.0F, 0.0F, false);

        lWingMembrane01 = new ModelPart(this);
        lWingMembrane01.setPos(0.0F, 0.0F, -0.1F);
        lWing03.addChild(lWingMembrane01);
        setRotateAngle(lWingMembrane01, 0.0F, -0.2618F, 0.0F);
        lWingMembrane01.texOffs(7, 38).addBox(-1.3F, -0.01F, -2.8F, 29.0F, 0.0F, 13.0F, 0.0F, false);

        lWingMembrane02 = new ModelPart(this);
        lWingMembrane02.setPos(0.2F, 0.0F, 1.0F);
        lWing02.addChild(lWingMembrane02);
        setRotateAngle(lWingMembrane02, 0.1745F, -0.6545F, -0.6981F);
        lWingMembrane02.texOffs(64, 46).addBox(-10.0F, -0.03F, 0.0F, 16.0F, 0.0F, 18.0F, 0.0F, false);

        lFinger = new ModelPart(this);
        lFinger.setPos(11.5F, 0.1F, -0.6F);
        lWing02.addChild(lFinger);
        setRotateAngle(lFinger, -0.6981F, -0.3054F, 0.6545F);
        lFinger.texOffs(63, 18).addBox(-1.0F, -0.5F, -5.0F, 2.0F, 1.0F, 5.0F, 0.0F, false);

        rWing01 = new ModelPart(this);
        rWing01.setPos(-4.5F, -2.2F, -7.0F);
        chest.addChild(rWing01);
        setRotateAngle(rWing01, 0.7418F, 0.1309F, -0.48F);
        rWing01.texOffs(43, 20).addBox(-6.0F, -1.5F, -1.5F, 6.0F, 3.0F, 3.0F, 0.0F, true);

        rWing02 = new ModelPart(this);
        rWing02.setPos(-5.7F, 0.0F, 0.0F);
        rWing01.addChild(rWing02);
        setRotateAngle(rWing02, 0.0F, -0.829F, -0.3491F);
        rWing02.texOffs(43, 27).addBox(-12.0F, -1.0F, -1.0F, 12.0F, 2.0F, 2.0F, 0.0F, true);

        rWing03 = new ModelPart(this);
        rWing03.setPos(-12.0F, 0.0F, -0.1F);
        rWing02.addChild(rWing03);
        setRotateAngle(rWing03, 0.0F, 2.1719F, 0.0F);
        rWing03.texOffs(43, 32).addBox(-13.0F, -0.5F, -1.0F, 13.0F, 1.0F, 2.0F, 0.0F, true);

        rWing04 = new ModelPart(this);
        rWing04.setPos(-12.7F, 0.0F, -0.2F);
        rWing03.addChild(rWing04);
        setRotateAngle(rWing04, 0.0F, 0.3491F, 0.0F);
        rWing04.texOffs(43, 36).addBox(-11.0F, -0.5F, -0.5F, 11.0F, 1.0F, 1.0F, 0.0F, true);

        rWingMembrane01 = new ModelPart(this);
        rWingMembrane01.setPos(0.0F, 0.0F, -0.1F);
        rWing03.addChild(rWingMembrane01);
        setRotateAngle(rWingMembrane01, 0.0F, 0.2618F, 0.0F);
        rWingMembrane01.texOffs(7, 38).addBox(-27.7F, -0.01F, -2.8F, 29.0F, 0.0F, 13.0F, 0.0F, true);

        rWingMembrane02 = new ModelPart(this);
        rWingMembrane02.setPos(-0.2F, 0.0F, 1.0F);
        rWing02.addChild(rWingMembrane02);
        setRotateAngle(rWingMembrane02, 0.1745F, 0.6545F, 0.6981F);
        rWingMembrane02.texOffs(64, 46).addBox(-6.0F, -0.03F, 0.0F, 16.0F, 0.0F, 18.0F, 0.0F, true);

        rFinger = new ModelPart(this);
        rFinger.setPos(-11.5F, 0.1F, -0.6F);
        rWing02.addChild(rFinger);
        setRotateAngle(rFinger, -0.6981F, 0.3054F, -0.6545F);
        rFinger.texOffs(63, 18).addBox(-1.0F, -0.5F, -5.0F, 2.0F, 1.0F, 5.0F, 0.0F, true);

        mane01 = new ModelPart(this);
        mane01.setPos(0.0F, -3.6F, -10.3F);
        chest.addChild(mane01);
        setRotateAngle(mane01, 0.2618F, 0.0F, 0.0F);
        mane01.texOffs(102, 13).addBox(-2.5F, -0.8F, 0.0F, 5.0F, 2.0F, 8.0F, 0.0F, false);

        mane02 = new ModelPart(this);
        mane02.setPos(0.0F, -4.0F, -9.0F);
        chest.addChild(mane02);
        setRotateAngle(mane02, 0.1222F, 0.0F, 0.0F);
        mane02.texOffs(98, 3).addBox(-3.5F, -0.7F, 0.0F, 7.0F, 2.0F, 8.0F, 0.0F, false);

        mane03 = new ModelPart(this);
        mane03.setPos(0.0F, 3.4F, -5.2F);
        chest.addChild(mane03);
        setRotateAngle(mane03, -0.0524F, 0.0F, 0.0F);
        mane03.texOffs(30, 51).addBox(-3.5F, 0.0F, -4.0F, 7.0F, 3.0F, 8.0F, 0.0F, false);

        mane04 = new ModelPart(this);
        mane04.setPos(0.0F, 2.1F, -10.6F);
        chest.addChild(mane04);
        setRotateAngle(mane04, 0.0873F, 0.0F, 0.0F);
        mane04.texOffs(102, 25).addBox(-2.5F, -0.8F, -0.5F, 5.0F, 6.0F, 2.0F, 0.0F, false);
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
