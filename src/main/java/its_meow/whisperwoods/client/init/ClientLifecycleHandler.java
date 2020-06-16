package its_meow.whisperwoods.client.init;

import org.apache.logging.log4j.LogManager;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import dev.itsmeow.imdlib.client.render.ImplRenderer;
import dev.itsmeow.imdlib.entity.util.IVariantTypes;
import its_meow.whisperwoods.WhisperwoodsMod;
import its_meow.whisperwoods.client.particle.WispParticle;
import its_meow.whisperwoods.client.renderer.entity.RenderWisp;
import its_meow.whisperwoods.client.renderer.entity.model.ModelHidebehind;
import its_meow.whisperwoods.client.renderer.entity.model.ModelMoth;
import its_meow.whisperwoods.client.renderer.tile.RenderTileGhostLight;
import its_meow.whisperwoods.entity.EntityHidebehind;
import its_meow.whisperwoods.entity.EntityHidebehind.HidebehindVariant;
import its_meow.whisperwoods.entity.EntityMoth;
import its_meow.whisperwoods.entity.EntityWisp;
import its_meow.whisperwoods.init.ModParticles;
import its_meow.whisperwoods.init.ModTextures;
import its_meow.whisperwoods.tileentity.TileEntityGhostLight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = WhisperwoodsMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientLifecycleHandler {

    public void clientSetup(FMLClientSetupEvent event) {

        RenderingRegistry.registerEntityRenderingHandler(EntityMoth.class, ImplRenderer.<EntityMoth, ModelMoth>factory(WhisperwoodsMod.MODID, 0.1F)
        .tMappedRaw(IVariantTypes::getVariantTexture)
        .mSingle(new ModelMoth())
        .preRender((e, f) -> {
            float s = e.getSize(Pose.STANDING).width;
            GlStateManager.scalef(s, s, s);
        }).done());

        RenderingRegistry.registerEntityRenderingHandler(EntityHidebehind.class, ImplRenderer.<EntityHidebehind, ModelHidebehind>factory(WhisperwoodsMod.MODID, 0.75F)
        .tMappedRaw(e -> {
            return e.getVariant().isPresent() ? (e.getVariant().get() instanceof HidebehindVariant ? ((HidebehindVariant) e.getVariant().get()).getHidebehindTexture(e) : null) : null;
        })
        .mSingle(new ModelHidebehind())
        .layer(mgr -> new LayerRenderer<EntityHidebehind, ModelHidebehind>(mgr) {
            @Override
            public void render(EntityHidebehind entityIn, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float p_212842_6_, float p_212842_7_, float p_212842_8_) {
                this.bindTexture(ModTextures.hidebehind_open_glow);
                GlStateManager.enableBlend();
                GlStateManager.disableAlphaTest();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                GlStateManager.depthMask(false);

                int i = 240;
                int j = i % 65536;
                int k = i / 65536;
                GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) j, (float) k);
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                GameRenderer gamerenderer = Minecraft.getInstance().gameRenderer;
                gamerenderer.setupFogColor(true);
                this.getEntityModel().render(entityIn, p_212842_2_, p_212842_3_, p_212842_5_, p_212842_6_, 23, p_212842_8_);
                gamerenderer.setupFogColor(false);
                i = entityIn.getBrightnessForRender();
                j = i % 65536;
                k = i / 65536;
                GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) j, (float) k);
                this.func_215334_a(entityIn);
                GlStateManager.depthMask(true);
                GlStateManager.disableBlend();
                GlStateManager.enableAlphaTest();
            }

            @Override
            public boolean shouldCombineTextures() {
                return false;
            }
        }).done());

        RenderingRegistry.registerEntityRenderingHandler(EntityWisp.class, RenderWisp::new);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGhostLight.class, new RenderTileGhostLight());
        LogManager.getLogger().info("Increasing wispiness of wisps...");
    }

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ModParticles.WISP, WispParticle.WispFactory::new);
    }

}
