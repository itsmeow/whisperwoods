package dev.itsmeow.whisperwoods.client.renderer.entity;

import com.google.common.collect.ImmutableList;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelHirschgeist;
import dev.itsmeow.whisperwoods.entity.EntityHirschgeist;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class RenderHirschgeist extends LivingEntityRenderer<EntityHirschgeist, ModelHirschgeist> {

    private static ResourceLocation hg(String num) {
        return new ResourceLocation(WhisperwoodsMod.MODID, "textures/entity/hirschgeist_" + num + ".png");
    }

    public static final List<ResourceLocation> HG_TEXTURES = ImmutableList.of(hg("01"), hg("02"), hg("03"), hg("04"), hg("05"), hg("06"), hg("07"), hg("08"));

    public RenderHirschgeist(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelHirschgeist(ctx.bakeLayer(new ModelLayerLocation(new ResourceLocation(WhisperwoodsMod.MODID, "hirschgeist"), "main"))), 0F);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHirschgeist entity) {
        return entity.getFlameAnimationIndex() < HG_TEXTURES.size() && entity.getFlameAnimationIndex() >= 0 ? HG_TEXTURES.get(entity.getFlameAnimationIndex()) : null;
    }

}
