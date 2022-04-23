package dev.itsmeow.whisperwoods.client.renderer.tile.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelHandOfFate extends EntityModel<Entity> {

    public ModelPart bowlBase;
    public ModelPart bowlWall01;
    public ModelPart bowlStand01;
    public ModelPart bowlStand02;
    public ModelPart bowlStand03;
    public ModelPart bowlStandRivit;
    public ModelPart metal01a;
    public ModelPart metal02a;
    public ModelPart metal03a;
    public ModelPart metal04a;
    public ModelPart metal05a;
    public ModelPart metal06a;
    public ModelPart metal07a;
    public ModelPart metal08a;

    public ModelHandOfFate(ModelPart root) {
        this.bowlBase = root.getChild("bowlBase");
        this.bowlWall01 = root.getChild("bowlWall01");
        this.bowlStand01 = root.getChild("bowlStand01");
        this.bowlStand02 = root.getChild("bowlStand02");
        this.bowlStand03 = root.getChild("bowlStand03");
        this.bowlStandRivit = root.getChild("bowlStandRivit");
        this.metal01a = root.getChild("metal01a");
        this.metal02a = root.getChild("metal02a");
        this.metal03a = root.getChild("metal03a");
        this.metal04a = root.getChild("metal04a");
        this.metal05a = root.getChild("metal05a");
        this.metal06a = root.getChild("metal06a");
        this.metal07a = root.getChild("metal07a");
        this.metal08a = root.getChild("metal08a");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bowlBase = partdefinition.addOrReplaceChild("bowlBase", CubeListBuilder.create().texOffs(0, 4).addBox(-3.5F, -0.5F, -3.5F, 7.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));
        PartDefinition bowlWall01 = partdefinition.addOrReplaceChild("bowlWall01", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, 3.2F, 6.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, 20.9F, 0.0F));
        PartDefinition bowlWall02 = bowlWall01.addOrReplaceChild("bowlWall02", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, 3.2F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));
        PartDefinition bowlWall03 = bowlWall01.addOrReplaceChild("bowlWall03", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, 3.2F, 6.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));
        PartDefinition bowlWall04 = bowlWall01.addOrReplaceChild("bowlWall04", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, 3.2F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -3.1416F, 0.0F));
        PartDefinition bowlWall05 = bowlWall01.addOrReplaceChild("bowlWall05", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, 3.2F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));
        PartDefinition bowlWall06 = bowlWall01.addOrReplaceChild("bowlWall06", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, 3.2F, 6.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));
        PartDefinition bowlStand01 = partdefinition.addOrReplaceChild("bowlStand01", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, 1.1F, -3.3F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, -0.2731F, 0.0F, 0.0F));
        PartDefinition bowlStand01b = bowlStand01.addOrReplaceChild("bowlStand01b", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 3.0F, -3.6F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2731F, 0.0F, 0.0F));
        PartDefinition bowlStand02 = partdefinition.addOrReplaceChild("bowlStand02", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, 1.1F, -3.3F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, -0.2731F, -2.0944F, 0.0F));
        PartDefinition bowlStand02b = bowlStand02.addOrReplaceChild("bowlStand02b", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 3.0F, -3.6F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2731F, 0.0F, 0.0F));
        PartDefinition bowlStand03 = partdefinition.addOrReplaceChild("bowlStand03", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, 1.1F, -3.3F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, -0.2731F, 2.0944F, 0.0F));
        PartDefinition bowlStand03b = bowlStand03.addOrReplaceChild("bowlStand03b", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, 3.0F, -3.6F, 2.0F, 1.0F, 3.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2731F, 0.0F, 0.0F));
        PartDefinition bowlStandRivit = partdefinition.addOrReplaceChild("bowlStandRivit", CubeListBuilder.create().texOffs(12, 15).addBox(-1.1F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 23.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
        PartDefinition metal01a = partdefinition.addOrReplaceChild("metal01a", CubeListBuilder.create().texOffs(2, 21).addBox(-0.5F, -9.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 19.7F, 2.2F, 0.0911F, 0.0F, 0.0F));
        PartDefinition metal01b = metal01a.addOrReplaceChild("metal01b", CubeListBuilder.create().texOffs(2, 21).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.8F, 0.0F, -0.2731F, 0.0F, 0.0F));
        PartDefinition metal01c = metal01b.addOrReplaceChild("metal01c", CubeListBuilder.create().texOffs(2, 21).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.8F, 0.0F, -0.3643F, 0.0F, 0.0F));
        PartDefinition metal01d = metal01c.addOrReplaceChild("metal01d", CubeListBuilder.create().texOffs(2, 21).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -3.7F, 0.0F, 0.4554F, 0.0F, 0.0F));
        PartDefinition metal01g = metal01c.addOrReplaceChild("metal01g", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.2276F, 0.3643F, 0.2731F));
        PartDefinition metal01h = metal01c.addOrReplaceChild("metal01h", CubeListBuilder.create().texOffs(0, 24).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.2276F, -0.3643F, -0.2731F));
        PartDefinition metal01e = metal01b.addOrReplaceChild("metal01e", CubeListBuilder.create().texOffs(2, 24).addBox(0.3F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.3F, 0.0F, 0.0F, 0.3643F, 0.0456F));
        PartDefinition metal01f = metal01b.addOrReplaceChild("metal01f", CubeListBuilder.create().texOffs(2, 24).addBox(-1.1F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, -3.3F, 0.0F, 0.0F, -0.3643F, -0.0456F));
        PartDefinition metal01i = metal01a.addOrReplaceChild("metal01i", CubeListBuilder.create().texOffs(2, 21).addBox(-0.6F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -6.5F, -0.1F, 0.0F, -0.6829F, 0.0F));
        PartDefinition metal01j = metal01a.addOrReplaceChild("metal01j", CubeListBuilder.create().texOffs(2, 21).addBox(-0.6F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7F, -3.8F, -0.2F, 0.0F, 0.6829F, 0.0F));
        PartDefinition metal01k = metal01a.addOrReplaceChild("metal01k", CubeListBuilder.create().texOffs(2, 21).addBox(-0.6F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -3.3F, -0.1F, 0.0F, -0.6829F, 0.0F));
        PartDefinition metal02a = partdefinition.addOrReplaceChild("metal02a", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, -9.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4F, 19.7F, 1.2F, 0.0456F, -0.2276F, 0.0F));
        PartDefinition metal02b = metal02a.addOrReplaceChild("metal02b", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.8F, 0.0F, -0.2276F, -0.1367F, 0.0F));
        PartDefinition metal02c = metal02b.addOrReplaceChild("metal02c", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.8F, 0.0F, -0.3643F, 0.0F, 0.0F));
        PartDefinition metal02d = metal02c.addOrReplaceChild("metal02d", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -2.8F, 0.0F, 0.4554F, 0.0F, 0.0F));
        PartDefinition metal02e = metal02b.addOrReplaceChild("metal02e", CubeListBuilder.create().texOffs(2, 24).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, -3.2F, 0.2F, 0.4554F, -0.9561F, -0.4554F));
        PartDefinition metal02f = metal02a.addOrReplaceChild("metal02f", CubeListBuilder.create().texOffs(4, 21).addBox(-0.7F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -5.8F, -0.1F, 0.0F, -0.9561F, 0.0F));
        PartDefinition metal02g = metal02a.addOrReplaceChild("metal02g", CubeListBuilder.create().texOffs(2, 21).addBox(-1.4F, -0.5F, -0.7F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -1.8F, -0.1F, 0.0F, -0.9561F, 0.0F));
        PartDefinition metal03a = partdefinition.addOrReplaceChild("metal03a", CubeListBuilder.create().texOffs(1, 21).addBox(-0.5F, -9.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4F, 19.7F, 1.2F, 0.0456F, 0.2276F, 0.0F));
        PartDefinition metal03b = metal03a.addOrReplaceChild("metal03b", CubeListBuilder.create().texOffs(1, 21).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.8F, 0.0F, -0.2276F, 0.1367F, 0.0F));
        PartDefinition metal03c = metal03b.addOrReplaceChild("metal03c", CubeListBuilder.create().texOffs(1, 21).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.8F, 0.0F, -0.3643F, 0.0F, 0.0F));
        PartDefinition metal03d = metal03c.addOrReplaceChild("metal03d", CubeListBuilder.create().texOffs(1, 21).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -2.8F, 0.0F, 0.4554F, 0.0F, 0.0F));
        PartDefinition metal03e = metal03b.addOrReplaceChild("metal03e", CubeListBuilder.create().texOffs(1, 24).addBox(0.2F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.2F, 0.2F, 0.1367F, 0.7285F, 0.182F));
        PartDefinition metal03f = metal03a.addOrReplaceChild("metal03f", CubeListBuilder.create().texOffs(1, 21).addBox(0.0F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, -6.8F, 0.1F, 0.0F, 0.9561F, 0.0F));
        PartDefinition metal03g = metal03a.addOrReplaceChild("metal03g", CubeListBuilder.create().texOffs(1, 21).addBox(0.0F, -0.5F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, -2.7F, 0.1F, 0.0F, 0.9561F, 0.0F));
        PartDefinition metal04a = partdefinition.addOrReplaceChild("metal04a", CubeListBuilder.create().texOffs(6, 21).addBox(-0.5F, -9.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.2F, 19.7F, -0.3F, 0.0F, 0.0F, -0.0456F));
        PartDefinition metal04b = metal04a.addOrReplaceChild("metal04b", CubeListBuilder.create().texOffs(6, 21).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.8F, 0.0F, -0.1367F, 0.0F, 0.182F));
        PartDefinition metal04c = metal04b.addOrReplaceChild("metal04c", CubeListBuilder.create().texOffs(6, 21).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.8F, 0.0F, -0.1367F, 0.0F, 0.0911F));
        PartDefinition metal04d = metal04c.addOrReplaceChild("metal04d", CubeListBuilder.create().texOffs(6, 21).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -2.8F, 0.0F, 0.2731F, 0.0F, -0.182F));
        PartDefinition metal04j = metal04c.addOrReplaceChild("metal04j", CubeListBuilder.create().texOffs(0, 25).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3F, 0.5F, 0.0F, 0.182F, 0.0F, -0.2276F));
        PartDefinition metal04e = metal04b.addOrReplaceChild("metal04e", CubeListBuilder.create().texOffs(4, 24).addBox(-0.5F, -0.5F, -1.2F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.2F, -0.3F, 0.3187F, 0.0911F, 0.0F));
        PartDefinition metal04f = metal04e.addOrReplaceChild("metal04f", CubeListBuilder.create().texOffs(4, 24).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.0456F, 0.4098F, 0.0F));
        PartDefinition metal04g = metal04a.addOrReplaceChild("metal04g", CubeListBuilder.create().texOffs(6, 21).addBox(-1.0F, -1.3F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -6.5F, -0.2F, 0.0F, -0.9105F, 0.0F));
        PartDefinition metal04h = metal04a.addOrReplaceChild("metal04h", CubeListBuilder.create().texOffs(6, 21).addBox(-1.0F, -0.8F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -4.7F, -0.2F, 0.0F, -0.9105F, 0.0F));
        PartDefinition metal04i = metal04a.addOrReplaceChild("metal04i", CubeListBuilder.create().texOffs(6, 21).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1F, -2.5F, -0.2F, 0.0F, -0.9105F, 0.0F));
        PartDefinition metal05a = partdefinition.addOrReplaceChild("metal05a", CubeListBuilder.create().texOffs(3, 21).addBox(-0.5F, -9.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.2F, 19.7F, -0.3F, 0.0F, 0.0F, 0.0456F));
        PartDefinition metal05b = metal05a.addOrReplaceChild("metal05b", CubeListBuilder.create().texOffs(3, 21).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.8F, 0.0F, -0.0456F, 0.0F, -0.2276F));
        PartDefinition metal05c = metal05b.addOrReplaceChild("metal05c", CubeListBuilder.create().texOffs(3, 21).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.8F, 0.0F, 0.182F, 0.0F, -0.0911F));
        PartDefinition metal05d = metal05c.addOrReplaceChild("metal05d", CubeListBuilder.create().texOffs(3, 21).addBox(-0.5F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -2.8F, 0.0F, 0.0F, 0.0F, 0.182F));
        PartDefinition metal05e = metal05b.addOrReplaceChild("metal05e", CubeListBuilder.create().texOffs(3, 24).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.2F, -0.3F, 0.2276F, -0.0911F, 0.0F));
        PartDefinition metal05f = metal05e.addOrReplaceChild("metal05f", CubeListBuilder.create().texOffs(1, 24).addBox(-0.5F, -0.5F, -1.9F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, 0.0F, -0.8F, 0.0456F, -0.6374F, 0.182F));
        PartDefinition metal05g = metal05a.addOrReplaceChild("metal05g", CubeListBuilder.create().texOffs(3, 21).addBox(-0.6F, -0.5F, -1.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.6F, -0.1F, 0.0F, -0.5918F, 0.0F));
        PartDefinition metal05h = metal05a.addOrReplaceChild("metal05h", CubeListBuilder.create().texOffs(3, 21).addBox(-0.6F, -0.5F, -1.1F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.4F, -0.1F, 0.0F, -0.5918F, 0.0F));
        PartDefinition metal06a = partdefinition.addOrReplaceChild("metal06a", CubeListBuilder.create().texOffs(6, 21).addBox(-0.5F, -9.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4F, 19.7F, -1.7F, -0.0456F, 0.0F, 0.0F));
        PartDefinition metal06b = metal06a.addOrReplaceChild("metal06b", CubeListBuilder.create().texOffs(6, 21).addBox(-0.5F, -2.6F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.9F, 0.0F, 0.1367F, 0.0F, 0.1367F));
        PartDefinition metal06c = metal06a.addOrReplaceChild("metal06c", CubeListBuilder.create().texOffs(6, 21).addBox(-0.6F, -0.5F, -1.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, -7.2F, -0.1F, 0.0F, 1.0472F, 0.0F));
        PartDefinition metal06d = metal06a.addOrReplaceChild("metal06d", CubeListBuilder.create().texOffs(6, 21).addBox(-0.6F, -0.5F, -1.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, -5.8F, -0.1F, 0.0F, 1.0472F, 0.0F));
        PartDefinition metal06e = metal06a.addOrReplaceChild("metal06e", CubeListBuilder.create().texOffs(6, 21).addBox(-0.6F, -0.5F, -1.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, -2.9F, -0.1F, 0.0F, 1.0472F, 0.0F));
        PartDefinition metal07a = partdefinition.addOrReplaceChild("metal07a", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, -9.0F, -0.5F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4F, 19.7F, -1.7F, -0.0456F, 0.0F, 0.0F));
        PartDefinition metal07b = metal07a.addOrReplaceChild("metal07b", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, -2.6F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.9F, 0.0F, 0.1367F, 0.0F, -0.1367F));
        PartDefinition metal07c = metal07a.addOrReplaceChild("metal07c", CubeListBuilder.create().texOffs(0, 21).addBox(-0.6F, -0.5F, -1.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3F, -6.5F, -0.1F, 0.0F, -1.0472F, 0.0F));
        PartDefinition metal07d = metal07a.addOrReplaceChild("metal07d", CubeListBuilder.create().texOffs(0, 21).addBox(-0.6F, -0.5F, -1.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3F, -4.2F, -0.1F, 0.0F, -1.0472F, 0.0F));
        PartDefinition metal07e = metal07a.addOrReplaceChild("metal07e", CubeListBuilder.create().texOffs(0, 21).addBox(-0.6F, -0.5F, -1.1F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3F, -1.9F, -0.1F, 0.0F, -1.0472F, 0.0F));
        PartDefinition metal08a = partdefinition.addOrReplaceChild("metal08a", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, -8.0F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 19.7F, -2.3F, -0.0456F, 0.0F, 0.0F));
        PartDefinition metal08b = metal08a.addOrReplaceChild("metal08b", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.8F, 0.0F, 0.1367F, 0.0F, 0.0F));
        PartDefinition metal08c = metal08b.addOrReplaceChild("metal08c", CubeListBuilder.create().texOffs(0, 23).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.8F, -0.2F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.bowlBase, this.bowlWall01, this.bowlStand01, this.bowlStand02, this.bowlStand03, this.bowlStandRivit, this.metal01a, this.metal02a, this.metal03a, this.metal04a, this.metal05a, this.metal06a, this.metal07a, this.metal08a).forEach((modelRenderer) -> {
            modelRenderer.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        });
    }

    @Override
    public void setupAnim(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

}
