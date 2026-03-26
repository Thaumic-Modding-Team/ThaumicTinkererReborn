package mod.emt.thaumictinkerer.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEnergeticNitor extends TileEntity implements ITickable {
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
    }
}
