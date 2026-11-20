package mod.emt.thaumictinkerer.casters.foci;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.registry.ModSoundEventsTT;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.NodeSetting;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;

import javax.annotation.Nullable;
import java.util.List;

public class FocusEffectCollector extends FocusEffect {
    @Override
    public Aspect getAspect() {
        return Aspect.MOTION;
    }

    @Override
    public String getKey() {
        return "focus." + ThaumicTinkerer.MOD_ID + ".collector";
    }

    @Override
    public NodeSetting[] createSettings() {
        int[] casterTeleport = {0, 1};
        String[] casterTeleportDesc = {"focus.common.no", "focus.common.yes"};
        return new NodeSetting[]{new NodeSetting("power", "focus.common.power", new NodeSetting.NodeSettingIntRange(1, 5)), new NodeSetting("teleport_to_caster", "focus.common.teleport_to_caster", new NodeSetting.NodeSettingIntList(casterTeleport, casterTeleportDesc))};
    }

    @Override
    public int getComplexity() {
        return 1 + (getSettingValue("power") - 1) + (getSettingValue("teleport_to_caster") == 0 ? 0 : 8);
    }

    // TODO: Add research and put requirement here
    @Override
    public String getResearch() {
        return "FOCUSHEAL";
    }

    @Override
    public boolean execute(RayTraceResult target, @Nullable Trajectory trajectory, float finalPower, int num) {
        if (target == null || target.hitVec == null) {
            return false;
        }

        World world = getPackage().world;
        if (world.isRemote) {
            return false;
        }

        PacketHandler.INSTANCE.sendToAllAround(new PacketFXFocusPartImpact(target.hitVec.x, target.hitVec.y, target.hitVec.z, new String[]{this.getKey()}), new NetworkRegistry.TargetPoint(world.provider.getDimension(), target.hitVec.x, target.hitVec.y, target.hitVec.z, 64.0D));
        world.playSound(null, target.hitVec.x, target.hitVec.y, target.hitVec.z, ModSoundEventsTT.FOCI_COLLECTOR_SHOOT.getSoundEvent(), SoundCategory.PLAYERS, 0.6F, 0.8F + (float) (world.rand.nextGaussian() * 0.05F));

        if (target.typeOfHit != RayTraceResult.Type.BLOCK) {
            return false;
        }

        Entity caster = getPackage().getCaster();
        Vector3 teleportPoint;

        if (getSettingValue("teleport_to_caster") == 1 && caster != null) {
            teleportPoint = new Vector3(caster.posX, caster.posY + caster.height / 2.0, caster.posZ);
        } else {
            teleportPoint = new Vector3(target.hitVec.x, target.hitVec.y + 0.5, target.hitVec.z);
        }

        final int range = 1 + getSettingValue("power");
        AxisAlignedBB area = new AxisAlignedBB(target.hitVec.x - range, target.hitVec.y - range, target.hitVec.z - range, target.hitVec.x + range, target.hitVec.y + range, target.hitVec.z + range);
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, area);
        int collected = 0;

        for (EntityItem item : list) {
            if (item.isDead || item.getItem().isEmpty()) {
                continue;
            }

            item.setPosition(teleportPoint.x, teleportPoint.y, teleportPoint.z);
            item.motionX = 0;
            item.motionY = 0;
            item.motionZ = 0;
            collected++;
        }

        return collected > 0;
    }

    @Override
    public void onCast(Entity caster) {
        caster.world.playSound(null, caster.getPosition().up(), ModSoundEventsTT.FOCI_COLLECTOR_SHOOT.getSoundEvent(), SoundCategory.PLAYERS, 0.6F, 1.2F + (float) (caster.world.rand.nextGaussian() * 0.05F));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderParticleFX(World world, double posX, double posY, double posZ, double velX, double velY, double velZ) {
        double angle = world.rand.nextDouble() * Math.PI * 2.0D;
        float swirlSpeed = 0.025F + world.rand.nextFloat() * 0.025F;
        double swirlX = -Math.sin(angle) * swirlSpeed;
        double swirlZ = Math.cos(angle) * swirlSpeed;
        FXGeneric pp = new FXGeneric(world, posX, posY, posZ, velX + swirlX, velY, velZ + swirlZ);
        int color = 13566207;

        pp.setAlphaF(0.0F, 0.6F, 0.6F, 0.0F);
        pp.setGridSize(64);
        pp.setParticles(448, 9, 1);
        pp.setScale(1.5F, 1.25F);
        pp.setGravity(0.035F);
        pp.setRandomMovementScale(0.0015F, 0.0015F, 0.0015F);
        pp.setMaxAge(9);
        pp.setRBGColorF(((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F);
        ParticleEngine.addEffectWithDelay(world, pp, world.rand.nextInt(4));
    }
}
