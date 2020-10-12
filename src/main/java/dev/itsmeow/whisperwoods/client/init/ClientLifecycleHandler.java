package dev.itsmeow.whisperwoods.client.init;

import org.apache.logging.log4j.LogManager;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.itsmeow.imdlib.client.IMDLibClient;
import dev.itsmeow.imdlib.client.render.RenderFactory;
import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.client.particle.FlameParticle;
import dev.itsmeow.whisperwoods.client.particle.WispParticle;
import dev.itsmeow.whisperwoods.client.renderer.entity.RenderHirschgeist;
import dev.itsmeow.whisperwoods.client.renderer.entity.RenderWisp;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelHidebehind;
import dev.itsmeow.whisperwoods.client.renderer.entity.model.ModelMoth;
import dev.itsmeow.whisperwoods.client.renderer.tile.RenderHGSkull;
import dev.itsmeow.whisperwoods.client.renderer.tile.RenderTileGhostLight;
import dev.itsmeow.whisperwoods.entity.EntityHidebehind;
import dev.itsmeow.whisperwoods.init.ModEntities;
import dev.itsmeow.whisperwoods.init.ModParticles;
import dev.itsmeow.whisperwoods.init.ModTileEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientLifecycleHandler {

    public static RenderFactory R = IMDLibClient.getRenderRegistry(WhisperwoodsMod.MODID);

    public static class RenderTypes extends RenderType {
        protected static final RenderState.WriteMaskState NO_WRITE = new RenderState.WriteMaskState(false, false);

        public RenderTypes() {
            super(null, null, 0, 0, false, false, null, null);
        }

        public static RenderType getEyesDepthMaskOff(ResourceLocation locationIn) {
            RenderState.TextureState renderstate$texturestate = new RenderState.TextureState(locationIn, false, false);
            return makeType("eyes_depth_mask_off", DefaultVertexFormats.ENTITY, 7, 256, false, true, RenderType.State.getBuilder().texture(renderstate$texturestate).cull(CULL_DISABLED).transparency(ADDITIVE_TRANSPARENCY).writeMask(COLOR_WRITE).fog(BLACK_FOG).build(false));
        }

        public static RenderType getEyesEntityCutoutNoCullDepthMaskOff(ResourceLocation locationIn) {
            RenderState.TextureState renderstate$texturestate = new RenderState.TextureState(locationIn, false, false);
            return makeType("eyes_entity_cutout_no_cull_depth_mask_off", DefaultVertexFormats.ENTITY, 7, 256, false, true, RenderType.State.getBuilder().texture(renderstate$texturestate).cull(CULL_DISABLED).transparency(ADDITIVE_TRANSPARENCY).writeMask(COLOR_WRITE).fog(BLACK_FOG).diffuseLighting(DIFFUSE_LIGHTING_ENABLED).alpha(DEFAULT_ALPHA).lightmap(LIGHTMAP_ENABLED).overlay(OVERLAY_ENABLED).build(false));
        }
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        R.addRender(ModEntities.MOTH.entityType, 0.1F, r -> r
        .tVariant()
        .mSingle(new ModelMoth())
        .simpleScale(e -> e.getSize(Pose.STANDING).width));

        R.addRender(ModEntities.HIDEBEHIND.entityType, 0.75F, r -> r
        .tVariant()
        .mSingle(new ModelHidebehind())
        .renderLayer((e, a, b, c, t) -> RenderType.getEntityTranslucent(t, true))
        .layer(mgr -> new LayerRenderer<EntityHidebehind, EntityModel<EntityHidebehind>>(mgr) {
            protected final ResourceLocation GLOW = new ResourceLocation(WhisperwoodsMod.MODID, "textures/entity/hidebehind_glow.png");
            protected final ResourceLocation GLOW_OPEN = new ResourceLocation(WhisperwoodsMod.MODID, "textures/entity/hidebehind_open_glow.png");
            @Override
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityHidebehind entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if(!entity.isInvisible()) {
                    matrixStackIn.push();
                    this.getEntityModel().setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
                    this.getEntityModel().setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    this.getEntityModel().render(matrixStackIn, bufferIn.getBuffer(entity.getOpen() ? RenderTypes.getEyesDepthMaskOff(GLOW_OPEN) : RenderTypes.getEyesDepthMaskOff(GLOW)), 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                    matrixStackIn.pop();
                }
            }
        }));

        RenderFactory.addRender(ModEntities.WISP.entityType, RenderWisp::new);
        RenderFactory.addRender(ModEntities.HIRSCHGEIST.entityType, RenderHirschgeist::new);

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.GHOST_LIGHT.get(), RenderTileGhostLight::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.HG_SKULL.get(), RenderHGSkull::new);
        LogManager.getLogger().info("Increasing wispiness of wisps...");
    }

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ModParticles.WISP.get(), WispParticle.WispFactory::new);
        Minecraft.getInstance().particles.registerFactory(ModParticles.FLAME.get(), FlameParticle.FlameFactory::new);
    }

}
