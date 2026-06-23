package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.ITransvectorLink;
import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.config.ConfigTags;
import mod.emt.thaumictinkerer.utils.TransvectorLink;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TileTransvectorDislocator extends TileEntityTT implements ITransvectorLink {
    private static final AxisAlignedBB SWAP_AREA;
    private static final List<BlockPos> OFFSETS;
    protected TransvectorLink link = TransvectorLink.EMPTY;
    protected boolean isSwapping = false;
    protected int redstonePower = 0;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.link = new TransvectorLink(compound.getCompoundTag("link"));
        this.redstonePower = compound.getInteger("redstonePower");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("link", this.link.serializeNBT());
        compound.setInteger("redstonePower", this.redstonePower);
        return compound;
    }

    @Override
    public void createLink(EntityPlayer player, EnumHand hand, EnumFacing clickedFace, TransvectorLink link, boolean isFaceLinking) {
        if(link.isSelf(this.world, this.pos)) {
            player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.self_link"), true);
        } else if (!this.isInRange(link)) {
            player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.out_of_range"), true);
        } else if(link.getTileEntity() instanceof TileTransvectorDislocator) {
            this.link = link;
            this.markDirty();
            player.sendStatusMessage(new TextComponentTranslation("chat.thaumictinkerer:transvector_binder.link_successful"), true);
        }
    }

    public boolean isSwapping() {
        return this.isSwapping;
    }

    public int getRedstonePower() {
        return this.redstonePower;
    }

    public void setRedstonePower(int redstonePower) {
        if(this.redstonePower == 0 && redstonePower != 0) {
            this.redstonePower = redstonePower;
            this.isSwapping = true;
            this.attemptSwap();
            this.isSwapping = false;
            this.markDirty();
        } else if(this.redstonePower > 0 && redstonePower == 0) {
            this.redstonePower = 0;
            this.isSwapping = false;
            this.markDirty();
        }
    }

    public boolean isInRange(TransvectorLink targetLink) {
        return targetLink.getWorld() != null && targetLink.getWorld().provider.getDimension() == this.world.provider.getDimension();
    }

    public void attemptSwap() {
        TileEntity linkedTile = this.link.getTileEntity();
        if(linkedTile instanceof TileTransvectorDislocator) {
            TileTransvectorDislocator target = (TileTransvectorDislocator) linkedTile;
            this.swapBlocks(target);
            this.swapEntities(target);
        }
    }

    public void swapBlocks(TileTransvectorDislocator target) {
        if(!ConfigHandlerTT.transvectorDislocator.transferBlocks)
            return;

        boolean transferred = false;
        for(BlockPos offset : OFFSETS) {
            BlockPos initialPos = this.pos.add(offset);
            BlockPos targetPos = target.pos.add(offset);
            transferred |= this.swapBlock(this.world, initialPos, targetPos);
        }

        if(transferred) {
            this.world.playSound(null, this.pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F);
            target.world.playSound(null, target.pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.AMBIENT, 1.0F, 1.0F);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public boolean swapBlock(World world, BlockPos initialPos, BlockPos targetPos) {
        IBlockState initialState = world.getBlockState(initialPos);
        IBlockState targetState = world.getBlockState(targetPos);

        if(!ConfigTags.canDislocatorSwap(world, initialPos, initialState) || !ConfigTags.canDislocatorSwap(world, targetPos, targetState))
            return false;

        TileEntity initialTile = world.getTileEntity(initialPos);
        NBTTagCompound initialTag = new NBTTagCompound();
        if (initialTile != null) {
            initialTile.writeToNBT(initialTag);
        }

        TileEntity targetTile = world.getTileEntity(targetPos);
        NBTTagCompound targetTag = new NBTTagCompound();
        if (targetTile != null) {
            targetTile.writeToNBT(targetTag);
        }

        if(!world.isAirBlock(initialPos) || !world.isAirBlock(targetPos)) {
            if(initialState.getBlock().hasTileEntity(initialState)) {
                world.removeTileEntity(initialPos);
            }
            if(targetState.getBlock().hasTileEntity(targetState)) {
                world.removeTileEntity(targetPos);
            }

            world.setBlockState(targetPos, initialState);
            if(initialTile != null) {
                TileEntity newTile = TileEntity.create(world, initialTag);
                world.setTileEntity(targetPos, newTile);
                newTile.setPos(targetPos);
                newTile.setWorld(world);
            }
            world.notifyNeighborsOfStateChange(targetPos, initialState.getBlock(), true);

            world.setBlockState(initialPos, targetState);
            if(targetTile != null) {
                TileEntity newTile = TileEntity.create(world, targetTag);
                world.setTileEntity(initialPos, newTile);
                newTile.setPos(initialPos);
                newTile.setWorld(world);
            }
            world.notifyNeighborsOfStateChange(initialPos, targetState.getBlock(), true);
        }
        return true;
    }

    public List<Entity> getSwapEntities() {
        List<Entity> entities = new ArrayList<>();
        if(ConfigHandlerTT.transvectorDislocator.transferEntities) {
            AxisAlignedBB area = SWAP_AREA.offset(this.pos);
            entities.addAll(this.world.getEntitiesWithinAABB(Entity.class, area, this::isEntityValid));
        }
        return entities;
    }

    public boolean isEntityValid(Entity entity) {
        return entity.isEntityAlive()
                && (!(entity instanceof EntityPlayer) || ConfigHandlerTT.transvectorDislocator.transferPlayers)
                && (entity.isNonBoss() || ConfigHandlerTT.transvectorDislocator.transferBosses);
    }

    public void swapEntities(TileTransvectorDislocator target) {
        boolean transferred = false;
        List<Entity> initialEntities = this.getSwapEntities();
        List<Entity> targetEntities = target.getSwapEntities();

        for (Entity entity : initialEntities) {
            transferred |= swapEntity(this.pos, target.pos, entity);
        }

        for (Entity entity : targetEntities) {
            transferred |= swapEntity(target.pos, this.pos, entity);
        }

        if(transferred) {
            this.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT,
                    SoundCategory.AMBIENT, 1.0F, 1.0F, false);
            target.world.playSound(target.pos.getX(), target.pos.getY(), target.pos.getZ(), SoundEvents.ENTITY_ENDERMEN_TELEPORT,
                    SoundCategory.AMBIENT, 1.0F, 1.0F, false);
        }
    }

    public boolean swapEntity(BlockPos initialPos, BlockPos targetPos, Entity entity) {
        double xOffset = entity.posX - initialPos.getX();
        double yOffset = entity.posY - initialPos.getY();
        double zOffset = entity.posZ - initialPos.getZ();
        entity.setPositionAndUpdate(targetPos.getX() + xOffset, targetPos.getY() + yOffset, targetPos.getZ() + zOffset);
        return true;
    }

    static {
        SWAP_AREA = new AxisAlignedBB(-1, 1, -1, 1, 3, 1);
        OFFSETS = new ArrayList<>();
        for(int x = -1; x <= 1; x++) {
            for(int y = 1; y <= 3; y++) {
                for(int z = -1; z <= 1; z++) {
                    OFFSETS.add(new BlockPos(x, y, z));
                }
            }
        }
    }
}
