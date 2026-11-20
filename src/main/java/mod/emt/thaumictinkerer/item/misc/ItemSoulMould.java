package mod.emt.thaumictinkerer.item.misc;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import mod.emt.thaumictinkerer.utils.helpers.PlayerHelper;
import net.minecraft.block.BlockBed;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ItemSoulMould extends AbstractItemAddition {
    public ItemSoulMould() {
        super("soul_mould");
        this.setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation("bound"), ((stack, worldIn, entityIn) -> getPlayerId(stack) != null || getEntity(stack) != null ? 1 : 0));
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, EntityPlayer player, @NotNull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        RayTraceResult trace = PlayerHelper.rayTraceBlocks(player, 0);
        if(player.isSneaking() && trace == null || trace.typeOfHit == RayTraceResult.Type.MISS) {
            clearEntityInfo(stack);
            if(world.isRemote) {
                world.playSound(player, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean itemInteractionForEntity(@NotNull ItemStack stack, EntityPlayer player, @NotNull EntityLivingBase target, @NotNull EnumHand hand) {
        ItemStack heldItem = player.getHeldItem(hand);
        World world = player.world;
        if(target instanceof EntityPlayer) {
            this.setPlayer(heldItem, (EntityPlayer) target);
        } else {
            setEntity(heldItem, target);
        }
        if (world.isRemote) {
            world.playSound(player, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
        return true;
    }

    @Override
    public @NotNull EnumActionResult onItemUseFirst(EntityPlayer player, @NotNull World world, @NotNull BlockPos pos, @NotNull EnumFacing side, float hitX, float hitY, float hitZ, @NotNull EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        if(player.isSneaking() && world.getBlockState(pos).getBlock() instanceof BlockBed) {
            for(EntityPlayer online : world.playerEntities) {
                if(online.bedLocation != null && online.getBedLocation().equals(pos)) {
                    this.setPlayer(heldStack, online);
                    if(world.isRemote) {
                        world.playSound(player, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        String playerName = getPlayerName(stack);
        if(!playerName.isEmpty()) {
            tooltip.add(I18n.format("tooltip.thaumictinkerer:soul_mould.bound", playerName));
        } else {
            EntityEntry entry = getEntity(stack);
            if(entry != null && worldIn != null) {
                Entity entity = entry.newInstance(worldIn);
                tooltip.add(I18n.format("tooltip.thaumictinkerer:soul_mould.bound", entity.getName()));
            }
        }
    }

    public static void clearEntityInfo(ItemStack stack) {
        if(stack.getTagCompound() != null) {
            stack.getTagCompound().removeTag("playerId");
            stack.getTagCompound().removeTag("playerName");
            stack.getTagCompound().removeTag("entityId");
            if(stack.getTagCompound().isEmpty()) {
                stack.setTagCompound(null);
            }
        }
    }

    public static boolean isSoulMould(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemSoulMould;
    }

    @Nullable
    public static EntityPlayer getPlayer(ItemStack stack) {
        UUID playerId = getPlayerId(stack);
        return playerId != null ? PlayerHelper.getPlayerFromUUID(playerId) : null;
    }

    @Nullable
    public static UUID getPlayerId(ItemStack stack) {
        if(!isSoulMould(stack))
            return null;

        NBTTagCompound tag = stack.getTagCompound();
        if(tag != null && tag.hasKey("playerId")) {
            return UUID.fromString(tag.getString("playerId"));
        }
        return null;
    }

    public static String getPlayerName(ItemStack stack) {
        return isSoulMould(stack) && stack.getTagCompound() != null ? stack.getTagCompound().getString("playerName") : "";
    }

    public void setPlayer(ItemStack stack, EntityPlayer player) {
        clearEntityInfo(stack);
        stack.setTagInfo("playerId", new NBTTagString(player.getGameProfile().getId().toString()));
        stack.setTagInfo("playerName", new NBTTagString(player.getName()));
    }

    @Nullable
    public static EntityEntry getEntity(ItemStack stack) {
        if(!isSoulMould(stack))
            return null;

        NBTTagCompound tag = stack.getTagCompound();
        if(tag != null && tag.hasKey("entityId")) {
            ResourceLocation loc = new ResourceLocation(tag.getString("entityId"));
            return ForgeRegistries.ENTITIES.getValue(loc);
        }
        return null;
    }

    public static void setEntity(ItemStack stack, EntityLivingBase entity) {
        clearEntityInfo(stack);
        EntityEntry entry = EntityRegistry.getEntry(entity.getClass());
        if(entry != null && entry.getRegistryName() != null) {
            stack.setTagInfo("entityId", new NBTTagString(entry.getRegistryName().toString()));
        }
    }
}
