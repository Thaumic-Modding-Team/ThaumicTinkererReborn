package mod.emt.thaumictinkerer.compat.crafttweaker.handlers;

import com.blamejared.compat.thaumcraft.handlers.aspects.CTAspectStack;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.recipes.INecromancyRecipe;
import mod.emt.thaumictinkerer.api.recipes.NecromancyRecipe;
import mod.emt.thaumictinkerer.recipes.NecromancyRecipeRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.aspects.AspectList;

import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@ZenRegister
@ZenClass("mods." + ThaumicTinkerer.MOD_ID + ".NecromancyTablet")
public class NecromancyTabletCT {
    @Optional.Method(modid = "modtweaker")
    @ZenMethod
    public static void addRecipe(String recipeName, IEntityDefinition entityDefinition, IIngredient centerItem, CTAspectStack[] aspectStacks, IIngredient... components) {
        AspectList aspectList = new AspectList();
        for(CTAspectStack aspectStack : aspectStacks) {
            aspectList.add(aspectStack.getInternal().getInternal(), aspectStack.getAmount());
        }

        INecromancyRecipe recipe = new NecromancyRecipe()
                .setSummonedEntity((EntityEntry) entityDefinition.getInternal())
                .setCenterIngredient(CraftTweakerMC.getIngredient(centerItem))
                .setAspects(aspectList)
                .setComponents(Arrays.stream(components).map(CraftTweakerMC::getIngredient).collect(Collectors.toList()));

        NecromancyRecipeRegistry.addRecipe(new ResourceLocation(recipeName), recipe);
    }

    @Optional.Method(modid = "modtweaker")
    @ZenMethod
    public static void addConsumingRecipe(String recipeName, IEntityDefinition entityDefinition, IIngredient centerItem, CTAspectStack[] aspectStacks, IIngredient... components) {
        AspectList aspectList = new AspectList();
        for(CTAspectStack aspectStack : aspectStacks) {
            aspectList.add(aspectStack.getInternal().getInternal(), aspectStack.getAmount());
        }

        INecromancyRecipe recipe = new NecromancyRecipe()
                .setSummonedEntity((EntityEntry) entityDefinition.getInternal())
                .setCenterIngredient(CraftTweakerMC.getIngredient(centerItem))
                .setAspects(aspectList)
                .setComponents(Arrays.stream(components).map(CraftTweakerMC::getIngredient).collect(Collectors.toList()))
                .setConsumeComponents(true);

        NecromancyRecipeRegistry.addRecipe(new ResourceLocation(recipeName), recipe);
    }

    @ZenMethod
    public static void removeRecipe(String recipeName) {
        NecromancyRecipeRegistry.removeRecipe(new ResourceLocation(recipeName));
    }

    @ZenMethod
    public static void removeRecipe(IEntityDefinition entityDefinition) {
        NecromancyRecipeRegistry.removeRecipes((EntityEntry) entityDefinition.getInternal());
    }

    @ZenMethod
    public static void removeAll() {
        NecromancyRecipeRegistry.removeAllRecipes();
    }
}
