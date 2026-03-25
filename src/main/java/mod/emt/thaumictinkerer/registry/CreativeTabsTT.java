package mod.emt.thaumictinkerer.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.items.ItemsTC;

public class CreativeTabsTT extends CreativeTabs {
    public CreativeTabsTT(int length, String name) {
        super(length, name);
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    public @NotNull ItemStack createIcon() {
        return new ItemStack(ItemsTC.casterBasic);
    }
}
