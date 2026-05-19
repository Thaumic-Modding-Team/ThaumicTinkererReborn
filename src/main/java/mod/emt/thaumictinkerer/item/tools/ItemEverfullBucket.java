package mod.emt.thaumictinkerer.item.tools;

import mod.emt.thaumictinkerer.api.item.AbstractItemAddition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.client.fx.FXDispatcher;

public class ItemEverfullBucket extends AbstractItemAddition {
    private static final int MAX_QUEUE = 10;
    private static final FluidStack WATER_STACK = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);

    public ItemEverfullBucket() {
        super("everfull_bucket");
        this.setMaxStackSize(1);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new EverfullBucketWrapper(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem() || slotChanged;
    }

    @Override
    public void onUpdate(@NotNull ItemStack stack, @NotNull World worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(!worldIn.isRemote) {
            int queue = getQueue(stack);
            if(queue > 0 && AuraHelper.getVis(worldIn, entityIn.getPosition()) >= 2.0f) {
                AuraHelper.drainVis(worldIn, entityIn.getPosition(), 2.0f, false);
                setQueue(stack, queue - 1);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, @NotNull EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        RayTraceResult trace = this.rayTrace(worldIn, playerIn, false);
        ActionResult<ItemStack> result = ForgeEventFactory.onBucketUse(playerIn, worldIn, stack, trace);
        if (result != null) {
            return result;
        }

        if(trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos tracePos = trace.getBlockPos();
            if (worldIn.isBlockModifiable(playerIn, tracePos)) {
                boolean isReplaceable = worldIn.getBlockState(tracePos).getBlock().isReplaceable(worldIn, tracePos);
                BlockPos offset = isReplaceable && trace.sideHit == EnumFacing.UP ? tracePos : tracePos.offset(trace.sideHit);
                if (playerIn.canPlayerEdit(offset, trace.sideHit, stack)) {
                    FluidActionResult fluidAction = tryPlaceContainedLiquid(playerIn, stack, worldIn, offset);
                    if (fluidAction.isSuccess()) {
                        return new ActionResult<>(EnumActionResult.SUCCESS, fluidAction.getResult());
                    }
                }
            }
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    public FluidActionResult tryPlaceContainedLiquid(@Nullable EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
        FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, pos, stack, WATER_STACK);
        if(result.isSuccess()) {
            if(world.isRemote) {
                for(int k = 0; k < 20; ++k) {
                    FXDispatcher.INSTANCE.jarSplashFx(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
                }
            }
        }
        return result;
    }

    @Override
    public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
        return EnumRarity.RARE;
    }

    public static int getQueue(ItemStack stack) {
        return stack.getTagCompound() != null ? stack.getTagCompound().getInteger("queue") : 0;
    }

    public static void setQueue(ItemStack stack, int queue) {
        stack.setTagInfo("queue", new NBTTagInt(queue));
    }

    public static class EverfullBucketWrapper extends FluidBucketWrapper {
        public EverfullBucketWrapper(@NotNull ItemStack container) {
            super(container);
        }

        protected boolean canDispense() {
            return getQueue(this.container) < MAX_QUEUE;
        }

        @Override
        public boolean canFillFluidType(FluidStack fluidStack) {
            return false;
        }

        @Override
        public @Nullable FluidStack getFluid() {
            return this.canDispense() ? WATER_STACK : null;
        }

        @Override
        protected void setFluid(@Nullable FluidStack fluidStack) {}

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Override
        public @Nullable FluidStack drain(FluidStack resource, boolean doDrain) {
            if(container.getCount() != 1 || resource == null || !canDispense()) {
                return null;
            } else {
                FluidStack fluid = this.getFluid();
                if (fluid != null && fluid.isFluidEqual(resource)) {
                    if(doDrain) {
                        setQueue(this.container, getQueue(this.container)  + 1);
                    }
                    return new FluidStack(fluid.getFluid(), Math.min(fluid.amount, resource.amount));
                }
            }
            return null;
        }

        @Override
        public @Nullable FluidStack drain(int maxDrain, boolean doDrain) {
            return this.drain(new FluidStack(FluidRegistry.WATER, Math.min(Fluid.BUCKET_VOLUME, maxDrain)), doDrain);
        }
    }
}
