package mod.emt.thaumictinkerer.item;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.item.IItemAddition;
import mod.emt.thaumictinkerer.registry.ModEnchantsTT;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.Objects;

// Just here for testing infusion enchants
public class ItemSwordTest extends ItemSword implements IItemAddition {
    public ItemSwordTest(String unlocName) {
        super(ToolMaterial.DIAMOND);
        this.setRegistryName(ThaumicTinkerer.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            if(ModEnchantsTT.EDUCATIONAL != null) {
                EnumInfusionEnchantment.addInfusionEnchantment(stack, ModEnchantsTT.EDUCATIONAL, 3);
            }
            items.add(stack);
        }
    }
}
