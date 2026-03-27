package mod.emt.thaumictinkerer.api.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public interface ITransvectorLink {
    /**
     * Creates a link between the passed position and this tile entity.
     *
     * @param player The player performing the interaction.
     * @param hand The hand being used in the interaction.
     * @param clickedFace The block face clicked by the player.
     * @param linkPos The position saved to the linking tool.
     * @param linkFace The face saved to the linking tool.
     * @param isFaceLinking Whether the link should unique per face or for the entire block.
     */
    void createLink(EntityPlayer player, EnumHand hand, EnumFacing clickedFace, BlockPos linkPos, EnumFacing linkFace, boolean isFaceLinking);
}
