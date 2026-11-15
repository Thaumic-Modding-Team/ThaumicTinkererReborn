package mod.emt.thaumictinkerer.container.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlackHoleRingHandler implements IItemHandlerModifiable, ICapabilitySerializable<NBTTagCompound> {
    protected ItemStack ringStack;
    protected NonNullList<ItemStack> filter;
    protected NonNullList<ItemStack> stacks;

    public BlackHoleRingHandler(ItemStack ring) {
        this.ringStack = ring;
        this.filter = NonNullList.withSize(5, ItemStack.EMPTY);
        this.stacks = NonNullList.withSize(5, ItemStack.EMPTY);
        this.deserializeNBT(ring.getTagCompound() != null ? ring.getTagCompound() : new NBTTagCompound());
    }

    public ItemStack getRing() {
        return this.ringStack;
    }

    @Override
    public int getSlots() {
        return this.stacks.size();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return this.stacks.get(slot);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.validateSlotIndex(slot);
        this.stacks.set(slot, stack);
        this.markDirty();
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if(stack.isEmpty()) return ItemStack.EMPTY;
        if(!this.isItemValid(slot, stack)) return stack;

        ItemStack existing = this.getStackInSlot(slot);
        int limit = this.getSlotLimit(slot);

        if(!existing.isEmpty()) {
            if(!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                return stack;
            }
            limit -= existing.getCount();
        }

        if(limit <= 0) {
            return stack;
        }

        boolean limitReached = stack.getCount() > limit;

        if(!simulate) {
            if(existing.isEmpty()) {
                this.stacks.set(slot, limitReached ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(limitReached ? limit : stack.getCount());
            }
            this.markDirty();
        }

        return limitReached ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(amount == 0) return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.getStackInSlot(slot);

        if(existing.isEmpty()) return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if(existing.getCount() <= toExtract) {
            if(!simulate) {
                this.stacks.set(slot, ItemStack.EMPTY);
                this.markDirty();
            }
            return existing;
        } else {
            if(!simulate) {
                this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                this.markDirty();
            }
            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        ItemStack filter = this.getFilter(slot);
        return !filter.isEmpty() && ItemHandlerHelper.canItemStacksStack(stack, this.getFilter(slot));
    }

    public ItemStack getFilter(int slot) {
        this.validateSlotIndex(slot);
        return this.filter.get(slot);
    }

    public void setFilter(int slot, ItemStack stack) {
        this.validateSlotIndex(slot);
        this.filter.set(slot, stack);
        this.markDirty();
    }

    public void markDirty() {
        this.ringStack.setTagInfo("inventory", this.serializeNBT());
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.stacks.size()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.stacks.size() + ")");
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList tagList = new NBTTagList();
        for(int slot = 0; slot < this.getSlots(); slot++) {
            NBTTagCompound slotTag = new NBTTagCompound();
            ItemStack filter = this.filter.get(slot);
            ItemStack stack = this.stacks.get(slot);
            int count = this.stacks.get(slot).getCount();
            stack = ItemHandlerHelper.copyStackWithSize(stack, 1);

            slotTag.setTag("filter", filter.serializeNBT());
            slotTag.setTag("item", stack.serializeNBT());
            slotTag.setInteger("count", count);

            tagList.appendTag(slotTag);
        }
        tag.setTag("slots", tagList);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        NBTTagList tagList = tag.getTagList("slots", Constants.NBT.TAG_COMPOUND);
        for(int slot = 0; slot < tagList.tagCount(); slot++) {
            NBTTagCompound slotTag = tagList.getCompoundTagAt(slot);
            ItemStack filter = new ItemStack(slotTag.getCompoundTag("filter"));
            ItemStack stack = new ItemStack(slotTag.getCompoundTag("item"));
            int count = slotTag.getInteger("count");
            if(!filter.isEmpty()) {
                this.filter.set(slot, filter);
            }
            if(!stack.isEmpty() && count > 0) {
                stack.setCount(count);
                this.stacks.set(slot, stack);
            }
        }
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public @Nullable <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this) : null;
    }
}
