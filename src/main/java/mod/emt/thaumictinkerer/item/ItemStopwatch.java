package mod.emt.thaumictinkerer.item;

import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import mod.emt.thaumictinkerer.utils.helpers.WorldHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectList;

import java.util.Map;

public class ItemStopwatch extends AbstractItemAddition implements IProxy {
    public static final int COOLDOWN_TIME = 1210;

    public ItemStopwatch() {
        super("recall_stopwatch");
        this.setMaxStackSize(1);
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            int cooldown = this.getCooldown(stack);
            if(cooldown > 0) {
                this.setCooldown(stack, cooldown - 1);
            } else if(this.getShouldTeleport(stack)) {
                this.attemptTeleport(stack, player);
            }
        }
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if(!worldIn.isRemote) {
            this.setShouldTeleport(heldStack, true);
            this.setPlayerPosition(heldStack, playerIn);
            playerIn.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:recall_stopwatch.start"), true);
        }
        this.setCooldown(heldStack, COOLDOWN_TIME);
        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem() || slotChanged;
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    public int getCooldown(ItemStack stopwatch) {
        return stopwatch.getTagCompound() != null ? stopwatch.getTagCompound().getInteger("cooldown") : 0;
    }

    public void setCooldown(ItemStack stopwatch, int cooldown) {
        stopwatch.setTagInfo("cooldown", new NBTTagInt(cooldown));
    }

    public boolean getShouldTeleport(ItemStack stopwatch) {
        return stopwatch.getTagCompound() != null && stopwatch.getTagCompound().getBoolean("shouldTeleport");
    }

    public void setShouldTeleport(ItemStack stopwatch, boolean shouldTeleport) {
        stopwatch.setTagInfo("shouldTeleport", new NBTTagByte((byte) (shouldTeleport ? 1 : 0)));
    }

    public void setPlayerPosition(ItemStack stopwatch, EntityPlayer player) {
        if(!player.world.isRemote) {
            stopwatch.setTagInfo("world", new NBTTagInt(player.world.provider.getDimension()));
            stopwatch.setTagInfo("posX", new NBTTagDouble(player.posX));
            stopwatch.setTagInfo("posY", new NBTTagDouble(player.posY));
            stopwatch.setTagInfo("posZ", new NBTTagDouble(player.posZ));
            stopwatch.setTagInfo("health", new NBTTagFloat(player.getHealth()));
            NBTTagCompound foodTag = new NBTTagCompound();
            player.getFoodStats().writeNBT(foodTag);
            stopwatch.setTagInfo("foodStats", foodTag);
            //TODO: SimpleDifficulty and TAN compat?
        }
    }

    public void clearPlayerPosition(ItemStack stopwatch) {
        if(stopwatch.getTagCompound() != null) {
            this.setShouldTeleport(stopwatch, false);
            stopwatch.getTagCompound().removeTag("world");
            stopwatch.getTagCompound().removeTag("posX");
            stopwatch.getTagCompound().removeTag("posY");
            stopwatch.getTagCompound().removeTag("posZ");
            stopwatch.getTagCompound().removeTag("health");
            stopwatch.getTagCompound().removeTag("foodStats");
            stopwatch.getTagCompound().removeTag("cooldown");
        }
    }

    public void attemptTeleport(ItemStack stopwatch, EntityPlayer player) {
        if(!player.world.isRemote) {
            NBTTagCompound tag = stopwatch.getTagCompound();
            if (tag != null) {
                World world = WorldHelper.getWorldFromId(tag.getInteger("world"), true);
                double posX = tag.getDouble("posX");
                double posY = tag.getDouble("posY");
                double posZ = tag.getDouble("posZ");
                float health = tag.getFloat("health");
                NBTTagCompound foodTag = tag.getCompoundTag("foodStats");

                if(world != null) {
                    if (player.world.provider.getDimension() != world.provider.getDimension()) {
                        player.changeDimension(world.provider.getDimension(), ((world1, entity, yaw1) -> {}));
                    }
                    player.setPositionAndUpdate(posX, posY, posZ);
                    player.setHealth(health);
                    player.getFoodStats().readNBT(foodTag);
                    player.fallDistance = 0;
                }
                this.clearPlayerPosition(stopwatch);
            }
        }
    }

    public int getCooldownFromInventory(EntityPlayer player) {
        for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(stack.getItem() instanceof ItemStopwatch) {
                int cooldown = this.getCooldown(stack);
                if(cooldown > 0) {
                    return cooldown;
                }
            }
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderOverlayPost(RenderGameOverlayEvent.Text.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            Minecraft mc = Minecraft.getMinecraft();
            int cooldown = this.getCooldownFromInventory(mc.player);
            if(cooldown > 0) {
                this.renderCooldownHud(mc, event.getResolution(), cooldown);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void renderCooldownHud(Minecraft mc, ScaledResolution resolution, int cooldownTicks) {
        int seconds = cooldownTicks / 20;
        String time = String.format("0:%02d", seconds);

        int height = 20 * resolution.getScaleFactor();
        int width = 16 + 3 + mc.fontRenderer.getStringWidth(time);
        int xStart = resolution.getScaledWidth() / 2 - width / 2;
        int yStart = resolution.getScaledHeight() / 2 + height;

        ItemStack stack = new ItemStack(this);
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, xStart, yStart);
        mc.fontRenderer.drawString(time, xStart + 19, yStart + 4, 0x9376e3, false);

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    //##########################################################
    // IItemAddition

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        MinecraftForge.EVENT_BUS.register(this);
    }


    //TODO: This stuff.

    @Override
    public void registerRecipe(IForgeRegistry<IRecipe> registry) {

    }

    @Override
    public void registerResearchLocation() {

    }

    @Override
    public void registerAspects(AspectEventProxy registry, Map<ItemStack, AspectList> aspectMap) {

    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
