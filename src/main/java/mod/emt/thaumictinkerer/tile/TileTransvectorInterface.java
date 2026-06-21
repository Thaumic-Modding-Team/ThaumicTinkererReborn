package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.ITransvectorLink;
import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.utils.TransvectorLink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
        this.linkedFacing.forEach((face, link) -> compound.setTag(face.getName(), link.serializeNBT()));
        return compound;
    }

    @Override
    public void createLink(EntityPlayer player, EnumHand hand, EnumFacing clickedFace, TransvectorLink link, boolean isFaceLinking) {
        if (link.isSelf(this.world, this.pos)) {
            player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.self_link"), true);
        } else {
            if (!this.isInRange(link)) {
                player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.out_of_range"), true);
            } else {
                if (isFaceLinking) {
                    this.linkToFace(clickedFace, link);
                } else {
                    this.linkToPosition(link);
                }
                player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.link_successful"), true);
            }
        }
    }

    public boolean isInRange(TransvectorLink link) {
        int range = ConfigHandlerTT.transvectorInterface.maxRange;
        return link.getWorld() != null
                && this.world.provider.getDimension() == link.dimensionId
                && this.pos.getX() - link.pos.getX() <= range
                && this.pos.getY() - link.pos.getY() <= range
                && this.pos.getZ() - link.pos.getZ() <= range;
    }

    public void linkToPosition(TransvectorLink link) {
        if(link.getWorld() != null) {
            for (EnumFacing facing : EnumFacing.VALUES) {
                this.linkedFacing.put(facing, new TransvectorLink(link.getWorld(), link.pos, facing));
            }
            this.markDirty();
        }
    }

    public void linkToFace(EnumFacing transvectorFace, TransvectorLink link) {
        this.linkedFacing.put(transvectorFace, link);
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
            TileEntity tile = linked.getTileEntity();
            if(tile != null && !(tile instanceof TileTransvectorInterface)) {
                return tile.hasCapability(capability, linked.face);
            }
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public @Nullable <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        TransvectorLink linked = this.getLinkedFacing(facing);
        if(linked != null) {
            TileEntity tile = linked.getTileEntity();
            if(tile != null && !(tile instanceof TileTransvectorInterface)) {
                return tile.getCapability(capability, linked.face);
            }
        }
        return super.getCapability(capability, facing);
    }
}
