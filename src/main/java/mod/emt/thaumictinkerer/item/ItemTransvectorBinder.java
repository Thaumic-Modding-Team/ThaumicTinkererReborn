package mod.emt.thaumictinkerer.item;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import mod.emt.thaumictinkerer.tile.TileTransvectorInterface;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagLong;
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
import java.util.Objects;

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
                this.setLinkedPos(stack, pos);
                this.setLinkedFacing(stack, side);
                player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.link_stored"), true);
            } else if (tile instanceof TileTransvectorInterface && this.isLinked(stack)) {
                BlockPos linkPos = this.getLinkedPos(stack);
                if (!pos.equals(linkPos)) {
                    if (this.isInRange(pos, linkPos)) {
                        if (this.isFaceLinkingMode(stack)) {
                            EnumFacing linkFace = this.getLinkedFacing(stack);
                            ((TileTransvectorInterface) tile).linkToFace(side, linkPos, linkFace);
                        } else {
                            ((TileTransvectorInterface) tile).linkToPosition(linkPos);
                        }
                        player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.link_successful"), true);
                    } else {
                        player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.out_of_range"), true);
                    }
                } else {
                    player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.self_link"), true);
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        if(this.isFaceLinkingMode(stack)) {
            tooltip.add(I18n.format("tooltip.thaumictinkerer:transvector_binder.link_mode.face"));
        } else {
            tooltip.add(I18n.format("tooltip.thaumictinkerer:transvector_binder.link_mode.block"));
        }
        if(this.isLinked(stack)) {
            BlockPos pos = this.getLinkedPos(stack);
            EnumFacing facing = this.getLinkedFacing(stack);
            tooltip.add("  " + I18n.format("tooltip.thaumictinkerer:transvector_binder.linked_face", Objects.requireNonNull(facing).toString()));
            tooltip.add("  " + I18n.format("tooltip.thaumictinkerer:transvector_binder.linked_pos", pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    public boolean isLinked(ItemStack stack) {
        return this.getLinkedFacing(stack) != null && this.getLinkedPos(stack) != null;
    }

    public boolean isInRange(BlockPos interfacePos, BlockPos linkedPos) {
        //TODO: Configurable distance
        return interfacePos.getX() - linkedPos.getX() < 8
                && interfacePos.getY() - linkedPos.getY() < 8
                && interfacePos.getZ() - linkedPos.getZ() < 8;
    }

    public BlockPos getLinkedPos(ItemStack stack) {
        return stack.getTagCompound() != null ? BlockPos.fromLong(stack.getTagCompound().getLong("pos")) : null;
    }

    public void setLinkedPos(ItemStack stack, BlockPos pos) {
        stack.setTagInfo("pos", new NBTTagLong(pos.toLong()));
    }

    @Nullable
    public EnumFacing getLinkedFacing(ItemStack stack) {
        return stack.getTagCompound() != null ? EnumFacing.VALUES[stack.getTagCompound().getInteger("facing")] : null;
    }

    public void setLinkedFacing(ItemStack stack, EnumFacing facing) {
        stack.setTagInfo("facing", new NBTTagInt(facing.ordinal()));
    }

    public boolean isFaceLinkingMode(ItemStack stack) {
        return stack.getTagCompound() != null && stack.getTagCompound().getBoolean("faceLinkMode");
    }

    public void setFaceLinkMode(ItemStack stack, boolean faceLinkMode) {
        stack.setTagInfo("faceLinkMode", new NBTTagByte((byte) (faceLinkMode ? 1 : 0)));
    }

    public void clearLink(ItemStack stack) {
        if(stack.getTagCompound() != null) {
            stack.getTagCompound().removeTag("pos");
            stack.getTagCompound().removeTag("facing");
        }
    }
}
