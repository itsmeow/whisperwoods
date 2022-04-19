package dev.itsmeow.whisperwoods.client.renderer.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.itsmeow.imdlib.block.AnimalSkullBlock;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.blockentity.HGSkullBlockEntity;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHGSkull;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class RenderHGSkull implements BlockEntityRenderer<HGSkullBlockEntity> {

    private final ModelHGSkull model;
    private static final ResourceLocation TEXTURE = new ResourceLocation(WhisperwoodsMod.MODID, "textures/entity/hirschgeist_bones.png");

    public RenderHGSkull(BlockEntityRendererProvider.Context ctx) {
        this.model = new ModelHGSkull(ctx.bakeLayer(new ModelLayerLocation(new ResourceLocation(WhisperwoodsMod.MODID, "hirschgeist_skull"), "main")));
    }

    @Override
    public void render(HGSkullBlockEntity te, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState iblockstate = te.getBlockState();
        if(iblockstate == null || !(iblockstate.getBlock() instanceof AnimalSkullBlock)) {
            return;
        }
        Direction enumfacing = te.getDirection();
        enumfacing = enumfacing == null ? Direction.NORTH : enumfacing;
        float rotation = -enumfacing.toYRot();
        rotation = (enumfacing == Direction.NORTH || enumfacing == Direction.SOUTH) ? enumfacing.getOpposite().toYRot() : rotation;
        rotation = (enumfacing == Direction.UP) ? te.getTopRotation() : rotation;
        this.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, enumfacing, rotation);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, int packedOverlayIn, Direction facing, float skullRotation) {
        matrixStackIn.pushPose();
        translateHead(matrixStackIn, facing, 1.32F);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        float rotX = 0F;
        if(facing != null) {
            rotX = facing == Direction.UP ? 0.0F : 90F;
        }
        model.setupAnim(null, skullRotation, rotX, 0.0F, 0.0F, 0.0F);
        model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), packedLightIn, packedOverlayIn, 1F, 1F, 1F, 1F);
        matrixStackIn.popPose();
    }

    private static void translateHead(PoseStack matrixStackIn, Direction face, float yOffset) {
        if(face == null) {
            matrixStackIn.translate(0.5F, 0.25F + yOffset + 0.3F, 1.0F);
            return;
        }
        switch(face) {
        case NORTH:
            matrixStackIn.translate(0.5F, 0.25F + yOffset + 0.3F, 1.0F);
            break;
        case EAST:
            matrixStackIn.translate(0F, 0.25F + yOffset + 0.3F, 0.5F);
            break;
        case SOUTH:
            matrixStackIn.translate(0.5F, 0.25F + yOffset + 0.3F, 0F);
            break;
        case WEST:
            matrixStackIn.translate(1F, 0.25F + yOffset + 0.3F, 0.5F);
            break;
        case UP:
            matrixStackIn.translate(0.5F, 0.18F + yOffset, 0.5F);
            break;
        default:
            matrixStackIn.translate(0F, 0.25F + yOffset + 0.3F, 0.5F);
            break;
        }
    }

}
