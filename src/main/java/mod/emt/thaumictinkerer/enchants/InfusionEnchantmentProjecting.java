package mod.emt.thaumictinkerer.enchants;

import com.expandedevents.api.event.ItemAttributeModifierEvent;
import com.invadermonky.thaumicapi.api.ThaumicAPI;
import mod.emt.thaumictinkerer.api.IAddition;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.registry.ModEnchantsTT;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

import java.util.UUID;

public class InfusionEnchantmentProjecting implements IAddition, IProxy {
    public static final UUID REACH_DISTANCE_MODIFIER = UUID.fromString("42ae9ee5-d44e-409f-b177-de359031bdb3");

    @SubscribeEvent
    public void onItemAttribute(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        int level = EnumInfusionEnchantment.getInfusionEnchantmentLevel(stack, ModEnchantsTT.PROJECTING);
        if(level > 0 && event.getSlotType() == EntityEquipmentSlot.MAINHAND) {
            double bonus = 0.5 * level;
            event.addModifier(EntityPlayer.REACH_DISTANCE, new AttributeModifier(
                    REACH_DISTANCE_MODIFIER,
                    "Tool modifier",
                    bonus,
                    Constants.AttributeModifierOperation.ADD
            ));
        }
    }

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
        ModEnchantsTT.PROJECTING = ThaumicAPI.registerInfusionEnchantment(
                "PROJECTING", 3, "TT_ARCANE_INFUSION_ENCHANTMENT", "weapon", "pickaxe", "shovel", "hoe", "axe");
    }
}
