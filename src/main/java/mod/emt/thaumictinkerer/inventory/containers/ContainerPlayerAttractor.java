package mod.emt.thaumictinkerer.inventory.containers;

import mod.emt.thaumictinkerer.item.misc.ItemSoulMould;
import mod.emt.thaumictinkerer.tile.TilePlayerAttractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ContainerPlayerAttractor extends AbstractContainerAttractor<TilePlayerAttractor> {
    public static final int BUTTON_WHITELIST = 0;
    public static final int BUTTON_IGNORE_SNEAK = 1;

    public ContainerPlayerAttractor(EntityPlayer player, TilePlayerAttractor tile) {
        super(player, tile);
    }

    @Override
    public boolean isValidAttractorItem(ItemStack stack) {
        return ItemSoulMould.getPlayerId(stack) != null;
    }

    @Override
    public boolean enchantItem(@NotNull EntityPlayer playerIn, int id) {
        TilePlayerAttractor tile = this.getAttractor();
        if(id == BUTTON_WHITELIST) {
            tile.setIsWhitelist(!tile.isWhitelist());
        } else if(id == BUTTON_IGNORE_SNEAK) {
            tile.setIgnoreSneak(!tile.ignoreSneak());
        }
        return false;
    }
}
