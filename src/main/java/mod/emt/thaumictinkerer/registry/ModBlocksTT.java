package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.block.*;
import mod.emt.thaumictinkerer.tile.TileItemAttractor;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ThaumicTinkerer.MOD_ID)
public class ModBlocksTT {
    public static final Block ENERGETIC_NITOR = null;
    public static final Block ITEM_ATTRACTOR = null;
    public static final Block THAUMIC_CAKE = null;
    public static final Block TRANSVECTOR_INTERFACE = null;
    public static final Block UMBRAL_ROSE = null;

    public static void initBlocks() {
        //Block initialization goes here. Be sure to register them with RegistrarTT#addAdditionToRegister()
        //TODO: Organize order
        RegistrarTT.addAdditionToRegister(new BlockEnergeticNitor());
        RegistrarTT.addAdditionToRegister(new BlockTransvectorInterface());
        RegistrarTT.addAdditionToRegister(new BlockFlower("umbral_rose"));
        RegistrarTT.addAdditionToRegister(new BlockThaumicCake());
        RegistrarTT.addAdditionToRegister(new BlockAttractor("item_attractor", TileItemAttractor.class));
    }
}
