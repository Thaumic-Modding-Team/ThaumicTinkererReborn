package mod.emt.thaumictinkerer.item.misc;

import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import mod.emt.thaumictinkerer.entity.EntityGasPhial;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class ItemGasPhial extends AbstractItemAddition implements IProxy {
    private final boolean isLight;

    public ItemGasPhial(String unlocName, boolean isLight) {
        super(unlocName);
        this.isLight = isLight;
    }

    public boolean isLightEmitting() {
        return this.isLight;
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        ItemStack thrown = playerIn.capabilities.isCreativeMode ? ItemHandlerHelper.copyStackWithSize(heldStack, 1) : heldStack.splitStack(1);
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.PLAYERS,
                0.5f, 0.4f / itemRand.nextFloat() * 0.4f + 0.8f);

        if(!worldIn.isRemote) {
            EntityGasPhial phial = new EntityGasPhial(worldIn, playerIn, thrown, this.isLightEmitting());
            phial.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0f, 0.5f, 1.0f);
            worldIn.spawnEntity(phial);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public boolean hasEffect(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public void init() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new PhialDispenserLogic());
    }

    public static class PhialDispenserLogic implements IBehaviorDispenseItem {
        @Override
        public @NotNull ItemStack dispense(@NotNull IBlockSource source, @NotNull ItemStack stack) {
            return new BehaviorProjectileDispense() {
                @Override
                protected @NotNull IProjectile getProjectileEntity(@NotNull World worldIn, @NotNull IPosition position, @NotNull ItemStack stackIn) {
                    ItemStack thrown = ItemHandlerHelper.copyStackWithSize(stack, 1);
                    boolean isLightEmitting = thrown.getItem() instanceof ItemGasPhial && ((ItemGasPhial) thrown.getItem()).isLightEmitting();
                    return new EntityGasPhial(worldIn, position.getX(), position.getY(), position.getZ(), thrown, isLightEmitting);
                }

                @Override
                protected float getProjectileInaccuracy() {
                    return super.getProjectileInaccuracy() * 0.5f;
                }

                @Override
                protected float getProjectileVelocity() {
                    return super.getProjectileVelocity() * 1.25f;
                }
            }.dispense(source, stack);
        }
    }
}
