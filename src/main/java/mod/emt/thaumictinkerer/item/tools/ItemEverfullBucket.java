package mod.emt.thaumictinkerer.item.tools;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.item.IItemAddition;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.client.fx.FXDispatcher;

import javax.annotation.Nullable;
import java.util.Objects;

public class ItemEverfullBucket extends ItemBucket implements IItemAddition {
    public ItemEverfullBucket() {
        super(Blocks.FLOWING_WATER);
        this.setRegistryName(ThaumicTinkerer.MOD_ID, "everfull_bucket");
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, @NotNull EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        RayTraceResult raytraceresult = rayTrace(world, player, false);
        ActionResult<ItemStack> result = ForgeEventFactory.onBucketUse(player, world, stack, raytraceresult);

        if(result != null) {
            return result;
        }

        if(raytraceresult == null) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        } else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        } else {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if(!world.isBlockModifiable(player, blockpos)) {
                return new ActionResult<>(EnumActionResult.FAIL, stack);
            } else {
                boolean flag1 = world.getBlockState(blockpos).getBlock().isReplaceable(world, blockpos);
                BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);

                if(!player.canPlayerEdit(blockpos1, raytraceresult.sideHit, stack)) {
                    return new ActionResult<>(EnumActionResult.FAIL, stack);
                } else if (tryPlaceContainedLiquid(player, world, blockpos1)) {
                    if(player instanceof EntityPlayerMP) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, blockpos1, stack);
                    }

                    player.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));
                    AuraHelper.drainVis(world, blockpos1, 2.0F, false);
                    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
                } else
                    return new ActionResult<>(EnumActionResult.FAIL, stack);
            }
        }
    }

    @Override
    public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, World world, @NotNull BlockPos pos) {
        IBlockState iblockstate = world.getBlockState(pos);
        Material material = iblockstate.getMaterial();
        boolean flag = !material.isSolid();
        boolean flag1 = iblockstate.getBlock().isReplaceable(world, pos);

        if(!world.isAirBlock(pos) && !flag && !flag1) {
            return false;
        } else if (world.provider.doesWaterVaporize()) {
            world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

            for(int k = 0; k < 8; ++k) {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0.0D, 0.0D, 0.0D);
            }
        } else {
            if(!world.isRemote && (flag || flag1) && !material.isLiquid()) {
                world.destroyBlock(pos, true);
            }

            for(int k = 0; k < 20; ++k) {
                FXDispatcher.INSTANCE.jarSplashFx(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
            }

            SoundEvent soundevent = SoundEvents.ITEM_BUCKET_EMPTY;
            world.playSound(player, pos, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState(), 11);
        }

        return true;
    }
}
