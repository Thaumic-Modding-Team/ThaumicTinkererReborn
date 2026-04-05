package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.block.BlockEtherealPlatform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEtherealPlatform extends TileEntityTT implements ITickable {
    @Override
    public void update() {
        if(!this.world.isRemote) {
            boolean setEthereal = false;

            AxisAlignedBB above = new AxisAlignedBB(
                    this.pos.getX() - 1, this.pos.getY() + 1, this.pos.getZ() - 1,
                    this.pos.getX() + 1, this.pos.getY() + 2, this.pos.getZ() + 1);
            AxisAlignedBB below = new AxisAlignedBB(
                    this.pos.getX() - 1, this.pos.getY() - 3, this.pos.getZ() - 1,
                    this.pos.getX() + 1, this.pos.getY(), this.pos.getZ() + 1);

            List<EntityPlayer> playersAbove = this.world.getEntitiesWithinAABB(EntityPlayer.class, above);
            List<EntityPlayer> playersBelow = this.world.getEntitiesWithinAABB(EntityPlayer.class, below);

            if(!playersBelow.isEmpty()) {
                setEthereal = true;
                for (EntityPlayer player : playersAbove) {
                    if(!player.isSneaking()) {
                        setEthereal = false;
                        break;
                    }
                }
            } else {
                for (EntityPlayer player : playersAbove) {
                    if (player.isSneaking()) {
                        setEthereal = true;
                        break;
                    }
                }
            }


            this.setEthereal(setEthereal);
        }
    }

    public void setEthereal(boolean ethereal) {
        IBlockState state = this.world.getBlockState(this.pos);
        if(state.getValue(BlockEtherealPlatform.ETHEREAL) != ethereal) {
            this.world.setBlockState(this.pos, state.withProperty(BlockEtherealPlatform.ETHEREAL, ethereal));
        }
    }
}
