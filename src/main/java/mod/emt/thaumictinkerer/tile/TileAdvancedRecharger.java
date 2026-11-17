package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.items.IRechargable;
import thaumcraft.api.items.RechargeHelper;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.basic.BlockPillar;
import thaumcraft.common.world.aura.AuraHandler;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class TileAdvancedRecharger extends TileEntityTT implements ITickable, IAspectContainer {
    private static final int EVENT_RECHARGE_EFFECT = 0;

    public ItemStackHandler stackHandler = new ItemStackHandler(1) {
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return this.isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof IRechargable;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private int counter = 0;

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
        this.counter++;
        if(!this.world.isRemote && this.counter % 5 == 0 && this.validateStructure()) {
            ItemStack toRecharge = this.getRechargingStack();
            if(!toRecharge.isEmpty() && toRecharge.getItem() instanceof IRechargable) {
                int maxCharge = ((IRechargable) toRecharge.getItem()).getMaxCharge(toRecharge, null);
                int currCharge = RechargeHelper.getCharge(toRecharge);
                int toCharge = Math.min(maxCharge - currCharge, 5);
                if(toCharge > 0 && this.drainAndRecharge(toRecharge, toCharge, false)) {
                    ArrayList<Aspect> primalAspects = Aspect.getPrimalAspects();
                    int color = primalAspects.get(this.world.rand.nextInt(primalAspects.size())).getColor();
                    this.world.addBlockEvent(this.pos, this.getBlockType(), EVENT_RECHARGE_EFFECT, color);
                    this.markDirty();
                }
            }
        }
    }

    public boolean validateStructure() {
        return this.getOffsetBlock(1, 0, 1).getBlock() instanceof BlockPillar
                && this.getOffsetBlock(-1, 0, 1).getBlock() instanceof BlockPillar
                && this.getOffsetBlock(1, 0, -1).getBlock() instanceof BlockPillar
                && this.getOffsetBlock(-1, 0, -1).getBlock() instanceof BlockPillar;
    }

    protected IBlockState getOffsetBlock(int xOffset, int yOffset, int zOffset) {
        return this.world.getBlockState(this.pos.add(xOffset, yOffset, zOffset));
    }

    public boolean drainAndRecharge(ItemStack toRecharge, float visDrain, boolean simulate) {
        int drainRange = 1;
        float aura = this.getAuraPool(this.world, this.pos, drainRange);
        float toDrain = Math.min(aura, visDrain);
        if(toDrain >= 1.0f) {
            if(!simulate) {
                int drained = (int) this.drainAuraPool(this.world, this.pos, drainRange, toDrain);
                RechargeHelper.rechargeItemBlindly(toRecharge, null, drained);
            }
            return true;
        }
        return false;
    }

    public float getAuraPool(World world, BlockPos pos, int chunkRange) {
        float total = 0;
        for(int x = -chunkRange; x <= chunkRange; x++) {
            for(int z = -chunkRange; z <= chunkRange; z++) {
                total += AuraHandler.getTotalAura(world, pos.add(x << 4, 0, z << 4));
            }
        }
        return total;
    }

    public float drainAuraPool(World world, BlockPos pos, int chunkRange, float visDrain) {
        float drained = 0;
        int chunks = (int) Math.pow((chunkRange * 2.0) + 1.0, 2.0);
        float chunkDrain = visDrain / (float) chunks;
        Map<Float, BlockPos> drainPositions = new TreeMap<>();
        for(int x = -chunkRange; x <= chunkRange; x++) {
            for(int z = -chunkRange; z <= chunkRange; z++) {
                BlockPos drainPos = pos.add(x << 4, 0, z << 4);
                float vis = AuraHandler.getVis(world, drainPos);
                drainPositions.put(vis, drainPos);
            }
        }
        for (Map.Entry<Float, BlockPos> entry : drainPositions.entrySet()) {
            Float chunkVis = entry.getKey();
            BlockPos chunkPos = entry.getValue();
            if (chunkDrain > chunkVis) {
                drained += AuraHandler.drainVis(world, chunkPos, chunkVis, false);
                chunkDrain = (visDrain - chunkVis) / --chunks;
            } else {
                drained += AuraHandler.drainVis(world, chunkPos, chunkDrain, false);
            }
        }
        return drained;
    }


    public ItemStackHandler getStackHandler() {
        return stackHandler;
    }

    public ItemStack getRechargingStack() {
        return this.stackHandler.getStackInSlot(0);
    }

    public void setRechargingStack(ItemStack stack) {
        this.stackHandler.setStackInSlot(0, stack);
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if(id == EVENT_RECHARGE_EFFECT) {
            if(this.world.isRemote) {
                for(int i = 0; i < 4; i++) {
                    FXDispatcher.INSTANCE.visSparkle(
                            this.pos.getX() + this.getWorld().rand.nextInt(3) - this.getWorld().rand.nextInt(3),
                            this.pos.up().getY() + this.getWorld().rand.nextInt(3),
                            this.pos.getZ() + this.getWorld().rand.nextInt(3) - this.getWorld().rand.nextInt(3),
                            this.pos.getX(),
                            this.pos.up().getY(),
                            this.pos.getZ(),
                            type
                    );
                }
            }
            return true;
        }
        return super.receiveClientEvent(id, type);
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    public @Nullable <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.stackHandler);
        }
        return super.getCapability(capability, facing);
    }

    //##########################################################
    // IAspectContainer

    @Override
    public AspectList getAspects() {
        ItemStack recharge = this.getRechargingStack();
        if(!recharge.isEmpty() && recharge.getItem() instanceof IRechargable) {
            int charge = RechargeHelper.getCharge(recharge);
            return new AspectList().add(Aspect.ENERGY, charge);
        }
        return null;
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
