package mod.emt.thaumictinkerer.api.block;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockTileAddition extends BlockContainer implements IBlockAddition {
    private final Class<? extends TileEntity> tileClazz;

    public BlockTileAddition(String unlocName, Material materialIn, MapColor color, Class<? extends TileEntity> tileClazz) {
        super(materialIn, color);
        this.setRegistryName(ThaumicTinkerer.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
        this.tileClazz = tileClazz;
    }

    public BlockTileAddition(String unlocName, Material materialIn, Class<? extends TileEntity> tileClazz) {
        this(unlocName, materialIn, materialIn.getMaterialMapColor(), tileClazz);
    }

    @Override
    public boolean hasTileEntity(@NotNull IBlockState state) {
        return this.tileClazz != null;
    }

    @Override
    public @Nullable TileEntity createNewTileEntity(@NotNull World worldIn, int meta) {
        try {
            return this.tileClazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize TileEntity", e);
        }
    }

    @Override
    public void registerBlock(IForgeRegistry<Block> registry) {
        IBlockAddition.super.registerBlock(registry);
        GameRegistry.registerTileEntity(this.tileClazz, Objects.requireNonNull(this.getRegistryName()));
    }
}
