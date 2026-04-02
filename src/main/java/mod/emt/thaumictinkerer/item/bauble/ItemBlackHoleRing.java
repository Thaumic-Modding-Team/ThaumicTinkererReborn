package mod.emt.thaumictinkerer.item.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.item.AbstractItemBauble;
import mod.emt.thaumictinkerer.recipes.crafting.RecipeBlackHoleRingExtract;
import mod.emt.thaumictinkerer.recipes.crafting.RecipeBlackHoleRingInsert;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemBlackHoleRing extends AbstractItemBauble implements IProxy {
    public ItemBlackHoleRing() {
        super("black_hole_ring");
        this.addPropertyOverride(new ResourceLocation("enabled"), ((stack, worldIn, entityIn) -> this.isEnabled() ? 1 : 0));
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(!worldIn.isRemote && this.getEnabled(stack) && entityIn instanceof EntityPlayer) {
            this.onWornTick(stack, (EntityPlayer) entityIn);
        }
    }

    @Override
    public void onWornTick(ItemStack ring, EntityLivingBase player) {
        Tuple<ItemStack, Integer> toGive = this.getToGivePlayer(ring);
        if(toGive != null && player instanceof EntityPlayer) {
            ItemHandlerHelper.giveItemToPlayer((EntityPlayer) player, toGive.getFirst(), toGive.getSecond());
            this.clearToGivePlayer(ring);
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            this.setEnabled(stack, !this.getEnabled(stack));
        } else {
            this.nextActiveSlot(stack);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.thaumictinkerer:" + (this.getEnabled(stack) ? "enabled" : "disabled")));
        tooltip.add(I18n.format("tooltip.thaumictinkerer:black_hole_ring.info"));
        if(this.isRingEmpty(stack)) {
            tooltip.add("  " + I18n.format("tooltip.thaumictinkerer:empty"));
        } else {
            //TODO: Create an indicator for the currently active slot.
            int activeSlot = this.getActiveSlot(stack);
            this.getRingContents(stack).forEach((contents, amount) -> {
                //TODO: fix these colors in the language key
                //TODO: Change the amount into a formatted string
                tooltip.add("  " + I18n.format("tooltip.thaumictinkerer:black_hole_ring.slot", contents.getDisplayName(), amount));
            });
        }
    }

    @Override
    public boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }

    public boolean isRingEmpty(ItemStack ring) {
        return ring.getTagCompound() == null
                || !ring.getTagCompound().hasKey("inventory")
                || ring.getTagCompound().getTagList("inventory", Constants.NBT.TAG_COMPOUND).isEmpty();
    }

    public Map<ItemStack, Integer> getRingContents(ItemStack ring) {
        Map<ItemStack, Integer> contents = new LinkedHashMap<>();
        if(ring.getTagCompound() != null) {
            NBTTagList list = ring.getTagCompound().getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                contents.put(new ItemStack(tag.getCompoundTag("item")), tag.getInteger("amount"));
            }
        }
        return contents;
    }

    public void addItem(ItemStack ring, ItemStack stack) {
        if(ring.getTagCompound() == null) {
            ring.setTagCompound(new NBTTagCompound());
        }

        NBTTagList list = ring.getTagCompound().getTagList("inventory", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            ItemStack listStack = new ItemStack(tag.getCompoundTag("item"));
            if(ItemHandlerHelper.canItemStacksStack(listStack, stack)) {
                int current = tag.getInteger("amount");
                tag.setInteger("amount", current + stack.getCount());
                return;
            }
        }
        ItemStack copy = stack.copy();
        int amount = copy.getCount();
        copy.setCount(1);

        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("item", copy.serializeNBT());
        tag.setInteger("amount", amount);
        list.appendTag(tag);
    }

    public void removeItem(ItemStack ring, ItemStack stack) {
        if(ring.getTagCompound() != null) {
            NBTTagList list = ring.getTagCompound().getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                ItemStack listStack = new ItemStack(tag.getCompoundTag("item"));
                if(ItemHandlerHelper.canItemStacksStack(listStack, stack)) {
                    list.removeTag(i);
                    if(this.getActiveSlot(ring) == i) {
                        this.nextActiveSlot(ring);
                    }
                    if(list.isEmpty()) {
                        ring.getTagCompound().removeTag("inventory");
                    }
                    return;
                }
            }
        }
    }

    public void removeAllItems(ItemStack ring) {
        if(ring.getTagCompound() != null) {
            ring.getTagCompound().removeTag("inventory");
            ring.getTagCompound().removeTag("activeSlot");
        }
    }

    public int getContainedAmount(ItemStack ring, ItemStack stack) {
        if(ring.getTagCompound() != null) {
            NBTTagList list = ring.getTagCompound().getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                ItemStack listStack = new ItemStack(tag.getCompoundTag("item"));
                if(ItemHandlerHelper.canItemStacksStack(listStack, stack)) {
                    return tag.getInteger("amount");
                }
            }
        }
        return -1;
    }

    public void setContainedAmount(ItemStack ring, ItemStack stack, int amount) {
        if(ring.getTagCompound() == null) {
            ring.setTagCompound(new NBTTagCompound());
        }

        NBTTagList list = ring.getTagCompound().getTagList("inventory", Constants.NBT.TAG_COMPOUND);
        for(int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            ItemStack listStack = new ItemStack(tag.getCompoundTag("item"));
            if(ItemHandlerHelper.canItemStacksStack(listStack, stack)) {
                tag.setInteger("amount", amount);
                ring.getTagCompound().setTag("inventory", list);
                return;
            }
        }
        NBTTagCompound newTag = new NBTTagCompound();
        newTag.setTag("item", stack.serializeNBT());
        newTag.setInteger("amount", amount);
        list.appendTag(newTag);
        ring.getTagCompound().setTag("inventory", list);
    }








    public boolean canInsertItem(ItemStack ring, ItemStack stack) {
        if(ring.getTagCompound() != null) {
            NBTTagList list = ring.getTagCompound().getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            for(int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                ItemStack listStack = new ItemStack(tag.getCompoundTag("item"));
                if(ItemHandlerHelper.canItemStacksStack(listStack, stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack insertStack(ItemStack ring, ItemStack stack, boolean forceInsert) {
        return insertAmount(ring, stack, stack.getCount(), forceInsert);
    }

    public ItemStack insertAmount(ItemStack ring, ItemStack stack, int amount, boolean forceInsert) {
        if(forceInsert || (this.getEnabled(ring) && this.canInsertItem(ring, stack))) {
            int current = Math.max(0, this.getContainedAmount(ring, stack));
            this.setContainedAmount(ring, stack, current + amount);
            return ItemStack.EMPTY;
        }
        return stack;
    }







    public ItemStack extractStack(ItemStack ring, ItemStack stack, boolean removeIfEmpty) {
        return this.extractAmount(ring, stack, stack.getCount(), removeIfEmpty);
    }

    public ItemStack extractAmount(ItemStack ring, ItemStack stack, int amount, boolean removeIfEmpty) {
        if(amount <= 0)
            return ItemStack.EMPTY;

        int current = this.getContainedAmount(ring, stack);
        if(current > -1) {
            if(current == 0) {
                if(removeIfEmpty) {
                    this.removeItem(ring, stack);
                }
            } else {
                int extracted = Math.min(amount, current);
                ItemStack copy = stack.copy();
                copy.setCount(extracted);
                this.setContainedAmount(ring, stack, current - extracted);
                return copy;
            }
        }
        return ItemStack.EMPTY;
    }













    @Nullable
    public Tuple<ItemStack,Integer> getToGivePlayer(ItemStack ring) {
        if(ring.getTagCompound() != null && ring.getTagCompound().hasKey("toGive")) {
            NBTTagCompound tag = ring.getTagCompound().getCompoundTag("toGive");
            ItemStack toGive = new ItemStack(tag.getCompoundTag("item"));
            int slot = tag.getInteger("slot");
            return new Tuple<>(toGive, slot);
        }
        return null;
    }

    public void setToGivePlayer(ItemStack ring, ItemStack toGive, int slotPriority) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("item", toGive.serializeNBT());
        tag.setInteger("slot", slotPriority);
        ring.setTagInfo("toGive", tag);
    }

    public void clearToGivePlayer(ItemStack ring) {
        if(ring.getTagCompound() != null) {
            ring.getTagCompound().removeTag("toGive");
        }
    }

    public boolean getEnabled(ItemStack ring) {
        return ring.getTagCompound() != null && ring.getTagCompound().getBoolean("isEnabled");
    }

    public void setEnabled(ItemStack ring, boolean isEnabled) {
        ring.setTagInfo("isEnabled", new NBTTagByte((byte) (isEnabled ? 1 : 0)));
    }

    public int getActiveSlot(ItemStack ring) {
        if(ring.getTagCompound() != null && ring.getTagCompound().hasKey("activeSlot")) {
            return ring.getTagCompound().getInteger("activeSlot");
        }
        return -1;
    }

    public void nextActiveSlot(ItemStack ring) {
        int activeSlot = this.getActiveSlot(ring);
        if(ring.getTagCompound() != null && ring.getTagCompound().hasKey("inventory") && activeSlot > -1) {
            NBTTagList list = ring.getTagCompound().getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            if(list.tagCount() > 0) {
                int next = (activeSlot + 1) % list.tagCount();
                ring.getTagCompound().setInteger("activeSlot", next);
            } else {
                ring.getTagCompound().removeTag("activeSlot");
            }
        }
    }






















    @SuppressWarnings("deprecation")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockPlaced(BlockEvent.PlaceEvent event) {
        if(!event.isCanceled() && event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote && event.getHand() != null) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            EnumHand hand = event.getHand();
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            int slot = BaublesApi.isBaubleEquipped(player, this);
            if(slot > -1) {
                ItemStack ring = handler.getStackInSlot(slot).copy();
                ItemStack placedStack = player.getHeldItem(hand);
                if(this.getEnabled(ring) && !placedStack.isEmpty() && placedStack.getCount() == 1 && !player.isCreative()) {
                    ItemStack extracted = this.extractAmount(ring, placedStack, 1, false);
                    if(!extracted.isEmpty()) {
                        int invSlot = hand == EnumHand.MAIN_HAND ? player.inventory.currentItem : player.inventory.getSizeInventory() - 1;
                        this.setToGivePlayer(ring, extracted, invSlot);
                        handler.setStackInSlot(slot, ring);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        ItemStack toInsert = event.getStack();
        int slot = BaublesApi.isBaubleEquipped(event.player, this);
        if(!toInsert.isEmpty() && slot > -1) {
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(event.player);
            ItemStack ringCopy = handler.getStackInSlot(slot).copy();
            if(!ringCopy.isEmpty() && this.insertStack(ringCopy, toInsert, false).isEmpty()) {
                handler.setStackInSlot(slot, ringCopy);
                event.setCanceled(true);
            }
        }
    }

    //##########################################################
    // IItemAddition

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerRecipe(IForgeRegistry<IRecipe> registry) {
        registry.register(new RecipeBlackHoleRingExtract());
        registry.register(new RecipeBlackHoleRingInsert());
    }

    @Override
    public boolean isEnabled() {
        //TODO: Config disable
        return true;
    }
}
