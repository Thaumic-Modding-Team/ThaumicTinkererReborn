package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.AbstractTileAttractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class TilePlayerAttractor extends AbstractTileAttractor<EntityPlayer> {
    public ItemStackHandler stackHandler = new ItemStackHandler(15) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        return compound;
    }

    @Override
    public Class<EntityPlayer> getEntityClass() {
        return EntityPlayer.class;
    }

    @Override
    public void openGui(EntityPlayer player) {

    }

    @Override
    public boolean isValidEntity(EntityPlayer entity) {
        return entity.isEntityAlive();
    }

    @Override
    public ItemStackHandler getStackHandler() {
        return this.stackHandler;
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
