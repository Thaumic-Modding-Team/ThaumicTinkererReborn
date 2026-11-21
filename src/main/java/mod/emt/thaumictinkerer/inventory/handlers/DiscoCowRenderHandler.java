package mod.emt.thaumictinkerer.inventory.handlers;

import mod.emt.thaumictinkerer.tile.TileMobAttractor;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class DiscoCowRenderHandler {
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world == null) {
            return;
        }

        for (TileEntity tile : mc.world.loadedTileEntityList) {
            if (tile instanceof TileMobAttractor) {
                ((TileMobAttractor) tile).renderDiscoCow(event.getPartialTicks());
            }
        }
    }
}
