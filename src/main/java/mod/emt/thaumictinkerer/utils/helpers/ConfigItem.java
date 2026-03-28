package mod.emt.thaumictinkerer.utils.helpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;

public class ConfigItem {
    private final ItemStack compareStack;
    private final String oreDict;
    private final int oreId;

    public ConfigItem(Item item, int meta) {
        this.compareStack = new ItemStack(item, 1, meta);
        this.oreDict = "";
        this.oreId = -1;
    }

    public ConfigItem(Item item) {
        this(item, OreDictionary.WILDCARD_VALUE);
    }

    public ConfigItem(String oreDict) {
        this.compareStack = ItemStack.EMPTY;
        this.oreDict = oreDict;
        this.oreId = OreDictionary.getOreID(this.oreDict);
    }

    public boolean matches(ItemStack stack) {
        if(!this.compareStack.isEmpty()) {
            return OreDictionary.itemMatches(this.compareStack, stack, false);
        } else if(!this.oreDict.isEmpty()) {
            return Arrays.stream(OreDictionary.getOreIDs(stack)).anyMatch(id -> id == this.oreId);
        }
        return false;
    }
}
