package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.block.IBlockAddition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class BlockThaumicCake extends BlockCake implements IBlockAddition {
    private static final ResourceLocation THAUMIC_CAKE = new ResourceLocation(ThaumicTinkerer.MOD_ID, "thaumic_cake");

    public BlockThaumicCake() {
        super();
        this.setRegistryName(ThaumicTinkerer.MOD_ID, "thaumic_cake");
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
        this.disableStats();
        this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, 0));
        this.setHardness(0.5F);
        this.setSoundType(SoundType.CLOTH);
        this.setTickRandomly(true);
    }

    @Override
    public boolean onBlockActivated(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer player, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            return this.eatCake(world, pos, state, player);
        } else {
            ItemStack stack = player.getHeldItem(hand);
            return this.eatCake(world, pos, state, player) || stack.isEmpty();
        }
    }

    @Override
    public int quantityDropped(@NotNull Random random) {
        return 1;
    }

    @Override
    public @NotNull Item getItemDropped(IBlockState state, @NotNull Random rand, int fortune) {
        if(state.getValue(BITES) == 0) {
            return ForgeRegistries.ITEMS.getValue(THAUMIC_CAKE);
        }

        return Items.AIR;
    }

    @Override
    public @NotNull ItemStack getItem(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state) {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(THAUMIC_CAKE));
    }

    private boolean eatCake(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        player.addStat(StatList.CAKE_SLICES_EATEN);
        player.getFoodStats().addStats(4, 0.2F);
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 8 * 20, 0));
        int i = state.getValue(BITES);
        ItemStack stack = this.getPickBlock(state, null, world, pos, player);

        if(i < 6) {
            world.setBlockState(pos, state.withProperty(BITES, i + 1), 3);
        } else {
            world.setBlockToAir(pos);
        }

        // Chomp Particles
        for(int y = 0; y < 5; ++y) {
            Vec3d vec3d = new Vec3d(((double) world.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3d = vec3d.rotatePitch(-player.rotationPitch * 0.02F);
            vec3d = vec3d.rotateYaw(-player.rotationYaw * 0.02F);
            double d0 = (double) (-world.rand.nextFloat()) * 0.6D - 0.3D;
            Vec3d vec3d1 = new Vec3d(((double) world.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
            vec3d1 = vec3d1.rotatePitch(-player.rotationPitch * 0.02F);
            vec3d1 = vec3d1.rotateYaw(-player.rotationYaw * 0.02F);
            vec3d1 = vec3d1.add(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
            player.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
        }

        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 0.5F + 0.5F * (float) world.rand.nextInt(2), (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
        return true;
    }

    @Override
    public void updateTick(@NotNull World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Random rand) {
        super.updateTick(world, pos, state, rand);
        int slicesEaten = getMetaFromState(state);

        // Restore slices overtime
        if(slicesEaten > 0) {
            slicesEaten--;
            world.setBlockState(pos, state.withProperty(BITES, slicesEaten), 2);
        }
    }

    @Override
    public void registerBlock(IForgeRegistry<Block> registry) {
        IBlockAddition.super.registerBlock(registry);
    }
}
