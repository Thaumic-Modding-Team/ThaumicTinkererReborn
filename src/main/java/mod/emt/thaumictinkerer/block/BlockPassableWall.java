package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.tile.TilePassableWall;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockPassableWall extends BlockTileAddition {
    public static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    public static final PropertyInteger LEVEL = BlockLiquid.LEVEL;
    public static final PropertyBool PASSABLE = PropertyBool.create("passable");

    public BlockPassableWall() {
        super("passable_wall", Material.ROCK, MapColor.STONE, TilePassableWall.class);
        this.setHardness(3.0f);
        this.setResistance(20.0f);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(PASSABLE, false).withProperty(LEVEL, 15));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return state.getValue(PASSABLE) ? EMPTY_AABB : super.getBoundingBox(state, source, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @Nullable AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return blockState.getValue(PASSABLE) ? NULL_AABB : FULL_BLOCK_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull AxisAlignedBB entityBox, @NotNull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if(!state.getValue(PASSABLE)) {
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, @NotNull BlockPos pos, @NotNull BlockPos neighbor) {
        IBlockState state = world.getBlockState(pos);
        IBlockState neighborState = world.getBlockState(neighbor);
        if(neighborState.getBlock() == this && world instanceof World) {
            boolean passable = neighborState.getValue(PASSABLE);
            if(state.getValue(PASSABLE) != passable) {
                ((World) world).setBlockState(pos, state.withProperty(PASSABLE, passable));
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull Material getMaterial(IBlockState state) {
        return state.getValue(PASSABLE) ? Material.WATER : super.getMaterial(state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return !state.getValue(PASSABLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return !state.getValue(PASSABLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean causesSuffocation(IBlockState state) {
        return !state.getValue(PASSABLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldSideBeRendered(@NotNull IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, @NotNull EnumFacing side) {
        return blockAccess.getBlockState(pos.offset(side)).getBlock() != this && (side == EnumFacing.UP || super.shouldSideBeRendered(blockState, blockAccess, pos, side));
    }

    @Override
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, @NotNull BlockRenderLayer layer) {
        if(!state.getValue(PASSABLE)) {
            return layer == BlockRenderLayer.SOLID;
        }
        return super.canRenderInLayer(state, layer);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(PASSABLE, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(PASSABLE) ? 1 : 0;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PASSABLE, LEVEL);
    }

    @Override
    public void registerModel(ModelRegistryEvent event) {
        super.registerModel(event);
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
    }
}
