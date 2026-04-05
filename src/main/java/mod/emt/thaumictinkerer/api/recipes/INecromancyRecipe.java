package mod.emt.thaumictinkerer.api.recipes;

import mod.emt.thaumictinkerer.block.BlockNecromancyTablet;
import mod.emt.thaumictinkerer.utils.helpers.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import thaumcraft.api.aspects.AspectList;

public interface INecromancyRecipe {

    Ingredient getCenterIngredient();

    Ingredient[] getComponents();

    boolean shouldConsumeComponents();

    AspectList getEssentia();

    EntityEntry getSummonedEntity();

    double getEntityHeight(World world);

    double getEntityWidth(World world);

    default Entity getSummonedEntity(World world) {
        return this.getSummonedEntity().newInstance(world);
    }

    default ItemStack getSpawnEgg() {
        EntityList.EntityEggInfo eggInfo = this.getSummonedEntity().getEgg();
        if(eggInfo != null) {
            ItemStack egg = new ItemStack(Items.SPAWN_EGG);
            ItemMonsterPlacer.applyEntityIdToItemStack(egg, eggInfo.spawnedID);
            return egg;
        }
        return ItemStack.EMPTY;
    }

    default void spawnEntity(World world, BlockPos pos) {
        if(!world.isRemote) {
            Entity entity = this.getSummonedEntity(world);
            entity.setLocationAndAngles(
                    pos.getX() + 0.5,
                    pos.getY() + BlockNecromancyTablet.TABLET_AABB.maxY,
                    pos.getZ() + 0.5,
                    world.rand.nextFloat() * 360.0f, 0
            );
            if(entity instanceof EntityLiving) {
                ((EntityLiving) entity).onInitialSpawn(world.getDifficultyForLocation(pos), null);
            }
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
