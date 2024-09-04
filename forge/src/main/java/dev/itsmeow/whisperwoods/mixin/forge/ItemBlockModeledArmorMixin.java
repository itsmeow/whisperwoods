package dev.itsmeow.whisperwoods.mixin.forge;

import dev.itsmeow.whisperwoods.item.ItemBlockModeledArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(ItemBlockModeledArmor.class)
public abstract class ItemBlockModeledArmorMixin extends ArmorItem {

    public ItemBlockModeledArmorMixin(ArmorMaterial arg, ArmorItem.Type arg2, Properties arg3) {
        super(arg, arg2, arg3);
    }

    @Shadow
    public abstract <T extends LivingEntity, A extends HumanoidModel<T>> A getArmorModel(T entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A defaultModel);

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return ItemBlockModeledArmorMixin.this.getArmorModel(livingEntity, itemStack, equipmentSlot, (HumanoidModel<? super LivingEntity>) original);
            }
        });
    }

}
