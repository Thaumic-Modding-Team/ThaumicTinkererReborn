package mod.emt.thaumictinkerer.compat.groovyscript.handlers;

import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.compat.mods.thaumcraft.aspect.AspectStack;
import com.cleanroommc.groovyscript.helper.SimpleObjectStream;
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder;
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry;
import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.recipes.INecromancyRecipe;
import mod.emt.thaumictinkerer.api.recipes.NecromancyRecipe;
import mod.emt.thaumictinkerer.recipes.NecromancyRecipeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.AspectList;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RegistryDescription(linkGenerator = ThaumicTinkerer.MOD_ID)
public class NecromancyTablet extends VirtualizedRegistry<INecromancyRecipe> {
    @GroovyBlacklist
    @Override
    public void onReload() {

    }

    @MethodDescription(
            type = MethodDescription.Type.ADDITION
            //TODO: Examples
    )
    public INecromancyRecipe addRecipe(String recipeName, EntityEntry entityEntry, IIngredient centerItem, Collection<AspectStack> aspectStacks, IIngredient... components) {
        return new RecipeBuilder().recipeName(recipeName)
                .summonedEntity(entityEntry)
                .centerItem(centerItem)
                .aspect(aspectStacks)
                .components(components)
                .register();
    }

    //TODO: Copy addRecipe description.
    public INecromancyRecipe addConsumingRecipe(String recipeName, EntityEntry entityEntry, IIngredient centerItem, Collection<AspectStack> aspectStacks, IIngredient... components) {
        return new RecipeBuilder().recipeName(recipeName)
                .summonedEntity(entityEntry)
                .centerItem(centerItem)
                .aspect(aspectStacks)
                .components(components)
                .setConsumeComponents(true)
                .register();
    }

    @MethodDescription(type = MethodDescription.Type.QUERY)
    public SimpleObjectStream<INecromancyRecipe> streamRecipes() {
        return new SimpleObjectStream<>(NecromancyRecipeRegistry.getRecipes());
    }

    @MethodDescription(type = MethodDescription.Type.QUERY, priority = 1001)
    public SimpleObjectStream<ResourceLocation> streamRecipeNames() {
        return new SimpleObjectStream<>(NecromancyRecipeRegistry.getRecipeNames());
    }

    @MethodDescription(type = MethodDescription.Type.QUERY, priority = 1002)
    public INecromancyRecipe getRecipe(ResourceLocation recipeName) {
        return NecromancyRecipeRegistry.getRecipe(recipeName);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("'minecraft:cow'"))
    public void removeRecipe(String recipeName) {
        NecromancyRecipeRegistry.removeRecipe(new ResourceLocation(recipeName));
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example("entity('minecraft:cow')"))
    public void removeRecipe(EntityEntry entityEntry) {
        NecromancyRecipeRegistry.removeRecipe(entityEntry);
    }

    @MethodDescription(type = MethodDescription.Type.REMOVAL, example = @Example(commented = true))
    public void removeAll() {
        NecromancyRecipeRegistry.removeAllRecipes();
    }

    public static class RecipeBuilder extends AbstractRecipeBuilder<INecromancyRecipe> {
        @Property(comp = @Comp(not = "null"))
        private String recipeName;
        @Property(comp = @Comp(not = "null"))
        private EntityEntry summonedEntity;
        @Property
        private IIngredient centerItem;
        @Property
        private IIngredient[] components;
        @Property
        private AspectList aspects = new AspectList();
        @Property
        private boolean consumeComponents;

        public RecipeBuilder() {
            this.recipeName("");
            this.summonedEntity(EntityPig.class);
            this.centerItem = IIngredient.EMPTY;
            this.components = new IIngredient[] {};
        }

        @RecipeBuilderMethodDescription(field = "recipeName")
        public RecipeBuilder recipeName(String recipeName) {
            this.recipeName = recipeName;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "summonedEntity")
        public RecipeBuilder summonedEntity(EntityEntry entityEntry) {
            this.summonedEntity = entityEntry;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "recipeName")
        public RecipeBuilder summonedEntity(Class<? extends Entity> entity) {
            return this.summonedEntity(EntityRegistry.getEntry(entity));
        }

        @RecipeBuilderMethodDescription(field = "centerItem")
        public RecipeBuilder centerItem(IIngredient centerItem) {
            this.centerItem = centerItem;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "components")
        public RecipeBuilder components(IIngredient... components) {
            this.components = components;
            return this;
        }

        @RecipeBuilderMethodDescription(field = "aspects")
        public RecipeBuilder aspect(AspectStack aspectStack) {
            this.aspects.add(aspectStack.getAspect(), aspectStack.getAmount());
            return this;
        }

        @RecipeBuilderMethodDescription(field = "aspects")
        public RecipeBuilder aspect(AspectStack... aspectStacks) {
            for(AspectStack aspectStack : aspectStacks) {
                this.aspect(aspectStack);
            }
            return this;
        }

        @RecipeBuilderMethodDescription(field = "aspects")
        public RecipeBuilder aspect(Collection<AspectStack> aspectStacks) {
            for(AspectStack aspectStack : aspectStacks) {
                this.aspect(aspectStack);
            }
            return this;
        }

        @RecipeBuilderMethodDescription(field = "consumeComponents")
        public RecipeBuilder setConsumeComponents(boolean consumeComponents) {
            this.consumeComponents = consumeComponents;
            return this;
        }

        @Override
        public String getErrorMsg() {
            return "Error adding Necromancy Tablet recipe";
        }

        @Override
        protected int getMaxItemInput() {
            return 1;
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            msg.add(this.recipeName == null || this.recipeName.toString().isEmpty(), "Recipe name cannot be null or empty");
            msg.add(this.summonedEntity == null, "Summoned entity cannot be null");
            msg.add(this.centerItem == null, "Center item cannot be null");
            msg.add(this.components.length > 8, "Necromancy recipes cannot have more than 8 components");
        }

        @RecipeBuilderRegistrationMethod
        @Override
        public @Nullable INecromancyRecipe register() {
            if(this.validate()) {
                INecromancyRecipe recipe = new NecromancyRecipe()
                        .setSummonedEntity(this.summonedEntity)
                        .setCenterIngredient(this.centerItem.toMcIngredient())
                        .setComponents(Arrays.stream(this.components).map(IIngredient::toMcIngredient).collect(Collectors.toList()))
                        .setAspects(this.aspects)
                        .setConsumeComponents(this.consumeComponents);
                NecromancyRecipeRegistry.addRecipe(new ResourceLocation(this.recipeName), recipe);
                return recipe;
            }
            return null;
        }
    }
}
