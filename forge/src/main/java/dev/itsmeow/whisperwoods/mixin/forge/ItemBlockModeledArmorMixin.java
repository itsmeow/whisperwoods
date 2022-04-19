package dev.itsmeow.whisperwoods.mixin.forge;

import dev.itsmeow.whisperwoods.item.ItemBlockModeledArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemBlockModeledArmor.class)
public abstract class ItemBlockModeledArmorMixin extends ArmorItem {

    public ItemBlockModeledArmorMixin(ArmorMaterial arg, EquipmentSlot arg2, Properties arg3) {
        super(arg, arg2, arg3);
    }

    @Shadow
    public abstract <T extends LivingEntity, A extends HumanoidModel<T>> A getArmorModel(T entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A defaultModel);

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> defaultModel) {
                return ItemBlockModeledArmorMixin.this.getArmorModel(entityLiving, itemStack, armorSlot, (HumanoidModel<? super LivingEntity>) defaultModel);
            }
        });
    }

}
