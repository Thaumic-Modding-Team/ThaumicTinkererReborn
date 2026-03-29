package mod.emt.thaumictinkerer.api.recipes;

import com.google.common.base.Preconditions;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import thaumcraft.api.aspects.AspectList;

public class NecromaticRecipe implements INecromaticRecipe {
    private EntityEntry entityEntry;
    private AspectList aspectList;
    private Object centerIngredient;
    private Object[] catalysts;
    private boolean consumeCatalysts;

    public NecromaticRecipe(EntityEntry summonedEntity, AspectList aspectList, Object centerIngredient, Object... catalysts) {
        this.setSummonedEntity(summonedEntity);
        this.setEssentia(aspectList);
        this.setCenterIngredient(centerIngredient);
        this.setCatalysts(catalysts);
    }

    @Override
    public EntityEntry getSummonedEntity() {
        return this.entityEntry;
    }

    @Override
    public NecromaticRecipe setSummonedEntity(EntityEntry entityEntry) {
        Preconditions.checkArgument(entityEntry != null, "EntityEntry cannot be null.");
        this.entityEntry = entityEntry;
        return this;
    }

    @Override
    public AspectList getEssentia() {
        return this.aspectList;
    }

    @Override
    public NecromaticRecipe setEssentia(AspectList aspectList) {
        this.aspectList = aspectList;
        return this;
    }

    @Override
    public Object getCenterIngredient() {
        return this.centerIngredient;
    }

    @Override
    public NecromaticRecipe setCenterIngredient(Object centerIngredient) {
        Preconditions.checkArgument(CraftingHelper.getIngredient(centerIngredient) != null || centerIngredient instanceof FluidStack, "Center ingredient must be an Ingredient or a FluidStack.");
        this.centerIngredient = centerIngredient;
        return this;
    }

    @Override
    public Object[] getCatalysts() {
        return this.catalysts;
    }

    @Override
    public NecromaticRecipe setCatalysts(Object... catalysts) {
        Preconditions.checkArgument(catalysts.length <= 8, "Necromatic recipes cannot support more than 8 catalysts.");
        this.catalysts = catalysts;
        return this;
    }

    @Override
    public boolean consumeCatalysts() {
        return this.consumeCatalysts;
    }

    public NecromaticRecipe setConsumeCatalysts() {
        this.consumeCatalysts = true;
        return this;
    }
}
