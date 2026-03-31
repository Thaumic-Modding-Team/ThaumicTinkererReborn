package mod.emt.thaumictinkerer.proxy;

import mod.emt.thaumictinkerer.api.IAddition;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.compat.crafttweaker.CTPlugin;
import mod.emt.thaumictinkerer.registry.RegistrarTT;
import mod.emt.thaumictinkerer.utils.helpers.CompatHelper;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public void preInit() {
        RegistrarTT.getProxyAdditions().forEach(IProxy::preInit);
        if(CompatHelper.isModTweakerLoaded) {
            MinecraftForge.EVENT_BUS.register(new CTPlugin());
        }
    }

    public void init() {
        RegistrarTT.getProxyAdditions().forEach(IProxy::init);
        registerResearch();
    }

    public void postInit() {
        RegistrarTT.getProxyAdditions().forEach(IProxy::postInit);
    }

    private void registerResearch() {
        //TODO: Register research.
        RegistrarTT.getAdditions().forEach(IAddition::registerResearchLocation);
    }
}
