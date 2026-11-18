package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.block.BlockDynamismTablet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileDynamismTablet extends TileEntityTT implements ITickable {
    private static final int CLICK_SPEED = 40;

    public ItemStackHandler stackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private ClickMode clickMode = ClickMode.LEFT_CLICK;
    private int counter;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stackHandler.deserializeNBT(compound.getCompoundTag("inventory"));
        this.clickMode = ClickMode.values()[compound.getInteger("clickMode")];
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inventory", this.stackHandler.serializeNBT());
        compound.setInteger("clickMode", this.clickMode.ordinal());
        return compound;
    }

    @Override
    public void update() {

    }

    public boolean tickRightClick(EntityPlayer player) {
        return false;
    }

    public boolean tickLeftClick(EntityPlayer player) {
//        player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, this.getTabletStack().copy());
//        AxisAlignedBB area = new AxisAlignedBB(this.pos.offset(this.getTabletFront()));
//        List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, area, EntityLivingBase::isEntityAlive);
//        for(EntityLivingBase entity : entities) {
//            player.attackTargetEntityWithCurrentItem(entity);
//        }
//        player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
        return false;
    }

    public boolean canAttack() {
        AxisAlignedBB area = new AxisAlignedBB(this.pos.offset(this.getTabletFront()));
        return !this.world.getEntitiesWithinAABB(EntityLivingBase.class, area, EntityLivingBase::isEntityAlive).isEmpty();
    }

    public EnumFacing getTabletFront() {
        IBlockState state = this.world.getBlockState(this.pos);
        return state.getValue(BlockDynamismTablet.FACING);
    }

    public ItemStackHandler getStackHandler() {
        return stackHandler;
    }

    public ItemStack getTabletStack() {
        return this.stackHandler.getStackInSlot(0);
    }

    public void setTabletStack(ItemStack stack) {
        this.stackHandler.setStackInSlot(0, stack);
    }

    public ClickMode getClickMode() {
        return clickMode;
    }

    public void setClickMode(ClickMode mode) {
        if(this.clickMode != mode) {
            this.clickMode = mode;
            this.markDirty();
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public @Nullable <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.stackHandler);
        }
        return super.getCapability(capability, facing);
    }

    public enum ClickMode {
        LEFT_CLICK,
        RIGHT_CLICK;
    }
}
