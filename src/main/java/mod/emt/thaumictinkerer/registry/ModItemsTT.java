package mod.emt.thaumictinkerer.registry;


import mod.emt.thaumictinkerer.item.ItemEnergeticNitor;
import net.minecraft.item.Item;

public class ModItemsTT {
    public static final Item ENERGETIC_NITOR = null;

    public static void initItems() {
        //Item initialization goes here. Be sure to register them with RegistrarTT#addAdditionToRegister()
        RegistrarTT.addAdditionToRegister(new ItemEnergeticNitor());
    }
}
