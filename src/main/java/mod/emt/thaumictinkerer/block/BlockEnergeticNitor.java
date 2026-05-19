package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.tile.TileEnergeticNitor;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

public class BlockEnergeticNitor extends BlockTileAddition {
    public static PropertyInteger LIGHT = PropertyInteger.create("light", 0, 10);

    public BlockEnergeticNitor() {
        super("energetic_nitor", Material.AIR, MapColor.AIR, TileEnergeticNitor.class);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LIGHT, 10));
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(@NotNull IBlockState state) {
        return state.getValue(LIGHT) + 5;
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
    public void registerItem(IForgeRegistry<Item> registry) {
        //Energetic Nitor block has no block item.
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LIGHT, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LIGHT);
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LIGHT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel(ModelRegistryEvent event) {
        super.registerModel(event);
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LIGHT).build());
    }
}
