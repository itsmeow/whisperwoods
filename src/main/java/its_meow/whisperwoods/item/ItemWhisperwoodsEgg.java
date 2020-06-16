package its_meow.whisperwoods.item;

import dev.itsmeow.imdlib.entity.util.EntityTypeContainer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ItemWhisperwoodsEgg extends SpawnEggItem {

    private final EntityType<?> type;

    public ItemWhisperwoodsEgg(EntityTypeContainer<?> container) {
        super(container.entityType, container.eggColorSolid, container.eggColorSpot, new Properties().group(ItemGroup.MISC));
        this.type = container.entityType;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if (type != null) {
            return "entity.whisperwoods." + this.type.getRegistryName().getPath();
        }
        return "item.whisperwoods.emptyegg";
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return new TranslationTextComponent("misc.whisperwoods.eggorder",
        new TranslationTextComponent(this.getTranslationKey(stack)));
    }

    @Override
    public EntityType<?> getType(CompoundNBT tag) {
        return this.type;
    }

    @Override
    public boolean hasType(CompoundNBT tag, EntityType<?> type) {
        return type == this.type;
    }
}