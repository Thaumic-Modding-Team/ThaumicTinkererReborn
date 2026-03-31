package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.block.BlockThaumicRestorer;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.utils.helpers.CompatHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.client.fx.FXDispatcher;

import java.awt.*;

public class TileThaumicRestorer extends TileEntityTT implements ITickable, IEssentiaTransport, IAspectContainer {
    public ItemStackHandler stackHandler = new ItemStackHandler(1) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return this.isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem().isDamageable() && stack.getItem().isRepairable();
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    protected boolean isRepairing;
    public int count;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stackHandler.deserializeNBT(compound.getCompoundTag("inventory"));
        this.isRepairing = compound.getBoolean("isRepairing");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inventory", this.stackHandler.serializeNBT());
        compound.setBoolean("isRepairing", this.isRepairing);
        return compound;
    }

    @Override
    public void update() {
        this.count++;

        boolean did = false;
        if(!this.world.isRemote) {
            //TODO: This might be too slow.
            if(this.count % 5 == 0) {
                if (this.shouldRepairStack()) {
                    if(this.attemptDrainAndRepair()) {
                        this.isRepairing = true;
                        did = true;
                    } else if(this.isRepairing) {
                        this.isRepairing = false;
                        did = true;
                    }
                } else if (this.isRepairing) {
                    this.isRepairing = false;
                    did = true;
                }
            }
        } else {
            if(this.isRepairing) {
                this.performRepairEffect();
            }
        }

        if(did) {
            this.markDirty();
        }
    }

    public ItemStack getStackToRepair() {
        return this.stackHandler.getStackInSlot(0);
    }

    public boolean isRepairing() {
        return isRepairing;
    }

    public Aspect getRepairAspect() {
        if(ConfigHandlerTT.thaumicRestorer.dynamicAspects) {
            ItemStack stack = this.getStackToRepair();
            if(!stack.isEmpty()) {
                //TODO: Construct's Armory support.
                if(CompatHelper.isTinkersConstructTool(stack)) {
                    return CompatHelper.getRepairAspect(stack);
                } else if (stack.getItem() instanceof ItemArmor) {
                    return Aspect.PROTECT;
                } else if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemBow) {
                    return Aspect.AVERSION;
                } else if (stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemHoe) {
                    return Aspect.TOOL;
                }
            }
        }
        return Aspect.CRAFT;
    }

    protected EnumFacing getEssentiaInputFace() {
        return this.world.getBlockState(this.pos).getValue(BlockThaumicRestorer.FACING).getOpposite();
    }

    protected boolean shouldRepairStack() {
        ItemStack stack = this.getStackToRepair();
        if(CompatHelper.isTinkersConstructTool(stack)) {
            return CompatHelper.isTinkersToolRepairable(stack);
        } else {
            return !stack.isEmpty() && stack.isItemStackDamageable() && stack.isItemDamaged();
        }
    }

    public int getRepairAmount() {
        ItemStack stack = this.getStackToRepair();
        if(CompatHelper.isTinkersConstructTool(stack)) {
            return CompatHelper.getTinkersToolDamage(stack);
        } else {
            return !stack.isEmpty() ? stack.getItemDamage() : 0;
        }
    }

    public void repairItemStack(int repairAmount) {
        ItemStack repairStack = this.getStackToRepair();
        if(CompatHelper.isTinkersConstructTool(repairStack)) {
            CompatHelper.repairTinkersTool(repairStack, repairAmount);
        } else {
            repairStack.setItemDamage(Math.max(repairStack.getItemDamage() - repairAmount, 0));
        }
    }

    protected boolean attemptDrainAndRepair() {
        int essentia = this.drawEssentia();
        if(essentia > 0) {
            this.repairItemStack(essentia);
            return true;
        }
        return false;
    }

    public int drawEssentia() {
        Aspect repairAspect = this.getRepairAspect();
        if(repairAspect != null) {
            EnumFacing rear = this.getEssentiaInputFace();
            TileEntity tile = this.world.getTileEntity(this.pos.offset(rear));
            if (tile instanceof IEssentiaTransport) {
                IEssentiaTransport transport = (IEssentiaTransport) tile;
                EnumFacing transportFace = rear.getOpposite();
                if (transport.canOutputTo(transportFace) && transport.getSuctionAmount(transportFace) < this.getSuctionAmount(rear)) {
                    return transport.takeEssentia(repairAspect, 1, transportFace);
                }
            }
        }
        return 0;
    }

    public void performRepairEffect() {
        if(this.world.rand.nextInt(20) == 0) {
            Aspect aspect = this.getRepairAspect();
            Color color = new Color(aspect.getColor());
            EnumFacing zapOrigin = EnumFacing.HORIZONTALS[this.world.rand.nextInt(EnumFacing.HORIZONTALS.length)];
            double xEnd = this.pos.getX() + 0.5;
            double yEnd = this.pos.getY() + 1.125; // 1.0 + itemHeight / 2
            double zEnd = this.pos.getZ() + 0.5;
            double xStart = xEnd - (zapOrigin.getXOffset() * 0.25);
            double yStart = this.pos.getY() + 0.875;
            double zStart = zEnd - (zapOrigin.getZOffset() * 0.25);
            FXDispatcher.INSTANCE.arcLightning(xStart, yStart, zStart, xEnd, yEnd, zEnd, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.25f);
        }
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public @Nullable <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.stackHandler);
        }
        return super.getCapability(capability, facing);
    }

    //##########################################################
    // IEssentiaTransport

    @Override
    public boolean isConnectable(EnumFacing facing) {
        return this.getEssentiaInputFace() == facing;
    }

    @Override
    public boolean canInputFrom(EnumFacing facing) {
        return this.isConnectable(facing);
    }

    @Override
    public boolean canOutputTo(EnumFacing facing) {
        return false;
    }

    @Override
    public void setSuction(Aspect aspect, int suction) {}

    @Override
    public Aspect getSuctionType(EnumFacing facing) {
        return this.getRepairAspect();
    }

    @Override
    public int getSuctionAmount(EnumFacing facing) {
        ItemStack stack = this.getStackToRepair();
        return !stack.isEmpty() && stack.isItemDamaged() ? 128 : 0;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, EnumFacing facing) {
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, EnumFacing facing) {
        Aspect repairAspect = this.getRepairAspect();
        if(repairAspect != null && aspect == repairAspect && amount > 0 && this.canInputFrom(facing)) {
            amount = Math.min(amount, this.getRepairAmount());
            this.repairItemStack(amount);
            this.markDirty();
            return amount;
        }
        return 0;
    }

    @Override
    public Aspect getEssentiaType(EnumFacing facing) {
        return this.getRepairAspect();
    }

    @Override
    public int getEssentiaAmount(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    //##########################################################
    // IAspectContainer

    @Override
    public AspectList getAspects() {
        Aspect repairAspect = this.getRepairAspect();
        int repairAmount = this.getRepairAmount();
        return repairAmount > 0 && repairAspect != null ? new AspectList().add(repairAspect, this.getRepairAmount()) : null;
    }

    @Override
    public void setAspects(AspectList aspectList) {}

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return false;
    }

    @Override
    public int addToContainer(Aspect aspect, int i) {
        return 0;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int i) {
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return false;
    }

    @Override
    public boolean doesContainerContain(AspectList aspectList) {
        return false;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return 0;
    }
}
