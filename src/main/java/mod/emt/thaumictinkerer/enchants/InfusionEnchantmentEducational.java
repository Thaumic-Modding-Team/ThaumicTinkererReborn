package mod.emt.thaumictinkerer.enchants;

import com.invadermonky.thaumicapi.api.ThaumicAPI;
import mod.emt.thaumictinkerer.api.IAddition;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.registry.ModEnchantsTT;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

@Mod.EventBusSubscriber
public class InfusionEnchantmentEducational implements IAddition, IProxy {
    @SubscribeEvent
    public void onXpDropped(LivingExperienceDropEvent event) {
        EntityPlayer player = event.getAttackingPlayer();

        if(event.getEntity() instanceof EntityLiving) {
            ItemStack heldStack = player.getHeldItemMainhand();
            int level = EnumInfusionEnchantment.getInfusionEnchantmentLevel(heldStack, ModEnchantsTT.EDUCATIONAL);
            if(level > 0) {
                int experience = event.getDroppedExperience();
                double bonus = experience * (ConfigHandlerTT.infusionEnchantments.educational.xpGainPerLevel * level);
                int cap = ConfigHandlerTT.infusionEnchantments.educational.maxXpPerLevel * level;
                if(cap > 0 && bonus > cap) {
                    bonus = cap;
                }
                event.setDroppedExperience(experience + (int) Math.round(bonus));
            }
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if(player != null) {
            ItemStack heldStack = player.getHeldItemMainhand();
            int level = EnumInfusionEnchantment.getInfusionEnchantmentLevel(heldStack, ModEnchantsTT.EDUCATIONAL);
            if(level > 0) {
                int experience = event.getExpToDrop();
                double bonus = experience * (ConfigHandlerTT.infusionEnchantments.educational.xpGainPerLevel * level);
                int cap = ConfigHandlerTT.infusionEnchantments.educational.maxXpPerLevel * level;
                if(cap > 0 && bonus > cap) {
                    bonus = cap;
                }
                event.setExpToDrop(experience + (int) Math.round(bonus));
            }
        }
    }

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
        ModEnchantsTT.EDUCATIONAL = ThaumicAPI.registerInfusionEnchantment(
                "EDUCATIONAL", 3, "INFUSIONENCHANTMENT", "weapon", "pickaxe", "shovel", "hoe", "axe");
    }
}
