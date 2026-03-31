package mod.emt.thaumictinkerer.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import thaumcraft.api.items.ItemsTC;

public class ModMaterialsTT {
    public static final Item.ToolMaterial TOOL_CONDOR = EnumHelper.addToolMaterial("TT_CONDOR", 4, 3000, 10.0F, 4.0F, 20);

    public static void setRepairItems() {
        TOOL_CONDOR.setRepairItem(new ItemStack(ItemsTC.ingots, 1, 0));
    }
}
