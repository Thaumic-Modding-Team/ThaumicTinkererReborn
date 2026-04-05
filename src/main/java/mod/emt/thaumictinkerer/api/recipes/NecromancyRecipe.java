package mod.emt.thaumictinkerer.api.recipes;

import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thaumcraft.api.aspects.AspectList;

import java.util.List;

public class NecromancyRecipe implements INecromancyRecipe {
    private EntityEntry entityEntry;
    private AspectList aspectList;
    private Ingredient centerIngredient;
    private Ingredient[] catalysts;
    private boolean consumeCatalysts;
    private boolean dimensionsInitialized;
    private double entityHeight;
    private double entityWidth;

    public NecromancyRecipe() {
        this.setSummonedEntity(EntityPig.class);
        this.setAspects(new AspectList());
        this.setCenterIngredient(Ingredient.fromStacks(ItemStack.EMPTY));
        this.dimensionsInitialized = false;
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

    public NecromancyRecipe setCenterIngredient(Ingredient centerIngredient) {
        Preconditions.checkArgument(CraftingHelper.getIngredient(centerIngredient) != null, "Center ingredient must be an Ingredient.");
        this.centerIngredient = centerIngredient;
        return this;
    }

    public NecromancyRecipe setComponents(Ingredient... catalysts) {
        Preconditions.checkArgument(catalysts.length <= 8, "Necromatic recipes cannot support more than 8 catalysts.");
        this.catalysts = catalysts;
        return this;
    }

    public NecromancyRecipe setComponents(List<Ingredient> catalysts) {
        return this.setComponents(catalysts.toArray(new Ingredient[0]));
    }

    public NecromancyRecipe setAspects(AspectList aspectList) {
        this.aspectList = aspectList;
        return this;
    }

    public NecromancyRecipe setConsumeComponents(boolean shouldConsume) {
        this.consumeCatalysts = shouldConsume;
        return this;
    }

    private void syncEntityDimensions(World world) {
        if(!this.dimensionsInitialized) {
            Entity entity = this.getSummonedEntity(world);
            this.entityHeight = entity.height;
            this.entityWidth = entity.width;
            this.dimensionsInitialized = true;
            entity.setDead();
        }
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
    public Ingredient getCenterIngredient() {
        return this.centerIngredient;
    }

    @Override
    public Ingredient[] getComponents() {
        return this.catalysts;
    }

    @Override
    public boolean shouldConsumeComponents() {
        return this.consumeCatalysts;
    }

    @Override
    public double getEntityHeight(World world) {
        this.syncEntityDimensions(world);
        return this.entityHeight;
    }

    @Override
    public double getEntityWidth(World world) {
        this.syncEntityDimensions(world);
        return this.entityWidth;
    }
}
