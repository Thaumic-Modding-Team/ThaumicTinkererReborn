package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.block.IBlockAddition;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BlockMaterialPillar extends BlockRotatedPillar implements IBlockAddition {
    boolean beaconBlock;
    boolean flammable;
    String oreDict;

    public BlockMaterialPillar(String unlocName, Material material, MapColor mapColor, float hardness, float resistance, SoundType soundType, boolean beaconBlock, boolean flammable, String oreDict) {
        super(material, mapColor);
        this.setRegistryName(ThaumicTinkerer.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(soundType);
        this.beaconBlock = beaconBlock;
        this.flammable = flammable;
        this.oreDict = oreDict;
    }

    public BlockMaterialPillar(String unlocName, Material material, MapColor mapColor, float hardness, SoundType soundType, String oreDict) {
        super(material, mapColor);
        this.setRegistryName(ThaumicTinkerer.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
        this.setHardness(hardness);
        this.setSoundType(soundType);
        this.oreDict = oreDict;
    }

    @Override
    public boolean isBeaconBase(@NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull BlockPos beacon) {
        return beaconBlock;
    }

    public boolean isFlammable() {
        return flammable;
    }

    @Override
    public int getFlammability(@NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        if(isFlammable()) {
            return Blocks.PLANKS.getFlammability(world, pos, face);
        } else {
            return 0;
        }
    }

    @Override
    public int getFireSpreadSpeed(@NotNull IBlockAccess world, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        if(isFlammable()) {
            return Blocks.PLANKS.getFireSpreadSpeed(world, pos, face);
        } else {
            return 0;
        }
    }

    @Override
    public void registerOreDicts() {
        if (oreDict != null) {
            OreDictionary.registerOre(oreDict, this);
        }
    }
}
