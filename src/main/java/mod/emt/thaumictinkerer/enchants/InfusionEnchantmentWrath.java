package mod.emt.thaumictinkerer.enchants;

import com.invadermonky.thaumicapi.api.ThaumicAPI;
import mod.emt.thaumictinkerer.api.IAddition;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.registry.ModEnchantsTT;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

// TODO: Temporary implementation until we adjust the attribute directly
@Mod.EventBusSubscriber
public class InfusionEnchantmentWrath implements IAddition, IProxy {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingHurt(LivingHurtEvent event) {
        DamageSource damageSource = event.getSource();
        Entity trueSource = damageSource.getTrueSource();

        if(trueSource instanceof EntityPlayer && trueSource != null) {
            ItemStack heldStack = ((EntityPlayer) trueSource).getHeldItemMainhand();
            int level = EnumInfusionEnchantment.getInfusionEnchantmentLevel(heldStack, ModEnchantsTT.WRATH);

            double bonus = 0.5D + (0.5D * level);
            if(level > 0) {
                event.setAmount(event.getAmount() + (float) bonus);
            }
        }
    }

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
        ModEnchantsTT.WRATH = ThaumicAPI.registerInfusionEnchantment(
                "WRATH", 5, "INFUSIONENCHANTMENT", "weapon", "pickaxe", "shovel", "hoe", "axe");
    }

    @Override
    public void registerRecipe(IForgeRegistry<IRecipe> registry) {
        //TODO: Recipes
    }

    @Override
    public void registerResearchLocation() {
        //TODO: Research location
    }

    @Override
    public boolean isEnabled() {
        //TODO: Config toggle
        return ConfigHandlerTT.infusionEnchantments.wrath.enable;
    }
}
