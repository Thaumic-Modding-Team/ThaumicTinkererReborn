package mod.emt.thaumictinkerer.registry;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.block.BlockEnergeticNitor;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(ThaumicTinkerer.MOD_ID)
public class ModBlocksTT {
    public static final Block ENERGETIC_NITOR = null;

    public static void initBlocks() {
        //Block initialization goes here. Be sure to register them with RegistrarTT#addAdditionToRegister()
        RegistrarTT.addAdditionToRegister(new BlockEnergeticNitor());
    }
}
