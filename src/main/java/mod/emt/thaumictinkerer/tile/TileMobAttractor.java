package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.tile.AbstractTileAttractor;
import mod.emt.thaumictinkerer.block.BlockAttractor;
import mod.emt.thaumictinkerer.item.misc.ItemSoulMould;
import mod.emt.thaumictinkerer.registry.ModGuiHandlerTT;
import mod.emt.thaumictinkerer.registry.ModSoundEventsTT;
import mod.emt.thaumictinkerer.utils.helpers.HolidayHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileMobAttractor extends AbstractTileAttractor<EntityLivingBase> {
    public static final int EFFECT_DISCO_COW = 0;
    public static final int DISCO_COW_DURATION = 140;

    public ItemStackHandler stackHandler = new ItemStackHandler(12) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return this.isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof ItemSoulMould && ItemSoulMould.getEntity(stack) != null;
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    public boolean ignoreAdults = false;
    public boolean ignoreChild = false;
    public int discoCowDuration = -1;
    public EntityCow discoCow = null;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.ignoreAdults = compound.getBoolean("ignoreAdults");
        this.ignoreChild = compound.getBoolean("ignoreChild");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("ignoreAdults", this.ignoreAdults);
        compound.setBoolean("ignoreChild", this.ignoreChild);
        return compound;
    }

    @Override
    public void update() {
        super.update();
        if(!this.world.isRemote) {
            if (this.world.getTotalWorldTime() % 200L == 0 && this.checkDiscoCow()) {
                this.beginDiscoCow();
            }

            if(this.discoCowDuration > 0 && this.discoCow != null) {
                this.discoCowDuration--;
                this.rotateCow(this.discoCow);
            } else {
                this.discoCow = null;
                this.discoCowDuration = 0;
            }
        } else {
            if(this.discoCowDuration > 0) {
                this.discoCowDuration--;

                if(this.discoCow != null) {
                    this.rotateCow(this.discoCow);
                }
            } else {
                this.discoCow = null;
                this.discoCowDuration = -1;
            }
        }
    }

    @Override
    public Class<EntityLivingBase> getEntityClass() {
        return EntityLivingBase.class;
    }

    @Override
    public void openGui(EntityPlayer player) {
        player.openGui(ThaumicTinkerer.instance, ModGuiHandlerTT.ID_MOB_ATTRACTOR, this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
    }

    @Override
    public boolean isValidEntity(EntityLivingBase entity) {
        return entity.isEntityAlive() && this.checkEntity(entity);
    }

    @Override
    public ItemStackHandler getStackHandler() {
        return stackHandler;
    }

    public boolean checkEntity(EntityLivingBase entity) {
        if(entity instanceof EntityPlayer || (entity.isChild() && this.ignoreChild()) || (!entity.isChild() && this.ignoreAdults())) {
            return false;
        } else {
            for(int i = 0; i < this.stackHandler.getSlots(); i++) {
                ItemStack stack = this.stackHandler.getStackInSlot(i);
                EntityEntry entry = ItemSoulMould.getEntity(stack);
                if(entry != null && entity.getClass().equals(entry.getEntityClass())) {
                    return this.isWhitelist();
                }
            }
        }
        return !this.isWhitelist();
    }

    public boolean ignoreAdults() {
        return ignoreAdults;
    }

    public void setIgnoreAdults(boolean ignoreAdults) {
        if(this.ignoreAdults != ignoreAdults) {
            this.ignoreAdults = ignoreAdults;
            this.markDirty();
        }
    }

    public boolean ignoreChild() {
        return ignoreChild;
    }

    public void setIgnoreChild(boolean ignoreChild) {
        if(this.ignoreChild != ignoreChild) {
            this.ignoreChild = ignoreChild;
            this.markDirty();
        }
    }

    public boolean checkDiscoCow() {
        //TODO: Remove force true
        if(this.world.rand.nextInt(HolidayHelper.isAprilFools() ? 10 : 100) == 0 || true) {
            IBlockState state = this.world.getBlockState(this.pos);
            if (this.getRedstonePower() > 0 && state.getValue(BlockAttractor.MODE) == BlockAttractor.AttractorMode.ATTRACT && state.getValue(BlockAttractor.FACING) == EnumFacing.UP) {
                List<EntityCow> cows = this.world.getEntitiesWithinAABB(EntityCow.class, new AxisAlignedBB(this.pos.offset(EnumFacing.DOWN)));
                return cows.size() == 1 && cows.get(0).getClass().equals(EntityCow.class);
            }
        }
        return false;
    }

    public void beginDiscoCow() {
        List<EntityCow> cows = this.world.getEntitiesWithinAABB(EntityCow.class, new AxisAlignedBB(this.pos.offset(EnumFacing.DOWN)));
        if(!cows.isEmpty()) {
            this.discoCow = cows.get(0);
            this.discoCowDuration = DISCO_COW_DURATION;
            this.world.playSound(null, this.discoCow.getPosition(), ModSoundEventsTT.BLOCK_CORPOREAL_ATTRACT0R_DISCO_COW.getSoundEvent(), SoundCategory.BLOCKS, 1.2f, 1.0f);
            this.world.addBlockEvent(this.pos, this.world.getBlockState(this.pos).getBlock(), EFFECT_DISCO_COW, this.discoCow.getEntityId());
        }
    }

    public void rotateCow(EntityCow cow) {
        float rotation = ((DISCO_COW_DURATION - this.discoCowDuration) * 26.0F) % 360.0F;
        cow.rotationYaw = rotation;
        cow.prevRotationYaw = rotation;
        cow.renderYawOffset = rotation;
        cow.prevRenderYawOffset = rotation;
        cow.rotationYawHead = rotation;
        cow.prevRotationYawHead = rotation;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if(id == EFFECT_DISCO_COW) {
            if(this.world.isRemote && type != -1) {
                Entity entity = this.world.getEntityByID(type);
                if(entity instanceof EntityCow) {
                    this.discoCow = (EntityCow) entity;
                    this.discoCowDuration = DISCO_COW_DURATION;
                }
            }
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public @Nullable <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.stackHandler);
        }
        return super.getCapability(capability, facing);
    }
}
