package mod.emt.thaumictinkerer.tile;

import mod.emt.thaumictinkerer.api.recipes.INecromancyRecipe;
import mod.emt.thaumictinkerer.api.tile.TileEntityTT;
import mod.emt.thaumictinkerer.block.BlockNecromancyTablet;
import mod.emt.thaumictinkerer.recipes.NecromancyRecipeRegistry;
import mod.emt.thaumictinkerer.registry.ModBlocksTT;
import mod.emt.thaumictinkerer.registry.ModSoundEventsTT;
import mod.emt.thaumictinkerer.utils.helpers.ItemHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
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
    public static final int EFFECT_BEAM = 4;
    public static final int EFFECT_CONSUME_ITEM = 5;
    public static final int EFFECT_SPAWN = 6;
    public static final BlockPos[] PEDESTAL_OFFSETS;
    public static final BlockPos[] QUARTZ_OFFSETS;
    public static final BlockPos[] NETHER_BRICK_OFFSETS;
    public static final int SPAWN_DELAY_MAX = 140;
    public static final int RESTART_DELAY_MAX = 60;

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
    private ResourceLocation recipeName = EMPTY;
    private INecromancyRecipe recipe = null;
    protected AspectList recipeEssentia = new AspectList();
    protected int spawnDelay = SPAWN_DELAY_MAX;
    protected int restartDelay = RESTART_DELAY_MAX;
    public int count;

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.stackHandler.deserializeNBT(compound.getCompoundTag("inventory"));
        this.spawnDelay = compound.getInteger("spawnDelay");
        this.restartDelay = compound.getInteger("restartDelay");
        this.recipeName = new ResourceLocation(compound.getString("recipeName"));
        this.recipeEssentia.readFromNBT(compound);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("inventory", this.stackHandler.serializeNBT());
        compound.setInteger("spawnDelay", this.spawnDelay);
        compound.setInteger("restartDelay", this.restartDelay);
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
                this.doPassiveParticles();
            }
        }

        if(did) {
            this.markDirty();
        }
    }

    public boolean isTabletActive() {
        return !this.world.isBlockPowered(this.pos) && this.world.getBlockState(this.pos).getValue(BlockNecromancyTablet.ENABLED);
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

    @SuppressWarnings("ConstantConditions")
    public void completeCraft() {
        if(this.getRecipe() != null) {
            this.consumeComponents();
            this.consumeCenterItem();
            this.recipe.spawnEntity(this.world, this.pos);
            this.world.addBlockEvent(this.pos, ModBlocksTT.NECROMANCY_TABLET, EFFECT_SPAWN, 0);
            this.world.playSound(null, pos, ModSoundEventsTT.BLOCK_NECROMANCY_TABLET_SPAWN_ENTITY.getSoundEvent(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            this.resetRecipe();
        }
    }

    public boolean checkAndUpdateRecipe() {
        ItemStack centerItem = this.getCenterItem();
        NonNullList<ItemStack> components = this.getComponents();
        if (this.getRecipe() == null || !this.recipe.matches(centerItem, components)) {
            Tuple<ResourceLocation, INecromancyRecipe> tuple = NecromancyRecipeRegistry.getRecipeAndName(centerItem, components);
            if(tuple != null) {
                this.setRecipe(tuple.getFirst(), tuple.getSecond());
                return true;
            } else if(this.getRecipe() != null) {
                this.resetRecipe();
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean processRecipe() {
        if(this.getRecipe() != null) {
            if(this.restartDelay > 0) {
                this.restartDelay--;
                return true;
            } else if (!this.recipeEssentia.aspects.isEmpty()) {
                if(this.count % 5 == 0) {
                    for (Aspect aspect : this.recipeEssentia.getAspects()) {
                        if (EssentiaHandler.drainEssentia(this, aspect, null, 12, 1)) {
                            this.recipeEssentia.remove(aspect, 1);
                            return true;
                        }
                    }
                }
            } else {
                if(this.spawnDelay > 0) {
                    if(this.spawnDelay >= SPAWN_DELAY_MAX) {
                        this.world.playSound(null, pos, ModSoundEventsTT.BLOCK_NECROMANCY_TABLET_BEAM_START.getSoundEvent(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    } else if(this.spawnDelay == 104) {
                        this.world.playSound(null, pos, ModSoundEventsTT.BLOCK_NECROMANCY_TABLET_BEAM_PROCESS.getSoundEvent(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                    for(int i = 0; i < PEDESTAL_OFFSETS.length; i++) {
                        if(!this.getPedestalItemStack(PEDESTAL_OFFSETS[i]).isEmpty()) {
                            this.world.addBlockEvent(this.pos, ModBlocksTT.NECROMANCY_TABLET, EFFECT_BEAM, i);
                        }
                    }
                    this.spawnDelay--;
                } else {
                    this.completeCraft();
                }
                return true;
            }
        }
        return false;
    }

    @Nullable
    public INecromancyRecipe getRecipe() {
        if(this.recipe == null && !this.recipeName.equals(EMPTY)) {
            this.recipe = NecromancyRecipeRegistry.getRecipe(this.recipeName);
        }
        return this.recipe;
    }

    public void setRecipe(ResourceLocation recipeName, @Nullable INecromancyRecipe recipe) {
        this.recipeName = recipeName;
        this.recipe = recipe;
        this.recipeEssentia = recipe != null ? recipe.getEssentia().copy() : new AspectList();
        this.restartDelay = RESTART_DELAY_MAX;
    }

    public void resetRecipe() {
        setRecipe(EMPTY, null);
        this.spawnDelay = SPAWN_DELAY_MAX;
    }

    public ItemStack getCenterItem() {
        return this.stackHandler.getStackInSlot(0);
    }

    public void consumeCenterItem() {
        if(this.getRecipe() != null) {
            ItemStack stack = this.getCenterItem();
            this.stackHandler.setStackInSlot(0, ItemHelper.consumeIngredient(stack, this.recipe.getCenterIngredient()));
        }
    }

    public NonNullList<ItemStack> getComponents() {
        NonNullList<ItemStack> components = NonNullList.create();
        for(BlockPos offset : PEDESTAL_OFFSETS) {
            components.add(this.getPedestalItemStack(offset));
        }
        return components;
    }

    public ItemStack getPedestalItemStack(BlockPos pedestalOffset) {
        TileEntity tile = this.world.getTileEntity(this.pos.add(pedestalOffset));
        if(tile instanceof TilePedestal && ((TilePedestal) tile).getSizeInventory() > 0) {
            return ((TilePedestal) tile).getStackInSlot(0);
        }
        return ItemStack.EMPTY;
    }

    @SuppressWarnings("ConstantConditions")
    public void consumeComponents() {
        INecromancyRecipe recipe = this.getRecipe();
        if(recipe == null || !recipe.shouldConsumeComponents())
            return;

        NonNullList<ItemStack> components = this.getComponents();
        for(Object component : recipe.getComponents()) {
            for(int i = 0; i < components.size(); i++) {
                ItemStack stack = components.get(i);
                if(ItemHelper.ingredientMatches(stack, component)) {
                    if(!stack.isEmpty()) {
                        this.world.addBlockEvent(this.pos, ModBlocksTT.NECROMANCY_TABLET, EFFECT_CONSUME_ITEM, i);
                    }
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

    @Override
    public boolean receiveClientEvent(int id, int type) {
        switch (id) {
            case EFFECT_BEAM:
                this.doComponentBeam(type);
                return true;
            case EFFECT_CONSUME_ITEM:
                this.doItemPoof(type);
                return true;
            case EFFECT_SPAWN:
                this.doSpawnPoof();
                return true;
        }
        return false;
    }

    public void doPassiveParticles() {
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

    public void doComponentBeam(int offsetIndex) {
        if (this.world.isRemote && this.getRecipe() != null) {
            BlockPos pedestalPos = this.pos.add(PEDESTAL_OFFSETS[offsetIndex]);
            Aspect[] sortedAspects = this.getRecipe().getEssentia().getAspectsSortedByAmount();
            FXDispatcher.INSTANCE.beamBore(
                    pedestalPos.getX() + 0.5,
                    pedestalPos.getY() + 1.0,
                    pedestalPos.getZ() + 0.5,
                    this.pos.getX() + 0.5,
                    this.pos.getY() + this.getRecipe().getEntityCenter(this.world) - 0.25,
                    this.pos.getZ() + 0.5,
                    1,
                    sortedAspects.length > 0 ? sortedAspects[0].getColor() : Aspect.DEATH.getColor(),
                    false,
                    0.4f,
                    1,
                    1
            );
        }
    }

    public void doItemPoof(int offsetIndex) {
        if (this.world.isRemote) {
            BlockPos pedestalPos = this.pos.add(PEDESTAL_OFFSETS[offsetIndex]);
            FXDispatcher.INSTANCE.drawBamf(
                    pedestalPos.getX() + 0.5,
                    pedestalPos.getY() + 1.25,
                    pedestalPos.getZ() + 0.5,
                    true,
                    true,
                    EnumFacing.UP
            );
        }
    }

    public void doSpawnPoof() {
        if(this.world.isRemote && this.getRecipe() != null) {
            Entity entity = this.getRecipe().getSummonedEntity(this.world);
            for(int i = 0; i < 20; ++i) {
                double motionX = this.world.rand.nextGaussian() * 0.02D;
                double motionY = this.world.rand.nextGaussian() * 0.02D;
                double motionZ = this.world.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(
                        EnumParticleTypes.EXPLOSION_NORMAL,
                        this.pos.getX() + 0.5 + (double) (this.world.rand.nextFloat() * entity.width * 2.0F) - entity.width - motionX * 10.0D,
                        this.pos.getY() + (double) (this.world.rand.nextFloat() * entity.height) - motionY * 10.0D,
                        this.pos.getZ() + 0.5 + (double) (this.world.rand.nextFloat() * entity.width * 2.0F) - entity.width - motionZ * 10.0D,
                        motionX, motionY, motionZ
                );
            }
        }
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
