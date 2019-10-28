package its_meow.whisperwoods.client.renderer.entity;

import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.platform.GlStateManager;

import its_meow.whisperwoods.entity.EntityWisp;
import its_meow.whisperwoods.particle.WispParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.entity.model.HumanoidHeadModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderWisp extends LivingRenderer<EntityWisp, EntityModel<EntityWisp>> {

    GenericHeadModel head = new HumanoidHeadModel();

    public RenderWisp(EntityRendererManager mgr) {
        super(mgr, null, 0F);
    }

    @Override
    public void doRender(EntityWisp entity, double x, double y, double z, float entityYaw, float partialTicks) {
        int color = getColorVariant(entity.getDataManager().get(EntityWisp.COLOR_VARIANT));
        float r = (color >> 16) & 0xFF;
        float g = (color >> 8) & 0xFF;
        float b = color & 0xFF;
        float scale = 1;
        if(entity.getDataManager().get(EntityWisp.ATTACK_STATE) > 0) {
            UUID target = UUID.fromString(entity.getDataManager().get(EntityWisp.TARGET));
            String name = entity.getDataManager().get(EntityWisp.TARGET_NAME);
            if(target != null && name != null && !name.equals("")) {
                // Bind skin texture
                {   
                    GameProfile profile = new GameProfile(target, name);
                    profile = SkullTileEntity.updateGameProfile(profile);
                    Map<Type, MinecraftProfileTexture> map = Minecraft.getInstance().getSkinManager().loadSkinFromCache(profile);
                    ResourceLocation skin;
                    if(map.containsKey(Type.SKIN)) {
                        skin = Minecraft.getInstance().getSkinManager().loadSkin(map.get(Type.SKIN), Type.SKIN);
                    } else {
                        skin = DefaultPlayerSkin.getDefaultSkin(target);
                        Minecraft.getInstance().getSkinManager().loadProfileTextures(profile, null, false);
                    }
                    this.bindTexture(skin);
                }
                GlStateManager.pushMatrix();
                {
                    GlStateManager.disableCull();
                    GlStateManager.translated(x, y + 0.8F, z);
                    GlStateManager.enableRescaleNormal();
                    GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
                    GlStateManager.enableAlphaTest();
                    GlStateManager.enableDepthTest();
                    GlStateManager.setProfile(GlStateManager.Profile.PLAYER_SKIN);
                    GlStateManager.color4f(r / 255F, g / 255F, b / 255F, 0.6F);

                    head.func_217104_a(0.0F, 0.0F, 0.0F, 180F + entity.rotationYawHead, 0.0F, 0.0625F);
                }
                GlStateManager.popMatrix();
                this.renderEntityName(entity, x, y + 1.2F, z, name + "'s soul", 32F);
            }
        }
        if(!Minecraft.getInstance().isGamePaused()) {
            if(System.nanoTime() - entity.lastSpawn >= 100_000_000L) {
                entity.lastSpawn = System.nanoTime();
                if(entity.getDataManager().get(EntityWisp.ATTACK_STATE) > 0) {
                    scale = ((400F - ((float) entity.getDataManager().get(EntityWisp.ATTACK_STATE))) / 400F) * 5F;
                } else {
                    scale = entity.getDataManager().get(EntityWisp.PASSIVE_SCALE);
                }
                // spawn bottom particles
                for(int i = 0; i < 2; i++) {
                    double xO = (entity.getRNG().nextFloat() * 2F - 1F) / 3.5;
                    double yO = (entity.getRNG().nextFloat() * 2F - 1F) / 6 + 0.8F;
                    double zO = (entity.getRNG().nextFloat() * 2F - 1F) / 3.5;
                    entity.world.addParticle(new WispParticleData(r, g, b, scale),
                    entity.posX + xO,
                    entity.posY + yO,
                    entity.posZ + zO, 0, 0.005F, 0);
                }
                // spawn upper particle
                entity.world.addParticle(new WispParticleData(r, g, b, scale),
                entity.posX + (entity.getRNG().nextFloat() * 2F - 1F) / 10,
                entity.posY + (entity.getRNG().nextFloat() * 2F - 1F) / 5 + 1.1F,
                entity.posZ + (entity.getRNG().nextFloat() * 2F - 1F) / 10, 0, 0.02F, 0);
            }
        }
    }

    private static int getColorVariant(int variant) {
        switch(variant) {
        case 1: return 0x00efef;
        case 2: return 0xf28900;
        case 3: return 0xffc61c;
        case 4: return 0x2bff39;
        case 5: return 0xca27ea;
        default:return 0xff0000;
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWisp entity) {
        return null;
    }

    protected boolean canRenderName(EntityWisp entity) {
        return false;
    }
}
