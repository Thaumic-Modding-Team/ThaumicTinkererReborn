package mod.emt.thaumictinkerer.enchants;

import com.expandedevents.events.ItemAttributeModifierEvent;
import com.invadermonky.thaumicapi.api.ThaumicAPI;
import mod.emt.thaumictinkerer.api.IAddition;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.registry.ModEnchantsTT;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.Collection;
import java.util.UUID;

// TODO: Temporary implementation until we adjust the attribute directly
@Mod.EventBusSubscriber
public class InfusionEnchantmentWrath implements IAddition, IProxy {
    //Copy of `Item#ATTACK_DAMAGE_MODIFIER`
    public static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

    @SubscribeEvent
    public static void onItemAttribute(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        int level = EnumInfusionEnchantment.getInfusionEnchantmentLevel(stack, ModEnchantsTT.WRATH);
        double bonus = 0.5D + (0.5D * level);

        if(level > 0  && event.getSlotType() == EntityEquipmentSlot.MAINHAND) {
            Collection<AttributeModifier> modifiers = event.getOriginalModifiers().get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());
            AttributeModifier toModify = null;
            for(AttributeModifier modifier : modifiers) {
                if(modifier.getID().equals(ATTACK_DAMAGE_MODIFIER)) {
                    toModify = modifier;
                    break;
                }
            }

            if(toModify != null) {
                event.removeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, toModify);
                event.addModifier(SharedMonsterAttributes.ATTACK_DAMAGE, new AttributeModifier(
                        toModify.getID(),
                        toModify.getName(),
                        toModify.getAmount() + bonus,
                        toModify.getOperation())
                );
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
