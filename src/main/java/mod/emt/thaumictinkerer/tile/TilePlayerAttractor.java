package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.tile.AbstractTileAttractor;
import mod.emt.thaumictinkerer.item.misc.ItemSoulMould;
import mod.emt.thaumictinkerer.registry.ModGuiHandlerTT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TilePlayerAttractor extends AbstractTileAttractor<EntityPlayer> {
    public ItemStackHandler stackHandler = new ItemStackHandler(12) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return this.isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return ItemSoulMould.getPlayerId(stack) != null;
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    public boolean ignoreSneak = false;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.setIgnoreSneak(compound.getBoolean("ignoreSneak"));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("ignoreSneak", this.ignoreSneak());
        return compound;
    }

    @Override
    public Class<EntityPlayer> getEntityClass() {
        return EntityPlayer.class;
    }

    @Override
    public void openGui(EntityPlayer player) {
        player.openGui(ThaumicTinkerer.instance, ModGuiHandlerTT.ID_PLAYER_ATTRACTOR, this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
    }

    @Override
    public boolean isValidEntity(EntityPlayer entity) {
        return entity.isEntityAlive() && this.checkEntity(entity);
    }

    @Override
    public ItemStackHandler getStackHandler() {
        return this.stackHandler;
    }

    public boolean checkEntity(EntityPlayer player) {
        if(this.ignoreSneak() && player.isSneaking()) {
            return false;
        } else {
            for(int i = 0; i < this.stackHandler.getSlots(); i++) {
                ItemStack stack = this.stackHandler.getStackInSlot(i);
                UUID uuid = ItemSoulMould.getPlayerId(stack);
                if(uuid != null && uuid.equals(player.getGameProfile().getId())) {
                    return this.isWhitelist();
                }
            }
        }
        return !this.isWhitelist();
    }

    public boolean ignoreSneak() {
        return ignoreSneak;
    }

    public void setIgnoreSneak(boolean ignoreSneak) {
        if(this.ignoreSneak != ignoreSneak) {
            this.ignoreSneak = ignoreSneak;
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
