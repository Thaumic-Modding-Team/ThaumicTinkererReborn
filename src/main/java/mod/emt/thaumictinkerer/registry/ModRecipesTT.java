package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;

public class ModRecipesTT {

    public static void initRecipes(RegistryEvent.Register<IRecipe> event) {
        initArcaneWorkbenchRecipes();
        initCrucibleRecipes();
        initInfusionRecipes();
    }

    private static void initArcaneWorkbenchRecipes() {

    }

    // TODO: Research keys and aspect balance
    private static void initCrucibleRecipes() {
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "AA_PRISMARINE"), new CrucibleRecipe(
                "",
                new ItemStack(Items.PRISMARINE_SHARD),
                "paneGlass",
                new AspectList().add(Aspect.WATER, 5).add(Aspect.EARTH, 5)));

        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "AA_SEA_LANTERN"), new CrucibleRecipe(
                "",
                new ItemStack(Blocks.SEA_LANTERN),
                new ItemStack(Blocks.REDSTONE_LAMP),
                new AspectList().add(Aspect.WATER, 5).add(Aspect.EARTH, 5)));

        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "AA_SPONGE"), new CrucibleRecipe(
                "",
                new ItemStack(Blocks.SPONGE, 1, 0),
                "wool",
                new AspectList().add(Aspect.WATER, 5).add(Aspect.EARTH, 5)));
    }

    private static void initInfusionRecipes() {

    }
}
