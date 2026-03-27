package mod.emt.thaumictinkerer.api.tile;

import mod.emt.thaumictinkerer.block.BlockAttractor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import thaumcraft.codechicken.lib.vec.Vector3;

public abstract class AbstractTileAttractor<S extends Entity> extends TileEntityTT implements ITickable {

    public abstract Class<S> getEntityClass();

    public abstract boolean isValidEntity(S entity);

    @Override
    public void update() {
        if(!this.world.isRemote && this.isEnabled()) {
            for(S entity : this.world.getEntitiesWithinAABB(this.getEntityClass(), this.getSearchArea())) {
                if(entity.isEntityAlive() && this.isValidEntity(entity)) {
                    double distance = this.pos.distanceSqToCenter(entity.posX, entity.posY, entity.posZ);
                    if(distance > 1.0) {
                        //TODO: Configurable attract/repulse
                        this.moveEntity(entity, true);
                    }
                }
            }
        }
    }

    public boolean isEnabled() {
        IBlockState state = this.world.getBlockState(this.pos);
        return state.getValue(BlockAttractor.ENABLED);
    }

    public EnumFacing getDirection() {
        IBlockState state = this.world.getBlockState(this.pos);
        return state.getValue(BlockAttractor.FACING);
    }

    public int getSearchRadius() {
        int power = 0;
        for(EnumFacing facing : EnumFacing.VALUES) {
            int checkPower = this.world.getRedstonePower(this.pos.offset(facing), facing);
            if(checkPower > power) {
                power = checkPower;
            }
        }
        return (int) Math.ceil((double) power / 4.0);
    }

    public AxisAlignedBB getSearchArea() {
        int radius = this.getSearchRadius();
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

        double movementModifier = shouldAttract ? 0.25 : -0.25;
        entity.motionX = finalVec.x * movementModifier;
        entity.motionY = finalVec.y * movementModifier;
        entity.motionZ = finalVec.z * movementModifier;
        entity.velocityChanged = true;
    }
}
