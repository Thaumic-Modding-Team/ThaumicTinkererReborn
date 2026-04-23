package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.enchants.InfusionEnchantmentEducational;
import mod.emt.thaumictinkerer.enchants.InfusionEnchantmentProjecting;
import mod.emt.thaumictinkerer.enchants.InfusionEnchantmentWrath;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

public class ModEnchantsTT {
    public static EnumInfusionEnchantment EDUCATIONAL;
    public static EnumInfusionEnchantment PROJECTING;
    public static EnumInfusionEnchantment WRATH;

    public static void initEnchants() {
        RegistrarTT.addAdditionToRegister(new InfusionEnchantmentEducational());
        RegistrarTT.addAdditionToRegister(new InfusionEnchantmentProjecting());
        RegistrarTT.addAdditionToRegister(new InfusionEnchantmentWrath());
    }
}
