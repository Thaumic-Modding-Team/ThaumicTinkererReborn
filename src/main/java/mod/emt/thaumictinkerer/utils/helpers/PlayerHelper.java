package mod.emt.thaumictinkerer.utils.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PlayerHelper {
    public static UUID getUUIDFromPlayer(EntityPlayer player) {
        return player.getGameProfile().getId();
    }

    public static EntityPlayer getPlayerFromUUID(UUID playerId) {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? null : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerId);
    }

    public static RayTraceResult rayTraceBlocks(EntityPlayer player, float partialTicks) {
        return rayTraceBlocks(player, player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue(), partialTicks);
    }

    public static RayTraceResult rayTraceBlocks(EntityLivingBase entityLiving, double blockReachDistance, float partialTicks) {
        Vec3d height = entityLiving.getPositionEyes(partialTicks);
        Vec3d look = entityLiving.getLook(partialTicks);
        Vec3d reach = height.add(look.x * blockReachDistance, look.y * blockReachDistance, look.z * blockReachDistance);
        return entityLiving.world.rayTraceBlocks(height, reach, false, false, true);
    }

    @Nullable
    public static RayTraceResult rayTraceEntities(EntityPlayer player, float partialTicks) {
        return rayTraceEntities(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue(), partialTicks);
    }

    @Nullable
    public static RayTraceResult rayTraceEntities(EntityLivingBase entityLiving, double reachDistance, float partialTicks) {
        RayTraceResult trace = rayTraceBlocks(entityLiving, reachDistance, partialTicks);

        if(trace == null)
            return null;

        Vec3d origin = entityLiving.getPositionEyes(partialTicks);
        Vec3d endpoint = trace.hitVec;
        AxisAlignedBB searchVolume = new AxisAlignedBB(origin.x, origin.y, origin.z, endpoint.x, endpoint.y, endpoint.z);
        List<Entity> entities = entityLiving.world.getEntitiesWithinAABB(Entity.class, searchVolume);

        Entity closestEntity = null;
        Vec3d closestHitPos = endpoint;
        AxisAlignedBB entityAABB;
        Vec3d intercept = null;

        for(Entity entity : entities) {
            if(entity == entityLiving)
                continue;

            entityAABB = entity.getEntityBoundingBox();
            if(entityAABB != null) {
                float borderSize = entity.getCollisionBorderSize();
                if(borderSize != 0) {
                    entityAABB = entityAABB.grow(borderSize);
                }

                RayTraceResult hit = entityAABB.calculateIntercept(origin, endpoint);
                if(hit != null) {
                    intercept = hit.hitVec;
                }
            }

            if(intercept != null) {
                float currentHitDist = (float) intercept.distanceTo(origin);
                float closestHitDist = (float) closestHitPos.distanceTo(origin);
                if(currentHitDist < closestHitDist) {
                    closestEntity = entity;
                    closestHitPos = intercept;
                }
            }
        }

        if(closestEntity != null) {
            trace = new RayTraceResult(closestEntity, closestHitPos);
        }
        return trace;
    }
}
