package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.client.gui.GuiItemAttractor;
import mod.emt.thaumictinkerer.client.gui.GuiMobAttractor;
import mod.emt.thaumictinkerer.client.gui.GuiPlayerAttractor;
import mod.emt.thaumictinkerer.inventory.containers.ContainerItemAttractor;
import mod.emt.thaumictinkerer.inventory.containers.ContainerMobAttractor;
import mod.emt.thaumictinkerer.inventory.containers.ContainerPlayerAttractor;
import mod.emt.thaumictinkerer.tile.TileItemAttractor;
import mod.emt.thaumictinkerer.tile.TileMobAttractor;
import mod.emt.thaumictinkerer.tile.TilePlayerAttractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.jetbrains.annotations.Nullable;

public class ModGuiHandlerTT implements IGuiHandler {
    public static final int ID_ITEM_ATTRACTOR = 0;
    public static final int ID_MOB_ATTRACTOR = 1;
    public static final int ID_PLAYER_ATTRACTOR = 2;

    @Override
    public @Nullable Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case ID_ITEM_ATTRACTOR:
                return new ContainerItemAttractor(player, (TileItemAttractor) world.getTileEntity(new BlockPos(x, y, z)));
            case ID_MOB_ATTRACTOR:
                return new ContainerMobAttractor(player, (TileMobAttractor) world.getTileEntity(new BlockPos(x, y, z)));
            case ID_PLAYER_ATTRACTOR:
                return new ContainerPlayerAttractor(player, (TilePlayerAttractor) world.getTileEntity(new BlockPos(x, y, z)));
        }
        return null;
    }

    @Override
    public @Nullable Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case ID_ITEM_ATTRACTOR:
                return new GuiItemAttractor(new ContainerItemAttractor(player, (TileItemAttractor) world.getTileEntity(new BlockPos(x, y, z))));
            case ID_MOB_ATTRACTOR:
                return new GuiMobAttractor(new ContainerMobAttractor(player, (TileMobAttractor) world.getTileEntity(new BlockPos(x, y, z))));
            case ID_PLAYER_ATTRACTOR:
                return new GuiPlayerAttractor(new ContainerPlayerAttractor(player, (TilePlayerAttractor) world.getTileEntity(new BlockPos(x, y, z))));
        }
        return null;
    }
}
