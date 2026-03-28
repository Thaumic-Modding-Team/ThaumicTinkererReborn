package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.AbstractTileAttractor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class TileMobAttractor extends AbstractTileAttractor<EntityLivingBase> {
    public ItemStackHandler stackHandler = new ItemStackHandler(12) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return this.isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            //TODO: Only allow mob filters.
            return super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    public boolean blacklist = false;
    public boolean ignoreAdults = false;
    public boolean ignoreChild = false;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stackHandler.deserializeNBT(compound.getCompoundTag("inventory"));
        this.blacklist = compound.getBoolean("blacklist");
        this.ignoreAdults = compound.getBoolean("ignoreAdults");
        this.ignoreChild = compound.getBoolean("ignoreChild");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inventory", this.stackHandler.serializeNBT());
        compound.setBoolean("blacklist", this.blacklist);
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
        //TODO
        // player.openGui(ThaumicTinkerer.instance, , this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
    }

    @Override
    public boolean isValidEntity(EntityLivingBase entity) {
        return entity.isEntityAlive() && this.blacklist != this.checkEntity(entity);
    }

    public boolean checkEntity(EntityLivingBase entity) {
        if(entity instanceof EntityPlayer)
            return false;
        return true;
    }

    public ItemStackHandler getStackHandler() {
        return stackHandler;
    }

    public boolean getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    public boolean getIgnoreAdults() {
        return ignoreAdults;
    }

    public void setIgnoreAdults(boolean ignoreAdults) {
        this.ignoreAdults = ignoreAdults;
    }

    public boolean getIgnoreChild() {
        return ignoreChild;
    }

    public void setIgnoreChild(boolean ignoreChild) {
        this.ignoreChild = ignoreChild;
    }

//    @Override
//    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
//        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
//    }
//
//    @Override
//    public @Nullable <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
//        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.stackHandler);
//        }
//        return super.getCapability(capability, facing);
//    }
}
