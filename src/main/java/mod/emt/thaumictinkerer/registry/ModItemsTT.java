package mod.emt.thaumictinkerer.registry;


import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.item.base.ItemBase;
import mod.emt.thaumictinkerer.item.bauble.ItemConsumingSigil;
import mod.emt.thaumictinkerer.item.bauble.ItemFelineCharm;
import mod.emt.thaumictinkerer.item.bauble.ItemGoliathRing;
import mod.emt.thaumictinkerer.item.bauble.ItemXpTalisman;
import mod.emt.thaumictinkerer.item.misc.ItemEnergeticNitor;
import mod.emt.thaumictinkerer.item.misc.ItemEntitySoul;
import mod.emt.thaumictinkerer.item.tools.*;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ThaumicTinkerer.MOD_ID)
public class ModItemsTT {
    public static final Item ARCANE_QUARTZ = null;
    public static final Item CONDOR_SWORD = null;
    public static final Item CONSUMING_SIGIL = null;
    public static final Item ENDER_MIRROR = null;
    public static final Item EVERFULL_BUCKET = null;
    public static final Item ENERGETIC_NITOR = null;
    public static final Item ENTITY_SOUL_ALIEN = null;
    public static final Item ENTITY_SOUL_ARCANE = null;
    public static final Item ENTITY_SOUL_DEMONIC = null;
    public static final Item ENTITY_SOUL_ELDRITCH = null;
    public static final Item ENTITY_SOUL_HOSTILE = null;
    public static final Item ENTITY_SOUL_PEACEFUL = null;
    public static final Item ENTITY_SOUL_TAINTED = null;
    public static final Item ESCAPE_SIGIL = null;
    public static final Item FELINE_CHARM = null;
    public static final Item GOLIATH_RING = null;
    public static final Item IGNIUM_STRIKER = null;
    public static final Item RECALL_STOPWATCH = null;
    public static final Item TRANSVECTOR_BINDER = null;
    public static final Item UMBRAL_DYE = null;
    public static final Item XP_TALISMAN = null;

    public static final Item TEST_SWORD = null;

    public static void initItems() {
        //Misc
        RegistrarTT.addAdditionToRegister(new ItemBase("umbral_dye", "dyeBlack"));
        RegistrarTT.addAdditionToRegister(new ItemBase("arcane_quartz", "gemArcaneQuartz"));
        RegistrarTT.addAdditionToRegister(new ItemEnergeticNitor());

        //Tools
        RegistrarTT.addAdditionToRegister(new ItemTransvectorBinder());
        RegistrarTT.addAdditionToRegister(new ItemStopwatch());
        RegistrarTT.addAdditionToRegister(new ItemEnderMirror());
        RegistrarTT.addAdditionToRegister(new ItemIgniumStriker());
        RegistrarTT.addAdditionToRegister(new ItemCondorSword());
        RegistrarTT.addAdditionToRegister(new ItemEverfullBucket());
        RegistrarTT.addAdditionToRegister(new ItemEscapeSigil());

        //Baubles
        RegistrarTT.addAdditionToRegister(new ItemGoliathRing());
        RegistrarTT.addAdditionToRegister(new ItemConsumingSigil());
        RegistrarTT.addAdditionToRegister(new ItemFelineCharm());
        RegistrarTT.addAdditionToRegister(new ItemXpTalisman());
        //TODO: RegistrarTT.addAdditionToRegister(new ItemBlackHoleRing());
        //TODO: Worldshaper's Looking glass

        //Entity Souls
        RegistrarTT.addAdditionToRegister(new ItemEntitySoul("entity_soul_peaceful", 16, EnumRarity.UNCOMMON));
        RegistrarTT.addAdditionToRegister(new ItemEntitySoul("entity_soul_hostile", 16, EnumRarity.UNCOMMON));
        RegistrarTT.addAdditionToRegister(new ItemEntitySoul("entity_soul_arcane", 8, EnumRarity.RARE));
        RegistrarTT.addAdditionToRegister(new ItemEntitySoul("entity_soul_demonic", 8, EnumRarity.RARE));
        RegistrarTT.addAdditionToRegister(new ItemEntitySoul("entity_soul_alien", 8, EnumRarity.RARE));
        RegistrarTT.addAdditionToRegister(new ItemEntitySoul("entity_soul_tainted", 4, EnumRarity.EPIC));
        RegistrarTT.addAdditionToRegister(new ItemEntitySoul("entity_soul_eldritch", 1, EnumRarity.EPIC));

        //Debug Items
        RegistrarTT.addAdditionToRegister(new ItemSwordTest("test_sword"));
    }
}
