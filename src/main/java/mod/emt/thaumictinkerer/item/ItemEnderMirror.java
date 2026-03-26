package mod.emt.thaumictinkerer.item;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ItemEnderMirror extends AbstractItemAddition {
    public ItemEnderMirror() {
        super("ender_mirror");
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        InventoryEnderChest chest = playerIn.getInventoryEnderChest();
        if(chest != null) {
            if(!worldIn.isRemote) {
                playerIn.displayGUIChest(chest);
                playerIn.addStat(StatList.ENDERCHEST_OPENED);
            } else {
                playerIn.playSound(SoundEvents.BLOCK_ENDERCHEST_OPEN, 1.0f, 1.0f);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
