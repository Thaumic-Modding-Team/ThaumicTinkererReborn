package mod.emt.thaumictinkerer.recipes.crafting;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.item.bauble.ItemBlackHoleRing;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

public class RecipeBlackHoleRingInsert extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe{

    public RecipeBlackHoleRingInsert() {
        this.setRegistryName(ThaumicTinkerer.MOD_ID, "black_hole_ring_insert");
    }

    @Override
    public boolean matches(@NotNull InventoryCrafting inv, @NotNull World worldIn) {
        boolean hasRing = false;
        ItemStack blockStack = ItemStack.EMPTY;
        for(int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack invStack = inv.getStackInSlot(i);
            if(!invStack.isEmpty()) {
                if(invStack.getItem() instanceof ItemBlackHoleRing) {
                    hasRing = true;
                } else if(invStack.getItem() instanceof ItemBlock) {
                    if(blockStack.isEmpty()) {
                        blockStack = invStack.copy();
                    } else if(!ItemHandlerHelper.canItemStacksStack(blockStack, invStack)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return hasRing && !blockStack.isEmpty();
    }

    @Override
    public @NotNull ItemStack getCraftingResult(@NotNull InventoryCrafting inv) {
        //TODO: This is broken and duping items.
        ItemStack ringStack = ItemStack.EMPTY;
        ItemStack blockStack = ItemStack.EMPTY;
        int amount = 0;
        for(int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack invStack = inv.getStackInSlot(i);
            if(!invStack.isEmpty()) {
                if(invStack.getItem() instanceof ItemBlackHoleRing) {
                    ringStack = invStack.copy();
                } else if(invStack.getItem() instanceof ItemBlock) {
                    if(blockStack.isEmpty()) {
                        blockStack = invStack.copy();
                        amount += blockStack.getCount();
                        blockStack.setCount(1);
                    } else if(ItemHandlerHelper.canItemStacksStack(blockStack, invStack)){
                        amount += invStack.getCount();
                    } else {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if(!ringStack.isEmpty() && !blockStack.isEmpty() && amount > 0) {
            ((ItemBlackHoleRing) ringStack.getItem()).insertAmount(ringStack, blockStack, amount, true);
            return ringStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public @NotNull ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}
