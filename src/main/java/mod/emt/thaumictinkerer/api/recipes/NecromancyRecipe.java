package mod.emt.thaumictinkerer.api.recipes;

import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thaumcraft.api.aspects.AspectList;

public class NecromancyRecipe implements INecromancyRecipe {
    private EntityEntry entityEntry;
    private AspectList aspectList;
    private Object centerIngredient;
    private Object[] catalysts;
    private boolean consumeCatalysts;

    public NecromancyRecipe() {
        this.setSummonedEntity(EntityPig.class);
        this.setAspects(new AspectList());
        this.setCenterIngredient(ItemStack.EMPTY);
    }

    public NecromancyRecipe setSummonedEntity(EntityEntry entityEntry) {
        Preconditions.checkArgument(entityEntry != null, "EntityEntry cannot be null.");
        this.entityEntry = entityEntry;
        return this;
    }

    public NecromancyRecipe setSummonedEntity(Class<? extends Entity> entityClazz) {
        this.setSummonedEntity(EntityRegistry.getEntry(entityClazz));
        return this;
    }

    public NecromancyRecipe setSummonedEntity(ResourceLocation entityRegistryName) {
        this.setSummonedEntity(ForgeRegistries.ENTITIES.getValue(entityRegistryName));
        return this;
    }

    public NecromancyRecipe setCenterIngredient(Object centerIngredient) {
        Preconditions.checkArgument(CraftingHelper.getIngredient(centerIngredient) != null || centerIngredient instanceof FluidStack, "Center ingredient must be an Ingredient or a FluidStack.");
        this.centerIngredient = centerIngredient;
        return this;
    }

    public NecromancyRecipe setComponents(Object... catalysts) {
        Preconditions.checkArgument(catalysts.length <= 8, "Necromatic recipes cannot support more than 8 catalysts.");
        this.catalysts = catalysts;
        return this;
    }

    public NecromancyRecipe setAspects(AspectList aspectList) {
        this.aspectList = aspectList;
        return this;
    }

    public NecromancyRecipe setConsumeComponents(boolean shouldConsume) {
        this.consumeCatalysts = shouldConsume;
        return this;
    }

    @Override
    public EntityEntry getSummonedEntity() {
        return this.entityEntry;
    }

    @Override
    public AspectList getEssentia() {
        return this.aspectList;
    }

    @Override
    public Object getCenterIngredient() {
        return this.centerIngredient;
    }

    @Override
    public Object[] getComponents() {
        return this.catalysts;
    }

    @Override
    public boolean shouldConsumeComponents() {
        return this.consumeCatalysts;
    }
}
