package mod.emt.thaumictinkerer.item;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.item.AbstractItemBauble;
import mod.emt.thaumictinkerer.config.ConfigTags;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemConsumingSigil extends AbstractItemBauble implements IProxy {
    public ItemConsumingSigil() {
        super("consuming_sigil");
        this.setMaxStackSize(1);
        //this.addPropertyOverride(new ResourceLocation("enabled"), (stack, worldIn, entityIn) -> getEnabled(stack) ? 1 : 0);
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            this.setEnabled(heldStack, !this.getEnabled(heldStack));
            return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean hasEffect(@NotNull ItemStack stack) {
        return this.getEnabled(stack);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.thaumictinkerer:" + (this.getEnabled(stack) ? "enabled" : "disabled")));
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.TRINKET;
    }

    public boolean getEnabled(ItemStack stack) {
        return stack.getTagCompound() != null && stack.getTagCompound().getBoolean("enabled");
    }

    public void setEnabled(ItemStack stack, boolean enabled) {
        stack.setTagInfo("enabled", new NBTTagByte((byte) (enabled ? 1 : 0)));
    }

    public boolean hasEnabledSigil(EntityPlayer player) {
        int slot = BaublesApi.isBaubleEquipped(player, this);
        if(slot > -1) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            ItemStack stack = handler.getStackInSlot(slot);
            if(this.getEnabled(stack)) {
                return true;
            }
        }

        for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(!stack.isEmpty() && stack.getItem() == this && this.getEnabled(stack)) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onItemHarvest(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        if(player != null && this.hasEnabledSigil(player)) {
            event.getDrops().removeIf(ConfigTags::shouldConsumingVoidDrop);
        }
    }

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
