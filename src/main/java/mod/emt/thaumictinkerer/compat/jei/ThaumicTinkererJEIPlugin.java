package mod.emt.thaumictinkerer.compat.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mod.emt.thaumictinkerer.api.recipes.INecromancyRecipe;
import mod.emt.thaumictinkerer.compat.jei.categories.NecromancyCategory;
import mod.emt.thaumictinkerer.compat.jei.wrappers.NecromancyRecipeWrapper;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.recipes.NecromancyRecipeRegistry;
import mod.emt.thaumictinkerer.registry.ModBlocksTT;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@JEIPlugin
public class ThaumicTinkererJEIPlugin implements IModPlugin {
    public static NecromancyCategory NECROMANCY;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        if(ConfigHandlerTT.necromancyTablet.enable)
            registry.addRecipeCategories(NECROMANCY = new NecromancyCategory());
    }

    @Override
    public void register(@NotNull IModRegistry registry) {
        this.initNecromancyTablet(registry);
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        //TODO: Hide unless research unlocked?
    }

    @SuppressWarnings("ConstantConditions")
    private void initNecromancyTablet(IModRegistry registry) {
        if(ConfigHandlerTT.necromancyTablet.enable) {
            registry.addRecipeCatalyst(new ItemStack(ModBlocksTT.NECROMANCY_TABLET), NECROMANCY.getUid());
            registry.handleRecipes(INecromancyRecipe.class, NecromancyRecipeWrapper::new, NECROMANCY.getUid());
            registry.addRecipes(NecromancyRecipeRegistry.getRecipes(), NECROMANCY.getUid());
        }
    }
}
