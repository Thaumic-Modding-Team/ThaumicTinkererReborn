package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.client.renderer.tile.TileThaumicRestorerTESR;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.tile.TileThaumicRestorer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
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
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;

public class BlockThaumicRestorer extends BlockTileAddition {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final AxisAlignedBB AABB_RESTORER = new AxisAlignedBB(0, 0, 0, 1.0, 0.75, 1.0);

    public BlockThaumicRestorer() {
        super("thaumic_restorer", Material.IRON, MapColor.PURPLE, TileThaumicRestorer.class);
        this.setHardness(2.0f);
        this.setResistance(10.f);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return AABB_RESTORER;
    }

    @Override
    public boolean onBlockActivated(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof TileThaumicRestorer) {
            IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
            if(handler != null) {
                ItemStack heldStack = playerIn.getHeldItemMainhand();
                if(!heldStack.isEmpty()) {
                    if(ItemHandlerHelper.insertItem(handler, heldStack, true).isEmpty()) {
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
        if(tile instanceof TileThaumicRestorer) {
            IItemHandler handler = ((TileThaumicRestorer) tile).stackHandler;
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
    public boolean isSideSolid(@NotNull IBlockState base_state, IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        return side == EnumFacing.DOWN || side == state.getValue(FACING).getOpposite();
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
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public @NotNull IBlockState getStateForPlacement(@NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, @NotNull EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    //##########################################################
    // IBlockAddition

    @Override
    public void registerRecipe(IForgeRegistry<IRecipe> registry) {
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "thaumic_restorer"), new InfusionRecipe(
                "",
                new ItemStack(this),
                6,
                new AspectList().add(Aspect.CRAFT, 60).add(Aspect.TOOL, 45).add(Aspect.ORDER, 30).add(Aspect.MAGIC, 30),
                new ItemStack(Blocks.ANVIL, 1, 0),
                "plankWood",
                "leather",
                "cobblestone",
                ItemsTC.fabric,
                "ingotThaumium",
                "ingotIron",
                "ingotGold",
                "gemDiamond",
                "blockEmerald"
        ));
    }

    @Override
    public void registerResearchLocation() {
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(ThaumicTinkerer.MOD_ID, "research/optional/thaumic_restorer"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel(ModelRegistryEvent event) {
        super.registerModel(event);
        ClientRegistry.bindTileEntitySpecialRenderer(TileThaumicRestorer.class, new TileThaumicRestorerTESR());
    }

    @Override
    public boolean isEnabled() {
        return ConfigHandlerTT.thaumicRestorer.enable;
    }
}
