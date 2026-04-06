package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.enchants.InfusionEnchantmentEducational;
import mod.emt.thaumictinkerer.enchants.InfusionEnchantmentProjecting;
import mod.emt.thaumictinkerer.enchants.InfusionEnchantmentWrath;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;

public class ModEnchantsTT {
    // Consuming List
    /*
    Block IDs:
    minecraft:dirt:1 (Corase Dirt)
    minecraft:dirt:2 (Podzol)
    minecraft:magma
    minecraft:mossy_cobblestone
    minecraft:mycelium
    minecraft:snow (should also do this for snow layers)
    minecraft:soul_sand

    Ore Dictionaries:
    cobblestone
    dirt
    endstone
    grass
    gravel
    netherrack
    sand
    sandstone
    stone
    stoneAndesite
    stoneDiorite
    stoneGranite
     */

    public static EnumInfusionEnchantment EDUCATIONAL;
    public static EnumInfusionEnchantment PROJECTING;
    public static EnumInfusionEnchantment WRATH;

    public static void initEnchants() {
        RegistrarTT.addAdditionToRegister(new InfusionEnchantmentEducational());
        RegistrarTT.addAdditionToRegister(new InfusionEnchantmentProjecting());
        RegistrarTT.addAdditionToRegister(new InfusionEnchantmentWrath());
    }
}
