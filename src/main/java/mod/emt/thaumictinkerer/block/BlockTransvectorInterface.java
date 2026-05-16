package mod.emt.thaumictinkerer.block;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.block.BlockTileAddition;
import mod.emt.thaumictinkerer.config.ConfigHandlerTT;
import mod.emt.thaumictinkerer.tile.TileTransvectorInterface;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.items.ItemsTC;

public class BlockTransvectorInterface extends BlockTileAddition {
    public BlockTransvectorInterface() {
        super("transvector_interface", Material.IRON, MapColor.OBSIDIAN, TileTransvectorInterface.class);
        this.setSoundType(SoundType.METAL);
        this.setHardness(3.0f);
        this.setResistance(12.0f);
    }

    @Override
    public void registerRecipe(IForgeRegistry<IRecipe> registry) {
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicTinkerer.MOD_ID, "transvector_interface"), new ShapedArcaneRecipe(
                new ResourceLocation(""),
                "",
                200,
                new AspectList().add(Aspect.ORDER, 10).add(Aspect.ENTROPY, 10),
                new ItemStack(this),
                "SRS",
                "LPL",
                "SCS",
                'S', BlocksTC.stoneArcane,
                'R', "blockRedstone",
                'L', "blockLapis",
                'P', Items.ENDER_PEARL,
                'C', ItemsTC.mechanismComplex
        ));
    }

    @Override
    public void registerResearchLocation() {
        //TODO
    }

    @Override
    public boolean isEnabled() {
        return ConfigHandlerTT.transvectorInterface.enable;
    }
}
