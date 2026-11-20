package mod.emt.thaumictinkerer.utils.helpers;

import com.google.common.base.Predicates;
import com.mojang.authlib.GameProfile;
import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.utils.misc.FakePlayerTT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ThaumicTinkerer.MOD_ID)
public class FakePlayerHelper {
    public static final UUID FAKE_PLAYER_UUID = UUID.fromString("7574aadc-c4ea-407b-8cd7-e13c2d45d969");
    public static final GameProfile FAKE_PROFILE_TT = new GameProfile(FAKE_PLAYER_UUID, "[ThaumicTinkerer]");
    private static final Map<Integer, WeakReference<FakePlayerTT>> PLAYER_REFS = new HashMap<>();

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        int dimId = event.getWorld().provider.getDimension();
        PLAYER_REFS.remove(dimId);
    }

    /**
     * Retrieves or creates an unconfigured new FakePlayer instance.
     */
    public static FakePlayerTT getFakePlayer(WorldServer world) {
        int dimId = world.provider.getDimension();
        if(!PLAYER_REFS.containsKey(dimId) || PLAYER_REFS.get(dimId) == null) {
            PLAYER_REFS.put(dimId, new WeakReference<>(new FakePlayerTT(world, FAKE_PROFILE_TT)));
        }
        return PLAYER_REFS.get(dimId).get();
    }

    /**
     * Retrieves or creates a new FakePlayer instance for use. This method sets the position, direction, equipped item
     * and sneaking status of the FakePlayer.
=     */
    public static FakePlayerTT getFakePlayer(WorldServer world, BlockPos pos, EnumFacing direction, ItemStack equippedStack, boolean isSneaking) {
        FakePlayerTT player = getFakePlayer(world);
        setPlayerPositionAndFacing(player, pos, direction);
        setPlayerHeldItem(player, equippedStack);
        player.setSneaking(isSneaking);
        return player;
    }

    /**
     * Sets the player position and facing direction. The player will be put at the center of the block facing
     * the direction.
     */
    public static void setPlayerPositionAndFacing(FakePlayerTT player, BlockPos pos, EnumFacing direction) {
        float pitch = direction == EnumFacing.UP ? -90 : direction == EnumFacing.DOWN ? 90 : 0;
        float yaw = direction == EnumFacing.SOUTH ? 0 : direction == EnumFacing.WEST ? 90 : direction == EnumFacing.NORTH ? 180 : -90;
        Vec3i sideVec = direction.getDirectionVec();
        Axis a = direction.getAxis();
        AxisDirection ad = direction.getAxisDirection();
        double x = a == Axis.X && ad == AxisDirection.NEGATIVE ? -0.5 : 0.5 + sideVec.getX() / 1.9D;
        double y = 0.5 + sideVec.getY() / 1.9D;
        double z = a == Axis.Z && ad == AxisDirection.NEGATIVE ? -0.5 : 0.5 + sideVec.getZ() / 1.9D;
        player.setLocationAndAngles(pos.getX() + x, pos.getY() + y, pos.getZ() + z, yaw, pitch);
    }

    /**
     * Sets the item that is currently held in the FakePlayer's main hand. This method also applies
     * any attribute values associated with the item.
     */
    public static void setPlayerHeldItem(FakePlayerTT player, ItemStack stack) {
        player.inventory.setInventorySlotContents(player.inventory.currentItem, stack.copy());
        if(!stack.isEmpty()) {
            player.getAttributeMap().applyAttributeModifiers(stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
        }
    }

    public static void cleanupFakePlayer(FakePlayerTT player) {
        ItemStack currStack = player.inventory.getStackInSlot(player.inventory.currentItem);
        player.getAttributeMap().removeAttributeModifiers(currStack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND));
        setPlayerHeldItem(player, ItemStack.EMPTY);
        player.setTicksSinceLastSwing(0);
        player.setFakePlayerTile(null);
        player.setSneaking(false);
    }

    public static ItemStack rightClickPosition(FakePlayerTT player, World world, BlockPos pos, EnumFacing interactSide) {
        ItemStack heldStack = player.getHeldItemMainhand();
        IBlockState state = world.getBlockState(pos);
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos), Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, Entity::canBeCollidedWith));
        if(!state.getBlock().isAir(state, world, pos)) {
            //Interacting with a block
            EnumActionResult enumactionresult = player.interactionManager.processRightClickBlock(player, world, heldStack, EnumHand.MAIN_HAND, pos, interactSide, 0, 0, 0);
            if (enumactionresult == EnumActionResult.SUCCESS) {
                return player.getHeldItemMainhand();
            }
        } else if (!entities.isEmpty()) {
            //Interacting with an entity
            RayTraceResult result = PlayerHelper.rayTraceEntities(player, 2.0f, 1.0f);
            if(result != null && (processUseEntity(player, result.entityHit, result, CPacketUseEntity.Action.INTERACT_AT) || processUseEntity(player, result.entityHit, null, CPacketUseEntity.Action.INTERACT))) {
                return player.getHeldItemMainhand();
            }
        } else {
            //Placing a block in the world
            pos = pos.offset(EnumFacing.DOWN);
            state = world.getBlockState(pos);
            if(!state.getBlock().isAir(state, world, pos)) {
                player.interactionManager.processRightClickBlock(player, world, heldStack, EnumHand.MAIN_HAND, pos, EnumFacing.UP, 0, 0, 0);
                return player.getHeldItemMainhand();
            }
        }

        if(!heldStack.isEmpty()) {
            //Right-clicking an item
            player.interactionManager.processRightClick(player, world, heldStack, EnumHand.MAIN_HAND);
        }
        return player.getHeldItemMainhand();
    }

    public static ItemStack leftClickPosition(FakePlayerTT player, World world, BlockPos pos, EnumFacing interactSide, int ticksSinceLastSwing) {
        ItemStack heldStack = player.getHeldItemMainhand();
        IBlockState state = world.getBlockState(pos);
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos), Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, Entity::canBeCollidedWith));

        if(!state.getBlock().isAir(state, world, pos)) {
            //Hitting a a block
            player.interactionManager.onBlockClicked(pos, interactSide);
            return player.getHeldItemMainhand();
        } else if (!entities.isEmpty()) {
            //Attacking with an entity
            RayTraceResult result = PlayerHelper.rayTraceEntities(player, 2.0f, 1.0f);
            player.setTicksSinceLastSwing(ticksSinceLastSwing);
            if(result != null && processUseEntity(player, result.entityHit, result, CPacketUseEntity.Action.ATTACK)) {
                return player.getHeldItemMainhand();
            }
        } else if(!heldStack.isEmpty()) {
            //Swinging an item
            ForgeHooks.onEmptyLeftClick(player);
        }
        return player.getHeldItemMainhand();
    }

    public static boolean processUseEntity(FakePlayerTT player, Entity entity, @Nullable RayTraceResult result, CPacketUseEntity.Action action) {
        if (entity != null) {
            double aggroDistance = player.canEntityBeSeen(entity) ? 36.0 : 9.0;

            if (player.getDistanceSq(entity) < aggroDistance) {
                switch (action) {
                    case INTERACT:
                        return player.interactOn(entity, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS;
                    case ATTACK:
                        if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity == player) {
                            return false;
                        } else {
                            player.attackTargetEntityWithCurrentItem(entity);
                            return true;
                        }
                    case INTERACT_AT:
                        return result != null && ForgeHooks.onInteractEntityAt(player, entity, result.hitVec, EnumHand.MAIN_HAND) == null
                                && entity.applyPlayerInteraction(player, result.hitVec, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS;
                }
            }
        }
        return false;
    }
}
