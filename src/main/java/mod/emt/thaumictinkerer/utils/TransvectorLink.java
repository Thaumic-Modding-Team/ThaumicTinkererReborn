package mod.emt.thaumictinkerer.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public class TransvectorLink implements INBTSerializable<NBTTagCompound> {
    public BlockPos pos;
    public EnumFacing face;

    public TransvectorLink(BlockPos pos, EnumFacing face) {
        this.pos = pos;
        this.face = face;
    }

    public TransvectorLink(NBTTagCompound tagCompound) {
        this.deserializeNBT(tagCompound);
    }

    @Nullable
    public TileEntity getTileEntity(World world) {
        return world.getTileEntity(this.pos);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("pos", pos.toLong());
        tag.setInteger("facing", this.face.ordinal());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.pos = BlockPos.fromLong(nbt.getLong("pos"));
        this.face = EnumFacing.VALUES[nbt.getInteger("facing")];
    }
}
