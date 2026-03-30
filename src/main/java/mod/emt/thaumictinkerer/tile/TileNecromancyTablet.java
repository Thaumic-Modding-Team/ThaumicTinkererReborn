package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.recipes.INecromaticRecipe;
import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.block.BlockNecromancyTablet;
import mod.emt.thaumictinkerer.recipes.NecromancyRecipeRegistry;
import mod.emt.thaumictinkerer.registry.ModBlocksTT;
import mod.emt.thaumictinkerer.utils.helpers.ItemHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.lib.events.EssentiaHandler;
import thaumcraft.common.tiles.crafting.TilePedestal;

import java.awt.*;

public class TileNecromancyTablet extends TileEntityTT implements ITickable, IAspectContainer {
    public static final ResourceLocation EMPTY = new ResourceLocation("");
    public static final int BEAM_EFFECT = 0;
    public static final BlockPos[] PEDESTAL_OFFSETS;
    public static final BlockPos[] QUARTZ_OFFSETS;
    public static final BlockPos[] NETHER_BRICK_OFFSETS;

    public ItemStackHandler stackHandler = new ItemStackHandler(1) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    protected ResourceLocation recipeName = EMPTY;
    protected INecromaticRecipe recipe = null;
    protected AspectList recipeEssentia = new AspectList();
    protected boolean isProcessing;
    protected int spawnDelay = 40;
    public int count;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stackHandler.deserializeNBT(compound.getCompoundTag("inventory"));
        this.spawnDelay = compound.getInteger("spawnDelay");
        this.isProcessing = compound.getBoolean("isProcessing");
        this.recipeName = new ResourceLocation(compound.getString("recipeName"));
        this.recipeEssentia.readFromNBT(compound);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inventory", this.stackHandler.serializeNBT());
        compound.setInteger("spawnDelay", this.spawnDelay);
        compound.setBoolean("isProcessing", this.isProcessing);
        compound.setString("recipeName", this.recipeName != null ? this.recipeName.toString() : "");
        this.recipeEssentia.writeToNBT(compound);
        return compound;
    }

    @Override
    public void update() {
        this.count++;
        boolean did = false;
        if(this.isTabletActive()) {
            if(!this.world.isRemote) {
                if(!this.isStructureValid()) {
                    BlockNecromancyTablet.setTabletActiveState(this.world, this.pos, false);
                    this.resetRecipe();
                    did = true;
                } else {
                    did |= this.checkAndUpdateRecipe();
                    did |= this.processRecipe();
                }
            } else {
                this.drawPassiveParticles();
                //TODO: Particle effects - maybe re-use effects from nature's aura?
            }
        }

        if(did) {
            this.markDirty();
        }
    }

    public boolean isTabletActive() {
        return this.world.getBlockState(this.pos).getValue(BlockNecromancyTablet.ENABLED);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isStructureValid() {
        //Pedestals
        for(BlockPos offset : PEDESTAL_OFFSETS) {
            TileEntity tile = this.world.getTileEntity(this.pos.add(offset));
            if(!(tile instanceof TilePedestal)) {
                return false;
            }
        }

        //Arcane Quartz
        for(BlockPos offset : QUARTZ_OFFSETS) {
            IBlockState state = this.world.getBlockState(this.pos.add(offset));
            if(state.getBlock() != ModBlocksTT.ARCANE_QUARTZ_BLOCK) {
                return false;
            }
        }

        //Nether Brick
        for(BlockPos offset : NETHER_BRICK_OFFSETS) {
            IBlockState state = this.world.getBlockState(this.pos.add(offset));
            if(state.getBlock() != Blocks.NETHER_BRICK) {
                return false;
            }
        }
        return true;
    }

    public void completeCraft() {
        if(this.recipe != null) {
            this.consumeComponents();
            this.consumeCenterItem();
            this.recipe.spawnEntity(this.world, this.pos);
            this.resetRecipe();
        }
    }

    public boolean checkAndUpdateRecipe() {
        ItemStack centerItem = this.getCenterItem();
        NonNullList<ItemStack> components = this.getComponents();
        if (this.recipe == null || !this.recipe.matches(centerItem, components)) {
            Tuple<ResourceLocation, INecromaticRecipe> tuple = NecromancyRecipeRegistry.getRecipeAndName(centerItem, components);
            if(tuple != null) {
                this.setRecipe(tuple.getFirst(), tuple.getSecond());
                return true;
            } else if(this.recipe != null) {
                this.resetRecipe();
                return true;
            }
        }
        return false;
    }

    public boolean processRecipe() {
        if(this.recipe != null) {
            if(this.recipeEssentia.aspects.isEmpty()) {
                if(this.spawnDelay > 0) {
                    //TODO: Summon effect and item consuming effect.
                    this.spawnDelay--;
                } else {
                    //TODO: Sound effect and summon entity effect.
                    this.completeCraft();
                }
                return true;
            } else {
                if(this.count % 5 == 0) {
                    for (Aspect aspect : this.recipeEssentia.getAspects()) {
                        if (EssentiaHandler.drainEssentia(this, aspect, null, 12, 1)) {
                            this.recipeEssentia.remove(aspect, 1);
                            this.isProcessing = true;
                            return true;
                        }
                    }
                    if(!this.recipeEssentia.aspects.isEmpty() && this.isProcessing) {
                        this.isProcessing = false;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setRecipe(ResourceLocation recipeName, @Nullable INecromaticRecipe recipe) {
        this.recipeName = recipeName;
        this.recipe = recipe;
        this.recipeEssentia = recipe != null ? recipe.getEssentia().copy() : new AspectList();
    }

    public void resetRecipe() {
        setRecipe(EMPTY, null);
        this.spawnDelay = 40;
    }

    public ItemStack getCenterItem() {
        return this.stackHandler.getStackInSlot(0);
    }

    public void consumeCenterItem() {
        if(this.recipe != null) {
            ItemStack stack = this.getCenterItem();
            this.stackHandler.setStackInSlot(0, ItemHelper.consumeIngredient(stack, this.recipe.getCenterIngredient()));
        }
    }

    public NonNullList<ItemStack> getComponents() {
        NonNullList<ItemStack> components = NonNullList.create();
        for(BlockPos offset : PEDESTAL_OFFSETS) {
            TileEntity tile = this.world.getTileEntity(this.pos.add(offset));
            if(tile instanceof TilePedestal && ((TilePedestal) tile).getSizeInventory() > 0) {
                components.add(((TilePedestal) tile).getStackInSlot(0));
            }
        }
        return components;
    }

    public void consumeComponents() {
        if(this.recipe == null || !this.recipe.shouldConsumeComponents())
            return;

        NonNullList<ItemStack> components = this.getComponents();
        for(Object component : this.recipe.getComponents()) {
            for(int i = 0; i < components.size(); i++) {
                ItemStack stack = components.get(i);
                if(ItemHelper.ingredientMatches(stack, component)) {
                    components.set(i, ItemHelper.consumeIngredient(stack, component));
                    break;
                }
            }
        }

        for (int i = 0; i < PEDESTAL_OFFSETS.length; i++) {
            BlockPos offset = PEDESTAL_OFFSETS[i];
            TileEntity tile = this.world.getTileEntity(this.pos.add(offset));
            if (tile instanceof TilePedestal) {
                if(i < components.size()) {
                    ((TilePedestal) tile).setInventorySlotContentsFromInfusion(0, components.get(i));
                } else {
                    ((TilePedestal) tile).setInventorySlotContentsFromInfusion(0, ItemStack.EMPTY);
                }
            }
        }
    }

    public void drawPassiveParticles() {
        int particles = 0;
        Color color = new Color(Aspect.DEATH.getColor());
        for(BlockPos offset : QUARTZ_OFFSETS) {
            if(this.world.rand.nextInt(40) != 0)
                continue;

            BlockPos particlePos = this.pos.add(offset.getX(), offset.getY() + 1, offset.getZ());
            FXDispatcher.INSTANCE.drawWispyMotes(
                    particlePos.getX() + this.world.rand.nextFloat(),
                    particlePos.getY() + this.world.rand.nextGaussian(),
                    particlePos.getZ() + this.world.rand.nextFloat(),
                    0, 0, 0,
                    20 + this.world.rand.nextInt(10),
                    color.getRed() / 255.0f,
                    color.getGreen() / 255.0f,
                    color.getBlue() / 255.0f,
                    -0.05f
            );

            particles++;
            if(particles >= 4)
                break;
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        return false;
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
        return this.recipeEssentia;
    }

    @Override
    public void setAspects(AspectList aspectList) {}

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return false;
    }

    @Override
    public int addToContainer(Aspect aspect, int amount) {
        return 0;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int amount) {
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int amount) {
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

    static {
        PEDESTAL_OFFSETS = new BlockPos[] {
                new BlockPos(3, 0, 0),
                new BlockPos(-3, 0, 0),
                new BlockPos(0, 0, 3),
                new BlockPos(0, 0, -3),
                new BlockPos(2, 0, 2),
                new BlockPos(-2, 0, 2),
                new BlockPos(2, 0, -2),
                new BlockPos(-2, 0, -2)
        };
        QUARTZ_OFFSETS = new BlockPos[] {
                //Middle
                new BlockPos(-1, -1, -1),
                new BlockPos(-1, -1, 0),
                new BlockPos(-1, -1, 1),
                new BlockPos(0, -1, -1),
                new BlockPos(0, -1, 0),
                new BlockPos(0, -1, 1),
                new BlockPos(1, -1, -1),
                new BlockPos(1, -1, 0),
                new BlockPos(1, -1, 1),
                //Outer
                new BlockPos(2, -1, 0),
                new BlockPos(-2, -1, 0),
                new BlockPos(0, -1, 2),
                new BlockPos(0, -1, -2)
        };
        NETHER_BRICK_OFFSETS = new BlockPos[] {
                new BlockPos(3, -1, 0),
                new BlockPos(3, -1, 1),
                new BlockPos(2, -1, 1),
                new BlockPos(2, -1, 2),
                new BlockPos(1, -1, 2),
                new BlockPos(1, -1, 3),

                new BlockPos(0, -1, 3),
                new BlockPos(-1, -1, 3),
                new BlockPos(-1, -1, 2),
                new BlockPos(-2, -1, 2),
                new BlockPos(-2, -1, 1),
                new BlockPos(3, -1, 1),

                new BlockPos(-3, -1, 0),
                new BlockPos(-3, -1, 1),
                new BlockPos(-2, -1, 1),
                new BlockPos(-2, -1, 2),
                new BlockPos(-1, -1, 2),
                new BlockPos(-1, -1, 3),

                new BlockPos(0, -1, -3),
                new BlockPos(-1, -1, -3),
                new BlockPos(-1, -1, -2),
                new BlockPos(-2, -1, -2),
                new BlockPos(-2, -1, -1),
                new BlockPos(3, -1, -1)
        };
    }
}
