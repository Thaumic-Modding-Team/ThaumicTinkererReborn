package mod.emt.thaumictinkerer.casters.foci;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXFocusPartImpact;

import java.util.List;

public class FocusEffectEfreetsFlame extends FocusEffect {
    @Override
    public Aspect getAspect() {
        return Aspect.FIRE;
    }

    @Override
    public String getKey() {
        return "focus." + ThaumicTinkerer.MOD_ID + ".efreets_flame";
    }

    @Override
    public NodeSetting[] createSettings() {
        return new NodeSetting[]{new NodeSetting("power", "focus.common.power", new NodeSetting.NodeSettingIntRange(1, 5))};
    }

    @Override
    public int getComplexity() {
        return 10 + this.getSettingValue("power") * 4;
    }

    // TODO: Add research and put requirement here
    @Override
    public String getResearch() {
        return "FOCUSHEAL";
    }

    @Override
    public boolean execute(RayTraceResult target, Trajectory trajectory, float finalPower, int num) {
        if (target == null || target.hitVec == null) {
            return false;
        }

        World world = getPackage().world;
        if (world.isRemote) {
            return false;
        }

        PacketHandler.INSTANCE.sendToAllAround(new PacketFXFocusPartImpact(target.hitVec.x, target.hitVec.y, target.hitVec.z, new String[]{this.getKey()}), new NetworkRegistry.TargetPoint(world.provider.getDimension(), target.hitVec.x, target.hitVec.y, target.hitVec.z, 64.0D));
        world.playSound(null, target.hitVec.x, target.hitVec.y, target.hitVec.z, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.PLAYERS, 1.0F, 1.5F + (float) (world.rand.nextGaussian() * 0.05F));
        boolean smelted = false;

        if (target.typeOfHit == RayTraceResult.Type.BLOCK) {
            smelted = smeltBlock(target);
        }

        if (smeltNearbyItems(world, target.hitVec.x, target.hitVec.y, target.hitVec.z)) {
            smelted = true;
        }

        return smelted;
    }

    private boolean smeltBlock(RayTraceResult target) {
        World world = getPackage().world;
        Entity caster = getPackage().getCaster();

        if (!(caster instanceof EntityPlayer)) {
            return false;
        }

        EntityPlayer player = (EntityPlayer) caster;
        BlockPos pos = target.getBlockPos();
        IBlockState state = world.getBlockState(pos);
        ItemStack input = state.getBlock().getPickBlock(state, target, world, pos, player);

        if (input.isEmpty()) {
            return false;
        }

        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
        if (result.isEmpty()) {
            return false;
        }

        if (!(result.getItem() instanceof ItemBlock)) {
            return false;
        }

        ItemBlock itemBlock = (ItemBlock) result.getItem();
        float hitX = (float) (target.hitVec.x - pos.getX());
        float hitY = (float) (target.hitVec.y - pos.getY());
        float hitZ = (float) (target.hitVec.z - pos.getZ());
        IBlockState newState = itemBlock.getBlock().getStateForPlacement(world, pos, target.sideHit, hitX, hitY, hitZ, result.getMetadata(), player, EnumHand.MAIN_HAND);

        if (world.setBlockState(pos, newState, 3)) {
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3F, 1.0F);
            return true;
        }

        return false;
    }

    private boolean smeltItem(EntityItem entityItem) {
        World world = entityItem.world;
        ItemStack input = entityItem.getItem();

        if (input.isEmpty()) {
            return false;
        }

        ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
        if (result.isEmpty()) {
            return false;
        }

        ItemStack output = result.copy();
        double x = entityItem.posX;
        double y = entityItem.posY;
        double z = entityItem.posZ;
        double motionX = entityItem.motionX;
        double motionY = entityItem.motionY;
        double motionZ = entityItem.motionZ;

        input.shrink(1);

        if (input.isEmpty()) {
            entityItem.setDead();
        } else {
            entityItem.setItem(input);
        }

        EntityItem smeltedItem = new EntityItem(world, x, y, z, output);

        smeltedItem.motionX = motionX;
        smeltedItem.motionY = motionY;
        smeltedItem.motionZ = motionZ;
        world.spawnEntity(smeltedItem);
        return true;
    }

    private boolean smeltNearbyItems(World world, double x, double y, double z) {
        AxisAlignedBB area = new AxisAlignedBB(x - 1.5D, y - 1.5D, z - 1.5D, x + 1.5D, y + 1.5D, z + 1.5D);
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, area);

        // Multiplies the amount smelted by 2 each level, Power 2 = 2, Power 3 = 4, and so on
        // Max is 16 at Power 5
        int power = Math.max(1, Math.min(5, this.getSettingValue("power")));
        int smeltCount = 1 << (power - 1);
        int smelted = 0;

        for (EntityItem item : list) {
            if (item.isDead || item.getItem().isEmpty()) {
                continue;
            }

            while (!item.isDead && !item.getItem().isEmpty() && smelted < smeltCount) {
                if (!smeltItem(item)) {
                    break;
                }

                smelted++;
            }

            if (smelted >= smeltCount) {
                break;
            }
        }

        if (smelted > 0) {
            world.playSound(null, x, y, z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3F, 1.0F);
        }

        return smelted > 0;
    }

    @Override
    public void onCast(Entity caster) {
        caster.world.playSound(null, caster.getPosition().up(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, 0.5F + caster.world.rand.nextFloat() * 0.05F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderParticleFX(World world, double posX, double posY, double posZ, double velX, double velY, double velZ) {
        final FXGeneric pp = new FXGeneric(world, posX, posY, posZ, velX, velY, velZ);
        int color = 25565230;

        pp.setAlphaF(0.7F);
        pp.setGravity(-0.2F);
        pp.setMaxAge(10);
        pp.setParticles(640, 10, 1);
        pp.setRBGColorF(((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F);
        pp.setSlowDown(0.75D);
        pp.setScale((float) (1.5F + world.rand.nextGaussian() * 0.2F));
        ParticleEngine.addEffect(world, pp);
    }
}
