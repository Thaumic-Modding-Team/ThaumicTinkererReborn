package mod.emt.thaumictinkerer.recipes.crafting;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

public class RecipeBlackHoleRingExtract extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe{

    public RecipeBlackHoleRingExtract() {
        this.setRegistryName(ThaumicTinkerer.MOD_ID, "black_hole_ring_extract");
    }

    @Override
    public boolean matches(@NotNull InventoryCrafting inv, @NotNull World worldIn) {
        return false;
    }

    @Override
    public @NotNull ItemStack getCraftingResult(@NotNull InventoryCrafting inv) {
        return ItemStack.EMPTY;
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
