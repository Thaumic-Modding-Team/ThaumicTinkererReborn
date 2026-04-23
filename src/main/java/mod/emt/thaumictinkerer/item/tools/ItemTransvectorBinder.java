package mod.emt.thaumictinkerer.item.tools;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import mod.emt.thaumictinkerer.api.tile.ITransvectorLink;
import mod.emt.thaumictinkerer.utils.TransvectorLink;
import mod.emt.thaumictinkerer.utils.helpers.WorldHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemTransvectorBinder extends AbstractItemAddition {
    public ItemTransvectorBinder() {
        super("transvector_binder");
        this.setMaxStackSize(1);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        if(playerIn.isSneaking()) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            boolean faceMode = !this.isFaceLinkingMode(stack);
            if(faceMode) {
                playerIn.sendStatusMessage(new TextComponentTranslation("tooltip.thaumictinkerer:transvector_binder.link_mode.face"), true);
            } else {
                playerIn.sendStatusMessage(new TextComponentTranslation("tooltip.thaumictinkerer:transvector_binder.link_mode.block"), true);
            }
            this.setFaceLinkMode(stack, faceMode);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public @NotNull EnumActionResult onItemUseFirst(EntityPlayer player, World world, @NotNull BlockPos pos, @NotNull EnumFacing side, float hitX, float hitY, float hitZ, @NotNull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        TileEntity tile = world.getTileEntity(pos);
        if(tile != null) {
            if (player.isSneaking()) {
                this.linkToBlock(player, stack, pos, side);
            } else if (tile instanceof ITransvectorLink && this.isLinked(stack)) {
                TransvectorLink link = this.getTransvectorLink(stack);
                boolean isFaceLinking = this.isFaceLinkingMode(stack);
                ((ITransvectorLink) tile).createLink(player, hand, side, link, isFaceLinking);
            }
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    public void linkToBlock(EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing facing) {
        this.setTransvectorLink(stack, player.world, pos, facing);
        player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.link_stored"), true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        if(this.isFaceLinkingMode(stack)) {
            tooltip.add(I18n.format("tooltip.thaumictinkerer:transvector_binder.link_mode.face"));
        } else {
            tooltip.add(I18n.format("tooltip.thaumictinkerer:transvector_binder.link_mode.block"));
        }
        if(worldIn != null && this.isLinked(stack)) {
            TransvectorLink link = this.getTransvectorLink(stack);
            tooltip.add("  " + I18n.format("tooltip.thaumictinkerer:transvector_binder.linked_world", WorldHelper.getDimensionName(link.dimensionId)));
            tooltip.add("  " + I18n.format("tooltip.thaumictinkerer:transvector_binder.linked_face", link.face.toString()));
            tooltip.add("  " + I18n.format("tooltip.thaumictinkerer:transvector_binder.linked_pos", link.pos.getX(), link.pos.getY(), link.pos.getZ()));
        }
    }

    public boolean isLinked(ItemStack stack) {
        return !this.getTransvectorLink(stack).isEmpty();
    }

    public TransvectorLink getTransvectorLink(ItemStack stack) {
        return new TransvectorLink(stack.getTagCompound() != null ? stack.getTagCompound().getCompoundTag("link") : new NBTTagCompound());
    }

    public void setTransvectorLink(ItemStack stack, TransvectorLink link) {
        stack.setTagInfo("link", link.serializeNBT());
    }

    public void setTransvectorLink(ItemStack stack, World world, BlockPos pos, EnumFacing facing) {
        this.setTransvectorLink(stack, new TransvectorLink(world, pos, facing));
    }


    public boolean isFaceLinkingMode(ItemStack stack) {
        return stack.getTagCompound() != null && stack.getTagCompound().getBoolean("faceLinkMode");
    }

    public void setFaceLinkMode(ItemStack stack, boolean faceLinkMode) {
        stack.setTagInfo("faceLinkMode", new NBTTagByte((byte) (faceLinkMode ? 1 : 0)));
    }
}
