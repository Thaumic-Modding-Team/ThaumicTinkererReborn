package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.block.BlockEssentiaFunnel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.*;
import thaumcraft.common.blocks.essentia.BlockJarItem;
import thaumcraft.common.tiles.essentia.TileJarFillable;

public class TileEssentiaFunnel extends TileEntityTT implements ITickable, IAspectContainer, IEssentiaTransport, IAspectSource {
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
            return stack.isEmpty() || stack.getItem() instanceof BlockJarItem;
        }

        @Override
        protected void onContentsChanged(int slot) {
            BlockEssentiaFunnel.setHasJar(world, pos, !getJarStack().isEmpty());
            markDirty();
        }
    };

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stackHandler.deserializeNBT(compound.getCompoundTag("inventory"));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inventory", this.stackHandler.serializeNBT());
        return compound;
    }

    @Override
    public void update() {
        boolean did = false;
        if(!this.world.isRemote) {
            EnumFacing hopperFacing = this.getHopperFacing();
            IEssentiaTransport transport = this.getHopperEssentiaTransport(hopperFacing);
            if(transport != null) {
                did = this.attemptEssentiaTransport(transport, hopperFacing.getOpposite());
            }
        }
        if(did) {
            this.markDirty();
        }
    }

    public EnumFacing getHopperFacing() {
        BlockPos hopperPos = this.pos.down();
        TileEntity tile = this.world.getTileEntity(hopperPos);
        if(tile instanceof TileEntityHopper && BlockHopper.isEnabled(tile.getBlockMetadata())) {
            return BlockHopper.getFacing(tile.getBlockMetadata());
        }
        return EnumFacing.UP;
    }

    @Nullable
    public IEssentiaTransport getHopperEssentiaTransport(EnumFacing hopperFacing) {
        if(hopperFacing == EnumFacing.UP)
            return null;

        BlockPos hopperPos = this.pos.down();
        TileEntity tile = this.world.getTileEntity(hopperPos);
        if(tile instanceof TileEntityHopper && BlockHopper.isEnabled(tile.getBlockMetadata())) {
            tile = this.world.getTileEntity(hopperPos.offset(hopperFacing));
            if(tile instanceof IEssentiaTransport && ((IEssentiaTransport) tile).canInputFrom(hopperFacing.getOpposite())) {
                return (IEssentiaTransport) tile;
            }
        }
        return null;
    }

    public boolean attemptEssentiaTransport(IEssentiaTransport target, EnumFacing inputFace) {
        AspectList aspectList = this.getAspects();
        if(aspectList != null) {
            for(Aspect aspect : aspectList.getAspects()) {
                if(aspectList.getAmount(aspect) >= 1 && target.addEssentia(aspect, 1, inputFace) == 1) {
                    this.takeFromContainer(aspect, 1);
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack getJarStack() {
        return this.stackHandler.getStackInSlot(0);
    }

    public void setJarStack(ItemStack jarStack) {
        this.stackHandler.setStackInSlot(0, jarStack);
    }

    @Nullable
    public IEssentiaContainerItem getContainerItem() {
        ItemStack stack = this.getJarStack();
        return !stack.isEmpty() && stack.getItem() instanceof IEssentiaContainerItem ? (IEssentiaContainerItem) stack.getItem() : null;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    private TileJarFillable getJarTile() {
        ItemStack stack = this.getJarStack();
        if(!stack.isEmpty() && stack.getItem() instanceof BlockJarItem) {
            BlockJarItem jarItem = (BlockJarItem) stack.getItem();
            Block block = jarItem.getBlock();
            IBlockState state = block.getStateFromMeta(stack.getItemDamage());
            if(block.hasTileEntity(state)) {
                TileEntity tile = block.createTileEntity(this.world, state);
                if(tile instanceof TileJarFillable) {
                    TileJarFillable jar = (TileJarFillable) tile;
                    jar.setAspects(jarItem.getAspects(stack));
                    if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("AspectFilter")) {
                        jar.aspectFilter = Aspect.getAspect(stack.getTagCompound().getString("AspectFilter"));
                    }
                    return (TileJarFillable) tile;
                }
            }
        }
        return null;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.DOWN;
    }

    @Override
    public @Nullable <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.DOWN) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.stackHandler);
        }
        return super.getCapability(capability, facing);
    }

    //##########################################################
    // IAspectContainer

    @Override
    public AspectList getAspects() {
        IEssentiaContainerItem container = this.getContainerItem();
        return container != null ? container.getAspects(this.getJarStack()) : null;
    }

    @Override
    public void setAspects(AspectList aspectList) {
        ItemStack stack = this.getJarStack();
        IEssentiaContainerItem container = this.getContainerItem();
        if(container != null) {
            ItemStack copy = stack.copy();
            container.setAspects(copy, aspectList);
            if(!ItemStack.areItemStacksEqual(stack, copy)) {
                if(aspectList.aspects.isEmpty() && copy.getTagCompound() != null) {
                    copy.getTagCompound().removeTag("Aspects");
                    if(copy.getTagCompound().isEmpty()) {
                        copy.setTagCompound(null);
                    }
                }
                this.setJarStack(copy);
            }
        }
    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        TileJarFillable container = this.getJarTile();
        return container != null && container.doesContainerAccept(aspect);
    }

    @Override
    public int addToContainer(Aspect aspect, int amount) {
        TileJarFillable container = this.getJarTile();
        if(container != null) {
            int added = container.addToContainer(aspect, amount);
            AspectList aspectList = container.getAspects();
            this.setAspects(aspectList);
            return added;
        }
        return amount;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int amount) {
        if(this.doesContainerContainAmount(aspect, amount)) {
            AspectList remaining = this.getAspects();
            remaining.remove(aspect, amount);
            this.setAspects(remaining);
            return true;
        }
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList aspectList) {
        if(this.doesContainerContain(aspectList)) {
            AspectList remaining = this.getAspects();
            remaining.remove(aspectList);
            this.setAspects(remaining);
            return true;
        }
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
        return this.containerContains(aspect) >= amount;
    }

    @Override
    public boolean doesContainerContain(AspectList aspectList) {
        for(Aspect aspect : aspectList.getAspects()) {
            if(!this.doesContainerContainAmount(aspect, aspectList.getAmount(aspect))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int containerContains(Aspect aspect) {
        AspectList aspectList = this.getAspects();
        return aspectList != null ? aspectList.getAmount(aspect) : 0;
    }

    //##########################################################
    // IAspectContainer

    @Override
    public boolean isConnectable(EnumFacing facing) {
        return !this.getJarStack().isEmpty() && facing == EnumFacing.UP;
    }

    @Override
    public boolean canInputFrom(EnumFacing facing) {
        return !this.getJarStack().isEmpty() && facing == EnumFacing.UP;
    }

    @Override
    public boolean canOutputTo(EnumFacing facing) {
        return !this.getJarStack().isEmpty() && facing == EnumFacing.UP;
    }

    @Override
    public void setSuction(Aspect aspect, int amount) {}

    @Override
    public Aspect getSuctionType(EnumFacing facing) {
        TileJarFillable container = this.getJarTile();
        return container != null ? container.getSuctionType(facing) : null;
    }

    @Override
    public int getSuctionAmount(EnumFacing facing) {
        TileJarFillable container = this.getJarTile();
        return container != null ? container.getSuctionAmount(facing) : 0;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, EnumFacing facing) {
        return this.canOutputTo(facing) && this.takeFromContainer(aspect, amount) ? amount : 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, EnumFacing facing) {
        return this.canInputFrom(facing) ? amount - this.addToContainer(aspect, amount) : 0;
    }

    @Override
    public Aspect getEssentiaType(EnumFacing facing) {
        TileJarFillable container = this.getJarTile();
        return container != null ? container.getEssentiaType(facing) : null;
    }

    @Override
    public int getEssentiaAmount(EnumFacing facing) {
        TileJarFillable container = this.getJarTile();
        return container != null ? container.getEssentiaAmount(facing) : 0;
    }

    @Override
    public int getMinimumSuction() {
        TileJarFillable container = this.getJarTile();
        return container != null ? container.getMinimumSuction() : 0;
    }

    //##########################################################
    // IAspectSource

    @Override
    public boolean isBlocked() {
        TileJarFillable jar = this.getJarTile();
        return jar == null || jar.isBlocked();
    }
}
