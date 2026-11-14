package mod.emt.thaumictinkerer.item.misc;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import mod.emt.thaumictinkerer.entity.EntityGasPhial;
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

public class ItemGasPhial extends AbstractItemAddition {
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
}
