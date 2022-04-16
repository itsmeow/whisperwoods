package dev.itsmeow.whisperwoods.client.renderer.tile.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelHirschgeist;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class ModelHGSkull extends EntityModel<Entity> {

    public ModelPart baseSkull;
    public ModelPart lAntler01;
    public ModelPart rAntler01;
    protected List<String> flameTips;

    public ModelHGSkull(ModelPart root) {
        this.baseSkull = root.getChild("baseSkull");
        this.lAntler01 = baseSkull.getChild("lAntler01");
        this.rAntler01 = baseSkull.getChild("rAntler01");
        this.flameTips = ImmutableList.of("rAntler06", "rAntler08b", "lAntler07b", "rAntler07b", "rAntler09c", "lAntler05b", "rAntler10b", "lAntler06", "rAntler05b", "lAntler09c", "lAntler08b", "lAntler10b");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition baseSkull = partdefinition.addOrReplaceChild("baseSkull", CubeListBuilder.create().texOffs(98, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition snout = baseSkull.addOrReplaceChild("snout", CubeListBuilder.create().texOffs(66, 13).addBox(-1.51F, -6.25F, -7.75F, 3.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2094F, 0.0F, 0.0F));
        PartDefinition lUpperJaw = baseSkull.addOrReplaceChild("lUpperJaw", CubeListBuilder.create().texOffs(86, 13).addBox(1.5F, -4.75F, -7.5F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1396F, 0.2269F, 0.0F));
        PartDefinition rUpperJaw = baseSkull.addOrReplaceChild("rUpperJaw", CubeListBuilder.create().texOffs(86, 13).mirror().addBox(-3.5F, -4.75F, -7.5F, 2.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1396F, -0.2269F, 0.0F));
        PartDefinition lowerJaw = baseSkull.addOrReplaceChild("lowerJaw", CubeListBuilder.create().texOffs(107, 14).addBox(-1.5F, -1.75F, -8.5F, 3.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));
        PartDefinition lAntler01 = baseSkull.addOrReplaceChild("lAntler01", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.99F, -2.95F, -0.9F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.75F, -5.5F, 0.75F, -0.0873F, 0.0F, 0.5236F));
        PartDefinition lAntler02 = lAntler01.addOrReplaceChild("lAntler02", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-1.24F, -3.95F, -0.85F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.25F, -2.5F, 0.0F, -0.0873F, 0.0F, 0.7854F));
        PartDefinition lAntler03 = lAntler02.addOrReplaceChild("lAntler03", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.99F, -4.65F, -0.59F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -3.75F, 0.0F, 0.0F, 0.0F, -0.5236F));
        PartDefinition lAntler04 = lAntler03.addOrReplaceChild("lAntler04", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.54F, -4.1F, -0.4F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.25F, 0.0F, 0.0F, 0.0F, -0.576F));
        PartDefinition lAntler05 = lAntler04.addOrReplaceChild("lAntler05", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.64F, -2.7F, -0.3F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
        PartDefinition lAntler05b = lAntler05.addOrReplaceChild("lAntler05b", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.59F, -1.65F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.0F, 0.0F, 0.5236F));
        PartDefinition lAntler06 = lAntler04.addOrReplaceChild("lAntler06", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.59F, -2.8F, -0.3F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(0.25F, -2.5F, 0.0F, 0.0F, 0.0F, 0.8727F));
        PartDefinition lAntler07 = lAntler03.addOrReplaceChild("lAntler07", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, -0.2618F, 0.0F, 0.6981F));
        PartDefinition lAntler07b = lAntler07.addOrReplaceChild("lAntler07b", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.5F, -1.75F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.5236F));
        PartDefinition lAntler08 = lAntler02.addOrReplaceChild("lAntler08", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.49F, -2.9F, -0.9F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.25F, -1.25F, 0.5F, 0.384F, 0.0F, 0.6109F));
        PartDefinition lAntler08b = lAntler08.addOrReplaceChild("lAntler08b", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.59F, -1.75F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.75F, -0.5F, 0.0F, 0.0F, -0.5236F));
        PartDefinition lAntler09 = lAntler02.addOrReplaceChild("lAntler09", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.49F, -3.0F, -0.4F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.25F, -2.75F, 0.0F, -0.3491F, 0.0F, -1.1345F));
        PartDefinition lAntler09b = lAntler09.addOrReplaceChild("lAntler09b", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.49F, -1.55F, -0.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.75F, 0.0F, 0.2269F, 0.0F, -0.1745F));
        PartDefinition lAntler09c = lAntler09b.addOrReplaceChild("lAntler09c", CubeListBuilder.create().texOffs(70, 26).mirror().addBox(-0.5F, -1.75F, -0.2F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.25F, 0.0F, 0.0F, 0.0F, 0.5236F));
        PartDefinition lAntler10 = lAntler02.addOrReplaceChild("lAntler10", CubeListBuilder.create().texOffs(70, 25).mirror().addBox(-0.69F, -2.05F, -0.4F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false), PartPose.offsetAndRotation(-0.25F, 0.0F, 0.0F, 0.3491F, 0.0F, -1.1345F));
        PartDefinition lAntler10b = lAntler10.addOrReplaceChild("lAntler10b", CubeListBuilder.create().texOffs(70, 26).mirror().addBox(-0.69F, -2.0F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, 0.0F, -0.5934F));
        PartDefinition rAntler01 = baseSkull.addOrReplaceChild("rAntler01", CubeListBuilder.create().texOffs(70, 25).addBox(-1.01F, -2.95F, -0.9F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.75F, -5.5F, 0.75F, -0.0873F, 0.0F, -0.5236F));
        PartDefinition rAntler02 = rAntler01.addOrReplaceChild("rAntler02", CubeListBuilder.create().texOffs(70, 25).addBox(-0.76F, -3.95F, -0.85F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.25F, -2.5F, 0.0F, -0.0873F, 0.0F, -0.7854F));
        PartDefinition rAntler03 = rAntler02.addOrReplaceChild("rAntler03", CubeListBuilder.create().texOffs(70, 25).addBox(-0.51F, -4.65F, -0.59F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.75F, 0.0F, 0.0F, 0.0F, 0.5236F));
        PartDefinition rAntler04 = rAntler03.addOrReplaceChild("rAntler04", CubeListBuilder.create().texOffs(70, 25).addBox(-0.46F, -4.1F, -0.4F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.25F, 0.0F, 0.0F, 0.0F, 0.576F));
        PartDefinition rAntler05 = rAntler04.addOrReplaceChild("rAntler05", CubeListBuilder.create().texOffs(70, 25).addBox(-0.36F, -2.7F, -0.3F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, -0.5236F));
        PartDefinition rAntler05b = rAntler05.addOrReplaceChild("rAntler05b", CubeListBuilder.create().texOffs(70, 25).addBox(-0.41F, -1.65F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.0F, 0.0F, -0.5236F));
        PartDefinition rAntler06 = rAntler04.addOrReplaceChild("rAntler06", CubeListBuilder.create().texOffs(70, 25).addBox(-0.41F, -2.8F, -0.3F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-0.25F, -2.5F, 0.0F, 0.0F, 0.0F, -0.8727F));
        PartDefinition rAntler07 = rAntler03.addOrReplaceChild("rAntler07", CubeListBuilder.create().texOffs(70, 25).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, -0.2618F, 0.0F, -0.6981F));
        PartDefinition lAntler07b2 = rAntler07.addOrReplaceChild("lAntler07b2", CubeListBuilder.create().texOffs(70, 25).addBox(-0.5F, -1.75F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
        PartDefinition rAntler08 = rAntler02.addOrReplaceChild("rAntler08", CubeListBuilder.create().texOffs(70, 25).addBox(-0.51F, -2.9F, -0.9F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, -1.25F, 0.5F, 0.384F, 0.0F, -0.6109F));
        PartDefinition rAntler08b = rAntler08.addOrReplaceChild("rAntler08b", CubeListBuilder.create().texOffs(70, 25).addBox(-0.41F, -1.75F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.15F)), PartPose.offsetAndRotation(0.0F, -2.75F, -0.5F, 0.0F, 0.0F, 0.5236F));
        PartDefinition rAntler09 = rAntler02.addOrReplaceChild("rAntler09", CubeListBuilder.create().texOffs(70, 25).addBox(-0.51F, -3.0F, -0.4F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -2.75F, 0.0F, -0.3491F, 0.0F, 1.1345F));
        PartDefinition rAntler09b = rAntler09.addOrReplaceChild("rAntler09b", CubeListBuilder.create().texOffs(70, 25).addBox(-0.51F, -1.55F, -0.3F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, -2.75F, 0.0F, 0.2269F, 0.0F, 0.1745F));
        PartDefinition rAntler09c = rAntler09b.addOrReplaceChild("rAntler09c", CubeListBuilder.create().texOffs(70, 26).addBox(-0.5F, -1.75F, -0.2F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -1.25F, 0.0F, 0.0F, 0.0F, -0.5236F));
        PartDefinition rAntler10 = rAntler02.addOrReplaceChild("rAntler10", CubeListBuilder.create().texOffs(70, 25).addBox(-0.31F, -2.05F, -0.4F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.25F, 0.0F, 0.0F, 0.3491F, 0.0F, 1.1345F));
        PartDefinition rAntler10b = rAntler10.addOrReplaceChild("rAntler10b", CubeListBuilder.create().texOffs(70, 26).addBox(-0.31F, -2.0F, -0.3F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, 0.0F, 0.5934F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.baseSkull.render(poseStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    public void renderFlames(PoseStack poseStack, MultiBufferSource multiBufferSource) {
        ModelPart.Visitor v = (PoseStack.Pose pose, String string, int i, ModelPart.Cube cube) -> {
            if(flameTips.stream().anyMatch(part -> string.endsWith(part))) {
                ModelHirschgeist.FlameRender.render(poseStack, multiBufferSource, cube, 1F);
            }
        };
        baseSkull.visit(poseStack, v);
    }

    @Override
    public void setupAnim(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.baseSkull.yRot = (float) Math.toRadians(limbSwing);
        this.baseSkull.xRot = (float) Math.toRadians(limbSwingAmount);
        this.baseSkull.x = 0F;
        this.baseSkull.z = 0F;
        if(this.baseSkull.xRot > 0F) {
            this.baseSkull.y = 17.0F;
        } else {
            this.baseSkull.y = 23.0F;
            int angle = Math.round(limbSwing);
            if(angle == 0) {
                this.baseSkull.z = 4F;
            } else if(angle == 90) {
                this.baseSkull.x = 4F;
            } else if(angle == 180) {
                this.baseSkull.z = -4F;
            } else if(angle == 270) {
                this.baseSkull.x = -4F;
            }
        }
    }

}
