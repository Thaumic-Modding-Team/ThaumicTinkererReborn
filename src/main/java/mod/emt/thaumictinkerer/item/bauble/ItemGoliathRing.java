package mod.emt.thaumictinkerer.item.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.item.AbstractItemBauble;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemGoliathRing extends AbstractItemBauble implements IProxy {
    public ItemGoliathRing() {
        super("goliath_ring");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World worldIn, @NotNull List<String> tooltip, @NotNull ITooltipFlag flagIn) {
        //TODO: Tooltip explaining the ring?
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        if(player instanceof EntityPlayer) {
            return BaublesApi.isBaubleEquipped((EntityPlayer) player, this) < 0;
        }
        return true;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        //TODO: Check this isn't double reducing the damage.
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            float health = player.getHealth();
            float damage = event.getAmount();
            if (health > 6.0f && damage >= health) {
                IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
                int slot = BaublesApi.isBaubleEquipped(player, this);
                if (handler != null && slot >= 0) {
                    handler.extractItem(slot, handler.getSlotLimit(slot), false);
                    player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    player.sendMessage(new TextComponentTranslation("chat.thaumictinkerer:golaith_ring.shattered"));
                    event.setAmount(health - 1.0f);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        //TODO: Check this isn't double reducing the damage.
        if(event.getEntityLiving() instanceof EntityPlayer && event.getAmount() > 1.0f) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if(BaublesApi.isBaubleEquipped(player, this) >= 0) {
                event.setAmount(event.getAmount() - 1.0f);
            }
        }
    }

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
