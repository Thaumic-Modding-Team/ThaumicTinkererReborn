package mod.emt.thaumictinkerer.recipes.crafting;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.container.handlers.BlackHoleRingHandler;
import mod.emt.thaumictinkerer.item.bauble.ItemBlackHoleRing;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

public class RecipeBlackHoleRingClearSlot extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    public RecipeBlackHoleRingClearSlot() {
        this.setRegistryName(ThaumicTinkerer.MOD_ID, "black_hole_ring_clear_slot");
    }

    @Override
    public boolean matches(@NotNull InventoryCrafting inv, @NotNull World worldIn) {
        boolean foundRing = false;
        for(int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if(stack.isEmpty()) continue;

            if(stack.getItem() instanceof ItemBlackHoleRing && !foundRing) {
                foundRing = true;
            } else {
                return false;
            }
        }
        return foundRing;
    }

    @Override
    public @NotNull ItemStack getCraftingResult(@NotNull InventoryCrafting inv) {
        ItemStack ringStack = ItemStack.EMPTY;
        for(int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack slotStack = inv.getStackInSlot(i);
            if(!slotStack.isEmpty() && slotStack.getItem() instanceof ItemBlackHoleRing) {
                ringStack = slotStack.copy();
                break;
            }
        }

        if(!ringStack.isEmpty()) {
            ItemBlackHoleRing item = (ItemBlackHoleRing) ringStack.getItem();
            int activeSlot = item.getActiveSlot(ringStack);
            BlackHoleRingHandler handler = item.getRingHandler(ringStack);
            if(handler != null) {
                handler.setFilter(activeSlot, ItemStack.EMPTY);
                handler.setStackInSlot(activeSlot, ItemStack.EMPTY);
            }
        }

        return ringStack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public @NotNull ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}
