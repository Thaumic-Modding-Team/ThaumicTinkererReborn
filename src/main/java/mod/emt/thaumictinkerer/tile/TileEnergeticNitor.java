package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.block.BlockEnergeticNitor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import thaumcraft.client.fx.FXDispatcher;

public class TileEnergeticNitor extends TileEntity implements ITickable {
    public int count = 0;
    public int duration = 20;

    @Override
    public void update() {
        if(!this.world.isRemote && this.world.getTotalWorldTime() % 20L == 0) {
            if(this.duration > 0) {
                this.duration--;
                if(this.duration < 10) {
                    this.reduceLightLevel();
                }
            } else {
                this.world.setBlockToAir(this.pos);
            }
        }

        if(this.world.isRemote) {
            FXDispatcher.INSTANCE.drawNitorFlames(
                    this.pos.getX() + 0.5F + this.world.rand.nextGaussian() * 0.025D,
                    this.pos.getY() + 0.45F + this.world.rand.nextGaussian() * 0.025D,
                    this.pos.getZ() + 0.5F + this.world.rand.nextGaussian() * 0.025D,
                    this.world.rand.nextGaussian() * 0.0025D,
                    this.world.rand.nextFloat() * 0.06F,
                    this.world.rand.nextGaussian() * 0.0025D,
                    15606528, 0);
            if(this.count++ % 10 == 0) {
                FXDispatcher.INSTANCE.drawNitorCore(
                        this.pos.getX() + 0.5F, this.pos.getY() + 0.49F, this.pos.getZ() + 0.5F,
                        0.0, 0.0, 0.0);
            }
        }
    }

    public void reduceLightLevel() {
        IBlockState state = this.world.getBlockState(this.pos);
        TileEnergeticNitor tile = this;
        this.world.setBlockState(this.pos, state.withProperty(BlockEnergeticNitor.LIGHT, this.duration));
        tile.validate();
        this.world.setTileEntity(this.pos, tile);
    }
}
