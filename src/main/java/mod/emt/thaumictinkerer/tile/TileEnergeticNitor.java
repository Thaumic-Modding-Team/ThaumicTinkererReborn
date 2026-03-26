package mod.emt.thaumictinkerer.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import thaumcraft.client.fx.FXDispatcher;

public class TileEnergeticNitor extends TileEntity implements ITickable {
    public int count = 0;
    //TODO: Configurable duration?
    public int duration = 20;

    @Override
    public void update() {
        if(!this.world.isRemote && this.world.getTotalWorldTime() % 20L == 0) {
            if(this.duration > 0) {
                this.duration--;
            } else {
                this.world.setBlockToAir(this.pos);
            }
        }

        if(world.isRemote) {
            FXDispatcher.INSTANCE.drawNitorFlames(pos.getX() + 0.5F + world.rand.nextGaussian() * 0.025D, pos.getY() + 0.45F + world.rand.nextGaussian() * 0.025D,
                    pos.getZ() + 0.5F + world.rand.nextGaussian() * 0.025D, world.rand.nextGaussian() * 0.0025D, world.rand.nextFloat() * 0.06F, world.rand.nextGaussian() * 0.0025D, 15606528, 0);
            if(count++ % 10 == 0) {
                FXDispatcher.INSTANCE.drawNitorCore(pos.getX() + 0.5F, pos.getY() + 0.49F, pos.getZ() + 0.5F, 0.0, 0.0, 0.0);
            }
        }
    }
}
