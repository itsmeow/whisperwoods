package dev.itsmeow.whisperwoods.client.init;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
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
import dev.itsmeow.whisperwoods.client.renderer.entity.layer.LayerEyesSwitching;
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
import dev.itsmeow.whisperwoods.entity.projectile.EntityHirschgeistFireball;
import dev.itsmeow.whisperwoods.init.*;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ClientLifecycleHandler {

    public static RenderFactory R = IMDLibClient.getRenderRegistry(WhisperwoodsMod.MODID);

    public static void clientInit() {
        ClientReloadShadersEvent.EVENT.register((resourceManager, shadersSink) -> {
            try {
                shadersSink.registerShader(new ShaderInstance(resourceManager,"ww_rendertype_eyes_custom", DefaultVertexFormat.NEW_ENTITY), shaderInstance -> RenderTypeAddition.eyesCustomShader = shaderInstance);
            } catch (IOException e) {
                WhisperwoodsMod.LOGGER.error(e);
            }
        });
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
        .layer(t -> new LayerEyesSwitching<>(t, EntityHidebehind::getOpen, ModResources.HIDEBEHIND_OPEN_GLOW, ModResources.HIDEBEHIND_GLOW)));

        RenderFactory.addRender(ModEntities.WISP::getEntityType, RenderWisp::new);
        RenderFactory.addRender(ModEntities.HIRSCHGEIST::getEntityType, RenderHirschgeist::new);

        RenderFactory.addRender(ModEntities.PROJECTILE_HIRSCHGEIST_FIREBALL::get, t -> new EntityRenderer<EntityHirschgeistFireball>(t) {

            @Override
            public void render(EntityHirschgeistFireball entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
                if(!Minecraft.getInstance().isPaused()) {
                    if(System.nanoTime() - entity.lastSpawn >= 10_000_000L) {
                        entity.lastSpawn = System.nanoTime();
                        for(int j = 0; j < 5; j++) {
                            double xO = (entity.level().getRandom().nextFloat() * 2F - 1F);
                            double yO = (entity.level().getRandom().nextFloat() * 2F - 1F);
                            double zO = (entity.level().getRandom().nextFloat() * 2F - 1F);
                            entity.level().addParticle(ModParticles.SOUL_FLAME.get(),
                                    entity.getX() + xO,
                                    entity.getY() + yO,
                                    entity.getZ() + zO, 0, 0.005F, 0);
                        }
                    }
                }
            }

            @Override
            public ResourceLocation getTextureLocation(EntityHirschgeistFireball entity) {
                return null;
            }
        });

        R.addRender(ModEntities.ZOTZPYRE::getEntityType, 0.4F, r -> r.tVariant().mSingle(ModelZotzpyre::new, "zotzpyre").layer(t -> new LayerEyesSwitching<>(t, e -> "6".equals(e.getVariantNameOrEmpty()), ModResources.ZOTZPYRE_6_EYES, ModResources.ZOTZPYRE_EYES)));
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
        register.accept(ModParticles.SOUL_FLAME.get(), FlameParticle.FlameFactory::new);
    }

    public static class RenderTypeAddition extends RenderType {

        private static ShaderInstance eyesCustomShader;
        private static final ShaderStateShard RENDERTYPE_WW_EYES_ENTITY_CUTOUT_NO_CULL_DEPTH_MASK_OFF = new ShaderStateShard(() -> eyesCustomShader);
        private static final Function<ResourceLocation, RenderType> WW_EYES_ENTITY_CUTOUT_NO_CULL_DEPTH_MASK_OFF;
        static {
            WW_EYES_ENTITY_CUTOUT_NO_CULL_DEPTH_MASK_OFF = Util.memoize((resourceLocation) -> create("ww_eyes_entity_cutout_no_cull_depth_mask_off", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_WW_EYES_ENTITY_CUTOUT_NO_CULL_DEPTH_MASK_OFF).setTextureState(new TextureStateShard(resourceLocation, false, false)).setCullState(NO_CULL).setTransparencyState(ADDITIVE_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setLightmapState(NO_LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false)));
        }

        private RenderTypeAddition() {
            super(null, null, VertexFormat.Mode.QUADS, 0, false, false, null, null);
        }

        public static RenderType getEyesEntityCutoutNoCullDepthMaskOff(ResourceLocation l) {
            return WW_EYES_ENTITY_CUTOUT_NO_CULL_DEPTH_MASK_OFF.apply(l);
        }
    }

}
