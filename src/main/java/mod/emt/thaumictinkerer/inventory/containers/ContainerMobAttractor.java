package mod.emt.thaumictinkerer.inventory.containers;

import mod.emt.thaumictinkerer.item.misc.ItemSoulMould;
import mod.emt.thaumictinkerer.tile.TileMobAttractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ContainerMobAttractor extends AbstractContainerAttractor<TileMobAttractor> {
    public static final int BUTTON_WHITELIST = 0;
    public static final int BUTTON_IGNORE_CHILD = 1;
    public static final int BUTTON_IGNORE_ADULT = 2;

    public ContainerMobAttractor(EntityPlayer player, TileMobAttractor tile) {
        super(player, tile);
    }

    @Override
    public boolean isValidAttractorItem(ItemStack stack) {
        return ItemSoulMould.getEntity(stack) != null;
    }

    @Override
    public boolean enchantItem(@NotNull EntityPlayer playerIn, int id) {
        TileMobAttractor tile = this.getAttractor();
        if(id == BUTTON_WHITELIST) {
            tile.setIsWhitelist(!tile.isWhitelist());
        } else if(id == BUTTON_IGNORE_CHILD) {
            tile.setIgnoreChild(!tile.ignoreChild());
        } else if(id == BUTTON_IGNORE_ADULT) {
            tile.setIgnoreAdults(!tile.ignoreAdults());
        }
        return false;
    }
}
