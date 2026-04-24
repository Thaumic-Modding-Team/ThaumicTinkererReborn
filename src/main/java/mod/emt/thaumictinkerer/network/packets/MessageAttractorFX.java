package mod.emt.thaumictinkerer.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;

public class MessageAttractorFX implements IMessage {
    private double posX;
    private double posY;
    private double posZ;
    private boolean isPulling;

    public MessageAttractorFX(double posX, double posY, double posZ, boolean isPulling) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.isPulling = isPulling;
    }

    public MessageAttractorFX() {
        this(0, 0, 0, true);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
        this.isPulling = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
        buf.writeBoolean(this.isPulling);
    }

    public static class MessageHandler implements IMessageHandler<MessageAttractorFX, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(MessageAttractorFX message, MessageContext ctx) {
            FMLClientHandler.instance().getClient().addScheduledTask(() -> {
                FXDispatcher.INSTANCE.sparkle((float) message.posX, (float) message.posY, (float) message.posZ,
                        message.isPulling ? 0 : 1.0f, 0, message.isPulling ? 1.0f : 0);
            });
            return null;
        }
    }
}
