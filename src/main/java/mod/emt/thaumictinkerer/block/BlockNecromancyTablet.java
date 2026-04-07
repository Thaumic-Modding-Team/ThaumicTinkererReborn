package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.client.renderer.tile.TileNecromancyTabletTESR;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.tile.TileNecromancyTablet;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectList;

import java.util.Map;

public class BlockNecromancyTablet extends BlockTileAddition {
    public static final AxisAlignedBB TABLET_AABB = new AxisAlignedBB(0, 0, 0, 1.0, 0.125, 1.0);
    public static final PropertyBool ENABLED = PropertyBool.create("enabled");

    public BlockNecromancyTablet() {
        super("necromancy_tablet", Material.ROCK, MapColor.PURPLE, TileNecromancyTablet.class);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ENABLED, false));
    }

    public static void setTabletActiveState(World world, BlockPos pos, boolean isActive) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TileNecromancyTablet) {
            IBlockState state = world.getBlockState(pos);
            world.setBlockState(pos, state.withProperty(ENABLED, isActive));
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return TABLET_AABB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileNecromancyTablet) {
            IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
            if(handler != null) {
                ItemStack heldStack = playerIn.getHeldItemMainhand();
                if(!heldStack.isEmpty()) {
                    ItemStack rem = ItemHandlerHelper.insertItem(handler, heldStack, true);
                    if(rem.isEmpty() || rem.getCount() != heldStack.getCount()) {
                        playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemHandlerHelper.insertItem(handler, heldStack, false));
                        return true;
                    }
                }

                ItemStack extracted = handler.extractItem(0, 1, true);
                if(!extracted.isEmpty()) {
                    extracted = handler.extractItem(0, 1, false);
                    ItemHandlerHelper.giveItemToPlayer(playerIn, extracted, playerIn.inventory.currentItem);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileNecromancyTablet) {
            IItemHandler handler = ((TileNecromancyTablet) tile).stackHandler;
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
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ENABLED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ENABLED) ? 1 : 0;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ENABLED);
    }

    //##########################################################
    // IBlockAddition

    @Override
    public void registerRecipe(IForgeRegistry<IRecipe> registry) {
        //TODO
    }

    @Override
    public void registerResearchLocation() {
        //TODO
    }

    @Override
    public void registerAspects(AspectEventProxy registry, Map<ItemStack, AspectList> aspectMap) {
        //TODO
    }

    @Override
    public void registerModel(ModelRegistryEvent event) {
        super.registerModel(event);
        ClientRegistry.bindTileEntitySpecialRenderer(TileNecromancyTablet.class, new TileNecromancyTabletTESR());
    }

    @Override
    public boolean isEnabled() {
        return ConfigHandlerTT.necromancyTablet.enable;
    }
}
