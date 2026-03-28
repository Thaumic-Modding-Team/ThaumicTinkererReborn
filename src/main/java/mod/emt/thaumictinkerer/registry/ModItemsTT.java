package mod.emt.thaumictinkerer.registry;


import mod.emt.thaumictinkerer.item.*;
import mod.emt.thaumictinkerer.item.bauble.ItemGoliathRing;
import net.minecraft.item.Item;

public class ModItemsTT {
    public static final Item ENDER_MIRROR = null;
    public static final Item ENERGETIC_NITOR = null;
    public static final Item FELINE_CHARM = null;
    public static final Item GOLIATH_RING = null;
    public static final Item TRANSVECTOR_BINDER = null;
    public static final Item UMBRAL_DYE = null;

    public static void initItems() {
        //Item initialization goes here. Be sure to register them with RegistrarTT#addAdditionToRegister()
        //TODO: Organize order
        RegistrarTT.addAdditionToRegister(new ItemEnderMirror());
        RegistrarTT.addAdditionToRegister(new ItemEnergeticNitor());
        RegistrarTT.addAdditionToRegister(new ItemGoliathRing());
        RegistrarTT.addAdditionToRegister(new ItemTransvectorBinder());
        RegistrarTT.addAdditionToRegister(new ItemUmbralDye());
        RegistrarTT.addAdditionToRegister(new ItemFelineCharm());
        RegistrarTT.addAdditionToRegister(new ItemSwordTest("test_sword"));
    }
}
