package dev.itsmeow.whisperwoods.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class ItemBlockModeledArmor extends ItemBlockArmor {

    public ItemBlockModeledArmor(Block block, ArmorMaterial material, EquipmentSlot slot, Item.Properties properties) {
        super(block, material, slot, properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A defaultModel) {
        if(itemStack != null) {
            if(itemStack.getItem() instanceof ArmorItem && defaultModel != null && armorSlot != null) {
                A armorModel = this.getBaseModelInstance();
                armorModel = displays(armorModel, armorSlot);

                armorModel.crouching = defaultModel.crouching;
                armorModel.riding = defaultModel.riding;
                armorModel.young = defaultModel.young;
                armorModel.rightArmPose = defaultModel.rightArmPose;
                armorModel.leftArmPose = defaultModel.leftArmPose;

                return armorModel;
            }
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract <A extends HumanoidModel<?>> A getBaseModelInstance();

    @OnlyIn(Dist.CLIENT)
    protected abstract <A extends HumanoidModel<?>> A displays(A armorModel, EquipmentSlot slot);

}
