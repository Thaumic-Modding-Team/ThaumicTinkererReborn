package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.tile.TileTransvectorInterface;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import org.jetbrains.annotations.NotNull;

public class BlockTransvectorInterface extends BlockTileAddition {
    public BlockTransvectorInterface() {
        super("transvector_interface", Material.IRON, MapColor.OBSIDIAN, TileTransvectorInterface.class);
        this.setSoundType(SoundType.METAL);
        this.setHardness(3.0f);
        this.setResistance(12.0f);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
