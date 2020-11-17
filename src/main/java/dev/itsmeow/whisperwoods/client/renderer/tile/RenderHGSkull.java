package dev.itsmeow.whisperwoods.client.renderer.tile;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.itsmeow.imdlib.block.BlockAnimalSkull;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHGSkull;
import dev.itsmeow.whisperwoods.tileentity.TileEntityHGSkull;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class RenderHGSkull extends TileEntityRenderer<TileEntityHGSkull> {

    private static final ModelHGSkull MODEL = new ModelHGSkull();
    private static final ResourceLocation TEXTURE = new ResourceLocation(WhisperwoodsMod.MODID, "textures/entity/hirschgeist_01.png");

    public RenderHGSkull(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityHGSkull te, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState iblockstate = te.getBlockState();
        if(iblockstate == null || !(iblockstate.getBlock() instanceof BlockAnimalSkull)) {
            return;
        }
        Direction enumfacing = te.getDirection();
        enumfacing = enumfacing == null ? Direction.NORTH : enumfacing;
        float rotation = -enumfacing.getHorizontalAngle();
        rotation = (enumfacing == Direction.NORTH || enumfacing == Direction.SOUTH) ? enumfacing.getOpposite().getHorizontalAngle() : rotation;
        rotation = (enumfacing == Direction.UP) ? te.getTopRotation() : rotation;
        this.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, enumfacing, rotation);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, int packedOverlayIn, @Nullable Direction facing, float skullRotation) {
        matrixStackIn.push();
        translateHead(matrixStackIn, facing, 1.32F);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        float rotX = 0F;
        if(facing != null) {
            rotX = facing == Direction.UP ? 0.0F : 90F;
        }
        MODEL.setRotationAngles(null, skullRotation, rotX, 0.0F, 0.0F, 0.0F);
        MODEL.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(TEXTURE)), packedLightIn, packedOverlayIn, 1F, 1F, 1F, 1F);
        matrixStackIn.pop();

    }

    private static void translateHead(MatrixStack matrixStackIn, Direction face, float yOffset) {
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
