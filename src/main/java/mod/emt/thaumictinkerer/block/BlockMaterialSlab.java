package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("deprecation")
public abstract class BlockMaterialSlab extends BlockSlab {
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    public BlockMaterialSlab(String name, Material material, MapColor mapColor) {
        super(material, mapColor);

        this.setRegistryName(ThaumicTinkerer.MOD_ID, name);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
        IBlockState state = this.blockState.getBaseState();
        if(!this.isDouble()) {
            state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
        }
        this.setDefaultState(state.withProperty(VARIANT, Variant.DEFAULT));
        this.useNeighborBrightness = true;
    }

    public BlockMaterialSlab(String name, Material material) {
        this(name, material, material.getMaterialMapColor());
    }

    @Override
    public @NotNull BlockMaterialSlab setSoundType(@NotNull SoundType sound) {
        super.setSoundType(sound);
        return this;
    }

    @Override
    public @NotNull BlockMaterialSlab setHardness(float hardness) {
        super.setHardness(hardness);
        return this;
    }

    @Override
    public @NotNull BlockMaterialSlab setResistance(float resistance) {
        super.setResistance(resistance);
        return this;
    }

    @Override
    public @NotNull String getTranslationKey(int meta) {
        return super.getTranslationKey();
    }

    @Override
    public @NotNull IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public @NotNull Comparable<?> getTypeForItem(@NotNull ItemStack stack) {
        return Variant.DEFAULT;
    }

    @Override
    public @NotNull IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState().withProperty(VARIANT, Variant.DEFAULT);
        if(!this.isDouble()) {
            state = state.withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
        }
        return state;
    }

    @Override
    public int getMetaFromState(@NotNull IBlockState state) {
        int meta = 0;
        if(!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP) {
            meta |= 8;
        }
        return meta;
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, java.util.@NotNull Random rand, int fortune) {
        if(this.isDouble()) {
            Block singleBlock = Block.getBlockFromName(Objects.requireNonNull(this.getRegistryName()).toString().replace("_double", ""));
            return singleBlock != null ? Item.getItemFromBlock(singleBlock) : Items.AIR;
        }

        return Item.getItemFromBlock(this);
    }

    public enum Variant implements IStringSerializable {
        DEFAULT;
        @Override
        public @NotNull String getName() { return "default"; }
    }

    public static class Double extends BlockMaterialSlab {
        public Double(String name, Material material, MapColor mapColor) {
            super(name + "_double", material, mapColor);
            this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString().replace("_double", ""));
        }

        @Override
        public boolean isDouble() {
            return true;
        }
    }

    public static class Half extends BlockMaterialSlab {
        public Half(String name, Material material, MapColor mapColor) {
            super(name, material, mapColor);
        }

        @Override
        public boolean isDouble() {
            return false;
        }
    }
}
