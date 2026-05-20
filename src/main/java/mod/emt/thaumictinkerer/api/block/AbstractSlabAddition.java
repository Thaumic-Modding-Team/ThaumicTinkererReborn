package mod.emt.thaumictinkerer.api.block;

import mod.emt.thaumictinkerer.api.item.IItemAddition;
import mod.emt.thaumictinkerer.block.BlockMaterialSlab;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

public class AbstractSlabAddition implements IBlockAddition, IItemAddition {
    private final BlockMaterialSlab singleSlab;
    private final BlockMaterialSlab doubleSlab;
    private final ItemSlab itemSlab;

    public AbstractSlabAddition(BlockMaterialSlab singleSlab, BlockMaterialSlab doubleSlab) {
        this.singleSlab = singleSlab;
        this.doubleSlab = doubleSlab;
        this.itemSlab = new ItemSlab(this.singleSlab, this.singleSlab, this.doubleSlab);

        this.itemSlab.setRegistryName(Objects.requireNonNull(this.singleSlab.getRegistryName()));
        this.itemSlab.setTranslationKey(this.singleSlab.getTranslationKey());
    }

    @Override
    public void registerBlock(IForgeRegistry<Block> registry) {
        registry.register(singleSlab);
        registry.register(doubleSlab);
    }

    @Override
    public void registerItem(IForgeRegistry<Item> registry) {
        registry.register(itemSlab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(itemSlab, 0,
                new ModelResourceLocation(Objects.requireNonNull(singleSlab.getRegistryName()), "inventory"));
    }
}
