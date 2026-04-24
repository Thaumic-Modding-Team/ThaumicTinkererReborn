package mod.emt.thaumictinkerer.network;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.network.packets.MessageAttractorFX;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(ThaumicTinkerer.MOD_ID);

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(MessageAttractorFX.MessageHandler.class, MessageAttractorFX.class, id++, Side.CLIENT);
    }
}
