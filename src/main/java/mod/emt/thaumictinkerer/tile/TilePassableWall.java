package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.block.BlockPassableWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.casters.IInteractWithCaster;

public class TilePassableWall extends TileEntityTT implements ITickable, IInteractWithCaster {
    private static int COUNTDOWN_MAX = 100;
    private boolean interacted = false;
    private int countdown;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.countdown = compound.getInteger("countdown");
        this.interacted = compound.getBoolean("interacted");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("countdown", this.countdown);
        compound.setBoolean("interacted", this.interacted);
        return compound;
    }

    @Override
    public void update() {
        if(!this.world.isRemote) {
            if(this.countdown > 0) {
                this.countdown--;
                this.markDirty();
            } else if(this.interacted) {
                this.interacted = false;
                this.setPermeable(false);
            }
        }
    }

    @Override
    public boolean onCasterRightClick(World world, ItemStack itemStack, EntityPlayer entityPlayer, BlockPos blockPos, EnumFacing enumFacing, EnumHand enumHand) {
        this.countdown = COUNTDOWN_MAX;
        this.interacted = true;
        this.setPermeable(true);
        this.markDirty();
        return true;
    }

    public void setPermeable(boolean isPermeable) {
        IBlockState state = this.world.getBlockState(this.pos);
        if(state.getValue(BlockPassableWall.PASSABLE) != isPermeable) {
            TileEntity tile = this;
            this.world.setBlockState(this.pos, state.withProperty(BlockPassableWall.PASSABLE, isPermeable));
            if(this.countdown > 0) {
                tile.validate();
                this.world.setTileEntity(this.pos, tile);
            }
            this.markDirty();
        }
    }
}
