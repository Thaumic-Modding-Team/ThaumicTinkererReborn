package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.tile.TileDissimulation;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockDissimulation extends BlockTileAddition {
    //TODO: The One Probe support for stored blocks (also fixes error tooltip)

    public BlockDissimulation() {
        super("dissimulation_block", Material.ROCK, MapColor.STONE, TileDissimulation.class);
        this.setHardness(3.0f);
        this.setResistance(20.0f);
        this.setSoundType(SoundType.STONE);
    }

    public IBlockState getStoredState(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileDissimulation && ((TileDissimulation) tile).getStoredState() != null) {
            return ((TileDissimulation) tile).getStoredState();
        }
        return this.getDefaultState();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getSelectedBoundingBox(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos) {
        IBlockState storedState = this.getStoredState(worldIn, pos);
        return storedState.getBlock() != this ? storedState.getSelectedBoundingBox(worldIn, pos) : FULL_BLOCK_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @Nullable AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        IBlockState storedState = this.getStoredState(worldIn, pos);
        return storedState.getBlock() != this ? storedState.getCollisionBoundingBox(worldIn, pos) : FULL_BLOCK_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(@NotNull IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull AxisAlignedBB entityBox, @NotNull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        IBlockState storedState = this.getStoredState(worldIn, pos);
        if (storedState.getBlock() != this) {
            storedState.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, false);
        } else {
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldStack = playerIn.getHeldItem(hand);
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileDissimulation) {
            if(heldStack.isEmpty() && playerIn.isSneaking()) {
                ((TileDissimulation) tile).setStoredState(null);
                return true;
            }

            if(!heldStack.isEmpty() && heldStack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) heldStack.getItem()).getBlock();
                IBlockState placement = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, heldStack.getItemDamage(), playerIn, hand);
                if(placement.getRenderType() == EnumBlockRenderType.MODEL) {
                    ((TileDissimulation) tile).setStoredState(placement);
                }
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getActualState(@NotNull IBlockState state, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return this.getStoredState(worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(@NotNull IBlockState state, IBlockAccess world, @NotNull BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileDissimulation && ((TileDissimulation) tile).getStoredState() != null) {
            return ((TileDissimulation) tile).getStoredState().getLightValue();
        }
        return 0;
    }

    @Override
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(@NotNull IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isNormalCube(@NotNull IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean causesSuffocation(@NotNull IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isTranslucent(@NotNull IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldSideBeRendered(@NotNull IBlockState blockState, @NotNull IBlockAccess blockAccess, @NotNull BlockPos pos, @NotNull EnumFacing side) {
        IBlockState storedState = this.getStoredState(blockAccess, pos);
        return storedState.getBlock() != this ? storedState.shouldSideBeRendered(blockAccess, pos, side) : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess worldIn, @NotNull IBlockState state, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        IBlockState storedState = this.getStoredState(worldIn, pos);
        return storedState.getBlock() != this ? storedState.getBlockFaceShape(worldIn, pos, face) : super.getBlockFaceShape(worldIn, state, pos, face);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getPackedLightmapCoords(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        IBlockState storedState = this.getStoredState(source, pos);
        return storedState.getBlock() != this ? storedState.getPackedLightmapCoords(source, pos) : super.getPackedLightmapCoords(state, source, pos);
    }

    @Override
    public boolean isNormalCube(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
        IBlockState storedState = this.getStoredState(world, pos);
        return storedState.getBlock() != this ? storedState.getBlock().isNormalCube(storedState, world, pos) : super.isNormalCube(state, world, pos);
    }

    @Override
    public boolean doesSideBlockRendering(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        IBlockState storedState = this.getStoredState(world, pos);
        return storedState.getBlock() != this ? storedState.doesSideBlockRendering(world, pos, face) : super.doesSideBlockRendering(state, world, pos, face);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(@NotNull IBlockState base_state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing side) {
        IBlockState storedState = this.getStoredState(world, pos);
        return storedState.getBlock() != this ? storedState.isSideSolid(world, pos, side) : super.isSideSolid(base_state, world, pos, side);
    }

    @Override
    public int getLightOpacity(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
        IBlockState storedState = this.getStoredState(world, pos);
        return storedState.getBlock() != this ? storedState.getLightOpacity(world, pos) : super.getLightOpacity(state, world, pos);
    }

    @Override
    public @NotNull SoundType getSoundType(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @Nullable Entity entity) {
        IBlockState storedState = this.getStoredState(world, pos);
        return storedState.getBlock() != this ? storedState.getBlock().getSoundType(storedState, world, pos, entity) : super.getSoundType(state, world, pos, entity);
    }
}
