package mod.emt.thaumictinkerer.utils.helpers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class PlayerHelper {
    public static UUID getUUIDFromPlayer(EntityPlayer player) {
        return player.getGameProfile().getId();
    }

    public static EntityPlayer getPlayerFromUUID(UUID playerId) {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? null : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerId);
    }

    public static RayTraceResult rayTrace(EntityPlayer player, float partialTicks) {
        return rayTrace(player, player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue(), partialTicks);
    }

    public static RayTraceResult rayTrace(EntityLivingBase entityLiving, double blockReachDistance, float partialTicks) {
        Vec3d height = entityLiving.getPositionEyes(partialTicks);
        Vec3d look = entityLiving.getLook(partialTicks);
        Vec3d reach = height.add(look.x * blockReachDistance, look.y * blockReachDistance, look.z * blockReachDistance);
        return entityLiving.world.rayTraceBlocks(height, reach, false, false, true);
    }
}
