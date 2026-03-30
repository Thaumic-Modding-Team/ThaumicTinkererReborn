package mod.emt.thaumictinkerer.recipes;

import mod.emt.thaumictinkerer.api.recipes.INecromancyRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class NecromancyRecipeRegistry {
    private static final Map<ResourceLocation, INecromancyRecipe> NECROMATIC_RECIPES = new HashMap<>();

    @Nullable
    public static INecromancyRecipe getRecipe(ItemStack centerStack, NonNullList<ItemStack> catalysts) {
        return NECROMATIC_RECIPES.values().stream()
                .filter(recipe -> recipe.matches(centerStack, catalysts))
                .findAny()
                .orElse(null);
    }

    @Nullable
    public static INecromancyRecipe getRecipe(ResourceLocation recipeName) {
        return NECROMATIC_RECIPES.getOrDefault(recipeName, null);
    }

    @Nullable
    public static Tuple<ResourceLocation, INecromancyRecipe> getRecipeAndName(ItemStack centerStack, NonNullList<ItemStack> catalysts) {
        return NECROMATIC_RECIPES.entrySet().stream()
                .filter(entry -> entry.getValue().matches(centerStack, catalysts))
                .findFirst()
                .map(entry -> new Tuple<>(entry.getKey(), entry.getValue()))
                .orElse(null);
    }

    @Nullable
    public static ResourceLocation getRecipeName(INecromancyRecipe recipe) {
        return NECROMATIC_RECIPES.entrySet().stream()
                .filter(entry -> recipe.equals(entry.getValue()))
                .findFirst().map(Map.Entry::getKey)
                .orElse(null);
    }

    public static void addRecipe(ResourceLocation recipeName, INecromancyRecipe recipe) {
        NECROMATIC_RECIPES.put(recipeName, recipe);
    }

    public static void removeRecipe(ResourceLocation recipeName) {
        NECROMATIC_RECIPES.remove(recipeName);
    }

    public static void removeAllRecipes() {
        NECROMATIC_RECIPES.clear();
    }
}
