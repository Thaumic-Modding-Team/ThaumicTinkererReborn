package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.block.IBlockAddition;
import mod.emt.thaumictinkerer.tile.TileEnergeticNitor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BlockEnergeticNitor extends BlockContainer implements IBlockAddition {
    public BlockEnergeticNitor() {
        super(Material.AIR, MapColor.AIR);
        this.setRegistryName(ThaumicTinkerer.MOD_ID, "energetic_nitor");
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
    }

    @Override
    public boolean hasTileEntity(@NotNull IBlockState state) {
        return true;
    }

    @Override
    public @Nullable TileEntity createNewTileEntity(@NotNull World worldIn, int meta) {
        return new TileEnergeticNitor();
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(@NotNull IBlockState state) {
        return 15;
    }

    @Override
    public boolean isReplaceable(@NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean causesSuffocation(@NotNull IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public void registerBlock(IForgeRegistry<Block> registry) {
        IBlockAddition.super.registerBlock(registry);
        GameRegistry.registerTileEntity(TileEnergeticNitor.class, Objects.requireNonNull(this.getRegistryName()));
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry) {
        //Energetic Nitor block has no block item.
    }
}
