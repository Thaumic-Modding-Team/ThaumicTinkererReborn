package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.client.renderer.tile.TileAdvancedRechargerTESR;
import mod.emt.thaumictinkerer.tile.TileAdvancedRecharger;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class BlockAdvancedRecharger extends BlockTileAddition {
    public static final AxisAlignedBB AABB_RECHARGER = new AxisAlignedBB(0, 0, 0, 1.0, 1.0, 1.0);

    public BlockAdvancedRecharger() {
        super("advanced_recharger", Material.ROCK, MapColor.STONE, TileAdvancedRecharger.class);
        this.setHardness(2.0f);
        this.setResistance(10.0f);
        this.setSoundType(SoundType.STONE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return AABB_RECHARGER;
    }

    @Override
    public boolean onBlockActivated(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileAdvancedRecharger) {
            TileAdvancedRecharger pedestal = (TileAdvancedRecharger) tile;
            ItemStack heldStack = playerIn.getHeldItem(hand);
            IItemHandler handler = pedestal.getStackHandler();
            if(pedestal.getRechargingStack().isEmpty()) {
                ItemStack toInsert = ItemHandlerHelper.copyStackWithSize(heldStack, 1);
                ItemStack remaining = ItemHandlerHelper.insertItem(handler, toInsert, true);
                if(remaining.isEmpty() || heldStack.getCount() != remaining.getCount()) {
                    ItemHandlerHelper.insertItem(handler, toInsert, false);
                    heldStack.shrink(1);
                }
            } else {
                ItemStack extracted = handler.extractItem(0, handler.getSlotLimit(0), false);
                if(!extracted.isEmpty()) {
                    ItemHandlerHelper.giveItemToPlayer(playerIn, extracted, playerIn.inventory.currentItem);
                }
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileAdvancedRecharger) {
            IItemHandler handler = ((TileAdvancedRecharger) tile).getStackHandler();
            for(int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if(!stack.isEmpty()) {
                    spawnAsEntity(worldIn, pos, stack.copy());
                }
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(@NotNull IBlockState base_state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing side) {
        return side == EnumFacing.DOWN;
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

    //##########################################################
    // IBlockAddition

    @Override
    public void registerModel(ModelRegistryEvent event) {
        super.registerModel(event);
        ClientRegistry.bindTileEntitySpecialRenderer(TileAdvancedRecharger.class, new TileAdvancedRechargerTESR());
    }
}
