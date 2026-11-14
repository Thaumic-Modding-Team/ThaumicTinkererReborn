package mod.emt.thaumictinkerer.item;

import mod.emt.thaumictinkerer.ThaumicTinkerer;
import mod.emt.thaumictinkerer.api.item.IItemAddition;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockSpecial;

import java.util.Objects;

public class ItemBlockSpecialTT extends ItemBlockSpecial implements IItemAddition {
    public ItemBlockSpecialTT(String unlocName, Block block) {
        super(block);
        this.setRegistryName(ThaumicTinkerer.MOD_ID, unlocName);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(ThaumicTinkerer.tabTT);
    }
}
