package mod.emt.thaumictinkerer.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;

public class ItemFelineCharm extends AbstractItemAddition implements IBauble {
    public ItemFelineCharm() {
        super("feline_charm");
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {
        entity.playSound(SoundEvents.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
        entity.playSound(SoundEvents.ENTITY_CAT_AMBIENT, 1.0F, 1.0F);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.CHARM;
    }
}
