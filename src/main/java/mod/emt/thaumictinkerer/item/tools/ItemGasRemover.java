package mod.emt.thaumictinkerer.item.tools;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import mod.emt.thaumictinkerer.block.BlockLuminousGas;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemGasRemover extends AbstractItemAddition {
    public ItemGasRemover() {
        super("gas_remover");
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public @NotNull ItemStack onItemUseFinish(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull EntityLivingBase entityLiving) {
        AxisAlignedBB area = new AxisAlignedBB(entityLiving.getPosition()).grow(4);
        for(double x = area.minX; x < area.maxX; x++) {
            for(double y = area.minY; y < area.maxY; y++) {
                for(double z = area.minZ; z < area.maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState state = worldIn.getBlockState(pos);
                    if(state.getBlock() instanceof BlockLuminousGas) {
                        if(!worldIn.isRemote) {
                            worldIn.setBlockToAir(pos);
                        } else {
                            //TODO: Do particle effect
                        }
                    }
                }
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public @NotNull EnumAction getItemUseAction(@NotNull ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(@NotNull ItemStack stack) {
        return 40;
    }
}
