package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.tile.TileTransvectorDislocator;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockTransvectorDislocator extends BlockTileAddition {
    public BlockTransvectorDislocator() {
        super("transvector_dislocator", Material.ROCK, MapColor.STONE, TileTransvectorDislocator.class);
        this.setHardness(2.0f);
        this.setResistance(12.0f);
        this.setSoundType(SoundType.STONE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(@NotNull IBlockState state, World worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileTransvectorDislocator && !((TileTransvectorDislocator) tile).isSwapping()) {
            EnumFacing face = EnumFacing.getFacingFromVector(pos.getX() - fromPos.getX(), pos.getY() - fromPos.getY(), pos.getZ() - fromPos.getZ());
            int prevPower = ((TileTransvectorDislocator) tile).getRedstonePower();
            int currPower = worldIn.getRedstonePower(pos, face);
            if(prevPower != currPower) {
                ((TileTransvectorDislocator) tile).setRedstonePower(currPower);
            }
        }
    }
}
