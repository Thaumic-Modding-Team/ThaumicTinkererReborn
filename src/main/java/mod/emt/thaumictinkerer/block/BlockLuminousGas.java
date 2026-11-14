package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.AbstractBlockAddition;
import mod.emt.thaumictinkerer.item.tools.ItemGasRemover;
import mod.emt.thaumictinkerer.registry.ModItemsTT;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.fx.FXDispatcher;

import java.util.Random;

public class BlockLuminousGas extends AbstractBlockAddition {
    public static PropertyBool LUMINOUS = PropertyBool.create("luminous");

    public BlockLuminousGas() {
        super("luminous_gas", Material.AIR, MapColor.AIR);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LUMINOUS, true));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @Nullable AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean canCollideCheck(@NotNull IBlockState state, boolean hitIfLiquid) {
        return false;
    }

    @Override
    public int getLightValue(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
        return state.getValue(LUMINOUS) ? 15 : 0;
    }

    @Override
    public int getLightOpacity(IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
        //Creates a light opacity of 60%
        return state.getValue(LUMINOUS) ? 0 : 153;
    }

    @Override
    public void onEntityCollision(@NotNull World worldIn, @NotNull BlockPos pos, IBlockState state, @NotNull Entity entityIn) {
        //TODO: Check if this works.
        if(state.getValue(LUMINOUS) && entityIn instanceof EntityLivingBase) {
            ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.GLOWING, 10, 0, true, false));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(@NotNull IBlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull Random rand) {
        double j = 1.2D;

        // Holding a Gas Remover on either hand will make the particles more visible
        if(isHoldingGasRemover(Minecraft.getMinecraft().player)) {
            FXDispatcher.INSTANCE.drawWispyMotes(
                    pos.getX() + 0.5D + world.rand.nextDouble() * j - j / 2,
                    pos.getY() + 0.5D + world.rand.nextDouble() * j - j / 2,
                    pos.getZ() + 0.5D + world.rand.nextDouble() * j - j / 2,
                    0.0D, 0.0D, 0.0D,
                    10,
                    state.getValue(LUMINOUS) ? 0.85F + world.rand.nextFloat() * 0.15F : 0.35F + world.rand.nextFloat() * 0.25F,
                    state.getValue(LUMINOUS) ? 0.65F + world.rand.nextFloat() * 0.30F : 0.08F + world.rand.nextFloat() * 0.10F,
                    state.getValue(LUMINOUS) ? world.rand.nextFloat() * 0.08F : 0.45F + world.rand.nextFloat() * 0.25F,
                    -0.2F
            );
        } else {
            FXDispatcher.INSTANCE.drawWispyMotes(
                    pos.getX() + 0.5D + world.rand.nextDouble() * j - j / 2,
                    pos.getY() + 0.5D + world.rand.nextDouble() * j - j / 2,
                    pos.getZ() + 0.5D + world.rand.nextDouble() * j - j / 2,
                    0.0D, 0.0D, 0.0D,
                    10,
                    state.getValue(LUMINOUS) ? 0.15F + world.rand.nextFloat() * 0.06F : 0.08F + world.rand.nextFloat() * 0.10F,
                    state.getValue(LUMINOUS) ? 0.10F + world.rand.nextFloat() * 0.05F : 0.02F + world.rand.nextFloat() * 0.04F,
                    state.getValue(LUMINOUS) ? 0.005F + world.rand.nextFloat() * 0.02F : 0.12F + world.rand.nextFloat() * 0.12F,
                    -0.2F
            );
        }
    }

    public static boolean isHoldingGasRemover(EntityPlayer player) {
        for(EnumHand hand : EnumHand.values()) {
            ItemStack stack = player.getHeldItem(hand);
            if(!stack.isEmpty() && stack.getItem() instanceof ItemGasRemover) {
                return true;
            }
        }
        return false;
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

    @Override
    public boolean isReplaceable(@NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess worldIn, @NotNull IBlockState state, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LUMINOUS, meta == 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LUMINOUS) ? 0 : 1;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LUMINOUS);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry) {
        //Gas does not have an item
    }

    @Override
    public void registerModel(ModelRegistryEvent event) {
        super.registerModel(event);
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LUMINOUS).build());
    }
}
