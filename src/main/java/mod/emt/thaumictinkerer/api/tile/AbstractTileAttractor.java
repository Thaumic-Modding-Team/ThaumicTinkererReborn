package mod.emt.thaumictinkerer.api.tile;

import mod.emt.thaumictinkerer.block.BlockAttractor;
import mod.emt.thaumictinkerer.block.BlockAttractor.AttractorMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import thaumcraft.codechicken.lib.vec.Vector3;

public abstract class AbstractTileAttractor<S extends Entity> extends TileEntityTT implements ITickable {

    public abstract Class<S> getEntityClass();

    public abstract void openGui(EntityPlayer player);

    public abstract boolean isValidEntity(S entity);

    @Override
    public void update() {
        int redstonePower = this.getRedstonePower();
        if(redstonePower > 0) {
            if (!this.world.isRemote) {
                AttractorMode mode = this.getAttractorMode();
                AxisAlignedBB searchArea = this.getSearchArea(redstonePower);
                for (S entity : this.world.getEntitiesWithinAABB(this.getEntityClass(), searchArea, this::isValidEntity)) {
                    double distance = mode == AttractorMode.ATTRACT ? this.pos.distanceSqToCenter(entity.posX, entity.posY, entity.posZ) : 1.1;
                    if (distance > 1.0) {
                        this.moveEntity(entity, mode == AttractorMode.ATTRACT);
                    }
                }
            }
        }
    }

    public int getRedstonePower() {
        int power = 0;
        for(EnumFacing facing : EnumFacing.VALUES) {
            int checkPower = this.world.getRedstonePower(this.pos.offset(facing), facing);
            if(checkPower > power) {
                power = checkPower;
            }
        }
        return power;
    }

    public EnumFacing getDirection() {
        IBlockState state = this.world.getBlockState(this.pos);
        return state.getValue(BlockAttractor.FACING);
    }

    public AttractorMode getAttractorMode() {
        IBlockState state = this.world.getBlockState(this.pos);
        return state.getValue(BlockAttractor.MODE);
    }

    public int getSearchRadius(int redstonePower) {
        return (int) Math.ceil((double) redstonePower / 4.0);
    }

    public AxisAlignedBB getSearchArea(int redstonePower) {
        int radius = this.getSearchRadius(redstonePower);
        EnumFacing opposite = this.getDirection();
        int minX = opposite.getXOffset() == -1 ? 0 : -radius;
        int minY = opposite.getYOffset() == -1 ? 0 : -radius;
        int minZ = opposite.getZOffset() == -1 ? 0 : -radius;
        int maxX = (opposite.getXOffset() == 1 ? 0 : radius) + 1;
        int maxY = (opposite.getYOffset() == 1 ? 0 : radius) + 1;
        int maxZ = (opposite.getZOffset() == 1 ? 0 : radius) + 1;

        return new AxisAlignedBB(this.pos.add(minX, minY, minZ), this.pos.add(maxX, maxY, maxZ));
    }

    public void moveEntity(S entity, boolean shouldAttract) {
        Vector3 entityVec = Vector3.fromEntityCenter(entity);
        Vector3 tileVec = new Vector3(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
        Vector3 finalVec = tileVec.copy().subtract(entityVec);

        if(finalVec.mag() > 1.0) {
            finalVec.normalize();
        }

        //TODO: Effects on moving entity.

        double movementModifier = shouldAttract ? 0.25 : -0.25;
        entity.motionX = finalVec.x * movementModifier;
        entity.motionY = finalVec.y * movementModifier;
        entity.motionZ = finalVec.z * movementModifier;
        entity.velocityChanged = true;
    }
}
