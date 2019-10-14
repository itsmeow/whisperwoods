package its_meow.whisperwoods.client.renderer.entity;

import its_meow.whisperwoods.client.renderer.entity.model.ModelHidebehind;
import its_meow.whisperwoods.entity.EntityHidebehind;
import its_meow.whisperwoods.init.ModTextures;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderHidebehind extends LivingRenderer<EntityHidebehind, ModelHidebehind>{

    public RenderHidebehind(EntityRendererManager mgr) {
        super(mgr, new ModelHidebehind(), 0.75F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHidebehind entity) {
        return ModTextures.hidebehind_black;
    }

    protected boolean canRenderName(EntityHidebehind entity) {
        return entity.hasCustomName() && super.canRenderName(entity);
    }

}
