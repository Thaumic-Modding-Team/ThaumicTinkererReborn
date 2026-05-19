package mod.emt.thaumictinkerer.utils;

import mod.emt.thaumictinkerer.utils.helpers.WorldHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public class TransvectorLink implements INBTSerializable<NBTTagCompound> {
    public static final TransvectorLink EMPTY = new TransvectorLink(new NBTTagCompound());
    public int dimensionId;
    public BlockPos pos;
    public EnumFacing face;
    private World world;
    private boolean isEmpty;

    public TransvectorLink(World world, BlockPos pos, EnumFacing face) {
        this.dimensionId = world.provider.getDimension();
        this.world = world;
        this.pos = pos;
        this.face = face;
    }

    public TransvectorLink(NBTTagCompound tagCompound) {
        this.isEmpty = tagCompound.isEmpty();
        this.deserializeNBT(tagCompound);
    }

    @Nullable
    public World getWorld() {
        if(this.isEmpty()) {
            return null;
        }

        if(this.world == null) {
            this.world = WorldHelper.getWorldFromId(this.dimensionId, false);
        }
        return world;
    }

    public boolean isSelf(World world, BlockPos pos) {
        World checkWorld = this.getWorld();
        return checkWorld != null && checkWorld.provider.getDimension() == world.provider.getDimension() && this.pos.equals(pos);
    }

    public boolean isEmpty() {
        return this == EMPTY || this.isEmpty;
    }

    @Nullable
    public TileEntity getTileEntity() {
        if(!this.isEmpty()) {
            World checkWorld = this.getWorld();
            if (checkWorld != null) {
                return checkWorld.getTileEntity(this.pos);
            }
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("dimensionId", this.dimensionId);
        tag.setLong("pos", this.pos.toLong());
        tag.setInteger("facing", this.face.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.dimensionId = nbt.getInteger("dimensionId");
        this.pos = BlockPos.fromLong(nbt.getLong("pos"));
        this.face = EnumFacing.VALUES[nbt.getInteger("facing")];
    }
}
