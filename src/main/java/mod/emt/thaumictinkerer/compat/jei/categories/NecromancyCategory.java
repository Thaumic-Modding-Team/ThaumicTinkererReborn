package mod.emt.thaumictinkerer.compat.jei.categories;

import com.invadermonky.thaumicapi.api.ThaumicAPIJEIPlugin;
import com.invadermonky.thaumicapi.jei.AspectIngredientRender;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.compat.jei.wrappers.NecromancyRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.AspectList;

import java.util.List;

public class NecromancyCategory implements IRecipeCategory<NecromancyRecipeWrapper> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ThaumicTinkerer.MOD_ID, "textures/gui/jei_necromancy_overlay.png");

    private final IDrawable background;

    public NecromancyCategory(IGuiHelper helper) {
        this.background = helper
                .drawableBuilder(TEXTURE, 0, 0, 86, 86)
                .addPadding(40, 44, 30, 30)
                .build();
    }

    @Override
    public @NotNull String getUid() {
        return "THAUMIC_TINKERER_NECROMANCY";
    }

    @Override
    public @NotNull String getTitle() {
        return I18n.format("jei.thaumictinkerer:necromancy.title");
    }

    @Override
    public @NotNull String getModName() {
        return ThaumicTinkerer.MOD_NAME;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        minecraft.renderEngine.bindTexture(new ResourceLocation("thaumcraft", "textures/gui/gui_researchbook_overlay.png"));
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(57, 0, 40.0F, 6.0F, 32, 32, 512.0F, 512.0F);
        GlStateManager.disableBlend();
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayout recipeLayout, @NotNull NecromancyRecipeWrapper wrapper, @NotNull IIngredients ingredients) {
        //Components
        int slot = 0;
        float rotation = -90.0f;
        for (List<ItemStack> stacks : ingredients.getInputs(VanillaTypes.ITEM)) {
            if(slot == 0) {
                recipeLayout.getItemStacks().init(slot, true, 64, 73);
            } else {
                recipeLayout.getItemStacks().init(slot, true,
                        (int) (MathHelper.cos(rotation / 180.0F * (float) Math.PI) * 40.0F) + 64,
                        (int) (MathHelper.sin(rotation / 180.0F * (float) Math.PI) * 40.0F) + 73);
            }

            recipeLayout.getItemStacks().set(slot, stacks);
            rotation += 360.0f / (float) wrapper.recipe.getComponents().length;
            slot++;
        }

        //Aspects
        int center = ingredients.getInputs(ThaumicAPIJEIPlugin.ASPECT_INGREDIENT).size() * 22 / 2;
        int x = 0;

        for(List<AspectList> aspectList : ingredients.getInputs(ThaumicAPIJEIPlugin.ASPECT_INGREDIENT)) {
            recipeLayout.getIngredientsGroup(ThaumicAPIJEIPlugin.ASPECT_INGREDIENT).init(x + slot, true, new AspectIngredientRender(),
                    76 - center + x * 22, 135, 16, 16, 0, 0);
            recipeLayout.getIngredientsGroup(ThaumicAPIJEIPlugin.ASPECT_INGREDIENT).set(x + slot, aspectList);
            x++;
        }
    }


}
