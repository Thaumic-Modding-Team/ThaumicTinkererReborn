package mod.emt.thaumictinkerer.block;

import com.invadermonky.magicultureintegrations.api.block.IHarvestableCrop;
import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.block.IBlockAddition;
import mod.emt.thaumictinkerer.registry.ModBlocksTT;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.item.IHornHarvestable;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Optional.InterfaceList({
        @Optional.Interface(modid = "botania", iface = "vazkii.botania.api.item.IHornHarvestable"),
        @Optional.Interface(modid = "magicultureintegrations", iface = "com.invadermonky.magicultureintegrations.api.block.IHarvestableCrop")
})
public class BlockUmbralRosebush extends BlockBush implements IGrowable, IShearable, IHarvestableCrop, IHornHarvestable, IBlockAddition {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    //TODO: Corrected bounding boxes
    private static final AxisAlignedBB[] AABB_BUSH = new AxisAlignedBB[] {
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)
    };

    public BlockUmbralRosebush() {
        this.setRegistryName(ThaumicTinkerer.MOD_ID, "umbral_rosebush");
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
        this.setSoundType(SoundType.PLANT);
        this.setHardness(0.0F);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), 0));
        this.disableStats();
    }



    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return AABB_BUSH[state.getValue(AGE)];
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onBlockActivated(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldStack = playerIn.getHeldItem(hand);
        int age = this.getAge(state);
        if(heldStack.getItem() instanceof ItemShears && age >= 4) {
            worldIn.setBlockState(pos, this.withAge(age - 1), 2);
            spawnAsEntity(worldIn, pos, new ItemStack(ModBlocksTT.UMBRAL_ROSE));
            playerIn.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0f, 1.0f);
            if(!playerIn.isCreative()) {
                heldStack.damageItem(1, playerIn);
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean canSustainBush(@NotNull IBlockState state) {
        return state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND;
    }

    @Override
    public void updateTick(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand) {
        this.checkAndDropBlock(worldIn, pos, state);
        if(!worldIn.isAreaLoaded(pos, 1)) return;
        if(worldIn.getLightFromNeighbors(pos.up()) >= 9) {
            int age = this.getAge(state);
            if(age < this.getMaxAge()) {
                float chance = this.getGrowthChance(worldIn, pos);
                if(ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0 / chance ) + 1) == 0)) {
                    worldIn.setBlockState(pos, withAge(age + 1), 2);
                    ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void getDrops(@NotNull NonNullList<ItemStack> drops, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull IBlockState state, int fortune) {
        int age = this.getAge(state);
        if(age >= 4) {
            for(int i = age; i <= this.getMaxAge(); i++) {
                drops.add(new ItemStack(ModBlocksTT.UMBRAL_ROSE));
            }
        }
    }

    public void grow(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
        int age = this.getAge(state);
        age += (age < 4 ? worldIn.rand.nextInt(2) + 1 : 1);
        int maxAge = this.getMaxAge();
        worldIn.setBlockState(pos, this.withAge(Math.min(age, maxAge)), 2);
    }

    protected PropertyInteger getAgeProperty()
    {
        return AGE;
    }

    public int getMaxAge() {
        return 7;
    }

    public int getAge(IBlockState state) {
        return state.getValue(this.getAgeProperty());
    }

    public IBlockState withAge(int age) {
        return this.getDefaultState().withProperty(this.getAgeProperty(), age);
    }

    public boolean isMaxAge(IBlockState state) {
        return state.getValue(this.getAgeProperty()) >= this.getMaxAge();
    }

    protected float getGrowthChance(World world, BlockPos pos) {
        float chance = 1.0F;
        BlockPos downPos = pos.down();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float bonus = 0.0F;
                IBlockState iblockstate = world.getBlockState(downPos.add(i, 0, j));

                if (iblockstate.getBlock().canSustainPlant(iblockstate, world, downPos.add(i, 0, j), EnumFacing.UP, this)) {
                    bonus = 1.0F;
                    if (iblockstate.getBlock().isFertile(world, downPos.add(i, 0, j))) {
                        bonus = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    bonus /= 4.0F;
                }

                chance += bonus;
            }
        }

        BlockPos northPos = pos.north();
        BlockPos southPos = pos.south();
        BlockPos westPos = pos.west();
        BlockPos eastPos = pos.east();
        boolean flag = this == world.getBlockState(westPos).getBlock() || this == world.getBlockState(eastPos).getBlock();
        boolean flag1 = this == world.getBlockState(northPos).getBlock() || this == world.getBlockState(southPos).getBlock();

        if (flag && flag1) {
            chance /= 2.0F;
        } else {
            boolean flag2 = this == world.getBlockState(westPos.north()).getBlock()
                    || this == world.getBlockState(eastPos.north()).getBlock()
                    || this == world.getBlockState(eastPos.south()).getBlock()
                    || this == world.getBlockState(westPos.south()).getBlock();

            if (flag2) {
                chance /= 2.0F;
            }
        }

        return chance;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.withAge(meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(AGE);
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    //##########################################################
    // IGrowable

    @Override
    public boolean canGrow(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, boolean isClient) {
        return !this.isMaxAge(state);
    }

    @Override
    public boolean canUseBonemeal(@NotNull World worldIn, @NotNull Random rand, @NotNull BlockPos pos, @NotNull IBlockState state) {
        return true;
    }

    @Override
    public void grow(@NotNull World worldIn, @NotNull Random rand, @NotNull BlockPos pos, @NotNull IBlockState state) {
        this.grow(worldIn, pos, state);
    }

    //##########################################################
    // IShearable

    @Override
    public boolean isShearable(@NotNull ItemStack item, IBlockAccess world, BlockPos pos) {
        return item.getItem() instanceof ItemShears;
    }

    @Override
    public @NotNull List<ItemStack> onSheared(@NotNull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        NonNullList<ItemStack> drops = NonNullList.create();
        this.getDrops(drops, world, pos, world.getBlockState(pos), 0);
        drops.add(new ItemStack(this));
        return drops;
    }

    //##########################################################
    // IHarvestableCrop

    @Optional.Method(modid = "magicultureintegrations")
    @Override
    public BlockPos getHarvestPosition(World world, BlockPos cropPos) {
        return cropPos;
    }

    @Optional.Method(modid = "magicultureintegrations")
    @Override
    public @NotNull HarvestResult getHarvestResult(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() == this) {
            return this.isMaxAge(state) ? HarvestResult.HARVEST : HarvestResult.PASS;
        }
        return HarvestResult.PASS;
    }

    @Optional.Method(modid = "magicultureintegrations")
    @Override
    public @NotNull NonNullList<ItemStack> harvestCrop(@Nullable EntityPlayer entityPlayer, World world, BlockPos pos, boolean silkTouch, int fortune) {
        NonNullList<ItemStack> drops = NonNullList.create();
        if(this.getHarvestResult(world, pos) == HarvestResult.HARVEST) {
            IBlockState state = world.getBlockState(pos);
            this.getDrops(drops, world, pos, state, 0);
            world.setBlockState(pos, this.withAge(3), 2);
        }
        return drops;
    }

    //##########################################################
    // IHarvestableCrop

    @Optional.Method(modid = "botania")
    @Override
    public boolean canHornHarvest(World world, BlockPos blockPos, ItemStack itemStack, EnumHornType enumHornType) {
        return enumHornType == EnumHornType.WILD;
    }

    @Optional.Method(modid = "botania")
    @Override
    public boolean hasSpecialHornHarvest(World world, BlockPos blockPos, ItemStack itemStack, EnumHornType enumHornType) {
        return enumHornType == EnumHornType.WILD;
    }

    @Optional.Method(modid = "botania")
    @Override
    public void harvestByHorn(World world, BlockPos pos, ItemStack itemStack, EnumHornType enumHornType) {
        IBlockState state = world.getBlockState(pos);
        if(this.isMaxAge(state)) {
            NonNullList<ItemStack> drops = NonNullList.create();
            this.getDrops(drops, world, pos, state, 0);
            drops.stream().filter(drop -> !drop.isEmpty()).forEach(drop -> spawnAsEntity(world, pos, drop));
            world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, getStateId(state));
            world.setBlockState(pos, this.withAge(3), 2);
        }
    }
}
