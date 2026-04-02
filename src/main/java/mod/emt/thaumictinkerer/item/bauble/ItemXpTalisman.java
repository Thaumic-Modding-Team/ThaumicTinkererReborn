package mod.emt.thaumictinkerer.item.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.item.AbstractItemBauble;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemXpTalisman extends AbstractItemBauble implements IProxy {
    public ItemXpTalisman() {
        super("xp_talisman");
        this.addPropertyOverride(new ResourceLocation("enabled"), ((stack, worldIn, entityIn) -> this.getTalismanEnabled(stack) ? 1 : 0));
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack heldStack = playerIn.getHeldItem(handIn);
        if(playerIn.isSneaking()) {
            this.setTalismanEnabled(heldStack, !this.getTalismanEnabled(heldStack));
        } else {
            int toDrain = this.getExperienceToDrain(playerIn);
            int drained = this.removeExperienceFromTalisman(heldStack, toDrain);
            this.addExperienceToPlayer(playerIn, drained);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof EntityPlayer) {
            this.onWornTick(stack, (EntityPlayer) entityIn);
        }
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entityLiving) {
        if(entityLiving instanceof EntityPlayer && this.getTalismanEnabled(stack)) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if(player.experienceTotal > 0 && !player.isCreative()) {
                int drained = this.removeExperienceFromPlayer(player, 1);
                if(drained > 0) {
                    this.addExperienceToTalisman(stack, drained);
                }
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != this || newStack.getItem() != this
                || this.getTalismanEnabled(oldStack) != this.getTalismanEnabled(newStack);
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public boolean willAutoSync(ItemStack stack, EntityLivingBase player) {
        return true;
    }

    @Override
    public BaubleType getBaubleType(ItemStack stack) {
        return BaubleType.CHARM;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltip.thaumictinkerer:" + (this.getTalismanEnabled(stack) ? "enabled" : "disabled")));
        int experienceTotal = this.getTalismanExperienceTotal(stack);
        int level = this.getTalismanLevel(stack);
        int experience = this.getTalismanExperience(stack);
        if(experienceTotal > 0) {
            tooltip.add("");
            tooltip.add(I18n.format("tooltip.thaumictinkerer:xp_talisman.level", level));
            tooltip.add(I18n.format("tooltip.thaumictinkerer:xp_talisman.partial", experience));
            tooltip.add(I18n.format("tooltip.thaumictinkerer:xp_talisman.total", experienceTotal));
        }
    }

    public int getTalismanExperienceTotal(ItemStack talisman) {
        return this.getStackTag(talisman).getInteger("xpTotal");
    }

    public void setTalismanExperienceTotal(ItemStack talisman, int xpTotal) {
        talisman.setTagInfo("xpTotal", new NBTTagInt(xpTotal));
    }

    public int getTalismanExperience(ItemStack talisman) {
        return this.getStackTag(talisman).getInteger("experience");
    }

    public void setTalismanExperience(ItemStack talisman, int experience) {
        talisman.setTagInfo("experience", new NBTTagInt(experience));
    }

    public int getTalismanLevel(ItemStack talisman) {
        return this.getStackTag(talisman).getInteger("xpLevel");
    }

    public void setTalismanLevel(ItemStack talisman, int experienceLevel) {
        talisman.setTagInfo("xpLevel", new NBTTagInt(experienceLevel));
    }

    public boolean getTalismanEnabled(ItemStack talisman) {
        return talisman.getTagCompound() != null && talisman.getTagCompound().getBoolean("enabled");
    }

    public void setTalismanEnabled(ItemStack talisman, boolean enabled) {
        talisman.setTagInfo("enabled", new NBTTagByte((byte) (enabled ? 1 : 0)));
    }

    public NBTTagCompound getStackTag(ItemStack stack) {
        if(stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    public void addExperienceToTalisman(ItemStack talisman, int amount) {
        if(amount > 0) {
            int experienceTotal = this.getTalismanExperienceTotal(talisman);
            int i = Integer.MAX_VALUE - experienceTotal;

            if (amount > i) {
                amount = i;
            }

            int experience = this.getTalismanExperience(talisman) + amount;
            int level = this.getTalismanLevel(talisman);
            int experienceToLevel = this.getExperienceToLevel(level);

            while (experience >= experienceToLevel) {
                level++;
                experience -= experienceToLevel;
                experienceToLevel = this.getExperienceToLevel(level);
            }

            this.setTalismanExperienceTotal(talisman, experienceTotal + amount);
            this.setTalismanLevel(talisman, level);
            this.setTalismanExperience(talisman, experience);
        }
    }

    public int removeExperienceFromTalisman(ItemStack talisman, int toRemove) {
        int totalXp = this.getTalismanExperienceTotal(talisman);
        if(toRemove > 0 && totalXp > 0) {
            int experience = this.getTalismanExperience(talisman);
            int level = this.getTalismanLevel(talisman);
            while(experience < toRemove && level > 0) {
                level--;
                experience += this.getExperienceToLevel(level);
            }
            toRemove = Math.min(toRemove, experience);
            this.setTalismanExperience(talisman, experience - toRemove);
            this.setTalismanLevel(talisman, level);
            this.setTalismanExperienceTotal(talisman, totalXp - toRemove);
            return toRemove;
        }
        return 0;
    }

    public void addExperienceToPlayer(EntityPlayer player, int experience) {
        if(experience > 0) {
            player.addExperience(experience);
        }
    }

    public int removeExperienceFromPlayer(EntityPlayer player, int toRemove) {
        int experienceTotal = player.experienceTotal;
        if(toRemove > 0 && experienceTotal > 0) {
            int level = player.experienceLevel;
            float percent = player.experience;
            int toLevel = this.getExperienceToLevel(level);
            int experience = (int) (toLevel * percent);
            while(experience < toRemove && level > 0) {
                level--;
                toLevel = this.getExperienceToLevel(level);
                experience += toLevel;
            }
            toRemove = Math.min(toRemove, experience);
            experience -= toRemove;
            player.experienceTotal = experienceTotal - toRemove;
            player.experienceLevel = level;
            player.experience = (float) experience / (float) toLevel;
            return toRemove;
        }
        return 0;
    }

    public int getExperienceToDrain(EntityPlayer player) {
        float percent = player.experience;
        int toLevel = player.xpBarCap();
        int current = (int) (percent * toLevel);
        return toLevel - current;
    }

    public int getExperienceToLevel(int experienceLevel) {
        if (experienceLevel >= 30) {
            return 112 + (experienceLevel - 30) * 9;
        } else {
            return experienceLevel >= 15 ? 37 + (experienceLevel - 15) * 5 : 7 + experienceLevel * 2;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onXpPickup(PlayerPickupXpEvent event) {
        if(!event.getEntityPlayer().world.isRemote && !event.getOrb().isDead && event.getOrb().getXpValue() > 0) {
            EntityPlayer player = event.getEntityPlayer();
            int slot = BaublesApi.isBaubleEquipped(player, this);
            if(slot > -1) {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                ItemStack talisman = handler.getStackInSlot(slot).copy();
                this.addExperienceToTalisman(talisman, event.getOrb().getXpValue());
                handler.setStackInSlot(slot, talisman);
                event.getOrb().setDead();
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
    public boolean isEnabled() {
        return true;
    }
}
