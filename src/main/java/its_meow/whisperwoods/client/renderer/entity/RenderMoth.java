package its_meow.whisperwoods.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;

import its_meow.whisperwoods.client.renderer.entity.model.ModelMoth;
import its_meow.whisperwoods.entity.EntityMoth;
import its_meow.whisperwoods.init.ModTextures;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;

public class RenderMoth extends LivingRenderer<EntityMoth, ModelMoth>{

    public RenderMoth(EntityRendererManager mgr) {
        super(mgr, new ModelMoth(), 0.1F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMoth entity) {
        int type = entity.getTypeNumber();
        switch(type) {
        case 1: return ModTextures.moth_1;
        case 2: return ModTextures.moth_2;
        case 3: return ModTextures.moth_3;
        case 4: return ModTextures.moth_4;
        case 5: return ModTextures.moth_5;
        case 6: return ModTextures.moth_6;
        case 7: return ModTextures.moth_7;
        case 8: return ModTextures.moth_8;
        default: return ModTextures.moth_1;
        }
    }

    @Override
    protected void preRenderCallback(EntityMoth entitylivingbaseIn, float partialTickTime) {
        float s = entitylivingbaseIn.getSize(Pose.STANDING).width;
        GlStateManager.scalef(s, s, s);
    }

    protected boolean canRenderName(EntityMoth entity) {
        return entity.hasCustomName() && super.canRenderName(entity);
    }

}
