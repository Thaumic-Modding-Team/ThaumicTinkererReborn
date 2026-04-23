package mod.emt.thaumictinkerer.item.tools;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.item.IItemAddition;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.items.IRechargable;
import thaumcraft.api.items.RechargeHelper;
import thaumcraft.common.lib.SoundsTC;

import java.util.Objects;

public class ItemIgniumStriker extends ItemFlintAndSteel implements IItemAddition, IRechargable {
    public ItemIgniumStriker() {
        this.setRegistryName(ThaumicTinkerer.MOD_ID, "ignium_striker");
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
        this.setMaxDamage(-1);
        this.addPropertyOverride(new ResourceLocation("depleted"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(@NotNull ItemStack stack, World worldIn, EntityLivingBase entity) {
                if(RechargeHelper.getCharge(stack) <= 0) {
                    return 1.0F;
                }

                return 0.0F;
            }
        });
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            ItemStack base = new ItemStack(this);
            items.add(base);
            ItemStack charged = base.copy();
            RechargeHelper.rechargeItemBlindly(charged, null, getMaxCharge(charged, null));
            items.add(charged);
        }
    }

    @Override
    public @NotNull EnumActionResult onItemUse(EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        pos = pos.offset(facing);
        ItemStack stack = player.getHeldItem(hand);

        if(this.drainEnergy(stack, true)) {
            if (!player.canPlayerEdit(pos, facing, stack)) {
                world.playSound(player, pos, SoundsTC.ticks, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 1.5F + 0.8F);
            } else {
                if (world.isAirBlock(pos)) {
                    world.playSound(player, pos, SoundsTC.crystal, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 1.5F + 0.8F);
                    world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                    world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
                }

                if (player instanceof EntityPlayerMP) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
                }

                if (!player.isCreative()) {
                    this.drainEnergy(stack, false);
                }
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public boolean drainEnergy(ItemStack stack, boolean simulate) {
        int energy = this.getEnergy(stack);
        int vis = this.getVisCharge(stack);

        if(simulate) {
            return energy > 0 || vis > 0;
        }

        if(energy > 0) {
            this.setEnergy(stack, energy - 1);
            return true;
        } else if(vis > 0) {
            this.setVisCharge(stack, vis - 1);
            //Since the first strike is "consumed" when the vis is converted, the energy is set to 2.
            this.setEnergy(stack, 2);
            return true;
        }
        return false;
    }

    public int getEnergy(ItemStack stack) {
        return stack.getTagCompound() != null ? stack.getTagCompound().getInteger("energy") : 0;
    }

    public void setEnergy(ItemStack stack, int energy) {
        stack.setTagInfo("energy", new NBTTagInt(energy));
    }

    public int getVisCharge(ItemStack stack) {
        return RechargeHelper.getCharge(stack);
    }

    public void setVisCharge(ItemStack stack, int visCharge) {
        stack.setTagInfo("tc.charge", new NBTTagInt(visCharge));
    }

    @Override
    public int getMaxCharge(ItemStack stack, EntityLivingBase entity) {
        return 60;
    }

    @Override
    public EnumChargeDisplay showInHud(ItemStack stack, EntityLivingBase entity) {
        return EnumChargeDisplay.NORMAL;
    }
}
