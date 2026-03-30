package mod.emt.thaumictinkerer.api.recipes;

import mod.emt.thaumictinkerer.utils.helpers.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thaumcraft.api.aspects.AspectList;

public interface INecromaticRecipe {

    Object getCenterIngredient();

    INecromaticRecipe setCenterIngredient(Object centerIngredient);

    Object[] getComponents();

    INecromaticRecipe setComponents(Object... catalysts);

    boolean shouldConsumeComponents();

    AspectList getEssentia();

    INecromaticRecipe setEssentia(AspectList aspectList);

    EntityEntry getSummonedEntity();

    INecromaticRecipe setSummonedEntity(EntityEntry entityEntry);

    default INecromaticRecipe setSummonedEntity(Class<? extends Entity> entityClazz) {
        this.setSummonedEntity(EntityRegistry.getEntry(entityClazz));
        return this;
    }

    default INecromaticRecipe setSummonedEntity(ResourceLocation entityRegistryName) {
        this.setSummonedEntity(ForgeRegistries.ENTITIES.getValue(entityRegistryName));
        return this;
    }

    default void spawnEntity(World world, BlockPos pos) {
        EntityEntry entry = this.getSummonedEntity();
        Entity entity = entry.newInstance(world);
        entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.125, pos.getZ() + 0.5);
        world.spawnEntity(entity);
    }

    default boolean matches(ItemStack centerItem, NonNullList<ItemStack> catalystStacks) {
        if(!ItemHelper.ingredientMatches(centerItem, this.getCenterIngredient())) {
            return false;
        }

        NonNullList<ItemStack> copy = NonNullList.create();
        copy.addAll(catalystStacks);
        copy.removeIf(ItemStack::isEmpty);

        outer:
        for(Object catalyst : this.getComponents()) {
            for (int i = 0; i < copy.size(); i++) {
                ItemStack stack = copy.get(i);
                if (ItemHelper.ingredientMatches(stack, catalyst)) {
                    copy.remove(i);
                    continue outer;
                }
            }
            return false;
        }
        return copy.isEmpty();
    }
}
