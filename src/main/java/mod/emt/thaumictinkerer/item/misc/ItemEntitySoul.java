package mod.emt.thaumictinkerer.item.misc;

import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemEntitySoul extends AbstractItemAddition implements IProxy {
    private final IRarity rarity;

    public ItemEntitySoul(String unlocName, int uses, IRarity rarity) {
        super(unlocName);
        this.setMaxStackSize(1);
        this.setMaxDamage(uses - 1);
        this.setNoRepair();
        this.canRepair = false;
        this.rarity = rarity;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public int getRGBDurabilityForDisplay(@NotNull ItemStack stack) {
        return 1872873;
    }

    @Override
    public boolean hasContainerItem(@NotNull ItemStack stack) {
        return stack.getItemDamage() < stack.getMaxDamage();
    }

    @Override
    public @NotNull ItemStack getContainerItem(ItemStack stack) {
        stack.setItemDamage(stack.getItemDamage() + 1);
        return stack;
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return this.rarity;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        float remaining = (float) (stack.getMaxDamage() - stack.getItemDamage()) / (float) stack.getMaxDamage();
        if(remaining >= 0.7f) {
            tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.thaumictinkerer:entity_soul.resonant"));
        } else if(remaining >= 0.3f) {
            tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.thaumictinkerer:entity_soul.fading"));
        } else {
            tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.thaumictinkerer:entity_soul.fragile"));
        }
    }

    @SubscribeEvent
    public void onRepairChanged(AnvilUpdateEvent event) {
        //Because MC code is really bad.
        if(!event.getLeft().isEmpty() && event.getLeft().getItem() instanceof ItemEntitySoul) {
            event.setCanceled(true);
        }
    }

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean isEnabled() {
        return ConfigHandlerTT.necromancyTablet.enable;
    }
}
