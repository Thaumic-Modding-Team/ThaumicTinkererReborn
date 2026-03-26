package mod.emt.thaumictinkerer.item;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import mod.emt.thaumictinkerer.registry.ModBlocksTT;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemEnergeticNitor extends AbstractItemAddition {
    public ItemEnergeticNitor() {
        super("energetic_nitor");
        this.setMaxStackSize(1);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onUpdate(@NotNull ItemStack stack, World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(!worldIn.isRemote && entityIn instanceof EntityPlayer && this.isEnabled(stack)) {
            EntityPlayer player = (EntityPlayer) entityIn;
            BlockPos pos = player.getPosition();
            if(worldIn.isAirBlock(pos) && player.ticksExisted % 5 == 0 && worldIn.getLight(pos) <= 8) {
                worldIn.setBlockState(pos, ModBlocksTT.ENERGETIC_NITOR.getDefaultState());
            }
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        if(playerIn.isSneaking()) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            this.setEnabled(stack, !this.isEnabled(stack));
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull EnumActionResult onItemUse(@NotNull EntityPlayer player, World worldIn, BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos placePos = pos.offset(facing);
        if(worldIn.isAirBlock(placePos) && !player.isSneaking()) {
            worldIn.setBlockState(placePos, ModBlocksTT.ENERGETIC_NITOR.getDefaultState());
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean hasEffect(@NotNull ItemStack stack) {
        return this.isEnabled(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.format("tooltip.thaumictinkerer:" + (this.isEnabled(stack) ? "enabled" : "disabled")));
    }

    public boolean isEnabled(ItemStack stack) {
        return stack.getTagCompound() != null && stack.getTagCompound().getBoolean("enabled");
    }

    public void setEnabled(ItemStack stack, boolean enabled) {
        stack.setTagInfo("enabled", new NBTTagByte((byte) (enabled ? 1 : 0)));
    }
}
