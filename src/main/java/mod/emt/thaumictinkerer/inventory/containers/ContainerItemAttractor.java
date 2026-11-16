package mod.emt.thaumictinkerer.inventory.containers;

import mod.emt.thaumictinkerer.tile.TileItemAttractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ContainerItemAttractor extends AbstractContainerAttractor<TileItemAttractor> {
    public static final int BUTTON_WHITELIST = 0;
    public static final int BUTTON_MATCH_META = 1;
    public static final int BUTTON_MATCH_NBT = 2;
    public static final int BUTTON_MATCH_ORE = 3;

    public ContainerItemAttractor(EntityPlayer player, TileItemAttractor tile) {
        super(player, tile);
    }

    @Override
    public boolean isValidAttractorItem(ItemStack stack) {
        return true;
    }

    @Override
    public boolean enchantItem(@NotNull EntityPlayer playerIn, int id) {
        TileItemAttractor tile = this.getAttractor();
        if(id == BUTTON_WHITELIST) {
            tile.setIsWhitelist(!tile.isWhitelist());
        } else if(id == BUTTON_MATCH_META) {
            tile.setMatchMeta(!tile.matchMeta());
        } else if(id == BUTTON_MATCH_NBT) {
            tile.setMatchNBT(!tile.matchNBT());
        } else if(id == BUTTON_MATCH_ORE) {
            tile.setMatchOreDict(!tile.matchOreDict());
        }
        return false;
    }
}
