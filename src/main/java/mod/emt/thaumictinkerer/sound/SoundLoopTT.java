package mod.emt.thaumictinkerer.sound;

import mod.emt.thaumictinkerer.tile.TileThaumicRestorer;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundLoopTT extends PositionedSound implements ITickableSound {
    protected TileThaumicRestorer restorer;
    protected BlockPos position;

    public SoundLoopTT(SoundEvent event, TileThaumicRestorer restorer, float volume) {
        super(event.getSoundName(), SoundCategory.BLOCKS);
        this.repeat = true;
        this.restorer = restorer;
        this.volume = volume;
        this.position = this.restorer.getPos();
        this.xPosF = position.getX();
        this.yPosF = position.getY();
        this.zPosF = position.getZ();
    }

    @Override
    public void update() {
        if (this.restorer.isInvalid() || !this.restorer.isRepairing()) {
            this.volume -= 0.05F;
        }
    }

    @Override
    public boolean isDonePlaying() {
        return this.volume <= 0.0F;
    }
}
