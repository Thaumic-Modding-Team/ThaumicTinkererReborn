package mod.emt.thaumictinkerer.utils.helpers;

import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import org.jetbrains.annotations.Nullable;

public class WorldHelper {
    @Nullable
    public static World getWorldFromId(int id, boolean forceLoad) {
        World world = DimensionManager.getWorld(id);
        if(world == null && forceLoad) {
            DimensionManager.initDimension(id);
            world = DimensionManager.getWorld(id);
        }
        return world;
    }

    public static String getDimensionName(int dimensionId) {
        if (!DimensionManager.isDimensionRegistered(dimensionId)) {
            return Integer.toString(dimensionId);
        }
        DimensionType type = DimensionManager.getProviderType(dimensionId);
        if (type == null) {
            return Integer.toString(dimensionId);
        }
        String name = type.getName();
        int[] dims = DimensionManager.getDimensions(type);
        if (dims != null && dims.length > 1) {
            name += " " + dimensionId;
        }
        return name;
    }
}
