package mod.emt.thaumictinkerer.enchants;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.registry.ModEnchantsTT;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

@Mod.EventBusSubscriber(modid = ThaumicTinkerer.MOD_ID)
public class InfusionEnchantmentEducational {
    @SubscribeEvent
    public static void onXpDropped(LivingExperienceDropEvent event) {
        EntityPlayer player = event.getAttackingPlayer();

        if(event.getEntity() instanceof EntityLiving) {
            ItemStack heldStack = player.getHeldItemMainhand();
            int level = EnumInfusionEnchantment.getInfusionEnchantmentLevel(heldStack, ModEnchantsTT.EDUCATIONAL);
            int experience = event.getDroppedExperience();
            double experienceCalc = experience * (0.25D * level);
            int experienceCap = 10 * level;

            if(level > 0) {
                if(experienceCalc > experienceCap) {
                    experienceCalc = experienceCap;
                }

                event.setDroppedExperience(experience + (int) Math.round(experienceCalc));
            }
        }
    }
}
