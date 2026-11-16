package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.tile.AbstractTileAttractor;
import mod.emt.thaumictinkerer.registry.ModGuiHandlerTT;
import mod.emt.thaumictinkerer.utils.helpers.ItemHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileItemAttractor extends AbstractTileAttractor<EntityItem> {
    public ItemStackHandler stackHandler = new ItemStackHandler(12) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    protected boolean matchNBT = false;
    protected boolean matchMeta = false;
    protected boolean matchOreDict = false;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.matchNBT = compound.getBoolean("matchNbt");
        this.matchMeta = compound.getBoolean("matchMeta");
        this.matchOreDict = compound.getBoolean("matchOreDict");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("matchNbt", this.matchNBT);
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
        player.openGui(ThaumicTinkerer.instance, ModGuiHandlerTT.ID_ITEM_ATTRACTOR, this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ());
    }

    @Override
    public boolean isValidEntity(EntityItem entity) {
        return entity.isEntityAlive() && this.itemMatches(entity.getItem());
    }

    public boolean itemMatches(ItemStack stack) {
        if(stack.isEmpty())
            return false;

        for(int i = 0; i < this.stackHandler.getSlots(); i++) {
            ItemStack slotStack = this.stackHandler.getStackInSlot(i);
            if(ItemHelper.itemMatches(slotStack, stack, this.matchNBT(), this.matchMeta(), this.matchOreDict())) {
                return this.isWhitelist();
            }
        }
        return !this.isWhitelist();
    }

    public ItemStackHandler getStackHandler() {
        return stackHandler;
    }

    public boolean matchMeta() {
        return matchMeta;
    }

    public void setMatchMeta(boolean matchMeta) {
        if(this.matchMeta != matchMeta) {
            this.matchMeta = matchMeta;
            this.markDirty();
        }
    }

    public boolean matchNBT() {
        return matchNBT;
    }

    public void setMatchNBT(boolean matchNBT) {
        if(this.matchNBT != matchNBT) {
            this.matchNBT = matchNBT;
            this.markDirty();
        }
    }

    public boolean matchOreDict() {
        return matchOreDict;
    }

    public void setMatchOreDict(boolean matchOreDict) {
        if(this.matchOreDict != matchOreDict) {
            this.matchOreDict = matchOreDict;
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
