package mod.emt.thaumictinkerer.utils.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
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

    public static boolean ingredientMatches(ItemStack stack, Object recipeIngredient) {
        Ingredient ingredient = CraftingHelper.getIngredient(recipeIngredient);
        if(ingredient != null) {
            return ingredient.apply(stack);
        } else if(recipeIngredient instanceof FluidStack) {
            FluidStack fluidStack = (FluidStack) recipeIngredient;
            IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
            if(handler != null) {
                FluidStack drained = handler.drain(fluidStack, false);
                return drained != null && drained.amount == fluidStack.amount;
            }
        }
        return false;
    }
}
