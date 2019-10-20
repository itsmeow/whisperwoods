package its_meow.whisperwoods.client.renderer.entity;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import its_meow.whisperwoods.client.renderer.entity.model.ModelHidebehind;
import its_meow.whisperwoods.entity.EntityHidebehind;
import its_meow.whisperwoods.init.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderHidebehind extends LivingRenderer<EntityHidebehind, ModelHidebehind>{

    public RenderHidebehind(EntityRendererManager mgr) {
        super(mgr, new ModelHidebehind(), 0.75F);
        this.addLayer(new HidebehindEyesLayer(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHidebehind entity) {
        return ModTextures.hidebehind_coniferous_open;
    }

    protected boolean canRenderName(EntityHidebehind entity) {
        return entity.hasCustomName() && super.canRenderName(entity);
    }
    
    public static class HidebehindEyesLayer extends LayerRenderer<EntityHidebehind, ModelHidebehind> {

        public HidebehindEyesLayer(IEntityRenderer<EntityHidebehind, ModelHidebehind> p_i50921_1_) {
           super(p_i50921_1_);
        }

        public void render(EntityHidebehind entityIn, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
           this.bindTexture(ModTextures.hidebehind_open_glow);
           GlStateManager.enableBlend();
           GlStateManager.disableAlphaTest();
           GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
           GlStateManager.depthMask(false);

           int i = 240;
           int j = i % 65536;
           int k = i / 65536;
           GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float)j, (float)k);
           GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
           GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
           gamerenderer.setupFogColor(true);
           this.getEntityModel().render(entityIn, p_212842_2_, p_212842_3_, p_212842_5_, p_212842_6_, p_212842_7_, p_212842_8_);
           gamerenderer.setupFogColor(false);
           i = entityIn.getBrightnessForRender();
           j = i % 65536;
           k = i / 65536;
           GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float)j, (float)k);
           this.func_215334_a(entityIn);
           GlStateManager.depthMask(true);
           GlStateManager.disableBlend();
           GlStateManager.enableAlphaTest();
        }

        public boolean shouldCombineTextures() {
           return false;
        }
     }

}
