package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.tile.TileTransvectorInterface;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectList;

import java.util.Map;

public class BlockTransvectorInterface extends BlockTileAddition {
    public BlockTransvectorInterface() {
        super("transvector_interface", Material.IRON, MapColor.OBSIDIAN, TileTransvectorInterface.class);
        this.setSoundType(SoundType.METAL);
        this.setHardness(3.0f);
        this.setResistance(12.0f);
    }

    @Override
    public void registerRecipe(IForgeRegistry<IRecipe> registry) {
        //TODO
    }

    @Override
    public void registerResearchLocation() {
        //TODO
    }

    @Override
    public void registerAspects(AspectEventProxy registry, Map<ItemStack, AspectList> aspectMap) {
        //TODO
    }

    @Override
    public boolean isEnabled() {
        return ConfigHandlerTT.transvectorInterface.enable;
    }
}
