package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.tile.AbstractTileAttractor;
import mod.emt.thaumictinkerer.item.misc.ItemSoulMould;
import mod.emt.thaumictinkerer.registry.ModGuiHandlerTT;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileMobAttractor extends AbstractTileAttractor<EntityLivingBase> {
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
                if(entry == null)
                    continue;

                if(entity.getClass().equals(entry.getEntityClass())) {
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
