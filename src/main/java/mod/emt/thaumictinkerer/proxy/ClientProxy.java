package mod.emt.thaumictinkerer.proxy;

import mod.emt.thaumictinkerer.api.IProxy;
import mod.emt.thaumictinkerer.registry.RegistrarTT;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
        RegistrarTT.getProxyAdditions().forEach(IProxy::preInitClient);
    }

    @Override
    public void init() {
        super.init();
        RegistrarTT.getProxyAdditions().forEach(IProxy::initClient);
    }

    @Override
    public void postInit() {
        super.postInit();
        RegistrarTT.getProxyAdditions().forEach(IProxy::postInitClient);
    }
}
