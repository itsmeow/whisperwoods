package dev.itsmeow.whisperwoods.client.renderer.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.itsmeow.whisperwoods.client.init.ClientLifecycleHandler;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

import java.util.function.Predicate;

public class LayerEyesSwitching<T extends Mob, A extends EntityModel<T>> extends RenderLayer<T, A> {

    protected final Predicate<T> condition;
    protected final RenderType trueType;
    protected final RenderType falseType;

    public LayerEyesSwitching(MobRenderer<T, A> baseRenderer, Predicate<T> condition, ResourceLocation textureTrue, ResourceLocation textureFalse) {
        super(baseRenderer);
        this.condition = condition;
        this.trueType = RenderType.eyes(textureTrue);
        this.falseType = RenderType.eyes(textureFalse);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible() && !entity.isBaby()) {
            this.getParentModel().renderToBuffer(matrixStackIn, bufferIn.getBuffer(condition.test(entity) ? trueType : falseType), 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
