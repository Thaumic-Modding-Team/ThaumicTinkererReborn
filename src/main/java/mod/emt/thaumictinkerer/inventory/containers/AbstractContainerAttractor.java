package mod.emt.thaumictinkerer.inventory.containers;

import mod.emt.thaumictinkerer.api.tile.AbstractTileAttractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractContainerAttractor<T extends AbstractTileAttractor<?>> extends Container {
    private final EntityPlayer player;
    private final T tile;

    public AbstractContainerAttractor(EntityPlayer player, T tile) {
        this.player = player;
        this.tile = tile;
        this.bindTileInventory();
        this.bindPlayerInventory();
    }

    public abstract boolean isValidAttractorItem(ItemStack stack);

    public EntityPlayer getPlayer() {
        return player;
    }

    public T getAttractor() {
        return tile;
    }

    public void bindTileInventory() {
        IItemHandler handler = this.getAttractor().getStackHandler();
        for(int i = 0; i < handler.getSlots(); i++) {
            int posX = ((i % 4) * 18) + 8;
            int posY = ((i / 4) * 18) + 18;
            this.addSlotToContainer(new SlotItemHandler(handler, i, posX, posY));
        }
    }

    public void bindPlayerInventory() {
        InventoryPlayer inv = this.getPlayer().inventory;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(inv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            int attractorInvSize = this.getAttractor().getStackHandler().getSlots();
            if(index < attractorInvSize) {
                //Moving from attractor to player
                if(!this.mergeItemStack(slotStack, attractorInvSize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                //Move from player to attractor
                if(this.isValidAttractorItem(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 0, attractorInvSize, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(index < this.inventorySlots.size() - 9) {
                    if(!this.mergeItemStack(slotStack, this.inventorySlots.size() - 9, this.inventorySlots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(!this.mergeItemStack(slotStack, attractorInvSize, this.inventorySlots.size() - 9, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if(slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return stack;
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer playerIn) {
        return true;
    }
}
