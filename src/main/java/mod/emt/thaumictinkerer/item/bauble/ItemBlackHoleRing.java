package mod.emt.thaumictinkerer.item.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.item.AbstractItemBauble;
import mod.emt.thaumictinkerer.inventory.handlers.BlackHoleRingHandler;
import mod.emt.thaumictinkerer.recipes.crafting.RecipeBlackHoleRingClearSlot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemBlackHoleRing extends AbstractItemBauble implements IProxy {
    private static final Map<UUID, ItemStack> PROVISION_CACHE = new HashMap<>();

    public ItemBlackHoleRing() {
        super("black_hole_ring");
        this.addPropertyOverride(new ResourceLocation("enabled"), ((stack, worldIn, entityIn) -> this.isEnabled() ? 1 : 0));
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new BlackHoleRingHandler(stack);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof EntityPlayer) {
            UUID playerId = ((EntityPlayer) entityIn).getGameProfile().getId();
            if(PROVISION_CACHE.containsKey(playerId)) {
                this.provideMaterials((EntityPlayer) entityIn, stack, PROVISION_CACHE.get(playerId));
            }
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            this.setRingEnabled(heldStack, !this.isRingEnabled(heldStack));
        } else {
            this.nextActiveSlot(heldStack);
            if(!worldIn.isRemote) {
                ItemStack currStack = this.getCurrentStack(heldStack);
                ItemStack filterStack = this.getCurrentFilter(heldStack);
                if(!filterStack.isEmpty()) {
                    String itemName = filterStack.getDisplayName();
                    int count = currStack.getCount();
                    playerIn.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:black_hole_ring.slotinfo", itemName, count), true);
                } else {
                    playerIn.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:black_hole_ring.slotempty", new TextComponentTranslation("tooltip.thaumictinkerer:empty")).setStyle(new Style().setColor(TextFormatting.GRAY)), true);
                }
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @Override
    public @NotNull EnumActionResult onItemUse(@NotNull EntityPlayer player, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldStack = player.getHeldItem(hand);
        RayTraceResult trace = new RayTraceResult(new Vec3d(hitX, hitY, hitZ), facing, pos);
        IBlockState state = worldIn.getBlockState(pos);
        ItemStack blockStack = state.getBlock().getPickBlock(state, trace, worldIn, pos, player);
        if (!blockStack.isEmpty()) {
            int slot = this.getActiveSlot(heldStack);
            BlackHoleRingHandler handler = this.getRingHandler(heldStack);
            if (handler.getFilter(slot).isEmpty()) {
                handler.setFilter(slot, blockStack);
                if(!worldIn.isRemote) {
                    player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:black_hole_ring.selected", blockStack.getDisplayName()), false);
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    @Override
    public @NotNull EnumActionResult onItemUseFirst(EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing side, float hitX, float hitY, float hitZ, @NotNull EnumHand hand) {
        ItemStack heldItem = player.getHeldItem(hand);
        if(player.isSneaking()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null) {
                IItemHandler tileHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
                if (tileHandler != null) {
                    //Pulling from the current active slot and inserting into the tile inventory
                    BlackHoleRingHandler ringHandler = this.getRingHandler(heldItem);
                    int slot = this.getActiveSlot(heldItem);
                    ItemStack slotStack = ringHandler.getStackInSlot(slot);
                    while(!slotStack.isEmpty()) {
                        ItemStack toInsert = ringHandler.extractItem(slot, ringHandler.getSlotLimit(slot), true);
                        ItemStack rem = ItemHandlerHelper.insertItem(tileHandler, toInsert, true);
                        if (rem.isEmpty() || rem.getCount() != toInsert.getCount()) {
                            ItemHandlerHelper.insertItem(tileHandler, ringHandler.extractItem(slot, toInsert.getCount() - rem.getCount(), false), false);
                            slotStack = ringHandler.getStackInSlot(slot);
                        } else {
                            break;
                        }
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public boolean hasEffect(@NotNull ItemStack stack) {
        return this.isRingEnabled(stack);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.thaumictinkerer:" + (this.isRingEnabled(stack) ? "enabled" : "disabled")));
        tooltip.add(I18n.format("tooltip.thaumictinkerer:black_hole_ring.info"));
        BlackHoleRingHandler handler = this.getRingHandler(stack);
        int activeSlot = this.getActiveSlot(stack);
        for (int i = 0; i < handler.getSlots(); i++) {
            String info = (i == activeSlot ? " ┠>" : " ┠ ");
            ItemStack filterStack = handler.getFilter(i);
            ItemStack slotStack = handler.getStackInSlot(i);
            if(filterStack.isEmpty()) {
                info += I18n.format("tooltip.thaumictinkerer:empty");
            } else {
                info += filterStack.getDisplayName() + " x" + slotStack.getCount();
            }
            tooltip.add(info);
        }
    }

    public List<ItemStack> getBlackHoleRings(EntityPlayer player) {
        List<ItemStack> rings = new ArrayList<>();
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        for(int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if(!stack.isEmpty() && stack.getItem() instanceof ItemBlackHoleRing) {
                rings.add(stack);
            }
        }
        for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(!stack.isEmpty() && stack.getItem() instanceof ItemBlackHoleRing) {
                rings.add(stack);
            }
        }
        return rings;
    }

    public BlackHoleRingHandler getRingHandler(ItemStack ring) {
        return new BlackHoleRingHandler(ring);
    }

    public ItemStack getCurrentStack(ItemStack ring) {
        return this.getRingHandler(ring).getStackInSlot(this.getActiveSlot(ring));
    }

    public ItemStack getCurrentFilter(ItemStack ring) {
        return this.getRingHandler(ring).getFilter(this.getActiveSlot(ring));
    }

    public void provideMaterials(EntityPlayer player, ItemStack ringStack, ItemStack toProvide) {
        if(!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            PROVISION_CACHE.remove(player.getGameProfile().getId());
            return;
        }

        if(!this.isRingEnabled(ringStack)) {
            return;
        }

        BlackHoleRingHandler handler = this.getRingHandler(ringStack);
        for(int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if(!stack.isEmpty() && ItemHandlerHelper.canItemStacksStack(stack, toProvide)) {
                ItemStack extracted = handler.extractItem(i, stack.getMaxStackSize(), false);
                ItemHandlerHelper.giveItemToPlayer(player, extracted, player.inventory.currentItem);
                PROVISION_CACHE.remove(player.getGameProfile().getId());
                return;
            }
        }
    }

    //##########################################################
    // Tag Stuff

    public boolean isRingEnabled(ItemStack stack) {
        return stack.getTagCompound() != null && stack.getTagCompound().getBoolean("enabled");
    }

    public void setRingEnabled(ItemStack stack, boolean enabled) {
        stack.setTagInfo("enabled", new NBTTagByte((byte) (enabled ? 1 : 0)));
    }

    public int getActiveSlot(ItemStack stack) {
        return stack.getTagCompound() != null ? stack.getTagCompound().getInteger("activeSlot") : 0;
    }

    public void setActiveSlot(ItemStack stack, int slot) {
        stack.setTagInfo("activeSlot", new NBTTagInt(slot));
    }

    public void nextActiveSlot(ItemStack stack) {
        int activeSlot = this.getActiveSlot(stack);
        IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if(handler != null) {
            this.setActiveSlot(stack, (activeSlot + 1) % handler.getSlots());
        }
    }

    //##########################################################
    // IBauble

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase player) {
        if(player instanceof EntityPlayer) {
            UUID playerId = ((EntityPlayer) player).getGameProfile().getId();
            if(PROVISION_CACHE.containsKey(playerId)) {
                this.provideMaterials((EntityPlayer) player, stack, PROVISION_CACHE.get(playerId));
            }
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

    //##########################################################
    // Events

    @SuppressWarnings("deprecation")
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockPlaced(BlockEvent.PlaceEvent event) {
        EntityPlayer player = event.getPlayer();
        if(player != null && !(player instanceof FakePlayer) && !player.isCreative() && event.getHand() == EnumHand.MAIN_HAND) {
            ItemStack heldStack = player.getHeldItem(event.getHand());
            if(heldStack.getCount() == 1) {
                PROVISION_CACHE.put(player.getGameProfile().getId(), heldStack.copy());
            }
        }
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        World world = player.world;
        EntityItem entityItem = event.getItem();
        ItemStack toPickUp = entityItem.getItem();

        if(!entityItem.isEntityAlive() || toPickUp.isEmpty())
            return;

        for(ItemStack ring : this.getBlackHoleRings(player)) {
            if(!this.isRingEnabled(ring)) {
                continue;
            }

            BlackHoleRingHandler handler = this.getRingHandler(ring);
            if(handler != null) {
                ItemStack remaining = ItemHandlerHelper.insertItem(handler, toPickUp, false);
                if(remaining.isEmpty()) {
                    entityItem.setItem(ItemStack.EMPTY);
                    entityItem.setDead();
                    event.setCanceled(true);
                    world.playSound(null,
                            player.posX, player.posY + 0.5, player.posZ,
                            SoundEvents.ENTITY_ITEM_PICKUP,
                            SoundCategory.PLAYERS,
                            0.2F,
                            ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    return;
                } else if(toPickUp.getCount() != remaining.getCount()) {
                    entityItem.setItem(remaining);
                    toPickUp = remaining;
                }
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
        registry.register(new RecipeBlackHoleRingClearSlot());
    }

    @Override
    public boolean isEnabled() {
        //TODO: Config disable
        return true;
    }
}
