package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.block.*;
import mod.emt.thaumictinkerer.tile.TileItemAttractor;
import mod.emt.thaumictinkerer.tile.TileMobAttractor;
import mod.emt.thaumictinkerer.tile.TilePlayerAttractor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ThaumicTinkerer.MOD_ID)
public class ModBlocksTT {
    public static final Block ARCANE_QUARTZ_BLOCK = null;
    public static final Block ARCANE_QUARTZ_PILLAR = null;
    public static final Block ARCANE_QUARTZ_STAIRS = null;
    public static final Block ATTRACTOR_ITEM = null;
    public static final Block ATTRACTOR_MOB = null;
    public static final Block ATTRACTOR_PLAYER = null;
    public static final Block CHISELED_ARCANE_QUARTZ = null;
    public static final Block ENERGETIC_NITOR = null;
    public static final Block THAUMIC_CAKE = null;
    public static final Block TRANSVECTOR_INTERFACE = null;
    public static final Block THAUMIC_RESTORER = null;
    public static final Block UMBRAL_ROSE = null;

    public static void initBlocks() {
        //Block initialization goes here. Be sure to register them with RegistrarTT#addAdditionToRegister()
        //TODO: Organize order
        RegistrarTT.addAdditionToRegister(new BlockEnergeticNitor());
        RegistrarTT.addAdditionToRegister(new BlockTransvectorInterface());
        RegistrarTT.addAdditionToRegister(new BlockFlower("umbral_rose"));
        RegistrarTT.addAdditionToRegister(new BlockThaumicCake());
        RegistrarTT.addAdditionToRegister(new BlockAttractor("attractor_item", TileItemAttractor.class));
        RegistrarTT.addAdditionToRegister(new BlockAttractor("attractor_mob", TileMobAttractor.class));
        RegistrarTT.addAdditionToRegister(new BlockAttractor("attractor_player", TilePlayerAttractor.class));
        RegistrarTT.addAdditionToRegister(new BlockThaumicRestorer());
        RegistrarTT.addAdditionToRegister(new BlockMaterial("arcane_quartz_block", Material.ROCK, MapColor.CYAN_STAINED_HARDENED_CLAY, 0.8F, SoundType.STONE));
        RegistrarTT.addAdditionToRegister(new BlockMaterial("chiseled_arcane_quartz_block", Material.ROCK, MapColor.CYAN_STAINED_HARDENED_CLAY, 0.8F, SoundType.STONE));
        RegistrarTT.addAdditionToRegister(new BlockMaterialPillar("arcane_quartz_pillar", Material.ROCK, MapColor.CYAN_STAINED_HARDENED_CLAY, 0.8F, SoundType.STONE));
        RegistrarTT.addAdditionToRegister(new BlockMaterialStairs("arcane_quartz_stairs", new BlockMaterial("arcane_quartz_block", Material.ROCK, MapColor.CYAN_STAINED_HARDENED_CLAY, 0.8F, SoundType.STONE).getDefaultState(), false));
        RegistrarTT.addAdditionToRegister(new BlockMaterial("terra_obsidian", Material.ROCK, MapColor.CYAN_STAINED_HARDENED_CLAY, 50.0F, 5000.0F, SoundType.STONE, true, false));
        RegistrarTT.addAdditionToRegister(new BlockMaterial("thaumium_plated_cobblestone", Material.ROCK, MapColor.STONE, 2.0F, 50.0F, SoundType.STONE, false, false));
    }
}
