package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.block.BlockDynamismTablet;
import mod.emt.thaumictinkerer.utils.helpers.FakePlayerHelper;
import mod.emt.thaumictinkerer.utils.misc.FakePlayerTT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileDynamismTablet extends TileEntityTT implements ITickable {
    public static final int MAX_ACTIVATION_COUNTER = 30;

    public ItemStackHandler stackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private ClickMode clickMode = ClickMode.LEFT_CLICK;
    private ClickDirection clickDirection = ClickDirection.FRONT;
    private boolean isSneaking = false;
    private int activationCounter;
    private boolean reversing;
    private int breakProgress;
    private int breakProgressMax = -1;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stackHandler.deserializeNBT(compound.getCompoundTag("inventory"));
        this.clickMode = ClickMode.getMode(compound.getInteger("clickMode"));
        this.clickDirection = ClickDirection.getDirection(compound.getInteger("clickDirection"));
        this.isSneaking = compound.getBoolean("isSneaking");
        this.activationCounter = compound.getInteger("activationCounter");
        this.reversing = compound.getBoolean("reversing");
        this.breakProgress = compound.getInteger("breakProgress");
        this.breakProgressMax = compound.getInteger("breakTimeMax");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inventory", this.stackHandler.serializeNBT());
        compound.setInteger("clickMode", this.clickMode.ordinal());
        compound.setInteger("clickDirection", this.clickDirection.ordinal());
        compound.setBoolean("isSneaking", this.isSneaking);
        compound.setInteger("activationCounter", this.activationCounter);
        compound.setBoolean("reversing", this.reversing);
        compound.setInteger("breakProgress", this.breakProgress);
        compound.setInteger("breakTimeMax", this.breakProgressMax);
        return compound;
    }

    @Override
    public void update() {
        boolean did = false;
        if(!this.world.isRemote && this.world instanceof WorldServer) {
            if(this.getTabletStack().isEmpty()) {
                if(this.shouldReset()) {
                    this.resetProgress();
                    did = true;
                }
            } else {

                if((this.isPowered() || this.getActivationCounter() > 0) && this.shouldActivationTick()) {
                    if (this.tickActivation()) {
                        ItemStack current = this.getTabletStack();
                        ItemStack result = current.copy();
                        FakePlayerTT player = FakePlayerHelper.getFakePlayer((WorldServer) this.world, this.pos, this.getTabletFront(), result, this.isSneaking());
                        player.setFakePlayerTile(this);
                        BlockPos interactPos = this.pos.offset(this.getTabletFront());

                        if (this.getClickMode() == ClickMode.LEFT_CLICK) {
                            result = FakePlayerHelper.leftClickPosition(player, this.world, interactPos, this.getClickDirectionFacing(), this.getMaxActivationCounter() * 60);
                        } else {
                            result = FakePlayerHelper.rightClickPosition(player, this.world, interactPos, this.getClickDirectionFacing());
                        }

                        if (!current.equals(result)) {
                            this.setTabletStack(result);
                        }

                        FakePlayerHelper.cleanupFakePlayer(player);
                    }
                    did = true;
                } else {
                    if(this.getBreakProgress() != 0 || this.getBreakProgressMax() != 0) {
                        this.resetBreakProgress();
                    }
                }
            }
        }

        if(did) {
            this.markDirty();
        }
    }

    public boolean shouldActivationTick() {
        boolean isPowered = this.isPowered();
        if (this.getActivationCounter() > 0) {
            return true;
        } else if (isPowered && this.getClickMode() == ClickMode.LEFT_CLICK && this.shouldProcessLeftClick()) {
            return true;
        } else if (isPowered && this.getClickMode() == ClickMode.RIGHT_CLICK && this.shouldProcessRightClick()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean shouldProcessLeftClick() {
        return true;
    }

    public boolean shouldProcessRightClick() {
        return true;
    }

    public boolean shouldReset() {
        return this.activationCounter > 0
                || this.reversing
                || this.breakProgressMax >= 0
                || this.breakProgress > 0;
    }

    public void resetProgress() {
        this.activationCounter = 0;
        this.reversing = false;
        this.breakProgressMax = -1;
        this.breakProgress = 0;
    }

    /**
     * Ticks the current activation status of the item stored in the tablet. Returns true when the
     * activation timer is finished.
     */
    public boolean tickActivation() {
        if(this.reversing) {
            if(this.activationCounter > 0) {
                this.activationCounter--;
            } else {
                this.activationCounter = 0;
                this.reversing = false;
            }
        } else {
            if(this.activationCounter < MAX_ACTIVATION_COUNTER) {
                this.activationCounter++;
            } else {
                this.activationCounter = MAX_ACTIVATION_COUNTER;
                this.reversing = true;
                return true;
            }
        }
        return false;
    }

    public boolean isPowered() {
        return this.world.isBlockPowered(this.pos);
    }

    public EnumFacing getTabletFront() {
        IBlockState state = this.world.getBlockState(this.pos);
        return state.getValue(BlockDynamismTablet.FACING);
    }

    public EnumFacing getClickDirectionFacing() {
        EnumFacing front = this.getTabletFront();
        switch (this.getClickDirection()) {
            case TOP:
                return EnumFacing.UP;
            case BOTTOM:
                return EnumFacing.DOWN;
            case FRONT:
                return front;
            case BACK:
                return front.getOpposite();
            case LEFT:
                return front.rotateYCCW();
            case RIGHT:
                return front.rotateY();
        }
        return front;
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

    public ClickDirection getClickDirection() {
        return clickDirection;
    }

    public void setClickDirection(ClickDirection clickDirection) {
        if(this.clickDirection != clickDirection) {
            this.clickDirection = clickDirection;
            this.markDirty();
        }
    }

    public boolean isSneaking() {
        return isSneaking;
    }

    public void setIsSneaking(boolean isSneaking) {
        if(this.isSneaking != isSneaking) {
            this.isSneaking = isSneaking;
            this.markDirty();
        }
    }

    public int getActivationCounter() {
        return activationCounter;
    }

    public int getMaxActivationCounter() {
        return MAX_ACTIVATION_COUNTER;
    }

    public int getBreakProgress() {
        return breakProgress;
    }

    public void setBreakProgress(int breakProgress) {
        if(this.breakProgress != breakProgress) {
            this.breakProgress = breakProgress;
            this.markDirty();
        }
    }

    public int getBreakProgressMax() {
        return breakProgressMax;
    }

    public void setBreakProgressMax(int breakProgressMax) {
        if(this.breakProgressMax != breakProgressMax) {
            this.breakProgressMax = breakProgressMax;
            this.markDirty();
        }
    }

    public void resetBreakProgress() {
        this.breakProgress = 0;
        this.breakProgressMax = 0;
        this.markDirty();
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

        public static ClickMode getMode(int ordinal) {
            ClickMode[] values = ClickMode.values();
            return values[ordinal % values.length];
        }
    }

    public enum ClickDirection {
        TOP,
        BOTTOM,
        FRONT,
        BACK,
        LEFT,
        RIGHT;

        public static ClickDirection getDirection(int ordinal) {
            ClickDirection[] values = ClickDirection.values();
            return values[ordinal % values.length];
        }

        public ClickDirection getNext() {
            ClickDirection[] values = ClickDirection.values();
            return values[(this.ordinal() + 1) % values.length];
        }
    }
}
