package dev.itsmeow.whisperwoods.client.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.itsmeow.imdlib.client.IMDLibClient;
import dev.itsmeow.imdlib.client.render.RenderFactory;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.block.BlockWispLantern;
import dev.itsmeow.whisperwoods.client.particle.FlameParticle;
import dev.itsmeow.whisperwoods.client.particle.WispParticle;
import dev.itsmeow.whisperwoods.client.renderer.entity.RenderHirschgeist;
import dev.itsmeow.whisperwoods.client.renderer.entity.RenderWisp;
import dev.itsmeow.whisperwoods.client.renderer.entity.layer.LayerEyes;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelHidebehind;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelMoth;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelZotzpyre;
import dev.itsmeow.whisperwoods.client.renderer.tile.RenderHGSkull;
import dev.itsmeow.whisperwoods.client.renderer.tile.RenderTileGhostLight;
import dev.itsmeow.whisperwoods.client.renderer.tile.RenderTileHandOfFate;
import dev.itsmeow.whisperwoods.entity.EntityHidebehind;
import dev.itsmeow.whisperwoods.init.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientLifecycleHandler {

    public static RenderFactory R = IMDLibClient.getRenderRegistry(WhisperwoodsMod.MODID);

    public static class RenderTypes extends RenderType {
        public RenderTypes() {
            super(null, null, 0, 0, false, false, null, null);
        }

        public static RenderType getEyesEntityCutoutNoCullDepthMaskOff(ResourceLocation locationIn) {
            RenderStateShard.TextureStateShard renderstate$texturestate = new RenderStateShard.TextureStateShard(locationIn, false, false);
            return create("eyes_entity_cutout_no_cull_depth_mask_off", DefaultVertexFormat.NEW_ENTITY, 7, 256, false, true, RenderType.CompositeState.builder().setTextureState(renderstate$texturestate).setCullState(NO_CULL).setTransparencyState(ADDITIVE_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setFogState(BLACK_FOG).setDiffuseLightingState(DIFFUSE_LIGHTING).setAlphaState(DEFAULT_ALPHA).setLightmapState(NO_LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false));
        }
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        R.addRender(ModEntities.MOTH.getEntityType(), 0.1F, r -> r
        .tVariant()
        .mSingle(new ModelMoth())
        .simpleScale(e -> e.getDimensions(Pose.STANDING).width));

        R.addRender(ModEntities.HIDEBEHIND.getEntityType(), 0.75F, r -> r
        .tVariant()
        .mSingle(new ModelHidebehind())
        .renderLayer((e, a, b, c, t) -> RenderType.entityTranslucent(t, true))
        .layer(mgr -> new RenderLayer<EntityHidebehind, EntityModel<EntityHidebehind>>(mgr) {
            @Override
            public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityHidebehind entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if(!entity.isInvisible()) {
                    matrixStackIn.pushPose();
                    this.getParentModel().prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
                    this.getParentModel().setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    this.getParentModel().renderToBuffer(matrixStackIn, bufferIn.getBuffer(entity.getOpen() ? RenderTypes.getEyesEntityCutoutNoCullDepthMaskOff(ModResources.HIDEBEHIND_OPEN_GLOW) : RenderTypes.getEyesEntityCutoutNoCullDepthMaskOff(ModResources.HIDEBEHIND_GLOW)), 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                    matrixStackIn.popPose();
                }
            }
        }));

        RenderFactory.addRender(ModEntities.WISP.getEntityType(), RenderWisp::new);
        RenderFactory.addRender(ModEntities.HIRSCHGEIST.getEntityType(), RenderHirschgeist::new);

        R.addRender(ModEntities.ZOTZPYRE.getEntityType(), 0.4F, r -> r.tVariant().mSingle(new ModelZotzpyre<>()).layer(t -> new LayerEyes<>(t, ModResources.ZOTZPYRE_EYES)));

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.GHOST_LIGHT.get(), RenderTileGhostLight::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.HG_SKULL.get(), RenderHGSkull::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.HAND_OF_FATE.get(), RenderTileHandOfFate::new);

        ModBlocks.getBlocks().forEach(blockEntry -> {
            Block block = blockEntry.get();
            if(block instanceof BlockWispLantern) {
                ItemBlockRenderTypes.setRenderLayer(block, t -> t == RenderType.solid() || t == RenderType.translucent());
            }
        });
        LogManager.getLogger().info("Increasing wispiness of wisps...");
    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(ModParticles.WISP.get(), WispParticle.WispFactory::new);
        Minecraft.getInstance().particleEngine.register(ModParticles.FLAME.get(), FlameParticle.FlameFactory::new);
    }

}
