package dev.itsmeow.whisperwoods.client.init;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.itsmeow.imdlib.client.IMDLibClient;
import dev.itsmeow.imdlib.client.render.RenderFactory;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.client.particle.FlameParticle;
import dev.itsmeow.whisperwoods.client.particle.WispParticle;
import dev.itsmeow.whisperwoods.client.renderer.entity.RenderHirschgeist;
import dev.itsmeow.whisperwoods.client.renderer.entity.RenderWisp;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelHidebehind;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelHirschgeist;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelMoth;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelZotzpyre;
import dev.itsmeow.whisperwoods.client.renderer.tile.RenderHGSkull;
import dev.itsmeow.whisperwoods.client.renderer.tile.RenderTileGhostLight;
import dev.itsmeow.whisperwoods.client.renderer.tile.RenderTileHandOfFate;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHGSkull;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHGSkullMask;
import dev.itsmeow.whisperwoods.client.renderer.tile.model.ModelHandOfFate;
import dev.itsmeow.whisperwoods.entity.EntityHidebehind;
import dev.itsmeow.whisperwoods.entity.EntityZotzpyre;
import dev.itsmeow.whisperwoods.init.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import org.apache.logging.log4j.LogManager;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ClientLifecycleHandler {

    public static RenderFactory R = IMDLibClient.getRenderRegistry(WhisperwoodsMod.MODID);

    public static void clientInit() {
        BlockEntityRendererRegistry.register(ModBlockEntities.GHOST_LIGHT.get(), RenderTileGhostLight::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.HG_SKULL.get(), RenderHGSkull::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.HAND_OF_FATE.get(), RenderTileHandOfFate::new);
        RenderTypeRegistry.register(RenderType.translucent(), ModBlocks.WISP_LANTERN_BLUE.get(), ModBlocks.WISP_LANTERN_GREEN.get(), ModBlocks.WISP_LANTERN_ORANGE.get(), ModBlocks.WISP_LANTERN_PURPLE.get(), ModBlocks.WISP_LANTERN_YELLOW.get());
        if(Platform.isFabric()) {
            ClientLifecycleHandler.registerEntityRenders();
        }
        LogManager.getLogger().info("Increasing wispiness of wisps...");
    }

    public static void registerEntityRenders() {
        R.addRender(ModEntities.MOTH::getEntityType, 0.1F, r -> r
        .tVariant()
        .mSingle(ModelMoth::new, "moth")
        .simpleScale(e -> e.getDimensions(Pose.STANDING).width));

        R.addRender(ModEntities.HIDEBEHIND::getEntityType, 0.75F, r -> r
        .tVariant()
        .mSingle(ModelHidebehind::new, "hidebehind")
        .renderLayer((e, a, b, c, t) -> RenderType.entityTranslucent(t, true))
        .layer(mgr -> new RenderLayer<EntityHidebehind, EntityModel<EntityHidebehind>>(mgr) {
            @Override
            public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityHidebehind entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if(!entity.isInvisible()) {
                    matrixStackIn.pushPose();
                    this.getParentModel().prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                    this.getParentModel().setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    this.getParentModel().renderToBuffer(matrixStackIn, bufferIn.getBuffer(entity.getOpen() ? RenderTypeAddition.getEyesEntityCutoutNoCullDepthMaskOff(ModResources.HIDEBEHIND_OPEN_GLOW) : RenderTypeAddition.getEyesEntityCutoutNoCullDepthMaskOff(ModResources.HIDEBEHIND_GLOW)), 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                    matrixStackIn.popPose();
                }
            }
        }));

        RenderFactory.addRender(ModEntities.WISP::getEntityType, RenderWisp::new);
        RenderFactory.addRender(ModEntities.HIRSCHGEIST::getEntityType, RenderHirschgeist::new);

        R.addRender(ModEntities.ZOTZPYRE::getEntityType, 0.4F, r -> r.tVariant().mSingle(ModelZotzpyre::new, "zotzpyre").layer(t -> new RenderLayer<EntityZotzpyre, EntityModel<EntityZotzpyre>>(t) {
            protected final RenderType GLOW_STATE_MAIN = ClientLifecycleHandler.RenderTypeAddition.getEyesEntityCutoutNoCullDepthMaskOff(ModResources.ZOTZPYRE_EYES);
            protected final RenderType GLOW_STATE_6 = ClientLifecycleHandler.RenderTypeAddition.getEyesEntityCutoutNoCullDepthMaskOff(ModResources.ZOTZPYRE_6_EYES);
            @Override
            public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityZotzpyre entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (!entity.isInvisible() && !entity.isBaby()) {
                    this.getParentModel().renderToBuffer(matrixStackIn, bufferIn.getBuffer(entity.getVariantNameOrEmpty().equals("6") ? GLOW_STATE_6 : GLOW_STATE_MAIN), 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }));
    }

    public static void layerDefinitions(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> b) {
        BiConsumer<String, LayerDefinition> r = (k, l) -> b.put(new ModelLayerLocation(new ResourceLocation(WhisperwoodsMod.MODID, k), "main"), l);
        r.accept("moth", ModelMoth.createBodyLayer());
        r.accept("hidebehind", ModelHidebehind.createBodyLayer());
        r.accept("hirschgeist", ModelHirschgeist.createBodyLayer());
        r.accept("zotzpyre", ModelZotzpyre.createBodyLayer());
        r.accept("hirschgeist_skull", ModelHGSkull.createBodyLayer());
        r.accept("hand_of_fate", ModelHandOfFate.createBodyLayer());
        r.accept("hirschgeist_skull_mask", ModelHGSkullMask.createBodyLayer());
    }

    public static void registerParticles(BiConsumer<ParticleType<?>, Function<SpriteSet, ParticleProvider<?>>> register) {
        register.accept(ModParticles.WISP.get(), WispParticle.WispFactory::new);
        register.accept(ModParticles.FLAME.get(), FlameParticle.FlameFactory::new);
    }

    public static class RenderTypeAddition extends RenderType {
        public RenderTypeAddition() {
            super(null, null, VertexFormat.Mode.QUADS, 0, false, false, null, null);
        }

        public static RenderType getEyesEntityCutoutNoCullDepthMaskOff(ResourceLocation locationIn) {
            return RenderType.eyes(locationIn);
            // TODO
            //RenderStateShard.TextureStateShard renderstate$texturestate = new RenderStateShard.TextureStateShard(locationIn, false, false);
            //return create("eyes_entity_cutout_no_cull_depth_mask_off", DefaultVertexFormat.NEW_ENTITY, 7, 256, false, true, RenderType.CompositeState.builder().setTextureState(renderstate$texturestate).setCullState(NO_CULL).setTransparencyState(ADDITIVE_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setFogState(BLACK_FOG).setDiffuseLightingState(DIFFUSE_LIGHTING).setAlphaState(DEFAULT_ALPHA).setLightmapState(NO_LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false));
        }
    }
}
