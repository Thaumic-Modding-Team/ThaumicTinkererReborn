package mod.emt.thaumictinkerer.utils.misc;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class FakeNetHandlerTT extends NetHandlerPlayServer {
    public FakeNetHandlerTT(EntityPlayerMP playerIn) {
        super(FMLCommonHandler.instance().getMinecraftServerInstance(), new NetworkManager(EnumPacketDirection.CLIENTBOUND), playerIn);
    }

    @Override
    public @NotNull NetworkManager getNetworkManager() {
        return super.getNetworkManager();
    }

    @Override
    public void disconnect(@NotNull ITextComponent textComponent) {}

    @Override
    public void processInput(@NotNull CPacketInput packetIn) {}

    @Override
    public void processVehicleMove(@NotNull CPacketVehicleMove packetIn) {}

    @Override
    public void processConfirmTeleport(@NotNull CPacketConfirmTeleport packetIn) {}

    @Override
    public void processPlayer(@NotNull CPacketPlayer packetIn) {}

    @Override
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch) {}

    @Override
    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, @NotNull Set<SPacketPlayerPosLook.EnumFlags> relativeSet) {}

    @Override
    public void processPlayerDigging(@NotNull CPacketPlayerDigging packetIn) {}

    @Override
    public void processTryUseItemOnBlock(@NotNull CPacketPlayerTryUseItemOnBlock packetIn) {}

    @Override
    public void processTryUseItem(@NotNull CPacketPlayerTryUseItem packetIn) {}

    @Override
    public void handleSpectate(@NotNull CPacketSpectate packetIn) {}

    @Override
    public void handleResourcePackStatus(@NotNull CPacketResourcePackStatus packetIn) {}

    @Override
    public void processSteerBoat(@NotNull CPacketSteerBoat packetIn) {}

    @Override
    public void onDisconnect(@NotNull ITextComponent reason) {}

    @Override
    public void sendPacket(@NotNull Packet<?> packetIn) {}

    @Override
    public void processHeldItemChange(@NotNull CPacketHeldItemChange packetIn) {}

    @Override
    public void processChatMessage(@NotNull CPacketChatMessage packetIn) {}

    @Override
    public void handleAnimation(@NotNull CPacketAnimation packetIn) {}

    @Override
    public void processEntityAction(@NotNull CPacketEntityAction packetIn) {}

    @Override
    public void processUseEntity(@NotNull CPacketUseEntity packetIn) {}

    @Override
    public void processClientStatus(@NotNull CPacketClientStatus packetIn) {}

    @Override
    public void processCloseWindow(@NotNull CPacketCloseWindow packetIn) {}

    @Override
    public void processClickWindow(@NotNull CPacketClickWindow packetIn) {}

    @Override
    public void processEnchantItem(@NotNull CPacketEnchantItem packetIn) {}

    @Override
    public void processCreativeInventoryAction(@NotNull CPacketCreativeInventoryAction packetIn) {}

    @Override
    public void processConfirmTransaction(@NotNull CPacketConfirmTransaction packetIn) {}

    @Override
    public void processUpdateSign(@NotNull CPacketUpdateSign packetIn) {}

    @Override
    public void processKeepAlive(@NotNull CPacketKeepAlive packetIn) {}

    @Override
    public void processPlayerAbilities(@NotNull CPacketPlayerAbilities packetIn) {}

    @Override
    public void processTabComplete(@NotNull CPacketTabComplete packetIn) {}

    @Override
    public void processClientSettings(@NotNull CPacketClientSettings packetIn) {}

    @Override
    public void processCustomPayload(@NotNull CPacketCustomPayload packetIn) {}
}
