package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.ITransvectorLink;
import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.utils.TransvectorLink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TileTransvectorInterface extends TileEntityTT implements ITransvectorLink {
    protected  Map<EnumFacing, TransvectorLink> linkedFacing = new HashMap<>(6);

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        for(EnumFacing facing : EnumFacing.VALUES) {
            if(compound.hasKey(facing.getName())) {
                TransvectorLink link = new TransvectorLink(compound.getCompoundTag(facing.getName()));
                this.linkedFacing.put(facing, link);
            }
        }
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.linkedFacing.forEach((face, link) -> {
            compound.setTag(face.getName(), link.serializeNBT());
        });
        return compound;
    }

    @Override
    public void createLink(EntityPlayer player, EnumHand hand, EnumFacing clickedFace, BlockPos linkPos, EnumFacing linkFace, boolean isFaceLinking) {
        if (this.pos.equals(linkPos)) {
            player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.self_link"), true);
        } else {
            if (!this.isInRange(linkPos)) {
                player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.out_of_range"), true);
            } else {
                if (isFaceLinking) {
                    this.linkToFace(clickedFace, linkPos, linkFace);
                } else {
                    this.linkToPosition(linkPos);
                }
                player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.link_successful"), true);
            }
        }
    }

    public boolean isInRange(BlockPos linkPos) {
        //TODO: Configurable distance
        return this.pos.getX() - linkPos.getX() < 8
                && this.pos.getY() - linkPos.getY() < 8
                && this.pos.getZ() - linkPos.getZ() < 8;
    }

    public void linkToPosition(BlockPos linkPos) {
        for(EnumFacing facing : EnumFacing.VALUES) {
            this.linkedFacing.put(facing, new TransvectorLink(linkPos, facing));
        }
        this.markDirty();
    }

    public void linkToFace(EnumFacing transvectorFace, BlockPos linkPos, EnumFacing linkFace) {
        this.linkedFacing.put(transvectorFace, new TransvectorLink(linkPos, linkFace));
        this.markDirty();
    }

    @Nullable
    public TransvectorLink getLinkedFacing(EnumFacing transvectorFace) {
        return this.linkedFacing.get(transvectorFace);
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        TransvectorLink linked = this.getLinkedFacing(facing);
        if(linked != null) {
            TileEntity tile = linked.getTileEntity(this.world);
            if(tile != null) {
                return tile.hasCapability(capability, linked.face);
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public @Nullable <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        TransvectorLink linked = this.getLinkedFacing(facing);
        if(linked != null) {
            TileEntity tile = linked.getTileEntity(this.world);
            if(tile != null) {
                return tile.getCapability(capability, linked.face);
            }
        }
        return super.getCapability(capability, facing);
    }
}
