package mod.emt.thaumictinkerer.compat.jei.wrappers;

import com.google.common.collect.ImmutableList;
import com.invadermonky.thaumicapi.api.ThaumicAPIJEIPlugin;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mod.emt.thaumictinkerer.api.recipes.INecromancyRecipe;
import mod.emt.thaumictinkerer.config.ConfigTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.*;

public class NecromancyRecipeWrapper implements IRecipeWrapper {
    public final INecromancyRecipe recipe;
    private World curWorld;
    private EntityLivingBase entityInst;
    private double renderScale = -1.0;

    public NecromancyRecipeWrapper(INecromancyRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(@NotNull IIngredients ingredients) {
        List<List<ItemStack>> inputs = new ArrayList<>();

        Object centerIngredient = this.recipe.getCenterIngredient();
        Ingredient ingredient = CraftingHelper.getIngredient(centerIngredient);
        if(ingredient != null) {
            inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
        } else {
            inputs.add(Collections.singletonList(ItemStack.EMPTY));
        }

        for(Object component : this.recipe.getComponents()) {
            ingredient = CraftingHelper.getIngredient(component);
            if(ingredient != null) {
                inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
            }
        }

        List<AspectList> list = new ArrayList<>(this.recipe.getEssentia().size());
        for (Aspect aspect : this.recipe.getEssentia().getAspectsSortedByAmount()) {
            list.add(new AspectList().add(aspect, this.recipe.getEssentia().getAmount(aspect)));
        }

        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setInputs(ThaumicAPIJEIPlugin.ASPECT_INGREDIENT, list);
        ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getSpawnEgg());
    }

    /**
     *  Entity rendering code courtesy of <a href="https://github.com/Elite-Modding-Team/TinkersAntique">Tinkers' Antique</a>
     *  <a href="https://github.com/Elite-Modding-Team/TinkersAntique/blob/1.12/src/main/java/slimeknights/tconstruct/plugin/jei/entitymelting/EntityMeltingRecipeWrapper.java">Entity Melting JEI</a>,
     *  licensed under GNU Lesser General Public License v3.0.
     */
    @Override
    public void drawInfo(@NotNull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (minecraft.world != null && minecraft.world != this.curWorld) {
            this.curWorld = minecraft.world;
            Entity entity = this.recipe.getSummonedEntity(this.curWorld);
            if (entity instanceof EntityLivingBase) {
                this.entityInst = (EntityLivingBase) entity;
            }
        }
        if (this.entityInst != null && minecraft.currentScreen != null) {
            if (this.renderScale < 0) {
                double width = this.entityInst.width;
                double height = this.entityInst.height;

                if (width > height) {
                    double scaleFact = 13;
                    this.renderScale = scaleFact / width;
                } else {
                    double scaleFact = 32;
                    this.renderScale = scaleFact / height;
                }
            }

            double configScale = ConfigTags.getJeiEntityRenderScale(this.recipe.getSummonedEntity());

            int x = 73;
            int y = 32;

            GlStateManager.enableDepth();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0);
            if (configScale != 1.0) {
                GlStateManager.scale(configScale, configScale, configScale);
            }
            GuiInventory.drawEntityOnScreen(0, 0, (int) Math.round(this.renderScale), -100, 0, this.entityInst);
            GlStateManager.popMatrix();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA); // I HATE YOU SPIDER EYES!!!!!
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA); // I HATE YOU SPIDER EYES!!!!!
        }
    }

    @Override
    public @NotNull List<String> getTooltipStrings(int mouseX, int mouseY) {
        if(mouseX >= 57 && mouseY >= 0 && mouseX <= 89 && mouseY <= 32 && this.entityInst != null) {
            ResourceLocation loc = Objects.requireNonNull(this.recipe.getSummonedEntity().getRegistryName());
            List<String> list = new ArrayList<>();
            list.add(this.entityInst.getName());
            ModContainer mod = Loader.instance().getIndexedModList().get(loc.getNamespace());
            if (Minecraft.getMinecraft().gameSettings.advancedItemTooltips) {
                list.add(TextFormatting.DARK_GRAY + loc.toString());
            }
            list.add(TextFormatting.BLUE + "" + TextFormatting.ITALIC + (mod == null ? "Unknown" : mod.getName()));
            if(this.recipe.shouldConsumeComponents()) {
                list.add(TextFormatting.RED + I18n.format("jei.thaumictinkerer:necromancy.consuming"));
            } else {
                list.add(TextFormatting.GREEN + I18n.format("jei.thaumictinkerer:necromancy.repeatable"));
            }
            return list;
        }
        return ImmutableList.of();
    }
}
