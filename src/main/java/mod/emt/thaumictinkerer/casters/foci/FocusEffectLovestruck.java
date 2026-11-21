package mod.emt.thaumictinkerer.casters.foci;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;

public class FocusEffectLovestruck extends FocusEffect {
    @Override
    public Aspect getAspect() {
        return Aspect.LIFE;
    }

    @Override
    public String getKey() {
        return "focus." + ThaumicTinkerer.MOD_ID + ".lovestruck";
    }

    @Override
    public int getComplexity() {
        return 15;
    }

    @Override
    public String getResearch() {
        return "TT_FOCUS_LOVESTRUCK";
    }

    @Override
    public boolean execute(RayTraceResult target, Trajectory trajectory, float finalPower, int num) {
        World world = this.getPackage().world;
        PacketHandler.INSTANCE.sendToAllAround(new PacketFXFocusPartImpact(target.hitVec.x, target.hitVec.y, target.hitVec.z, new String[]{this.getKey()}), new NetworkRegistry.TargetPoint(world.provider.getDimension(), target.hitVec.x, target.hitVec.y, target.hitVec.z, 64.0D));
        world.playSound(null, target.hitVec.x, target.hitVec.y, target.hitVec.z, SoundsTC.wand, SoundCategory.PLAYERS, 0.525F, 0.7F + (float) (world.rand.nextGaussian() * 0.05F));

        if (target.typeOfHit == RayTraceResult.Type.ENTITY && target.entityHit != null) {
            if (target.entityHit instanceof EntityAnimal) {
                EntityAnimal animal = (EntityAnimal) target.entityHit;

                if (animal.getGrowingAge() == 0 && animal.inLove <= 0) {
                    animal.inLove = 20 * 20;
                    world.setEntityState(animal, (byte) 18);
                }
            } else {
                Entity entity = target.entityHit;

                if (!world.isRemote && world instanceof WorldServer) {
                    WorldServer serverWorld = (WorldServer) world;

                    for (int i = 0; i < 7; ++i) {
                        double x = entity.posX + (world.rand.nextDouble() - 0.5D) * entity.width;
                        double y = entity.posY + 0.5D + world.rand.nextDouble() * entity.height;
                        double z = entity.posZ + (world.rand.nextDouble() - 0.5D) * entity.width;
                        serverWorld.spawnParticle(EnumParticleTypes.HEART, true, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.02D);
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void onCast(Entity caster) {
        caster.world.playSound(null, caster.getPosition().up(), SoundsTC.wand, SoundCategory.PLAYERS, 0.825F, 3.0F + (float) (caster.world.rand.nextGaussian() * 0.05F));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderParticleFX(World world, double posX, double posY, double posZ, double velX, double velY, double velZ) {
        FXGeneric pp = new FXGeneric(world, posX, posY, posZ, velX, velY, velZ);
        int color = 16728727;

        pp.setAlphaF(0.0F, 0.7F, 0.7F, 0.0F);
        pp.setGridSize(64);
        pp.setParticles(0, 1, 1);
        pp.setScale(world.rand.nextFloat() * 4.0F, world.rand.nextFloat());
        pp.setSlowDown(0.8);
        pp.setGravity((float) (world.rand.nextGaussian() * 0.1F));
        pp.setRandomMovementScale(0.0125F, 0.0125F, 0.0125F);
        pp.setRotationSpeed((float) world.rand.nextGaussian());
        pp.setRBGColorF(((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F);
        ParticleEngine.addEffectWithDelay(world, pp, world.rand.nextInt(4));
    }
}
