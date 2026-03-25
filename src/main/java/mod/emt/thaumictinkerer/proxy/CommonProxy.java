package mod.emt.thaumictinkerer.proxy;

import mod.emt.thaumictinkerer.api.IAddition;
import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.registry.RegistrarTT;

public class CommonProxy {
    public void preInit() {
        RegistrarTT.getProxyAdditions().forEach(IProxy::preInit);
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
