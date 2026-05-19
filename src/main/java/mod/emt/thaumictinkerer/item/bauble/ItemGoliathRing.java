package mod.emt.thaumictinkerer.item.bauble;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.api.item.AbstractItemBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

public class ItemGoliathRing extends AbstractItemBauble implements IProxy {
    public ItemGoliathRing() {
        super("goliath_ring");
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        if(player instanceof EntityPlayer) {
            return BaublesApi.isBaubleEquipped((EntityPlayer) player, this) < 0;
        }
        return false;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.RING;
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        if(!event.getEntityLiving().world.isRemote && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            float health = player.getHealth();
            float damage = event.getAmount();
            if (health >= 6.0f && damage >= health && event.getSource() != DamageSource.FALL) {
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
