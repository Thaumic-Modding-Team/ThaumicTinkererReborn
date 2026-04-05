package mod.emt.thaumictinkerer;

import mod.emt.thaumictinkerer.config.ConfigTags;
import mod.emt.thaumictinkerer.proxy.CommonProxy;
import mod.emt.thaumictinkerer.registry.CreativeTabsTT;
import mod.emt.thaumictinkerer.utils.helpers.LogHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = ThaumicTinkerer.MOD_ID,
        name = ThaumicTinkerer.MOD_NAME,
        version = ThaumicTinkerer.MOD_VERSION,
        dependencies = ThaumicTinkerer.DEPENDENCIES
)
public class ThaumicTinkerer {
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = Tags.VERSION;
    public static final String DEPENDENCIES = "required-after:thaumcraft" +
            ";required-after:thaumicapi" +
            ";required-after:expandedevents";

    public static final String CLIENT_PROXY = "mod.emt.thaumictinkerer.proxy.ClientProxy";
    public static final String COMMON_PROXY = "mod.emt.thaumictinkerer.proxy.CommonProxy";

    public static final CreativeTabs tabTT = new CreativeTabsTT(CreativeTabs.CREATIVE_TAB_ARRAY.length, "ThaumicTinkererTab");

    @Mod.Instance(MOD_ID)
    public static ThaumicTinkerer instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LogHelper.info("Starting " + MOD_NAME);
        proxy.preInit();
        LogHelper.debug("Finished preInit phase.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        LogHelper.debug("Finished init phase.");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
        ConfigTags.syncConfigs();
        LogHelper.debug("Finished postInit phase.");
    }
}
