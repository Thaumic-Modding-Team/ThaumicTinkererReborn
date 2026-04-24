package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.api.tile.AbstractTileAttractor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.fx.FXDispatcher;

import java.util.List;
import java.util.Random;

public class BlockAttractor extends BlockTileAddition {
    public static final PropertyEnum<AttractorMode> MODE = PropertyEnum.create("mode", AttractorMode.class);
    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
    public static final AxisAlignedBB[] AABB_BASE = new AxisAlignedBB[] {
            new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.125,0.9375),     //DOWN
            new AxisAlignedBB(0.0625, 0.875, 0.0625, 0.9375, 1.0, 0.9375),  //UP
            new AxisAlignedBB(0.0625, 0.0625, 0, 0.9375, 0.9375, 0.125),    //NORTH
            new AxisAlignedBB(0.0625, 0.0625, 0.875, 0.9375, 0.9375, 1.0),  //SOUTH
            new AxisAlignedBB(0, 0.0625, 0.0625, 0.125, 0.9375, 0.9375),    //WEST
            new AxisAlignedBB(0.875, 0.0625, 0.0625, 1.0, 0.9375, 0.9375)   //EAST

    };
    public static final AxisAlignedBB[] AABB_SPIRE = new AxisAlignedBB[] {
            new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 1.0, 0.6875),  //DOWN
            new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 1.0, 0.6875),  //UP
            new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, 1.0),  //NORTH
            new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, 1.0),  //SOUTH
            new AxisAlignedBB(0, 0.3125, 0.3125, 1.0, 0.6875, 0.6875),  //WEST
            new AxisAlignedBB(0, 0.3125, 0.3125, 1.0, 0.6875, 0.6875)   //EAST
    };

    public BlockAttractor(String unlocName, Class<? extends TileEntity> tileClazz) {
        super(unlocName, Material.IRON, MapColor.GRAY, tileClazz);
        this.setSoundType(SoundType.METAL);
        this.setHardness(2.0f);
        this.setResistance(12.0f);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(MODE, AttractorMode.ATTRACT));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull AxisAlignedBB getBoundingBox(IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return AABB_SPIRE[state.getValue(FACING).getIndex()];
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull AxisAlignedBB entityBox, @NotNull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        EnumFacing facing = state.getValue(FACING);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BASE[facing.getIndex()]);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SPIRE[facing.getIndex()]);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @Nullable RayTraceResult collisionRayTrace(IBlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Vec3d start, @NotNull Vec3d end) {
        EnumFacing facing = state.getValue(FACING);
        if(rayTrace(pos, start, end, AABB_BASE[facing.getIndex()]) != null || rayTrace(pos, start, end, AABB_SPIRE[facing.getIndex()]) != null) {
            return super.collisionRayTrace(state, worldIn, pos, start, end);
        } else {
            return null;
        }
    }

    @Override
    public boolean onBlockActivated(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof AbstractTileAttractor<?>) {
            if(playerIn.isSneaking()) {
                AttractorMode nextMode = state.getValue(MODE).next();
                worldIn.setBlockState(pos, state.withProperty(MODE, nextMode));
                tile.validate();
                worldIn.setTileEntity(pos, tile);
                if(!worldIn.isRemote) {
                    playerIn.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:attractor.mode." + nextMode.getName()), true);
                } else {
                    playerIn.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.4f, 0.7f);
                }
            } else {
                ((AbstractTileAttractor<?>) tile).openGui(playerIn);
            }
            return true;
        }
        return false;
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
    public boolean doesSideBlockRendering(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(@NotNull IBlockState base_state, IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing side) {
        return world.getBlockState(pos).getValue(FACING).getOpposite() == side;
    }

    @Override
    public @NotNull IBlockState getStateForPlacement(@NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @NotNull EntityLivingBase placer, @NotNull EnumHand hand) {
        return this.getDefaultState().withProperty(MODE, AttractorMode.ATTRACT).withProperty(FACING, facing.getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MODE, AttractorMode.values()[meta / 6])
                .withProperty(FACING, EnumFacing.byIndex(meta % 6));
    }

    @Override
    public int getMetaFromState(@NotNull IBlockState state) {
        return (state.getValue(MODE).ordinal() * 6) + state.getValue(FACING).getIndex();
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, MODE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState withRotation(@NotNull IBlockState state, @NotNull Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull IBlockState withMirror(@NotNull IBlockState state, @NotNull Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(@NotNull IBlockState stateIn, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull Random rand) {
        if(worldIn.isBlockPowered(pos)) {
            boolean isPulling = stateIn.getValue(MODE) == AttractorMode.ATTRACT;
            for(int i = 0; i < 5; i++) {
                if(rand.nextInt(5) == 0) {
                    float motionX = (worldIn.rand.nextFloat() - 0.5f) * 0.05f;
                    float motionY = (worldIn.rand.nextFloat() - 0.5f) * 0.05f;
                    float motionZ = (worldIn.rand.nextFloat() - 0.5f) * 0.05f;
                    int age = 20 + worldIn.rand.nextInt(10);

                    float offsetX = 0.5f;
                    float offsetY = worldIn.rand.nextFloat() * 0.8f + 0.2f;
                    float offsetZ = 0.5f;
                    if(isPulling) {
                        offsetX += motionX * age * -1.0f;
                        offsetY += motionY * age * -1.0f;
                        offsetZ += motionZ * age * -1.0f;
                    }

                    FXDispatcher.INSTANCE.drawWispyMotes(
                            pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ,
                            motionX, motionY, motionZ, age,
                            isPulling ? 0 : 1.0f, 0, isPulling ? 1.0f : 0,
                            0
                    );
                }
            }
        }
    }

    public enum AttractorMode implements IStringSerializable {
        ATTRACT,
        REPULSE;

        public AttractorMode next() {
            return AttractorMode.values()[(this.ordinal() + 1) % AttractorMode.values().length];
        }

        @Override
        public @NotNull String getName() {
            return this.toString().toLowerCase();
        }
    }
}
