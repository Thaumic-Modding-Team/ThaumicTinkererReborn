package mod.emt.thaumictinkerer.utils.helpers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHelper {
    public static boolean itemMatches(ItemStack filterStack, ItemStack input, boolean matchNbt, boolean matchMeta, boolean matchOreDict) {
        if(filterStack.isEmpty() || input.isEmpty())
            return false;
        if(ItemHandlerHelper.canItemStacksStack(filterStack, input))
            return true;
        if(!matchNbt && filterStack.getItem() == input.getItem() && (!matchMeta || filterStack.getMetadata() == input.getMetadata()))
            return true;
        if(matchOreDict) {
            //OreDict
            for(int oreId : OreDictionary.getOreIDs(filterStack)) {
                for(int oreId2 : OreDictionary.getOreIDs(input)) {
                    if(oreId == oreId2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
