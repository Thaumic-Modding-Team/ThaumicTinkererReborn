package mod.emt.thaumictinkerer.api.recipes;

import mod.emt.thaumictinkerer.block.BlockNecromancyTablet;
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

public interface INecromancyRecipe {

    Object getCenterIngredient();

    Object[] getComponents();

    boolean shouldConsumeComponents();

    AspectList getEssentia();

    EntityEntry getSummonedEntity();

    default Entity getSummonedEntity(World world) {
        return this.getSummonedEntity().newInstance(world);
    }

    default double getEntityCenter(World world) {
        Entity entity = this.getSummonedEntity().newInstance(world);
        double center = entity.getYOffset() + (entity.height / 2.0f);
        entity.setDead();
        return center;
    }

    default void spawnEntity(World world, BlockPos pos) {
        if(!world.isRemote) {
            EntityEntry entry = this.getSummonedEntity();
            Entity entity = entry.newInstance(world);
            entity.setPosition(pos.getX() + 0.5, pos.getY() + BlockNecromancyTablet.TABLET_AABB.maxY, pos.getZ() + 0.5);
            world.spawnEntity(entity);
        }
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
