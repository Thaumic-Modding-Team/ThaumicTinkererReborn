package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.client.renderer.tile.TileNecromancyTabletTESR;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.registry.ModBlocksTT;
import mod.emt.thaumictinkerer.registry.ModItemsTT;
import mod.emt.thaumictinkerer.tile.TileNecromancyTablet;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
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
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.common.items.consumables.ItemPhial;

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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void registerRecipe(IForgeRegistry<IRecipe> registry) {
        //Necromancy Tablet
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "necromancy_tablet"), new InfusionRecipe(
                "TT_NECROMANCY",
                new ItemStack(this),
                5,
                new AspectList().add(Aspect.UNDEAD, 75).add(Aspect.LIFE, 75).add(Aspect.SOUL, 75).add(Aspect.DEATH, 50).add(Aspect.EXCHANGE, 50).add(Aspect.DESIRE, 30),
                ModBlocksTT.ARCANE_QUARTZ_BLOCK,
                Items.EGG,
                Items.PORKCHOP,
                Items.BEEF,
                Items.BONE,
                Items.ROTTEN_FLESH,
                Items.LEATHER,
                Items.SKULL,
                Blocks.MOSSY_COBBLESTONE,
                "blockDiamond"
        ));

        //Entity Souls
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "entity_soul_alien"), new InfusionRecipe(
                "TT_ALIEN_SOUL",
                new ItemStack(ModItemsTT.ENTITY_SOUL_ALIEN),
                4,
                new AspectList().add(Aspect.SOUL, 10),
                ItemPhial.makeFilledPhial(Aspect.ELDRITCH),
                ThaumcraftApiHelper.makeCrystal(Aspect.MOTION),
                ThaumcraftApiHelper.makeCrystal(Aspect.VOID)
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "entity_soul_arcane"), new InfusionRecipe(
                "TT_ARCANE_SOUL",
                new ItemStack(ModItemsTT.ENTITY_SOUL_ARCANE),
                2,
                new AspectList().add(Aspect.SOUL, 10),
                ItemPhial.makeFilledPhial(Aspect.MAGIC),
                ThaumcraftApiHelper.makeCrystal(Aspect.MAN),
                ThaumcraftApiHelper.makeCrystal(Aspect.AURA)
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "entity_soul_demonic"), new InfusionRecipe(
                "TT_DEMONIC_SOUL",
                new ItemStack(ModItemsTT.ENTITY_SOUL_DEMONIC),
                2,
                new AspectList().add(Aspect.SOUL, 10),
                ItemPhial.makeFilledPhial(Aspect.FIRE),
                ThaumcraftApiHelper.makeCrystal(Aspect.UNDEAD),
                ThaumcraftApiHelper.makeCrystal(Aspect.DARKNESS)
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "entity_soul_eldritch"), new InfusionRecipe(
                "TT_ELDRITCH_SOUL",
                new ItemStack(ModItemsTT.ENTITY_SOUL_ELDRITCH),
                8,
                new AspectList().add(Aspect.SOUL, 50).add(Aspect.ENTROPY, 50),
                ItemPhial.makeFilledPhial(Aspect.ELDRITCH),
                ThaumcraftApiHelper.makeCrystal(Aspect.FLUX),
                ThaumcraftApiHelper.makeCrystal(Aspect.SOUL)
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "entity_soul_hostile"), new InfusionRecipe(
                "TT_HOSTILE_SOUL",
                new ItemStack(ModItemsTT.ENTITY_SOUL_HOSTILE),
                2,
                new AspectList().add(Aspect.SOUL, 10),
                ItemPhial.makeFilledPhial(Aspect.AVERSION),
                ThaumcraftApiHelper.makeCrystal(Aspect.UNDEAD),
                ThaumcraftApiHelper.makeCrystal(Aspect.ENTROPY)
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "entity_soul_peaceful"), new InfusionRecipe(
                "TT_NECROMANCY",
                new ItemStack(ModItemsTT.ENTITY_SOUL_PEACEFUL),
                2,
                new AspectList().add(Aspect.SOUL, 10),
                ItemPhial.makeFilledPhial(Aspect.BEAST),
                ThaumcraftApiHelper.makeCrystal(Aspect.LIFE),
                ThaumcraftApiHelper.makeCrystal(Aspect.EARTH)
        ));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "entity_soul_tainted"), new InfusionRecipe(
                "TT_TAINTED_SOUL",
                new ItemStack(ModItemsTT.ENTITY_SOUL_TAINTED),
                6,
                new AspectList().add(Aspect.SOUL, 10),
                ItemPhial.makeFilledPhial(Aspect.FLUX),
                ThaumcraftApiHelper.makeCrystal(Aspect.ELDRITCH),
                ThaumcraftApiHelper.makeCrystal(Aspect.DARKNESS)
        ));
    }

    @Override
    public void registerResearchLocation() {
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(ThaumicTinkerer.MOD_ID, "research/optional/necromancy"));
    }

    @SideOnly(Side.CLIENT)
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
