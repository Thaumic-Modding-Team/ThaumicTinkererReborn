package mod.emt.thaumictinkerer.casters.foci;

import mod.emt.thaumictinkerer.ThaumicTinkerer;

import mod.emt.thaumictinkerer.item.bauble.ItemBlackHoleRing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.IFocusBlockPicker;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.api.casters.ICaster;

import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;

import thaumcraft.common.lib.SoundsTC;
import thaumcraft.common.lib.events.ServerEvents;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class FocusEffectWorldshaper extends FocusEffect implements IFocusBlockPicker {
    @Override
    public Aspect getAspect() {
        return Aspect.CRAFT;
    }

    @Override
    public String getKey() {
        return "focus." + ThaumicTinkerer.MOD_ID + ".worldshaper";
    }

    @Override
    public int getComplexity() {
        return 2;
    }

    @Override
    public String getResearch() {
        return "TT_FOCUS_WORLDSHAPER";
    }

    @Override
    public boolean execute(RayTraceResult target, @Nullable Trajectory trajectory, float finalPower, int num) {
        if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK || target.sideHit == null) {
            return false;
        }

        World world = getPackage().world;
        Entity caster = getPackage().getCaster();

        if (world == null || world.isRemote || !(caster instanceof EntityPlayer)) {
            return false;
        }

        EntityPlayer player = (EntityPlayer) caster;
        ItemStack casterStack = player.getHeldItemMainhand().getItem() instanceof ICaster ? player.getHeldItemMainhand() :
                player.getHeldItemOffhand().getItem() instanceof ICaster ? player.getHeldItemOffhand() : ItemStack.EMPTY;

        if (casterStack.isEmpty()) {
            return false;
        }

        ItemStack pickedBlock = ((ICaster) casterStack.getItem()).getPickedBlock(casterStack);
        if (pickedBlock == null || pickedBlock.isEmpty() || pickedBlock.getCount() <= 0) {
            return false;
        }

        BlockPos placementPos = target.getBlockPos().offset(target.sideHit);
        if (!world.isBlockLoaded(placementPos) || !world.isAirBlock(placementPos)) {
            return false;
        }

        ItemStack placementStack = pickedBlock.copy();
        placementStack.setCount(1);
        world.playSound(null, placementPos, SoundsTC.wand, SoundCategory.PLAYERS, 0.25F, 1.0F);
        AtomicBoolean fromRing = new AtomicBoolean(false);
        if(!player.isCreative()) {
            fromRing.set(ItemBlackHoleRing.extractStacksFromRings(player, placementStack, 1, false) > 0);
        }
        ServerEvents.addRunnableServer(world, () -> ServerEvents.addSwapper(world, placementPos, null, placementStack,
                !fromRing.get(), 0, player, true, true, 8776595, true, false, 0,
                ServerEvents.DEFAULT_PREDICATE, 0.0F), 0);
        return true;
    }

    @Override
    public void onCast(Entity caster) {
        caster.world.playSound(null, caster.getPosition().up(), SoundsTC.hhoff, SoundCategory.PLAYERS, 0.8F, 0.45F + (float) (caster.world.rand.nextGaussian() * 0.05F));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderParticleFX(World world, double posX, double posY, double posZ, double velX, double velY, double velZ) {
        FXGeneric fb = new FXGeneric(world, posX, posY, posZ, velX + world.rand.nextGaussian() * 0.01, velY + world.rand.nextGaussian() * 0.01, velZ + world.rand.nextGaussian() * 0.01);
        fb.setMaxAge(9);
        fb.setRBGColorF(0.25f + world.rand.nextFloat() * 0.25f, 0.25f + world.rand.nextFloat() * 0.25f, 0.25f + world.rand.nextFloat() * 0.25f);
        fb.setAlphaF(0.0f, 0.6f, 0.6f, 0.0f);
        fb.setGridSize(64);
        fb.setParticles(448, 9, 1);
        fb.setScale(0.5f, 0.25f);
        fb.setGravity((float) (world.rand.nextGaussian() * 0.009999999776482582));
        fb.setRandomMovementScale(0.0025f, 0.0025f, 0.0025f);
        ParticleEngine.addEffect(world, fb);
    }
}
