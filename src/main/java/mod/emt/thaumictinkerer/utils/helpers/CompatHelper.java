package mod.emt.thaumictinkerer.utils.helpers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.utils.ToolHelper;
import thaumcraft.api.aspects.Aspect;

public class CompatHelper {
    public static final boolean isTinkersConstructLoaded = Loader.isModLoaded("tconstruct");

    //##########################################################
    // Tinkers' Construct

    public static boolean isTinkersConstructTool(ItemStack stack) {
        return isTinkersConstructLoaded && !stack.isEmpty() && stack.getItem() instanceof ToolCore;
    }

    public static boolean isTinkersToolRepairable(ItemStack stack) {
        return isTinkersConstructTool(stack) && ToolHelper.getMaxDurability(stack) != ToolHelper.getCurrentDurability(stack);
    }

    public static Aspect getRepairAspect(ItemStack stack) {
        if(isTinkersConstructTool(stack)) {
            ToolCore toolCore = (ToolCore) stack.getItem();
            if(toolCore.hasCategory(Category.WEAPON) || toolCore.hasCategory(Category.LAUNCHER)) {
                return Aspect.AVERSION;
            } else if(toolCore.hasCategory(Category.TOOL)) {
                return Aspect.TOOL;
            }
        }
        return Aspect.CRAFT;
    }

    public static int getTinkersToolDamage(ItemStack stack) {
        return isTinkersConstructTool(stack) ? ToolHelper.getMaxDurability(stack) - ToolHelper.getCurrentDurability(stack) : 0;
    }

    public static void repairTinkersTool(ItemStack stack, int repairAmount) {
        if(isTinkersConstructTool(stack)) {
            ToolHelper.repairTool(stack, repairAmount);
        }
    }
}
