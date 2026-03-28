package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.enchants.InfusionEnchantmentEducational;
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

    public static void initEnchants() {
        RegistrarTT.addAdditionToRegister(new InfusionEnchantmentEducational());
    }
}
