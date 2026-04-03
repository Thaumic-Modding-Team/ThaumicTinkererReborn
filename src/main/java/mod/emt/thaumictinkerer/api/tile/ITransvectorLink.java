package mod.emt.thaumictinkerer.api.tile;

import mod.emt.thaumictinkerer.utils.TransvectorLink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

public interface ITransvectorLink {
    /**
     * Creates a link between the passed position and this tile entity.
     *
     * @param player The player performing the interaction.
     * @param hand The hand being used in the interaction.
     * @param clickedFace The block face clicked by the player.
     * @param link The world, position and face saved to the linking tool.
     * @param isFaceLinking Whether the link should unique per face or for the entire block.
     */
    void createLink(EntityPlayer player, EnumHand hand, EnumFacing clickedFace, TransvectorLink link, boolean isFaceLinking);
}
