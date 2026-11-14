package mod.emt.thaumictinkerer.entity;

import mod.emt.thaumictinkerer.block.BlockLuminousGas;
import mod.emt.thaumictinkerer.registry.ModBlocksTT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityGasPhial extends EntityThrowable {
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityGasPhial.class, DataSerializers.ITEM_STACK);
    private static final DataParameter<Boolean> LIGHT = EntityDataManager.createKey(EntityGasPhial.class, DataSerializers.BOOLEAN);
    private static final Set<BlockPos> OFFSETS;

    public EntityGasPhial(World worldIn) {
        super(worldIn);
    }

    public EntityGasPhial(World worldIn, EntityLivingBase throwerIn, ItemStack thrownItem, boolean isLightEmitting) {
        super(worldIn, throwerIn);
        this.setItem(thrownItem);
        this.setLightEmitting(isLightEmitting);
    }

    public EntityGasPhial(World worldIn, double x, double y, double z, ItemStack thrownItem, boolean isLightEmitting) {
        super(worldIn, x, y, z);
        this.setItem(thrownItem);
        this.setLightEmitting(isLightEmitting);
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(ITEM, ItemStack.EMPTY);
        this.dataManager.register(LIGHT, true);
    }

    @Override
    protected void onImpact(@NotNull RayTraceResult result) {
        ItemStack stack = this.getItem();
        if(!stack.isEmpty()) {
            if(result.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos impact = result.getBlockPos().offset(result.sideHit);
                this.createGas(impact);
                this.applyGlowing(impact);
                if(!this.world.isRemote) {
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.AMBIENT, 1.0f, 1.0f);
                    this.setDead();
                }
            }

        }
    }

    public void createGas(BlockPos impact) {
        IBlockState state = ModBlocksTT.LUMINOUS_GAS.getDefaultState().withProperty(BlockLuminousGas.LUMINOUS, this.isLightEmitting());

        for(BlockPos offset : OFFSETS) {
            BlockPos checkPos = offset.add(impact);
            IBlockState checkState = this.world.getBlockState(checkPos);
            if (checkState.getBlock().isAir(checkState, this.world, checkPos) && checkState.getBlock().isReplaceable(this.world, checkPos)) {
                if (!this.world.isRemote) {
                    this.world.setBlockState(checkPos, state);
                } else {
                    //TODO: Impact effect
                }
            }
        }
    }

    public void applyGlowing(BlockPos impact) {
        if(!this.world.isRemote) {
            AxisAlignedBB area = new AxisAlignedBB(impact).grow(2);
            PotionEffect glowing = new PotionEffect(MobEffects.GLOWING, 3600, 0, true, false);
            List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, area);
            for (EntityLivingBase entity : entities) {
                entity.addPotionEffect(glowing);
            }
        }
    }

    public ItemStack getItem() {
        return this.getDataManager().get(ITEM);
    }

    public void setItem(ItemStack stack) {
        this.getDataManager().set(ITEM, stack);
        this.getDataManager().setDirty(ITEM);
    }

    public boolean isLightEmitting() {
        return this.getDataManager().get(LIGHT);
    }

    public void setLightEmitting(boolean isLightEmitting) {
        this.getDataManager().set(LIGHT, isLightEmitting);
        this.getDataManager().setDirty(LIGHT);
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Item", this.getItem().writeToNBT(new NBTTagCompound()));
        compound.setTag("Light", new NBTTagByte((byte) (this.isLightEmitting() ? 0 : 1)));
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setItem(new ItemStack(compound.getCompoundTag("Item")));
        this.setLightEmitting(compound.getBoolean("Light"));
    }

    static {
        OFFSETS = new HashSet<>();
        for(int x = -2; x <= 2; x++) {
            for(int y = -1; y <= 1; y++) {
                for(int z = -2; z <= 2; z++) {
                    OFFSETS.add(new BlockPos(x, y, z));
                }
            }
        }
    }
}
