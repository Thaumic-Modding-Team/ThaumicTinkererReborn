package mod.emt.thaumictinkerer.utils.helpers;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHelper {
    public static final UUID FAKE_PLAYER_UUID = UUID.fromString("7574aadc-c4ea-407b-8cd7-e13c2d45d969");
    public static final GameProfile FAKE_PROFILE_TT = new GameProfile(FAKE_PLAYER_UUID, "[ThaumicTinkerer]");
    private static final Map<Integer, WeakReference<FakePlayer>> PLAYER_REFS = new HashMap<>();

    public static FakePlayer getFakePlayer(WorldServer world) {
        int dimId = world.provider.getDimension();
        if(!PLAYER_REFS.containsKey(dimId) || PLAYER_REFS.get(dimId) == null) {
            PLAYER_REFS.put(dimId, new WeakReference<>(FakePlayerFactory.get(world, FAKE_PROFILE_TT)));
        }
        return PLAYER_REFS.get(dimId).get();
    }

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
