package mod.emt.thaumictinkerer.inventory.containers;

import mod.emt.thaumictinkerer.tile.TileDynamismTablet;
import mod.emt.thaumictinkerer.tile.TileDynamismTablet.ClickMode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ContainerDyamismTablet extends Container {
    public static final int BUTTON_LEFT_CLICK = 0;
    public static final int BUTTON_RIGHT_CLICK = 1;
    public static final int BUTTON_SNEAKING = 2;

    private final EntityPlayer player;
    private final TileDynamismTablet tile;

    public ContainerDyamismTablet(EntityPlayer player, TileDynamismTablet tile) {
        this.player = player;
        this.tile = tile;
        this.bindTileInventory();
        this.bindPlayerInventory();
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public TileDynamismTablet getTablet() {
        return tile;
    }

    public void bindTileInventory() {
        this.addSlotToContainer(new SlotItemHandler(this.getTablet().getStackHandler(), 0, 44, 36));
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
    public boolean enchantItem(@NotNull EntityPlayer playerIn, int id) {
        TileDynamismTablet tablet = this.getTablet();
        if(id == BUTTON_LEFT_CLICK) {
            tablet.setClickMode(ClickMode.LEFT_CLICK);
        } else if(id == BUTTON_RIGHT_CLICK) {
            tablet.setClickMode(ClickMode.RIGHT_CLICK);
        } else if(id == BUTTON_SNEAKING) {
            tablet.setIsSneaking(!tablet.isSneaking());
        }
        return false;
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();
            int tabletSize = this.getTablet().getStackHandler().getSlots();
            if(index < tabletSize) {
                //Moving from attractor to player
                if(!this.mergeItemStack(slotStack, tabletSize, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                //Move from player to attractor
                if(!this.mergeItemStack(slotStack, 0, tabletSize, false)) {
                    return ItemStack.EMPTY;
                } else if(index < this.inventorySlots.size() - 9) {
                    if(!this.mergeItemStack(slotStack, this.inventorySlots.size() - 9, this.inventorySlots.size(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(!this.mergeItemStack(slotStack, tabletSize, this.inventorySlots.size() - 9, false)) {
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
