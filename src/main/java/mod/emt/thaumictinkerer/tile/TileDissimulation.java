package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileDissimulation extends TileEntityTT {
    private IBlockState storedState;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.storedState = this.deserializeStoredState(compound.getCompoundTag("storedState"));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("storedState", this.serializeStoredState());
        return compound;
    }

    @Nullable
    public IBlockState getStoredState() {
        return this.storedState;
    }

    public void setStoredState(@Nullable IBlockState state) {
        this.storedState = state;
        this.markDirty();
    }

    protected NBTTagCompound serializeStoredState() {
        NBTTagCompound tag = new NBTTagCompound();
        if(this.storedState != null && this.storedState.getBlock().getRegistryName() != null) {
            Block block = this.storedState.getBlock();
            tag.setString("block", block.getRegistryName().toString());
            tag.setInteger("meta", block.getMetaFromState(this.storedState));
        }
        return tag;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    protected IBlockState deserializeStoredState(NBTTagCompound compound) {
        if (!compound.isEmpty()) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(compound.getString("block")));
            int meta = compound.getInteger("meta");
            if(block != null && block != Blocks.AIR) {
                return block.getStateFromMeta(meta);
            }
        }
        return null;
    }
}
