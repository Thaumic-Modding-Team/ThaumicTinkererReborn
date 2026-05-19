package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.client.renderer.tile.TileEssentiaFunnelTESR;
import mod.emt.thaumictinkerer.tile.TileEssentiaFunnel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class BlockEssentiaFunnel extends BlockTileAddition {
    public static final AxisAlignedBB[] FUNNEL_AABBS = new AxisAlignedBB[] {
            new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.375, 0.9375),
            new AxisAlignedBB(0.1875, 0, 0.1875, 0.8125, 0.875, 0.8125),
    };
    public static final PropertyBool HAS_JAR = PropertyBool.create("has_jar");

    public BlockEssentiaFunnel() {
        super("essentia_funnel", Material.IRON, MapColor.STONE, TileEssentiaFunnel.class);
        this.setHardness(3.0f);
        this.setResistance(12.0f);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HAS_JAR, false));
    }

    public static void setHasJar(World world, BlockPos pos, boolean hasJar) {
        TileEntity tile = world.getTileEntity(pos);
        IBlockState state = world.getBlockState(pos);
        if(tile instanceof TileEssentiaFunnel && hasJar != state.getValue(BlockEssentiaFunnel.HAS_JAR)) {
            world.setBlockState(pos, state.withProperty(BlockEssentiaFunnel.HAS_JAR, hasJar));
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return FUNNEL_AABBS[state.getValue(HAS_JAR) ? 1 : 0];
    }

    @Override
    public boolean onBlockActivated(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileEssentiaFunnel) {
            IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
            if(handler != null) {
                ItemStack heldStack = playerIn.getHeldItem(hand);
                if(!heldStack.isEmpty()) {
                    ItemStack rem = ItemHandlerHelper.insertItem(handler, heldStack, true);
                    if (rem.isEmpty() || rem.getCount() != heldStack.getCount()) {
                        playerIn.setHeldItem(hand, ItemHandlerHelper.insertItem(handler, heldStack, false));
                        return true;
                    }
                }

                ItemStack extracted = handler.extractItem(0, 1, true);
                if(!extracted.isEmpty()) {
                    extracted = handler.extractItem(0, 1, false);
                    ItemHandlerHelper.giveItemToPlayer(playerIn, extracted, playerIn.inventory.currentItem);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos.down());
        return state.getBlock() == Blocks.HOPPER && super.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public void breakBlock(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileEssentiaFunnel) {
            ItemStack stack = ((TileEssentiaFunnel) tile).getJarStack();
            if(!stack.isEmpty()) {
                spawnAsEntity(worldIn, pos, stack);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(@NotNull IBlockState state, World worldIn, BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos) {
        IBlockState down = worldIn.getBlockState(pos.down());
        if(down.getBlock() != Blocks.HOPPER) {
            worldIn.destroyBlock(pos, true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public @NotNull BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean canRenderInLayer(@NotNull IBlockState state, @NotNull BlockRenderLayer layer) {
        boolean hasJar = state.getValue(HAS_JAR);
        return (!hasJar && layer == BlockRenderLayer.SOLID) || (hasJar && layer == BlockRenderLayer.TRANSLUCENT);
    }



    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(@NotNull IBlockState base_state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing side) {
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HAS_JAR, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HAS_JAR) ? 1 : 0;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HAS_JAR);
    }

    //##########################################################
    // IAspectContainer

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel(ModelRegistryEvent event) {
        super.registerModel(event);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEssentiaFunnel.class, new TileEssentiaFunnelTESR());
    }
}
