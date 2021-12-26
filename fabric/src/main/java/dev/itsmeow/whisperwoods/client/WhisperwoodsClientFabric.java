package dev.itsmeow.whisperwoods.client;

import dev.itsmeow.whisperwoods.WhisperwoodsMod;
import dev.itsmeow.whisperwoods.client.init.ClientLifecycleHandler;
import dev.itsmeow.whisperwoods.init.ModItems;
import dev.itsmeow.whisperwoods.item.ItemBlockHirschgeistSkull;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.inventory.InventoryMenu;

public class WhisperwoodsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleHandler.clientInit();
        ClientSpriteRegistryCallback.event(InventoryMenu.BLOCK_ATLAS).register(((atlasTexture, registry) -> {
            registry.register(new ResourceLocation(WhisperwoodsMod.MODID, "particle/flame"));
            for(int i = 0; i < 6; i++) {
                registry.register(new ResourceLocation(WhisperwoodsMod.MODID, "particle/wisp_" + i));
            }
        }));
        ItemBlockHirschgeistSkull armor = ModItems.HIRSCHGEIST_SKULL.get();
        ArmorRenderingRegistry.registerModel((entity, stack, slot, defaultModel) -> {
            HumanoidModel<LivingEntity> model = armor.getArmorModel(entity, stack, slot, defaultModel);
            if (!Minecraft.getInstance().isPaused()) {
                float g = Minecraft.getInstance().getFrameTime();
                float h = Mth.rotLerp(g, entity.yBodyRotO, entity.yBodyRot);
                float j = Mth.rotLerp(g, entity.yHeadRotO, entity.yHeadRot);
                float k = j - h;
                float o;
                if (entity.isPassenger() && entity.getVehicle() instanceof LivingEntity) {
                    LivingEntity livingEntity2 = (LivingEntity) entity.getVehicle();
                    h = Mth.rotLerp(g, livingEntity2.yBodyRotO, livingEntity2.yBodyRot);
                    k = j - h;
                    o = Mth.wrapDegrees(k);
                    if (o < -85.0F) {
                        o = -85.0F;
                    }

                    if (o >= 85.0F) {
                        o = 85.0F;
                    }

                    h = j - o;
                    if (o * o > 2500.0F) {
                        h += o * 0.2F;
                    }

                    k = j - h;
                }

                float m = Mth.lerp(g, entity.xRotO, entity.xRot);
                float p;
                if (entity.getPose() == Pose.SLEEPING) {
                    Direction direction = entity.getBedOrientation();
                    if (direction != null) {
                        p = entity.getEyeHeight(Pose.STANDING) - 0.1F;
                    }
                }

                o = (float) entity.tickCount + g;
                p = 0.0F;
                float q = 0.0F;
                if (!entity.isPassenger() && entity.isAlive()) {
                    p = Mth.lerp(g, entity.animationSpeedOld, entity.animationSpeed);
                    q = entity.animationPosition - entity.animationSpeed * (1.0F - g);
                    if (entity.isBaby()) {
                        q *= 3.0F;
                    }

                    if (p > 1.0F) {
                        p = 1.0F;
                    }
                }
                model.setupAnim(entity, q, p, o, k, m);
            }
            return model;
        }, armor);
        ArmorRenderingRegistry.registerSimpleTexture(new ResourceLocation(WhisperwoodsMod.MODID, armor.getMaterial().getName()), armor);
    }
}

