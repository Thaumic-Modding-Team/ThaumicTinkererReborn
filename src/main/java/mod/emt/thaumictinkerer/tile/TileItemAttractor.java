package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.AbstractTileAttractor;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class TileItemAttractor extends AbstractTileAttractor<EntityItem> {
    public ItemStackHandler stackHandler = new ItemStackHandler(12) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    protected boolean blacklist = false;
    protected boolean matchNbt = false;
    protected boolean matchMeta = false;
    protected boolean matchOreDict = false;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stackHandler.deserializeNBT(compound.getCompoundTag("inventory"));
        this.blacklist = compound.getBoolean("blacklist");
        this.matchNbt = compound.getBoolean("matchNbt");
        this.matchMeta = compound.getBoolean("matchMeta");
        this.matchOreDict = compound.getBoolean("matchOreDict");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inventory", this.stackHandler.serializeNBT());
        compound.setBoolean("blacklist", this.blacklist);
        compound.setBoolean("matchNbt", this.matchNbt);
        compound.setBoolean("matchMeta", this.matchMeta);
        compound.setBoolean("matchOreDict", this.matchOreDict);
        return compound;
    }

    @Override
    public Class<EntityItem> getEntityClass() {
        return EntityItem.class;
    }

    @Override
    public void openGui(EntityPlayer player) {
        //TODO
        // player.openGui(ThaumicTinkerer.instance, , this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
    }

    @Override
    public boolean isValidEntity(EntityItem entity) {
        return entity.isEntityAlive() && this.blacklist != this.itemMatches(entity.getItem());
    }

    public boolean itemMatches(ItemStack stack) {
        if(stack.isEmpty())
            return false;

        boolean isEmpty = true;
//        for(int slot = 0; slot < this.stackHandler.getSlots(); slot++) {
//            ItemStack slotStack = this.stackHandler.getStackInSlot(slot);
//            if(!slotStack.isEmpty()) {
//                if(ItemHelper.itemMatches(slotStack, stack, this.matchNbt, this.matchMeta, this.matchOreDict)) {
//                    return true;
//                }
//                isEmpty = false;
//            }
//        }
        return isEmpty;
    }

    public ItemStackHandler getStackHandler() {
        return stackHandler;
    }

    public boolean getMatchMeta() {
        return matchMeta;
    }

    public void setMatchMeta(boolean matchMeta) {
        this.matchMeta = matchMeta;
    }

    public boolean getMatchOreDict() {
        return matchOreDict;
    }

    public void setMatchOreDict(boolean matchOreDict) {
        this.matchOreDict = matchOreDict;
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
